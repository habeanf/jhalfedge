package segmentation;

import attributes.MeshAttribute;
import model.Face;
import model.HalfEdgeDataStructure;
import model.Vertex;

/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartBasedSegment implements MeshAttribute {
    private int clusterAmount = 0;

    public String getName() {
        return "Part Based Segmentation";
    }

    public boolean doFaceRendering() {
        return true;
    }

    public int getClustersAmount() {
        return clusterAmount;
    }

    public void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        // calulate the segmentation
        //TODO

        // assign an id to each face
        clusterAmount = 5;
        for (Face face : halfEdgeDataStructure.getAllFaces()) {
            final float[] xyz = face.getHalfEdge().getVertex().getXyz();
            float value = (float) (Math.sqrt(xyz[0] * xyz[0]) + (xyz[1] * xyz[1]) + (xyz[2] * xyz[2]));
            face.setSegment((int) (value*clusterAmount));
        }

    }

    // ignore
    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return 0;
    }
}

