package attributes.contraction;

import model.*;
import utils.InfoLogger;

import javax.swing.text.NumberFormatter;
import java.text.ParseException;
import java.util.*;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:06 PM
 */
public class MultiResRepresentor {
    private static final float DECIMATION_RATIO = 0.1f;

    private int originalNumberOfFaces;

    private boolean enableRandom = true;

    private List<DecimationRecord> decimationRecords;

    MeshPersistenceService meshPersistenceService = new MeshPersistenceService();

    NumberFormatter numberFormatter = new NumberFormatter();

    public MultiResRepresentor() {
    }

    public void build(InfoLogger infoLogger, IMesh mesh) {


        // get a set of all edges and construct priority que from all edges (use their length for rank)
        PriorityQueue<IEdge> queue = new PriorityQueue<IEdge>(mesh.getAllUndirectedEdges());

        // initialize a list of decimation records
        decimationRecords = new ArrayList<DecimationRecord>();

        originalNumberOfFaces = mesh.getAllFaces().size();

        stepSize = (int) (originalNumberOfFaces * DECIMATION_RATIO / 3);

        System.out.println("Starting contraction, total faces :" + originalNumberOfFaces);

        // while the mesh is not coarse enough
        while (!isMeshCoarse(mesh) && queue.size() > 0) {
            final IEdge edge = queue.poll();

            // if the contraction is not allowed continue to the next
            if (!isContractionAllowed(mesh, edge)) {
                continue;
            }

            // build a decimation record according to edge's contraction
            DecimationRecord record = createEdgeContractionRecord(mesh, edge);

            // store the record in the decimation records list
            decimationRecords.add(record);

            // those edges should be updated before the next iteration
            final Set<IEdge> staleEdges = mesh.getEdgesAdjacentToVertex(record.deletedVertexId);
            staleEdges.addAll(mesh.getEdgesAdjacentToVertex(record.otherVertexId));

            // apply the edge contraction on the mesh
            contract(mesh, record);

            // update the information of edges in the queue
            final Set<IEdge> modifiedEdges = mesh.getEdgesAdjacentToVertex(record.otherVertexId);

            updateQueue(queue, staleEdges, modifiedEdges);

            try {
                infoLogger.setDebugRow("Created " + decimationRecords.size() + " decimation records" + " (" +
                        numberFormatter.valueToString(Math.min(100, (originalNumberOfFaces - mesh.getAllFaces().size()) / ((1 - DECIMATION_RATIO) * originalNumberOfFaces) * 100)) + "%)");
            } catch (ParseException e) {
                System.out.println("Failed to format percent");
            }

        }

        infoLogger.setDebugRow("Done!, total amount of faces left: " + mesh.getAllFaces().size() + " (original " + originalNumberOfFaces + ")");

        System.out.println("Starting splitting");
        Collections.reverse(decimationRecords);

        int counter = 0;
        for (DecimationRecord record : decimationRecords) {
            System.out.println("Reveresed " + counter++ + "/" + decimationRecords.size() + "  records succesfully");
            split(mesh, record);
        }

        Collections.reverse(decimationRecords);
        decreaseResolution(infoLogger, mesh,Integer.MAX_VALUE);
    }

    private void updateQueue(PriorityQueue<IEdge> queue, Collection<IEdge> staleEdges, Collection<IEdge> modifiedEdges) {
        // remove all the edges of the deleted vertex
        for (IEdge edge : staleEdges) {
            queue.remove(edge);
        }

        // add again all the edges of the other vertex
        for (IEdge edge : modifiedEdges) {
            queue.remove(edge);
        }

        queue.addAll(modifiedEdges);
    }

    private void contract(IMesh mesh, DecimationRecord record) {
        // move triangles vertices that are attached to deleted vertex
        final Set<IFace> adjacentFaces = mesh.getFacesAdjacentToVertex(record.deletedVertexId);

        List<Integer> facesIds = new ArrayList<Integer>();
        for (IFace adjacentFace : adjacentFaces) {
            if (record.firstRemovedTriangleId != adjacentFace.getId() && record.secondRemovedTriangleId != adjacentFace.getId()) {
                facesIds.add(adjacentFace.getId());
            }
        }

        for (Integer faceId : facesIds) {
            if (meshPersistenceService.isValidReplacment(mesh, faceId, record.deletedVertexId, record.otherVertexId)) {
                final boolean success = meshPersistenceService.changeFaceVertices(mesh, faceId, record.deletedVertexId, record.otherVertexId);

                // should we support faces that turns to edges? TODO
                if (!success) {
                    meshPersistenceService.removeFace(mesh, faceId);
                    problematicFaces.add(faceId);
                }
            }
        }

        // delete the attached triangles
        meshPersistenceService.removeFace(mesh, record.firstRemovedTriangleId);

        if (record.secondRemovedTriangleId != 0)
            meshPersistenceService.removeFace(mesh, record.secondRemovedTriangleId);

        // delete the vertex
        meshPersistenceService.removeVertex(mesh, record.deletedVertexId);
    }

