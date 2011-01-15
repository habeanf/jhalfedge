package attributes;

import model.IVertex;
import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:05:19 PM
 */
public interface MeshAttribute {
    String getName();

    float getValue(IVertex vertex);

    boolean doFaceRendering();

    int getClustersAmount();
}
