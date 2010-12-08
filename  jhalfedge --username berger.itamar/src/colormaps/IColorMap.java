package colormaps;

/**
 * Interface the represent a conversion from a normalized 0-1 float value to RGB color 
 * User: itamar
 * Date: Oct 27, 2010
 * Time: 10:50:35 PM
 */
public interface IColorMap {
    float[] getColor(float value);
}
