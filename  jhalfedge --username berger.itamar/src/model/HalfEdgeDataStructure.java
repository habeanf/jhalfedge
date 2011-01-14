package model;

import attributes.graph.Edge;

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


    public Set<Edge> getEdges() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Face> getFaces() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Face[] getFacesAdjacentToEdge(Edge edge) {
        return new Face[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int[] getFaceAdjacentIds(Face triangle) {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Face> getFacesAdjacentToVertex(int vertex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeFace(int firstRemovedTriangleIndex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeVertex(int deletedVertex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateFacesVertices(Face face, int deletedVertex, int otherVertex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Edge> geEdgesAdjacentToVertex(int deletedVertex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
