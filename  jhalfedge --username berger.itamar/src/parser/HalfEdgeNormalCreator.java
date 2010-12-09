package parser;

import model.Face;
import model.HalfEdgeDataStructure;
import model.Vertex;

import java.util.Set;

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
        // calc normal for all the faces
        for (Face face : halfEdgeDataStructure.getAllFaces()) {
            final Vertex[] vertices = halfEdgeDataStructure.getFaceVertices(face);
            float[] normal = calcNormalForFace(vertices);
            face.setNormal(normal);
        }

        // for each vertices calculate its normal
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            calcNormalForCorner(vertex);
        }
    }

    private void calcNormalForCorner(Vertex vertex){
        // get neighbour faces
        final Set<Face> faces = halfEdgeDataStructure.getFaceNeighbours(vertex);
        for (Face face : faces) {
            //TODO
        }
    }

    private float[] calcNormalForFace(Vertex[] vertices) {
        ///TODO
        return new float[0];  //To change body of created methods use File | Settings | File Templates.
    }
}
