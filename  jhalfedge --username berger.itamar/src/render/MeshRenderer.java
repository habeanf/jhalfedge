package render;

import attributes.MeshAttribute;
import colormaps.IColorMap;
import model.HalfEdgeDataStructure;
import model.Vertex;
import model.*;

import javax.media.opengl.GL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: itamar
 * Date: Dec 7, 2010
 * Time: 5:30:42 PM
 */
public class MeshRenderer {

    // hold a cache of openGL display lists
    private int displayList;

    RenderState prevState;

    private final HalfEdgeDataStructure halfEdgeDataStructure;

    public MeshRenderer(HalfEdgeDataStructure halfEdgeDataStructure) {
        this.halfEdgeDataStructure = halfEdgeDataStructure;
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
                if (state.isNeihbourTest()) {
                    renderNeigbours(gl, halfEdgeDataStructure);
                } else {
                    setMinMax(halfEdgeDataStructure);
                    final MeshAttribute attribute = state.getMeshAttribute();

                    if (state.getTransparent()) {
                        float alpha = 0.4f;
                        if (attribute == null) {
                            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                            gl.glColor4f(1f, 1f, 1f, alpha);
                            renderTriangles(gl, attribute, alpha, state.getColorMap());
                        }

                        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                        gl.glColor4f(1f, 1, 1f, 0.4f);
                        renderTriangles(gl, attribute, alpha, state.getColorMap());

                    } else {
                        float alpha = 1f;
                        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                        gl.glColor4f(1f, 1, 1f, alpha);
                        renderTriangles(gl, attribute, alpha, state.getColorMap());
                    }
                }
            } else {
                gl.glColor4f(1f, 1f, 1f, 0.8f);
                renderVertices(gl, halfEdgeDataStructure.getVertexes());
            }


            gl.glEndList();
            prevState = state;
        }

        gl.glCallList(displayList);
    }

    private void renderNeigbours(GL gl, HalfEdgeDataStructure halfEdgeDataStructure) {
        //render face neigbours
        renderFace(gl);

        //render vertex neigbours
        final Vertex vertex = halfEdgeDataStructure.getVertex(2);
        final Set<Vertex> neighbours = halfEdgeDataStructure.getNeighbours(vertex);

        gl.glColor4f(1f, 1f, 1f, 1f);
        renderVertices(gl, neighbours);

        gl.glColor4f(1f, 0f, 0f, 1f);
        renderVertices(gl, Arrays.asList(vertex));


    }

    private void renderVertices(GL gl, Collection<Vertex> vertices) {
        gl.glBegin(GL.GL_POINTS);
        for (Vertex vertex : vertices) {
            gl.glVertex3fv(vertex.getXyz(), 0);
        }
        gl.glEnd();
    }

    private void renderTriangles(GL gl, MeshAttribute attribute, float alpha, IColorMap colormap) {
        for (Face face : halfEdgeDataStructure.getAllFaces()) {
            gl.glBegin(GL.GL_TRIANGLES);
            HalfEdge firstHalfEdge = face.getHalfEdge();
            HalfEdge nextHalfEdge = firstHalfEdge;
            int counter = 0;

            do {
                gl.glNormal3fv(nextHalfEdge.getCornerNormal(), 0);
                
                Vertex vertex = nextHalfEdge.getVertex();
                if (attribute != null) {
                    float value = attribute.getValue(vertex, halfEdgeDataStructure);
                    value = (value / (Math.abs(max - min)));
                    final float[] color = colormap.getColor(value);
                    gl.glColor4f(color[0], color[1], color[2], alpha);
                }

                gl.glVertex3fv(vertex.getXyz(), 0);
                nextHalfEdge = nextHalfEdge.getNext();
                counter++;

                // for ignoring inifinte loop in case of corruption
                if (counter > 4) {
                    break;
                }
            } while (nextHalfEdge != firstHalfEdge);


            gl.glEnd();
        }
    }

    public void renderFace(GL gl) {
        final Set<Face> faces = halfEdgeDataStructure.getNeighbours(halfEdgeDataStructure.getAllFaces().get(0));

        for (Face face : faces) {
            System.out.print("1");
        }
    }


    private float min;
    private float max;

    public void setMinMax(HalfEdgeDataStructure halfEdgeDataStructure) {
        float minx = Integer.MAX_VALUE;
        float maxx = Integer.MIN_VALUE;
        float miny = Integer.MAX_VALUE;
        float maxy = Integer.MIN_VALUE;
        float minz = Integer.MAX_VALUE;
        float maxz = Integer.MIN_VALUE;
        for (Vertex v : halfEdgeDataStructure.getVertexes()) {
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
