package attributes.contraction;

import attributes.graph.Edge;
import model.Face;
import model.IMesh;

import java.util.*;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:06 PM
 */
public class MultiResRepresentor {
    private static final float DECIMATION_RATIO = 0.1f;

    private int originalNumberOfFaces;

    private List<DecimationRecord> decimationRecords;

    public MultiResRepresentor() {
    }

    public void build(IMesh mesh) {
        // get a set of all edges
        final Set<Edge> edges = mesh.getEdges();

        // construct priority que from all edges (use their length for rank)
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>(edges);

        // initialize a list of decimation records
        decimationRecords = new ArrayList<DecimationRecord>();

        originalNumberOfFaces = mesh.getFaces().size();

        // while the mesh is not coarse enough
        while (!isMeshCoarse(mesh)) {
            final Edge edge = queue.poll();

            // if the contraction is not allowed continue to the next
            if (!isContractionAllowed()) {
                continue;
            }

            // build a decimation record according to edge's contraction
            DecimationRecord record = createEdgeContractionRecord(mesh, edge);

            // store the record in the decimation records list
            decimationRecords.add(record);

            // get edges that are going to be updated after the contraction
            final Set<Edge> staleEdges = mesh.geEdgesAdjacentToVertex(record.deletedVertex);

            // apply the edge contraction on the mesh
            contract(mesh, record);

            // update the information of edges in the queue
            final Set<Edge> modifiedEdges = mesh.geEdgesAdjacentToVertex(record.otherVertex);
            updateQueue(queue, staleEdges, modifiedEdges);
        }
    }

    private void updateQueue(PriorityQueue<Edge> queue, Set<Edge> stailEdges, Set<Edge> modifiedEdges) {
        // remove all the edges of the deleted vertex
        for (Edge edge : stailEdges) {
            queue.remove(edge);
        }

        // add again all the edges of the other vertex
        queue.addAll(modifiedEdges);
    }

    private void contract(IMesh mesh, DecimationRecord record) {
        // move triangles vertices that are attached to deleted vertex
        final Set<Face> adjacentFaces = mesh.getFacesAdjacentToVertex(record.deletedVertex);

        for (Face face : adjacentFaces) {
            mesh.updateFacesVertices(face, record.deletedVertex, record.otherVertex);
        }

        // delete the attached triangles
        mesh.removeFace(record.firstRemovedTriangleIndex);

        if (record.secondRemovedTriangleIndex != -1)
            mesh.removeFace(record.secondRemovedTriangleIndex);

        // delete the vertex
        mesh.removeVertex(record.deletedVertex);
    }

    private void split(IMesh mesh, DecimationRecord record) {
        // TODO
    }

    private DecimationRecord createEdgeContractionRecord(IMesh mesh, Edge edge) {
        DecimationRecord record = new DecimationRecord();

        // choosing one of the vertices randomly
        if (Math.random() > 0.5) {
            record.deletedVertex = edge.getFromId();
            record.otherVertex = edge.getToId();
        } else {
            record.deletedVertex = edge.getToId();
            record.otherVertex = edge.getFromId();
        }

        // set adjacent triangles
        final Face[] triangles = mesh.getFacesAdjacentToEdge(edge);
        record.firstRemovedTriangleIndex = triangles[0].getId();
        record.firstTriangleVertices = mesh.getFaceAdjacenVerticestIds(triangles[0]);

        if (triangles.length == 2) {
            record.secondRemovedTriangleIndex = triangles[1].getId();
            record.secondTriangleVertices = mesh.getFaceAdjacenVerticestIds(triangles[1]);
        }

        // the list of triangles that changed one of their vertices from v2 to v1
        Set<Face> faces = mesh.getFacesAdjacentToVertex(record.deletedVertex);

        List<Integer> changedTrianglesIds = new ArrayList<Integer>(faces.size());
        for (Face face : faces) {
            final int id = face.getId();

            if (id != record.firstRemovedTriangleIndex && id != record.secondRemovedTriangleIndex) {
                changedTrianglesIds.add(id);
            }
        }
        record.changedTrianglesIds = changedTrianglesIds;

        return record;
    }

    private boolean isMeshCoarse(IMesh mesh) {
        return (mesh.getFaces().size() / (float) originalNumberOfFaces < DECIMATION_RATIO);
    }

    public boolean isContractionAllowed() {
        return true;  // TODO
    }

    int p = 0;
    public void decreaseResolution(IMesh mesh) {

        if (p == decimationRecords.size())
            return;

        final DecimationRecord decimationRecord = decimationRecords.get(p++);

        contract(mesh, decimationRecord);
    }

    public void increaseResolution(IMesh mesh) {
        if (p == 0)
            return;

        final DecimationRecord decimationRecord = decimationRecords.get(--p);

        split(mesh, decimationRecord);
    }

}
