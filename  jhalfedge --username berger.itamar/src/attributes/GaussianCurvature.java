package attributes;

import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vector3D;
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

    public boolean doFaceRendering() {
        return false;
    }

    public int getClustersAmount() {
        return 0;
    }

    /**Calculate Gaussian Curvature of vertexes
     * Formula implemented uses the full area of the triangle when dividing 2*PI - total angles
     * (as opposed to the local area) 
     *
     * @param halfEdgeDataStructure
     */
    public void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            HalfEdge firstEdge      = vertex.getHalfEdge();
            HalfEdge currentEdge    = firstEdge;
            double totalArea         = 0.0;
            double totalAngle        = 0.0;
            
            Vector3D prevVertex,currentVertex,nextVertex;
            Vector3D vector1,vector2;
            if (currentEdge != null) {  // for problem models like liberty
                do {
                    currentVertex   = new Vector3D(currentEdge.getVertex());
                    prevVertex      = new Vector3D(currentEdge.getPrev().getVertex());
                    nextVertex      = new Vector3D(currentEdge.getNext().getVertex());

                    // calculate area and triangle for current face
                    vector1         = currentVertex.sub(prevVertex);
                    vector2         = currentVertex.sub(nextVertex);

                    totalArea       += vector1.calculateTriangleArea(vector2);
                    totalAngle      += vector1.calculateAngleTo(vector2);

                    // get next face
                    currentEdge = currentEdge.getOpp().getNext();
                } while (!currentEdge.equals(firstEdge));

                vertex.setGaussianCurvature((float)((2*Math.PI - totalAngle)/totalArea));
            }
        }
    }
}
