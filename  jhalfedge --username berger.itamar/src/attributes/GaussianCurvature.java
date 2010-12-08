package attributes;

import model.HalfEdgeDataStructure;
import model.Vertex;

import java.util.List;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:15:39 PM
 */
public class GaussianCurvature implements MeshAttribute {
    private static final float TWOPI = (float) (Math.PI*2);  

    public String getName() {
        return "Gaussian Curvature";
    }

    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return vertex.getGaussianCurvature();
    }

    public static void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            //final List<Vertex> neigbVertices = halfEdgeDataStructure.getNeighbours(vertex);
            vertex.setGaussianCurvature(0.6f);
        }
    }
}
