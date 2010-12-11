package model;

import java.util.*;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 11:30:33 PM
 */
public class HalfEdgeDataStructure {

    private final Collection<HalfEdge> halfEdges;
    private final List<Face> faces;
    private final Map<Integer, Vertex> vertexes;

    public HalfEdgeDataStructure(Collection<HalfEdge> halfEdges, List<Face> faces, Map<Integer, Vertex> vertexes) {
        this.halfEdges = halfEdges;
        this.faces = faces;
        this.vertexes = vertexes;
    }

    public Collection<HalfEdge> getAllHalfEdges() {
        return halfEdges;
    }

    public List<Face> getAllFaces() {
        return faces;
    }

    public Collection<Vertex> getVertexes() {
        return vertexes.values();
    }

    public Set<Vertex> getNeighbours(Vertex vertex) {
        Set<Vertex> result = new HashSet<Vertex>();

        HalfEdge firstHalfEdge = vertex.getHalfEdge();
        HalfEdge nextHalfEdge = firstHalfEdge;

        do {
            if (nextHalfEdge == null) {
                break;
            }

            nextHalfEdge = nextHalfEdge.getOpp().getNext();
            result.add(nextHalfEdge.getNext().getVertex());
        } while (!firstHalfEdge.equals(nextHalfEdge));

        return result;
    }

    public Set<Face> getNeighbours(Face face) {
        Set<Face> result = new HashSet<Face>();

        if (face.getHalfEdge() != null) {
            HalfEdge firstHalfEdge = face.getHalfEdge();
            HalfEdge nextHalfEdge = firstHalfEdge;

            do {
                Face neighbourFace = nextHalfEdge.getOpp().getFace();
                if (neighbourFace != null) {
                    result.add(neighbourFace);
                }
                nextHalfEdge = nextHalfEdge.getNext();
            } while (firstHalfEdge != nextHalfEdge);
        }
        return result;
    }

    public Set<Face> getFaceNeighbours(Vertex vertex) {
        Set<Face> result = new HashSet<Face>();

        HalfEdge firstHalfEdge = vertex.getHalfEdge();
        HalfEdge nextHalfEdge = firstHalfEdge;

        do {
            if (nextHalfEdge == null) {
                break;
            }

            if (nextHalfEdge.getFace() != null) {
                result.add(nextHalfEdge.getFace());
            }

            if (nextHalfEdge.getOpp().getFace() != null) {
                result.add(nextHalfEdge.getOpp().getFace());
            }

            nextHalfEdge = nextHalfEdge.getOpp().getNext();
        } while (firstHalfEdge != nextHalfEdge);

        return result;
    }

    public Vertex[] getFaceVertices(Face face) {
        HalfEdge edge = face.getHalfEdge();
        List<Vertex> vertexList = new ArrayList<Vertex>(3);
        do {
            vertexList.add(edge.getVertex());
            edge = edge.getNext();
        } while(!edge.equals(face.getHalfEdge()));
        return vertexList.toArray(new Vertex[vertexList.size()]);
    }

    public Vertex getVertex(int vertexId) {
        return vertexes.get(vertexId);
    }


}
