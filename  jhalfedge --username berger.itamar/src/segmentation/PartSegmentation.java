package segmentation;

import attributes.MeshAttribute;
import model.*;
import segmentation.clustering.HierarchicalClustering;
import utils.Vector3D;

import java.util.Collection;

/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartSegmentation implements MeshAttribute {
    HierarchicalClustering hcluster;
    int clusterAmount = 5;

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
        return hcluster.getNumOfSegments();
    }

    public void calculate(IMesh mesh) {
        hcluster = new HierarchicalClustering();
        hcluster.getHierarchicalClustering(mesh);
        while(getClustersAmount()<clusterAmount) {
            goDown();
        }
    }

    public void goUp() {
        hcluster.goUp();
    }

    public void goDown() {
        hcluster.goDown();
    }

}

