package render;

import attributes.MeshAttribute;
import colormaps.ColorMapFactory;
import colormaps.IColorMap;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 9:59:28 AM
 */
public class RenderState {
    public boolean shouldUpdate = false;
    private boolean transparent = false;
    private boolean isMesh = true;
    private MeshAttribute meshAttribute;

    private boolean calculatedGaussian = false;
    private boolean calculatedCentricity = false;
    private boolean calculatedDistance = false;

    private boolean vertexNeighbourTest = false;
    private boolean faceNeighbourTest = false;
    private boolean showCornerNormals = false;

    private IColorMap colorMap = ColorMapFactory.getFirstColorMap();

    public boolean getTransparent() {
        return transparent;
    }

    public void toggleTransparent() {
        transparent = !transparent;
        shouldUpdate = true;
    }

    boolean isChanged() {
        boolean stateChanged = shouldUpdate;
        shouldUpdate = false;
        return stateChanged;

    }

    public void toggleCloud() {
        isMesh = !isMesh;
        shouldUpdate = true;
    }

    public boolean isMesh() {
        return isMesh;
    }

    public MeshAttribute getMeshAttribute() {
        return meshAttribute;
    }

    public void setMeshAttribute(MeshAttribute meshAttribute) {
        this.meshAttribute = meshAttribute;
        shouldUpdate = true;
    }

    public boolean isCalculatedGaussian() {
        return calculatedGaussian;
    }

    public void setCalculatedGaussian(boolean calculatedGaussian) {
        this.calculatedGaussian = calculatedGaussian;
    }

    public boolean isCalculatedCentricity() {
        return calculatedCentricity;
    }

    public void setCalculatedCentricity(boolean calculatedCentricity) {
        this.calculatedCentricity = calculatedCentricity;
    }

    public boolean isCalculatedDistanceToCentroid() {
        return calculatedDistance;
    }

    public void setCalculatedDistance(boolean calculatedDistance) {
        this.calculatedDistance = calculatedDistance;
    }

    public void transperacy(boolean transparent) {
        this.transparent = transparent;
        shouldUpdate = true;
    }

    public void setAttributeColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        shouldUpdate = true;
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void toggleVertexNeighbourTest() {
        vertexNeighbourTest = !vertexNeighbourTest;
        faceNeighbourTest = false;
        shouldUpdate = true;
    }

    public void toggleFaceNeighbourTest() {
        faceNeighbourTest = !faceNeighbourTest;
        vertexNeighbourTest = false;
        shouldUpdate = true;
    }

    public boolean isVertexNeihbourTest() {
        return vertexNeighbourTest;
    }

    public boolean isFaceNeihbourTest() {
        return faceNeighbourTest;
    }

    public void toggleShowCornerNormals() {
        showCornerNormals = !showCornerNormals;
        shouldUpdate = true;
    }

    public boolean isShowCornerNormals() {
        return showCornerNormals;
    }
}
