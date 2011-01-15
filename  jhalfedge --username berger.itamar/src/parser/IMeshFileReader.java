package parser;

import model.Vertex;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 7:09:32 PM
 */
public interface IMeshFileReader {
    boolean isVertex(String[] fields);
    Vertex getVertex(String[] fields);
    boolean isFace(String[] fields);
    int[] getFaceIds(String[] fields);

    void preProcess(OBJLineIterator lineIterator);
}
