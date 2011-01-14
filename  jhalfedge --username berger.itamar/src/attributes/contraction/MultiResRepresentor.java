package attributes.contraction;

import model.Edge;
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

    private boolean enableRandom = false;

    private List<DecimationRecord> decimationRecords;

    public MultiResRepresentor() {
    }

    public void build(IMesh mesh) {
        // get a set of all edges
        final Set<Edge> edges = mesh.getAllEdges();
        updateWeights(edges);

        // construct priority que from all edges (use their length for rank)
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>(edges);

        // initialize a list of decimation records
        decimationRecords = new ArrayList<DecimationRecord>();

        originalNumberOfFaces = mesh.getAllFaces().size();

        // while the mesh is not coarse enough
        while (!isMeshCoarse(mesh) && queue.size() > 0) {
            final Edge edge = queue.poll();

            // if the contraction is not allowed continue to the next
            if (!isContractionAllowed()) {
                continue;
            }

            // build a decimation record according to edge's contraction
            DecimationRecord record = createEdgeContractionRecord(mesh, edge);

            // store the record in the decimation records list
            decimationRecords.add(record);

            // apply the edge contraction on the mesh
            contract(mesh, record);

            // update the information of edges in the queue
            final Set<Edge> modifiedEdges = mesh.getEdgesAdjacentToVertex(record.otherVertex);
            updateQueue(queue,  modifiedEdges);

            System.out.println("created " + decimationRecords.size() + " records ,queue size: " + queue.size());
        }


    }

    private void updateWeights(Set<Edge> edges) {
        for (Edge edge : edges) {
            edge.updateWeight();
        }
    }

    private void updateQueue(PriorityQueue<Edge> queue, Set<Edge> modifiedEdges) {
        // remove all the edges of the deleted vertex
        for (Edge edge : modifiedEdges) {
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
        mesh.removeFace(record.firstRemovedTriangle);

        if (record.secondRemovedTriangle != null)
            mesh.removeFace(record.secondRemovedTriangle);

        // delete the vertex
        mesh.removeVertex(record.deletedVertex);
    }

    private void split(IMesh mesh, DecimationRecord record) {
        // TODO
    }

    private DecimationRecord createEdgeContractionRecord(IMesh mesh, Edge edge) {
        DecimationRecord record = new DecimationRecord();

        // choosing one of the vertices randomly
        if (enableRandom && Math.random() > 0.5) {
            record.deletedVertex = edge.getFrom();
            record.otherVertex = edge.getTo();
        } else {
            record.deletedVertex = edge.getTo();
            record.otherVertex = edge.getFrom();
        }

        // set adjacent triangles
        final List<Face> triangles = mesh.getFacesAdjacentToEdge(edge);
        record.firstRemovedTriangle = triangles.get(0);
        record.firstTriangleVertices = mesh.getFaceAdjacentVerticesIds(triangles.get(0));

        if (triangles.size() == 2) {
            record.secondRemovedTriangle = triangles.get(1);
            record.secondTriangleVertices = mesh.getFaceAdjacentVerticesIds(triangles.get(1));
        }

        // the list of triangles that changed one of their vertices from v2 to v1
        Set<Face> faces = mesh.getFacesAdjacentToVertex(record.deletedVertex);

        List<Face> changedTriangles = new ArrayList<Face>(faces.size());
        for (Face face : faces) {
            if (face != record.firstRemovedTriangle && face != record.secondRemovedTriangle) {
                changedTriangles.add(face);
            }
        }
        record.changedTriangles = changedTriangles;

        return record;
    }

    private boolean isMeshCoarse(IMesh mesh) {
        return (mesh.getAllFaces().size() / (float) originalNumberOfFaces < DECIMATION_RATIO);
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
