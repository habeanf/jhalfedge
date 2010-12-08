package render;

import javax.media.opengl.GL;

/**
 * The class render the bounding box of the world
 * User: itamar
 * Date: Oct 31, 2010
 * Time: 7:26:24 PM
 */
public class BoxRenderer {
        public static void drawBox(GL gl, float x, float y, float z, float edgeSize) {
        edgeSize /= 2;
        float[] v0 = {x - edgeSize,y - edgeSize,z - edgeSize };
        float[] v1 = {x + edgeSize,y - edgeSize,z - edgeSize };
        float[] v2 = {x + edgeSize,y - edgeSize,z + edgeSize };
        float[] v3 = {x - edgeSize,y - edgeSize,z + edgeSize };
        float[] v4 = {x - edgeSize,y + edgeSize,z - edgeSize };
        float[] v5 = {x + edgeSize,y + edgeSize,z - edgeSize };
        float[] v6 = {x + edgeSize,y + edgeSize,z + edgeSize };
        float[] v7 = {x - edgeSize,y + edgeSize,z + edgeSize };

        gl.glBegin(GL.GL_QUADS);         // Draw A Quad
        gl.glVertex3fv(v5,0);            // Top Right Of The Quad (Top)
        gl.glVertex3fv(v4,0);            // Top Left Of The Quad (Top)
        gl.glVertex3fv(v7,0);            // Bottom Left Of The Quad (Top)
        gl.glVertex3fv(v6,0);            // Bottom Right Of The Quad (Top)

        gl.glVertex3fv(v2,0);            // Top Right Of The Quad (Bottom)
        gl.glVertex3fv(v3,0);            // Top Left Of The Quad (Bottom)
        gl.glVertex3fv(v0,0);            // Bottom Left Of The Quad (Bottom)
        gl.glVertex3fv(v1,0);            // Bottom Right Of The Quad (Bottom)

        gl.glVertex3fv(v6,0);            // Top Right Of The Quad (Front)
        gl.glVertex3fv(v7,0);            // Top Left Of The Quad (Front)
        gl.glVertex3fv(v3,0);            // Bottom Left Of The Quad (Front)
        gl.glVertex3fv(v2,0);            // Bottom Right Of The Quad (Front)

        gl.glVertex3fv(v1,0);            // Bottom Left Of The Quad (Back)
        gl.glVertex3fv(v0,0);            // Bottom Right Of The Quad (Back)
        gl.glVertex3fv(v4,0);            // Top Right Of The Quad (Back)
        gl.glVertex3fv(v5,0);            // Top Left Of The Quad (Back)

        gl.glVertex3fv(v7,0);            // Top Right Of The Quad (Left)
        gl.glVertex3fv(v4,0);            // Top Left Of The Quad (Left)
        gl.glVertex3fv(v0,0);            // Bottom Left Of The Quad (Left)
        gl.glVertex3fv(v3,0);            // Bottom Right Of The Quad (Left)

        gl.glVertex3fv(v5,0);            // Top Right Of The Quad (Right)
        gl.glVertex3fv(v6,0);            // Top Left Of The Quad (Right)
        gl.glVertex3fv(v2,0);            // Bottom Left Of The Quad (Right)
        gl.glVertex3fv(v1,0);            // Bottom Right Of The Quad (Right)
        gl.glEnd();                // Done Drawing The Quad
    }
}
