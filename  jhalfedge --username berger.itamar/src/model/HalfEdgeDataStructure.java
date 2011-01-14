package model;


import java.util.*;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 11:30:33 PM
 */
public class HalfEdgeDataStructure implements IMesh {

    private final Collection<HalfEdge> halfEdges;
    private final List<Face> faces;
    private final Map<Integer, Vertex> vertexes;
    private Map<Edge, HalfEdge> edgeToHalfEdgeMap;

    public HalfEdgeDataStructure(Collection<HalfEdge> halfEdges, List<Face> faces, Map<Integer, Vertex> vertexes, Map<Edge, HalfEdge> edgeToHalfEdgeMap) {
        this.halfEdges = halfEdges;
        this.faces = faces;
        this.vertexes = vertexes;
        this.edgeToHalfEdgeMap = edgeToHalfEdgeMap;
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
        } while (!edge.equals(face.getHalfEdge()));
        return vertexList.toArray(new Vertex[vertexList.size()]);
    }

    public Vertex getVertex(int vertexId) {
        return vertexes.get(vertexId);
    }


    public Set<Edge> getAllEdges() {
        return edgeToHalfEdgeMap.keySet();
    }

    public List<Face> getFacesAdjacentToEdge(Edge edge) {
        List<Face> result = new ArrayList<Face>();

        final HalfEdge halfEdge = edgeToHalfEdgeMap.get(edge);

        if (halfEdge != null && halfEdge.getFace() != null) {
            result.add(halfEdge.getFace());

            HalfEdge oppHalfEdge = halfEdge.getOpp();

            if (oppHalfEdge != null && oppHalfEdge.getFace() != null) {
                result.add(oppHalfEdge.getFace());
            }
        }

        return result;
    }

    public int[] getFaceAdjacenVerticestIds(Face triangle) {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Face> getFacesAdjacentToVertex(Vertex vertex) {
        return getFaceNeighbours(vertex);
    }


    public void removeFace(Face face) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeVertex(Vertex deletedVertex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateFacesVertices(Face face, Vertex deletedVertex, Vertex otherVertex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Edge> geEdgesAdjacentToVertex(Vertex vertex) {
        Set<Edge> result = new HashSet<Edge>();

        

        return result;
    }
}
