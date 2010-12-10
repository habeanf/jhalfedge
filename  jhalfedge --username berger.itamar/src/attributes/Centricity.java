package attributes;

import model.HalfEdgeDataStructure;
import model.Vertex;

import java.util.HashMap;
import java.util.Map;

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
        GeodesicDistanceCalculator geodesicDistanceCalculator = new GeodesicDistanceCalculator(halfEdgeDataStructure);

        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            vertex.setCentricity(geodesicDistanceCalculator.getGeodesicDistances(vertex));
        }
    }

    public static void calculateOnePointOnly(HalfEdgeDataStructure halfEdgeDataStructure) {
        GeodesicDistanceCalculator geodesicDistanceCalculator = new GeodesicDistanceCalculator(halfEdgeDataStructure);

        geodesicDistanceCalculator.showGeodesicForVertex(halfEdgeDataStructure.getVertex((int) (Math.random() *
                halfEdgeDataStructure.getVertexes().size() + 1)));
    }
}
