package parser;

import model.HalfEdge;
import model.HalfEdgeDataStructure;


/**
 * Created by IntelliJ IDEA.
 * User: habeanf
 * Date: Dec 8, 2010
 * Time: 11:10:50 PM
 * 
 */
public class HalfEdgeTester {
    public static void main(String[] args) {
            final long l1 = System.currentTimeMillis();

            HalfEdgeReader gen = new HalfEdgeReader(true,true);
            final HalfEdgeDataStructure halfEdgeDataStructure =
                    gen.get("/Users/amirmore/Downloads/ex2/src/Models/Candil.obj",true);

            int halfCountFace =0 ;
            for (HalfEdge halfEdge : halfEdgeDataStructure.getAllHalfEdges()) {
                if (halfEdge.getFace() == null){
                    halfCountFace++;
                }
            }
            System.out.println("halfCount " + halfCountFace);
            final long l2 = System.currentTimeMillis();

            System.out.println(l2 - l1);
        }
}
