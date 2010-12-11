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
        // the normal of a half edges represents the normal of the vertex for the face to the left of the half edge
        // only one such half edge should exist for each vertex
        for (HalfEdge edge : halfEdgeDataStructure.getAllHalfEdges()) {
            Vector3D vp         = new Vector3D(edge.getPrev().getVertex());
            Vector3D v          = new Vector3D(edge.getVertex());
            Vector3D vn         = new Vector3D(edge.getNext().getVertex());

            Vector3D vector1    = v.sub(vp);
            Vector3D vector2    = v.sub(vn);

            // normal is calculated as the cross product of the incoming and outgoing half edges on a vertex
            float result[]      = vector1.calculateCrossProductWith(vector2).normalize().getFloatArray();
            edge.setCornerNormal(result);

        }

        // for each vertex find contiguous faces with a dihedral angle less than given constant and average
        // contiguous faces
        for (Vertex v : halfEdgeDataStructure.getVertexes()) {
            // initializations

            HalfEdge firstHalfEdge = v.getHalfEdge();
            LinkedList<NormalRange> angleRanges = new LinkedList<NormalRange>();

            double currentAngle = 0.0;
            Vector3D currentNormal;
            NormalRange lastRange = null;
            HalfEdge nextEdge = firstHalfEdge;

            // iterate edges to calculate angle ranges
            do {
                // for empty vertexes (usually due to corrupt data)
                if (nextEdge==null){
                    break;
                }

                currentNormal = new Vector3D(nextEdge.getCornerNormal());
                if (nextEdge.getFace()!=null) {
                    currentAngle = currentNormal.calculateAngleTo((new Vector3D(nextEdge.getCornerNormal())));
                }
                if( lastRange != null && currentAngle<MAX_ANGLE_RADIANS) {
                    // new vertex or dihedral angle less than max
                    lastRange.addNormal(nextEdge,currentNormal);
                }
                else {
                    // null face or large than dihedral max
                    // so create new range
                    lastRange = new NormalRange(firstHalfEdge,firstHalfEdge.getOpp().getNext(),currentNormal);
                    angleRanges.add(lastRange);
                }

                nextEdge = nextEdge.getOpp().getNext();

            } while (firstHalfEdge != nextEdge);

            // for each range update it's edges to the average normal of the range
            for (NormalRange range : angleRanges) {

                range.calculateAverageNormal();

                for(HalfEdge currentEdge = range.from; currentEdge!=range.to; currentEdge = currentEdge.getOpp().getNext()) {
                    currentEdge.setCornerNormal(range.getAverageNormal().getFloatArray());
                }
            }

        }
    }

    /**Helper class stores a range of normal values for average calculation
     *
     */
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
