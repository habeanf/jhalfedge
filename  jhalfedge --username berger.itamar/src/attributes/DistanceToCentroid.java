package attributes;

import model.HalfEdgeDataStructure;
import model.Vertex;

/**
 * Calculate the distance to centroid. The mesh is align to (0,0,0), so the function
 * computes the distance from center.
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:15:26 PM
 */
public class DistanceToCentroid implements MeshAttribute {
    public String getName() {
        return "Distance To Centroid";
    }

    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return vertex.getDistance();
    }

    public boolean doFaceRendering() {
        return false;
    }

    public int getClustersAmount() {
        return 0;
    }

    public void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            final float[] xyz = vertex.getXyz();
            float value = (float) (Math.sqrt(xyz[0]*xyz[0])+(xyz[1]*xyz[1])+(xyz[2]*xyz[2]));
            vertex.setDistance(value);
        }
    }
}
