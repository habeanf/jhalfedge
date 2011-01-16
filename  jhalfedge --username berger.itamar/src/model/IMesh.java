package model;


import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:08 PM
 */
public interface IMesh {

    Collection<IEdge> getAllUndirectedEdges();

    Collection<IFace> getAllFaces();

    Collection<IVertex> getAllVertices();
    
    List<IFace> getFacesAdjacentToEdge(IEdge edge);

    List<IVertex> getVerticesAdjacentToFace(int faceId);

    Set<IFace> getFacesAdjacentToVertex(int vertexId);

    Set<IEdge> getEdgesAdjacentToVertex(int vertexId);

    boolean isEdgeValid(IEdge edge);

    void removeFace(int faceId);

    void removeVertex(int vertexId);

    boolean changeFaceVertices(int faceId, int from, int to);

    void addFace(int faceId, int[] verticesIds,boolean calcNormals);

    void addVertex(int vertexId);

    boolean isValidReplacment(Integer faceId, int from, int to);
}
