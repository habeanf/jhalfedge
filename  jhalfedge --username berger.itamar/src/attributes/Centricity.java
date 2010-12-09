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
            float value = 0.2f;

            final Map<Integer, Float> vertexIdToDistance = geodesicDistanceCalculator.getGeodesicDistances(vertex);

            float size = vertexIdToDistance.size();

            if (size > 0) {
                float sum = 0;
                for (Float distance : vertexIdToDistance.values()) {
                    sum += distance;
                }
                value = sum / size;
            }

            vertex.setCentricity(value);
        }
    }
}
