package parser;

import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vector3D;
import model.Vertex;

import java.util.LinkedList;


/**
 * User: itamar
 * Date: Dec 9, 2010
 * Time: 11:15:23 AM
 */
public class HalfEdgeNormalCreator {

    private static final double MAX_ANGLE = 100;
    private static final double MAX_ANGLE_RADIANS = Math.toRadians(MAX_ANGLE);

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
            float result[]      = vector1.calculateCrossProductWith(vector2).normalize().getFloatArray();
            edge.setCornerNormal(result);

        }


        for (Vertex v : halfEdgeDataStructure.getVertexes()) {
            HalfEdge firsHalfEdge = v.getHalfEdge();
            LinkedList<NormalRange> angleRanges = new LinkedList<NormalRange>();

            double currentAngle = 0.0;
            Vector3D currentNormal;
            NormalRange lastRange = null;
            HalfEdge nextEdge = firsHalfEdge;
            // iterate edges to calculate angle ranges
            int i=0;
            do {
                currentNormal = new Vector3D(nextEdge.getCornerNormal());
                if (nextEdge.getFace()!=null) {
                    currentAngle = currentNormal.calculateAngleTo((new Vector3D(nextEdge.getCornerNormal())));
                }
                if( lastRange != null && currentAngle<MAX_ANGLE_RADIANS) {
                    lastRange.addNormal(nextEdge,currentNormal);
                }
                else {
                    lastRange = new NormalRange(firsHalfEdge,nextEdge,currentNormal);
                    angleRanges.add(lastRange);
                }

                nextEdge = nextEdge.getOpp().getNext();

            } while (firsHalfEdge != nextEdge);

            // set average normal
            for (NormalRange range : angleRanges) {

                range.calculateAverageNormal();

                for(HalfEdge currentEdge = range.from; currentEdge!=range.to; currentEdge = currentEdge.getOpp().getNext()) {
                    currentEdge.setCornerNormal(range.getAverageNormal().getFloatArray());
                }
            }

        }
    }

    private class NormalRange {
        HalfEdge from,to;
        Vector3D totalNormal;
        int      numberOfNormals;
        Vector3D averageNormal;

        public void calculateAverageNormal() {
            totalNormal.scale(1f/numberOfNormals);
            averageNormal = totalNormal.normalize();
        }

        public Vector3D getAverageNormal() {
            return averageNormal;
        }
        
        public void addNormal(HalfEdge to, Vector3D normal) {
            this.to = to;
            totalNormal.add(normal);
            numberOfNormals++;
        }

        public NormalRange(HalfEdge from, HalfEdge to, Vector3D normal) {
            this.from = from;
            this.to = to;
            this.totalNormal = normal;
            numberOfNormals = 1;
            averageNormal = null;
        }
    }

}
