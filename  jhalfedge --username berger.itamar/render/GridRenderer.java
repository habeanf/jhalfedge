package render;

import contoller.Controller;

import javax.media.opengl.GL;

/**
 * This class used for rendering the grid sample
 * User: itamar
 * Date: Oct 30, 2010
 * Time: 12:41:57 PM
 */
public class GridRenderer {
    private float worldSize;
    private float freq;

    // display list number
    private int wire;


    public GridRenderer(float freq) {
        this.worldSize = Controller.WORLD_SIZE;
        this.freq = freq;
    }

    public void render(GL gl) {
        if (wire != 0) {
            gl.glCallList(wire);

        } else {
            float sampleSize = freq * worldSize;
            float edgeSize = worldSize / 2;

            gl.glDeleteLists(2, 1);
            wire = gl.glGenLists(2);
            gl.glNewList(wire, GL.GL_COMPILE);
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
            gl.glColor4f(0.3f, 0.3f, 0.3f, 0.1f);
            // because we are starting to draw from the middle we need add step/2
            for (float z = -edgeSize + sampleSize / 2; z <= edgeSize; z += sampleSize) {
                for (float x = -edgeSize + sampleSize / 2; x <= edgeSize; x += sampleSize) {
                    for (float y = -edgeSize + sampleSize / 2; y <= edgeSize; y += sampleSize) {
                        BoxRenderer.drawBox(gl, x, y, z, sampleSize);
                    }
                }
            }
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
            gl.glEndList();
            gl.glCallList(wire);

        }
    }
}

