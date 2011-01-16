package model;

import java.util.*;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 4:09 PM
 */
public class IndexFacedSetMesh implements IMesh {
    private final Map<Integer, IVertex> vertexIdMap;
    private final Map<Integer, IFace> faceMap;

    // hold the original faces
    private final Map<Integer, Vector3D> faceNormalMap;

    public IndexFacedSetMesh(Map<Integer, IVertex> vertexMap, Map<Integer, IFace> faceMap) {
        this.vertexIdMap = vertexMap;
        this.faceMap = faceMap;

        faceNormalMap = new HashMap<Integer, Vector3D>(faceMap.size());
    }

    public List<IEdge> getAllUndirectedEdges() {
        Set<IEdge> edges = new HashSet<IEdge>();

        for (IFace face : getAllFaces()) {

            final List<IVertex> vertices = face.getVertices();
            final IVertex vertex1 = vertices.get(0);

            final IVertex vertex2 = vertices.get(1);
            final IVertex vertex3 = vertices.get(2);

            assert vertex1.getFaces().contains(face);
            assert vertex2.getFaces().contains(face);
            assert vertex3.getFaces().contains(face);

            assert (vertex1 != vertex2);
            assert (vertex2 != vertex3);
            assert (vertex3 != vertex1);


            edges.add(new Edge(vertex1, vertex2));
            edges.add(new Edge(vertex2, vertex3));
            edges.add(new Edge(vertex3, vertex1));
        }
        return new ArrayList<IEdge>(edges);
    }

    public Collection<IFace> getAllFaces() {
        return faceMap.values();
    }

    public Collection<IVertex> getAllVertices() {
        Collection<IVertex> activeVertices = new ArrayList<IVertex>(vertexIdMap.values());

        for (IVertex vertex : vertexIdMap.values()) {
            if (vertex.isActive()) {
                activeVertices.add(vertex);
            }
        }

        return activeVertices;
    }

    public List<IFace> getFacesAdjacentToEdge(IEdge edge) {
        List<IFace> adjacentFaces = new ArrayList<IFace>();

        final IVertex fromVertex = getVertex(edge.getFromId());
        final IVertex toVertex = getVertex(edge.getToId());

        assert fromVertex != null && toVertex != null;

        final Set<IFace> fromFaces = fromVertex.getFaces();
        final Set<IFace> toFaces = toVertex.getFaces();

        for (IFace fromFace : fromFaces) {
            if (toFaces.contains(fromFace)) {
                adjacentFaces.add(fromFace);
            }
        }

        return adjacentFaces;
    }

    public List<IVertex> getVerticesAdjacentToFace(int faceId) {
        final IFace face = faceMap.get(faceId);

        if (face != null)
            return face.getVertices();

        return null;
    }

    public Set<IFace> getFacesAdjacentToVertex(int vertexId) {
        final IVertex vertex = getVertex(vertexId);

        if (vertex != null) {
            return vertex.getFaces();
        }

        return null;
    }


    public Set<IEdge> getEdgesAdjacentToVertex(int vertexId) {
        Set<IEdge> edges = new HashSet<IEdge>();

        final IVertex vertex = getVertex(vertexId);

        final Set<IFace> faces = vertex.getFaces();

        for (IFace face : faces) {
            for (IVertex faceVertex : face.getVertices()) {
                if (faceVertex.getId() != vertexId) {
                    edges.add(new Edge(getVertex(faceVertex.getId()), vertex));
                }
            }
        }

        return edges;
    }

    public boolean isEdgeValid(IEdge edge) {
        return true;
    }

    public void removeFace(int faceId) {
        final IFace face = faceMap.get(faceId);

        final List<IVertex> vertices = face.getVertices();

        for (IVertex vertex : vertices) {
            vertex.removeFace(face);

        }

        faceMap.remove(faceId);
    }

    public void removeVertex(int vertexId) {
        final IVertex vertex = getVertex(vertexId);

        for (IFace face : vertex.getFaces()) {
            face.removeVertex(vertex);
        }

        getVertex(vertexId).setActive(false);
    }


    public boolean isValidReplacment(Integer faceId, int from, int to) {
        final IVertex fromVertex = getVertex(from);
        final IVertex toVertex = getVertex(to);

        final IFace face = faceMap.get(faceId);

        return face.isValidReplacement(fromVertex, toVertex);
    }

    public boolean changeFaceVertices(int faceId, int from, int to) {
        final IFace face = faceMap.get(faceId);

        final IVertex fromVertex = getVertex(from);
        final IVertex toVertex = getVertex(to);

        boolean success = face.replaceVertex(fromVertex, toVertex);

        fromVertex.removeFace(face);

        if (success)
            toVertex.addFace(face);

        return success;

    }

    public void addFace(int faceId, int[] verticesIds, boolean doCalcNormals) {
        Face face = new Face(faceId);
        faceMap.put(faceId, face);

        List<IVertex> vertices = new ArrayList<IVertex>();
        for (int vertexId : verticesIds) {
            final IVertex vertex = getVertex(vertexId);
            vertices.add(vertex);
            addFaceToVertex(vertex, face);
        }

        addVerticesToFace(face, vertices);
        
        if (doCalcNormals) {
            face.calcNormal();
            faceNormalMap.put(face.getId(),face.getNormal());
        }else{
            face.setNormal(faceNormalMap.get(faceId));
        }
    }

    public void addVertex(int vertexId) {
        vertexIdMap.get(vertexId).setActive(true);
    }

    private IVertex getVertex(int vertexId) {
        final IVertex vertex = vertexIdMap.get(vertexId);

        if (vertex.isActive())
            return vertex;

        return null;
    }

    private void addFaceToVertex(IVertex vertex, Face face) {
        vertex.addFace(face);
    }

    private void addVerticesToFace(IFace face, List<IVertex> vertices) {
        face.addVertices(vertices);
    }

}
