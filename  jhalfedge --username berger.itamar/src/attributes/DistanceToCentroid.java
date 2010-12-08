package attributes;

import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:15:26 PM
 */
public class DistanceToCentroid implements MeshAttribute {
    public String getName() {
        return "Distance To Centroid";
    }

    public float getValue(Vertex vertex) {
        return vertex.getDistance();
    }
}
