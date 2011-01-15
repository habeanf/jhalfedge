package model;

import java.util.List;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 3:43 PM
 */
public interface IFace {
    int getSegment();

    List<IVertex> getVertices();

    void addVertices(List<IVertex> vertices);

    int getId();

    void removeVertex(IVertex vertex);

    boolean replaceVertex(IVertex vertex, IVertex toVertex);

    boolean isValidReplacement(IVertex fromVertex, IVertex toVertex);
}
