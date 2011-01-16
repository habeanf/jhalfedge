package attributes;

import model.IMesh;
import model.IVertex;

/**
 * User: itamar
 * Date: 1/16/11
 * Time: 8:05 PM
 */
public class DistanceToCentroid implements MeshAttribute {
    public String getName() {
        return "Distance To Centroid";
    }

    public float getValue(IVertex vertex) {
        return vertex.getDistance();
    }

    public boolean doFaceRendering() {
        return false;
    }

    public int getClustersAmount() {
        return 0;
    }

    public void calculate(IMesh mesh) {
        for (IVertex vertex : mesh.getAllVertices()) {
            final float[] xyz = vertex.getXyz();
            float value = (float) (Math.sqrt(xyz[0]*xyz[0])+(xyz[1]*xyz[1])+(xyz[2]*xyz[2]));
            vertex.setDistance(value);
        }
    }
}

