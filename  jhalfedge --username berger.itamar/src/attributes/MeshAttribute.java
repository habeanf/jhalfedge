package attributes;

import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:05:19 PM
 */
public interface MeshAttribute {
    String getName();

    float getValue(Vertex vertex);
}
