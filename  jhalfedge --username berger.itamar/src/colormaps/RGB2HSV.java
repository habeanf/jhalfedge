package colormaps;

/**
 * User: itamar
 * Date: Nov 1, 2010
 * Time: 11:21:48 PM
 */
public class RGB2HSV {

    public static float[] toHsv(float r, float g, float b) {
        float h;
        float s;
        float v;

        float min, max, delta;
        min = Math.min(r, Math.min(g, b));
        max = Math.max(r, Math.max(g, b));
        v = max;                // v
        delta = max - min;
        if (max != 0) {
            s = delta / max;        // s
        } else {
            // r = g = b = 0		// s = 0, v is undefined
            s = 0;
            h = -1;
            return new float[]{h,s,v};
        }
        if (r == max) {
            h = (g - b) / delta;        // between yellow & magenta
        } else if (g == max) {
            h = 2 + (b - r) / delta;    // between cyan & yellow
        } else {
            h = 4 + (r - g) / delta;    // between magenta & cyan
        }
        h *= 60;                // degrees
        if (h < 0)
            h += 360;

        return new float[]{h,s,v};
    }

    public static float[] toRgb(float h, float s, float v) {
        float r;
        float g;
        float b;

        	int i;
	float f, p, q, t;
	if( s == 0 ) {
		// achromatic (grey)
		r = g = b = v;
		return new float[]{r,g,b};
	}
	h /= 60;			// sector 0 to 5
	i = (int) Math.floor( h );
	f = h - i;			// factorial part of h
	p = v * ( 1 - s );
	q = v * ( 1 - s * f );
	t = v * ( 1 - s * ( 1 - f ) );
	switch( i ) {
		case 0:
			r = v;
			g = t;
			b = p;
			break;
		case 1:
			r = q;
			g = v;
			b = p;
			break;
		case 2:
			r = p;
			g = v;
			b = t;
			break;
		case 3:
			r = p;
			g = q;
			b = v;
			break;
		case 4:
			r = t;
			g = p;
			b = v;
			break;
		default:		// case 5:
			r = v;
			g = p;
			b = q;
			break;
	}
        return new float[]{r,g,b};
    }
}
