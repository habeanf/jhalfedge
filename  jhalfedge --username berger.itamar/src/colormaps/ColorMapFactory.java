package colormaps;

import java.util.ArrayList;
import java.util.List;

/**
 * This factory class hold all types of ColorMaps
 * User: itamar
 * Date: Oct 31, 2010
 * Time: 11:29:01 PM
 */
public class ColorMapFactory {
    private static List<IColorMap> colormapList = new ArrayList<IColorMap>();

    static {
        colormapList.add(new HueColorMap());
        colormapList.add(new GrayScaleColorMap());
        colormapList.add(new ValueRedColorMap());
        colormapList.add(new MirroredHue());
    }

    static int counter = 0;

    public static IColorMap getNextColorMap() {
        counter %= colormapList.size();
        return colormapList.get(counter++);
    }

    public static IColorMap getFirstColorMap() {
        counter = 0;
        return colormapList.get(counter++);
    }
}
