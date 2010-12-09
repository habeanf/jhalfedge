package attributes;

import model.HalfEdgeDataStructure;
import model.Vertex;

import java.util.HashMap;
import java.util.Map;

/**
 * User: itamar
 * Date: Dec 9, 2010
 * Time: 1:19:38 AM
 */
public class GeodesicDistanceCalculator {
    HalfEdgeDataStructure halfEdgeDataStructure;

    public GeodesicDistanceCalculator(HalfEdgeDataStructure halfEdgeDataStructure) {
        this.halfEdgeDataStructure = halfEdgeDataStructure;
    }

    public Map<Integer, Float> getGeodesicDistances(Vertex vertex) {
        Map<Integer, Float> result = new HashMap<Integer, Float>(halfEdgeDataStructure.getVertexes().size());
        
        return result;
    }

    private void createShortCutPaths(){
        // TODO
    }
}
