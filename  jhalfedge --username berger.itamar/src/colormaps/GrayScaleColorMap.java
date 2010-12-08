package colormaps;

/**
 * User: itamar
 * Date: Oct 30, 2010
 * Time: 11:58:39 AM
 */
public class GrayScaleColorMap implements IColorMap{
    public float[] getColor(float value) {
         return new float[]{value, value, value};
    }
}

