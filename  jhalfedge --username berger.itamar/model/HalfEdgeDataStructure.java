package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 11:30:33 PM
 */
public class HalfEdgeDataStructure {

    private final Collection<HalfEdge> halfEdges;
    private final Collection<Face> faces;
    private final Map<Integer, Vertex> vertexes;

    public HalfEdgeDataStructure(Collection<HalfEdge> halfEdges, Collection<Face> faces, Map<Integer, Vertex> vertexes) {
        this.halfEdges = halfEdges;
        this.faces = faces;
        this.vertexes = vertexes;
    }

    public Collection<HalfEdge> getAllHalfEdges() {
        return halfEdges;
    }

    public Collection<Face> getAllFaces() {
        return faces;
    }

    public Collection<Vertex> getVertexes() {
        return vertexes.values();
    }

    public List<Vertex> getNeighbours(Vertex vertex) {
        List<Vertex> result = new ArrayList<Vertex>();

        HalfEdge firstHalfEdge = vertex.getHalfEdge();
        HalfEdge nextHalfEdge = firstHalfEdge.getNext();

        do{
            result.add(nextHalfEdge.getVertex());
            nextHalfEdge = nextHalfEdge.getOpp().getNext();

        }while (firstHalfEdge!=nextHalfEdge);

        return result;
    }

    public List<Face> getNeighbours(Face face) {
        List<Face> result = new ArrayList<Face>();

        return result;
    }


    public Vertex getVertex(int vertexId) {
        return vertexes.get(vertexId);
    }
}
