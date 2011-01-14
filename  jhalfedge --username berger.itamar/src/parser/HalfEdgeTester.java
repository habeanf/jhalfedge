package parser;

import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vertex;

import java.util.Set;


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
                    gen.get("/Users/amirmore/Downloads/Models/drill.obj",true);

            Set<Vertex> result = halfEdgeDataStructure.getNeighbours(halfEdgeDataStructure.getVertex(69));
            

            int halfCountFace =0 ;
            for (HalfEdge halfEdge : halfEdgeDataStructure.getAllHalfEdges()) {
                if (halfEdge.getFace() == null){
                    halfCountFace++;
                }
            }
            System.out.println("res size " + result.size());
            final long l2 = System.currentTimeMillis();

            System.out.println(l2 - l1);
        }
}
