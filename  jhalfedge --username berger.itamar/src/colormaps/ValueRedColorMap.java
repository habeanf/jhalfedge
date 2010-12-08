package colormaps;

/**
 * User: itamar
 * Date: Oct 27, 2010
 * Time: 10:51:32 PM
 */
public class ValueRedColorMap implements IColorMap {
    public float[] getColor(float value) {
        return RGB2HSV.toRgb(1, 1f, value);
    }
}