    public List<Integer> problematicFaces = new ArrayList<Integer>();

    private void split(IMesh mesh, DecimationRecord record) {
        // recreate the vertex
        meshPersistenceService.addVertex(mesh, record.deletedVertexId);

        // recreate first face
        if (record.firstRemovedTriangleId > 0)
            meshPersistenceService.addFace(mesh, record.firstRemovedTriangleId, record.firstTriangleVerticesIds);

        // recreate second face
        if (record.secondRemovedTriangleId > 0)
            meshPersistenceService.addFace(mesh, record.secondRemovedTriangleId, record.secondTriangleVerticesIds);
        if (record.changedTrianglesIds != null) {
            for (Integer faceId : record.changedTrianglesIds) {
                meshPersistenceService.changeFaceVertices(mesh, faceId, record.otherVertexId, record.deletedVertexId);
            }
        }

    }

    private DecimationRecord createEdgeContractionRecord(IMesh mesh, IEdge edge) {
        DecimationRecord record = new DecimationRecord();

        // choosing one of the vertices randomly
        if (enableRandom && Math.random() > 0.5) {
            record.deletedVertexId = edge.getFromId();
            record.otherVertexId = edge.getToId();
        } else {
            record.deletedVertexId = edge.getToId();
            record.otherVertexId = edge.getFromId();
        }

        // set adjacent triangles
        final List<IFace> triangles = mesh.getFacesAdjacentToEdge(edge);
        if (triangles.size() > 0) {
            final List<IVertex> faceAdjacentVertices1 = mesh.getVerticesAdjacentToFace(triangles.get(0).getId());
            int[] verticesIds = new int[faceAdjacentVertices1.size()];
            for (int i = 0; i < verticesIds.length; i++) {
                verticesIds[i] = faceAdjacentVertices1.get(i).getId();
            }

            record.firstRemovedTriangleId = triangles.get(0).getId();
            record.firstTriangleVerticesIds = verticesIds;
        }

        if (triangles.size() == 2) {
            final List<IVertex> faceAdjacentVertices2 = mesh.getVerticesAdjacentToFace(triangles.get(1).getId());
            int[] verticesIds = new int[faceAdjacentVertices2.size()];
            for (int i = 0; i < verticesIds.length; i++) {
                verticesIds[i] = faceAdjacentVertices2.get(i).getId();
            }
            record.secondRemovedTriangleId = triangles.get(1).getId();
            record.secondTriangleVerticesIds = verticesIds;
        }

        // the list of triangles that changed one of their vertices from v2 to v1
        Set<IFace> faces = mesh.getFacesAdjacentToVertex(record.deletedVertexId);

        List<Integer> changedTrianglesIds = new ArrayList<Integer>(faces.size());
        for (IFace face : faces) {
            int faceId = face.getId();
            if (faceId != record.firstRemovedTriangleId && faceId != record.secondRemovedTriangleId) {
                changedTrianglesIds.add(faceId);
            }
        }
        record.changedTrianglesIds = changedTrianglesIds;

        return record;
    }

    private boolean isMeshCoarse(IMesh mesh) {
        return (mesh.getAllFaces().size() / (float) originalNumberOfFaces < DECIMATION_RATIO);
    }

    public boolean isContractionAllowed(IMesh mesh, IEdge edge) {
        if (!mesh.isEdgeValid(edge))
            return false;

        return true;
    }

    int p = 0;
    int stepSize = 300;
    public void decreaseResolution(InfoLogger infoLogger, IMesh mesh, double speed) {
        if (p == decimationRecords.size())
            return;

        int end = (int) Math.min(p + (stepSize*speed), decimationRecords.size() - 2);
        while (p < end) {
            final DecimationRecord decimationRecord = decimationRecords.get(p++);
            contract(mesh, decimationRecord);
        }

        infoLogger.setDebugRow("Faces amount: " + mesh.getAllFaces().size() + "/" + originalNumberOfFaces);

    }

    public void increaseResolution(InfoLogger infoLogger, IMesh mesh, double speed) {

        if (p == -1)
            return;

        int end = (int) Math.max(p - (stepSize*speed)-1, 0);
        while (p > end) {
            final DecimationRecord decimationRecord = decimationRecords.get(--p);
            split(mesh, decimationRecord);
        }

        infoLogger.setDebugRow("Faces amount: " + mesh.getAllFaces().size() + "/" + originalNumberOfFaces);
    }

}
