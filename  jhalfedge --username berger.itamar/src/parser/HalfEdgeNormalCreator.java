package parser;

import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vector3D;


/**
 * User: itamar
 * Date: Dec 9, 2010
 * Time: 11:15:23 AM
 */
public class HalfEdgeNormalCreator {

    private static final float MAX_ANGLE = 100;

    HalfEdgeDataStructure halfEdgeDataStructure;

    public HalfEdgeNormalCreator(HalfEdgeDataStructure halfEdgeDataStructure) {
        this.halfEdgeDataStructure = halfEdgeDataStructure;
    }

    public void calcNormals() {        
        // calc normal for all the half edges
        for (HalfEdge edge : halfEdgeDataStructure.getAllHalfEdges()) {
            Vector3D vp         = new Vector3D(edge.getPrev().getVertex());
            Vector3D v          = new Vector3D(edge.getVertex());
            Vector3D vn         = new Vector3D(edge.getNext().getVertex());

            Vector3D vector1    = v.sub(vp);
            Vector3D vector2    = v.sub(vn);
            edge.setCornerNormal(vector1.calculateCrossProductWith(vector2).getFloatArray());
        }


//        for (Vertex v : halfEdgeDataStructure.getVertexes()) {
//            HalfEdge currentEdge = v.getHalfEdge();
//
//        }
    }

}
