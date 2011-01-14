package model;


import java.util.List;
import java.util.Set;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:08 PM
 */
public interface IMesh {

    Set<Edge> getAllEdges();

    List<Face> getAllFaces();

    List<Face> getFacesAdjacentToEdge(Edge edge);

    int[] getFaceAdjacentVerticesIds(Face triangle);

    Set<Face> getFacesAdjacentToVertex(Vertex vertex);

    void removeFace(Face face);

    void removeVertex(Vertex deletedVertex);

    void updateFacesVertices(Face face, Vertex deletedVertex, Vertex otherVertex);

    Set<Edge> getEdgesAdjacentToVertex(Vertex vertex);

    boolean isEdgeValid(Edge edge);
}
