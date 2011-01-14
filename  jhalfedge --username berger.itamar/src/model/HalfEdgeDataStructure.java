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

            if (nextHalfEdge.getOpp() != null && nextHalfEdge.getOpp().getFace() != null) {
                result.add(nextHalfEdge.getOpp().getFace());
            }
            
            if (nextHalfEdge.getOpp() != null) {
                nextHalfEdge = nextHalfEdge.getOpp().getNext();
            }

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

    public int[] getFaceAdjacentVerticesIds(Face triangle) {
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Face> getFacesAdjacentToVertex(Vertex vertex) {
        return getFaceNeighbours(vertex);
    }

    public void removeFace(Face face) {
        if (face == null){
            return;
        }

        HalfEdge firstEdge = face.getHalfEdge();
        HalfEdge currentEdge = face.getHalfEdge();

        do {
            currentEdge.setFace(null);
            currentEdge = currentEdge.getNext();
        } while (!firstEdge.equals(currentEdge));

        faces.remove(face);
    }

    public void removeEdge(Edge edge) {
        HalfEdge fromToEdge = edgeToHalfEdgeMap.get(edge);
        HalfEdge toFromEdge = fromToEdge.getOpp();
        if (!(fromToEdge.getFace()==null))
            removeFace(fromToEdge.getFace());
        if (!(toFromEdge.getFace()==null))
            removeFace(toFromEdge.getFace());

        Vertex fromVertex = fromToEdge.getVertex();

        HalfEdge fromIn = fromToEdge.getPrev();
        HalfEdge fromOut = fromToEdge.getOpp().getNext(); // fromToEdge.rotateNext()

        if (fromVertex.getHalfEdge().equals(fromToEdge)) {
            if (fromOut.equals(fromToEdge))
                fromVertex.setHalfEdge(null);
            else
                fromVertex.setHalfEdge(fromOut);
        }

        fromIn.setNext(fromOut, true);
        fromOut.setPrev(fromIn);

        Vertex toVertex = toFromEdge.getVertex();

        HalfEdge toIn = toFromEdge.getPrev();
        HalfEdge toOut = toFromEdge.getOpp().getNext(); // toFromEdge.rotateNext()

        if (toVertex.getHalfEdge().equals(toFromEdge)) {
            if (toOut.equals(toFromEdge))
                toVertex.setHalfEdge(null);
            else
                toVertex.setHalfEdge(toOut);
        }

        toIn.setNext(toOut,true);
        toOut.setPrev(toIn);

        halfEdges.remove(toFromEdge);
        halfEdges.remove(fromToEdge);
        edgeToHalfEdgeMap.remove(edge);
    }

    public void removeVertex(Vertex vertex) {
        if (!vertex.isIsolated()) {
            // Remove every edge that is connected to this vertex


            HalfEdge current;
            HalfEdge next = vertex.getHalfEdge();
            do {
                current = next;
                next = next.getOpp().getNext(); // next.rotateNext()
                // Avoid removing the same edge twice in case of an edge loop

                if (next.getEdge().equals(current.getEdge()))
                    next = next.getOpp().getNext(); // next.rotateNext()
                removeEdge(current.getEdge());
            } while (!current.equals(next));
        }
        vertexes.remove(vertex.getId());
    }

    public void updateFacesVertices(Face face, Vertex deletedVertex, Vertex otherVertex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Edge> getEdgesAdjacentToVertex(Vertex vertex) {
        Set<Edge> result = new HashSet<Edge>();

        HalfEdge firstHalfEdge = vertex.getHalfEdge();
        HalfEdge nextHalfEdge = firstHalfEdge;

        do {
            if (nextHalfEdge == null) {
                break;
            }

            result.add(nextHalfEdge.getEdge());

            if (nextHalfEdge.getOpp() != null) {
                nextHalfEdge = nextHalfEdge.getOpp().getNext();
            }

        } while (!firstHalfEdge.equals(nextHalfEdge));

        return result;
    }

    public boolean isEdgeValid(Edge edge) {
        final HalfEdge halfEdge = edgeToHalfEdgeMap.get(edge);

        return (halfEdge!= null);
    }
}
