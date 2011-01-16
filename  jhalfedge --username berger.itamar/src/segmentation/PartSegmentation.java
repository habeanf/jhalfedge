package segmentation;

import attributes.MeshAttribute;
import model.*;
import utils.InfoLogger;
/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartSegmentation implements MeshAttribute {
    int clusterAmount = 5;
    private double segmentationThreshold;
    private InfoLogger infoLogger;
    
    public PartSegmentation(InfoLogger infoLogger, double segmentationThreshold) {
        this.segmentationThreshold = segmentationThreshold;
        this.infoLogger = infoLogger;
    }

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
        for (IFace face : mesh.getAllFaces()) {
            face.setSegment(-1);
            face.setRelatedFaceId(-1);
        }

        RegionGrowClustering regionGrowClustering = new RegionGrowClustering(mesh, segmentationThreshold,infoLogger);

        regionGrowClustering.calculate();

        clusterAmount = regionGrowClustering.getClusterAmount();


    }
}

