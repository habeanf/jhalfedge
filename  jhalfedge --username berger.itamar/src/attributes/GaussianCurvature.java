package attributes;

import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:15:39 PM
 */
public class GaussianCurvature implements MeshAttribute {
    public String getName() {
        return "Gaussian Curvature";
    }

    public float getValue(Vertex vertex) {
        return vertex.getGaussianCurvature();
    }
}
