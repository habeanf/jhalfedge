package segmentation;

import attributes.MeshAttribute;
import model.Face;
import model.IMesh;
import model.IVertex;
import model.Vertex;
import utils.Vector3D;

/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartSegmentation implements MeshAttribute {
    private int clusterAmount = 0;

    public String getName() {
        return "Part Based Segmentation";
    }

    public float getValue(IVertex vertex) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean doFaceRendering() {
        return true;
    }

    public int getClustersAmount() {
        return clusterAmount;
    }

    public void calculate(IMesh mesh) {
    }

}

