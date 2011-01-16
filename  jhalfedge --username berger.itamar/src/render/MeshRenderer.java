package render;

import attributes.MeshAttribute;
import model.*;

import javax.media.opengl.GL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * User: itamar
 * Date: Dec 7, 2010
 * Time: 5:30:42 PM
 */
public class MeshRenderer {

    // hold a cache of openGL display lists
    private int displayList;

    // hold the current renderer state
    RenderState prevState;

    private final IMesh mesh;

    public MeshRenderer(IMesh mesh) {
        this.mesh = mesh;
    }

    public void render(GL gl, RenderState state) {
        if (displayList == 0 || state.isChanged()) {
            if (displayList != 0) {
                gl.glDeleteLists(1, 1);
            }

            displayList = gl.glGenLists(1);
            gl.glNewList(displayList, GL.GL_COMPILE);

            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

            if (state.isMesh()) {
                // a method for displaying the neigbours of a face/vertex
                if (state.isVertexNeihbourTest()) {
                    renderVertexNeigbours(gl, mesh);
                } else if (state.isFaceNeihbourTest()) {
                    renderFaceNeigbours(gl, mesh);
                } else {
                    setMinMax(mesh);

                    if (state.getMeshAttribute() != null && state.getMeshAttribute().doFaceRendering()) {
                        renderfaces(gl, state, mesh.getAllFaces());
                    } else {
                        if (state.getTransparent()) {
                            float alpha = 0.5f;
                            if (state.getMeshAttribute() == null) {
                                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                                gl.glColor4f(1f, 1f, 1f, alpha);
                                renderTriangles(gl, alpha, state, mesh.getAllFaces(), true);
                            }

                            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                            gl.glColor4f(1f, 1, 1f, 0.5f);
                            renderTriangles(gl, alpha, state, mesh.getAllFaces(), true);

                        } else {
                            float alpha = 1f;
                            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                            gl.glColor4f(1f, 1, 1f, alpha);
                            renderTriangles(gl, alpha, state, mesh.getAllFaces(), true);
                        }
                    }
                }
            } else {
                // draw cloud points
                gl.glColor4f(1f, 1f, 1f, 0.8f);
                renderVertices(gl, mesh.getAllVertices());
            }

            gl.glEndList();
            prevState = state;
        }

        gl.glCallList(displayList);
    }

    private void renderfaces(GL gl, RenderState state, Collection<IFace> faces) {
        float amount = state.getMeshAttribute().getClustersAmount();

        for (IFace candidate : faces) {
            if (candidate != null) {
                final int segment = candidate.getSegment();
                final float[] color = state.getColorMap().getColor(segment/amount);
                gl.glColor4f(color[0], color[1], color[2], 1);
                renderTriangles(gl, 1, state, Arrays.asList(candidate), false);
            }
        }
    }

    private void renderVertexNeigbours(GL gl, IMesh mesh) {
//        for (int i = 1; i < 100; i++) {
//            IVertex candidate = mesh.getVertex((int) (1 + Math.random() * mesh.getAllVertices().size()));
//
//            if (candidate != null) {
//                final Set<IVertex> neighbours = mesh.getNeighbours(candidate);
//                float[] values = {(float) Math.random(), (float) Math.random(), (float) Math.random()};
//                gl.glColor4f(values[0], values[1], values[2], 1f);
//                renderVertices(gl, neighbours);
//
//                gl.glColor4f(1f, 1f, 1f, 1f);
//                renderVertices(gl, Arrays.asList(candidate));
//            }
//        }
    }

    private void renderFaceNeigbours(GL gl, IMesh mesh) {
//        for (int i = 1; i < 100; i++) {
//            Face candidate = mesh.getAllFaces().get((int) (Math.random() * mesh.getAllFaces().size()));
//
//            if (candidate != null) {
//                final Set<Face> neighbours = mesh.getNeighbours(candidate);
//                float[] values = {(float) Math.random(), (float) Math.random(), (float) Math.random()};
//                gl.glColor4f(values[0], values[1], values[2], 1f);
//                renderTriangles(gl, 0.5f, prevState, neighbours, true);
//
//                gl.glColor4f(1f, 1f, 1f, 1f);
//                renderTriangles(gl, 0.5f, prevState, Arrays.asList(candidate), true);
//            }
//        }
    }

    private void renderVertices(GL gl, Collection<IVertex> vertices) {
        gl.glBegin(GL.GL_POINTS);
        for (IVertex vertex : vertices) {
            gl.glVertex3fv(vertex.getXyz(), 0);
        }
        gl.glEnd();
    }

    private void renderTriangles(GL gl, float alpha, RenderState state, Collection<IFace> faces, boolean overrideColor) {
        for (IFace face : faces) {
            gl.glBegin(GL.GL_TRIANGLES);

            gl.glNormal3fv(face.getNormal().getFloatArray(),0);
            for (IVertex vertex : face.getVertices()) {
                MeshAttribute attribute = state.getMeshAttribute();

                if (overrideColor) {
                    if (attribute != null) {
                        float value = attribute.getValue(vertex);
                        value = (value / (Math.abs(max - min)));
                        final float[] color = state.getColorMap().getColor(value);
                        gl.glColor4f(color[0], color[1], color[2], alpha);
                    }
                }

                gl.glVertex3fv(vertex.getXyz(), 0);

            }

            gl.glEnd();
        }
    }



    private float min;
    private float max;

    public void setMinMax(IMesh mesh) {
        float minx = Integer.MAX_VALUE;
        float maxx = Integer.MIN_VALUE;
        float miny = Integer.MAX_VALUE;
        float maxy = Integer.MIN_VALUE;
        float minz = Integer.MAX_VALUE;
        float maxz = Integer.MIN_VALUE;
        for (IVertex v : mesh.getAllVertices()) {
            final float[] xyz = v.getXyz();
            minx = Math.min(minx, xyz[0]);
            maxx = Math.max(maxx, xyz[0]);
            miny = Math.min(miny, xyz[1]);
            maxy = Math.max(maxy, xyz[1]);
            minz = Math.min(minz, xyz[2]);
            maxz = Math.max(maxz, xyz[2]);
        }
        min = (minx + miny + minz) / 3;
        max = (maxx + maxy + maxz) / 3;
    }
}
