package model;

import attributes.graph.Edge;

import java.util.Set;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:08 PM
 */
public interface IMesh {

    Set<Edge> getEdges();

    Set<Face> getFaces();

    Face[] getFacesAdjacentToEdge(Edge edge);

    int[] getFaceAdjacentIds(Face triangle);

    Set<Face> getFacesAdjacentToVertex(int vertex);

    void removeFace(int firstRemovedTriangleIndex);

    void removeVertex(int deletedVertex);

    void updateFacesVertices(Face face, int deletedVertex, int otherVertex);

    Set<Edge> geEdgesAdjacentToVertex(int deletedVertex);
}
