package colormaps;

/**
 * User: itamar
 * Date: Nov 1, 2010
 * Time: 11:30:45 PM
 */
public class HueColorMap implements IColorMap {
    public float[] getColor(float value) {
        return RGB2HSV.toRgb(value * 360, 1f, 1f);
    }
}
