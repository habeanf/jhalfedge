package colormaps;

/**
 * User: itamar
 * Date: Nov 2, 2010
 * Time: 10:09:08 PM
 */
public class MirroredHue implements IColorMap{
    public float[] getColor(float value) {
        if (value > 0.5){
            return RGB2HSV.toRgb(((value*2)-1) * 360, 1f, 1f);
        }
        else{
            return RGB2HSV.toRgb((1-(value*2)) * 360, 1f, 1f);
        }
    }
}
