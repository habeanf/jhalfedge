package model;

import attributes.graph.Edge;

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

    Face[] getFacesAdjacentToEdge(Edge edge);

    int[] getFaceAdjacenVerticestIds(Face triangle);

    Set<Face> getFacesAdjacentToVertex(int vertex);

    void removeFace(int firstRemovedTriangleIndex);

    void removeVertex(int deletedVertex);

    void updateFacesVertices(Face face, int deletedVertex, int otherVertex);

    Set<Edge> geEdgesAdjacentToVertex(int deletedVertex);
}
