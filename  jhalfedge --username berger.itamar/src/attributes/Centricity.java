package attributes;

import model.HalfEdgeDataStructure;
import model.Vertex;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:03:17 PM
 */
public class Centricity implements MeshAttribute {
    public String getName() {
        return "Centricity";
    }

    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return vertex.getCentricity();
    }

    public static void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            //TODO
            vertex.setCentricity(0.2f);
        }
    }
}
