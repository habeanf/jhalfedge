package render;

import attributes.MeshAttribute;

/**
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 9:59:28 AM
 */
public class RenderState {

    private boolean shouldUpdate = false;
    private boolean transparent = true;
    private boolean isMesh = true;
    private MeshAttribute meshAttribute;

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
}
