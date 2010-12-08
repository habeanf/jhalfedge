package attributes;

import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:03:17 PM
 */
public class Centricity implements MeshAttribute{
    public String getName() {
        return "Centricity";
    }

    public float getValue(Vertex vertex) {
        return vertex.getCentricity();
    }
}
