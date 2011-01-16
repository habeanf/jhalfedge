package segmentation;

import attributes.MeshAttribute;
import model.*;
import utils.Vector3D;

import java.util.Collection;

/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartSegmentation implements MeshAttribute {
    private int clusterAmount = 5;

    public String getName() {
        return "Part Based Segmentation";
    }

    public float getValue(IVertex vertex) {
        return 0;
    }

    public boolean doFaceRendering() {
        return true;
    }

    public int getClustersAmount() {
        return clusterAmount;
    }

    public void calculate(IMesh mesh) {
        final Collection<IFace> allFaces = mesh.getAllFaces();

        for (IFace face : allFaces) {
            face.setSegment((int) (Math.random()*clusterAmount));
        }
    }

}

