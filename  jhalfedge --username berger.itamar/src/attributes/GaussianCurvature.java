package attributes;

import model.HalfEdge;
import model.HalfEdgeDataStructure;
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

    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return vertex.getGaussianCurvature();
    }

    public static void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            HalfEdge firstEdge      = vertex.getHalfEdge();
            HalfEdge currentEdge    = firstEdge;
            double totalArea         = 0.0;
            double totalAngle        = 0.0;
            
            double vector1Size,vector2Size,currentAngle;
            Vertex prevVertex,currentVertex,nextVertex;
            double[] vector1,vector2;
            do {
                currentVertex   = currentEdge.getVertex();
                prevVertex      = currentEdge.getPrev().getVertex();
                nextVertex      = currentEdge.getNext().getVertex();

                // calculate area and triangle for current face
                vector1         = calculateVector(currentVertex,prevVertex);
                vector2         = calculateVector(currentVertex,nextVertex);

                vector1Size     = calculateSize(vector1);
                vector2Size     = calculateSize(vector2);
                currentAngle    = calculateAngle(vector1,vector2,vector1Size,vector2Size);
                totalArea       += calculateTriangleArea(vector1Size,vector2Size,currentAngle);
                totalAngle      += currentAngle;

                // get next face
                currentEdge = currentEdge.getOpp().getNext();
            } while (!currentEdge.equals(firstEdge));
            vertex.setGaussianCurvature((float)((2*Math.PI - totalAngle)/totalArea));
        }
    }

    private static double[] calculateVector(Vertex vertex1, Vertex vertex2) {
        float v1[]      = vertex1.getXyz();
        float v2[]      = vertex2.getXyz();

        double result[] = {(double)(v2[0]-v1[0]),(double)(v2[1]-v1[1]),(double)(v2[2]-v1[2])};

        return result;
    }

    private static double calculateDot(double[] vector1, double[] vector2) {
        return vector1[0]*vector2[0] + vector1[1]*vector2[1] + vector1[2]*vector2[2];
    }

    private static double calculateSize(double[] vector) {
        return Math.sqrt(calculateDot(vector,vector));
    }
    
    private static double calculateAngle(double[] vector1, double[] vector2, double vector1Size, double vector2Size) {
        return Math.acos((calculateDot(vector1,vector2)/(vector1Size*vector2Size)));
    }

    private static double calculateTriangleArea(double vector1Size, double vector2Size, double angle) {
        return 0.5*vector1Size*vector2Size*Math.sin(angle);
    }
}
