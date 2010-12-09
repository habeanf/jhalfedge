package render;

import attributes.MeshAttribute;
import colormaps.IColorMap;
import model.HalfEdgeDataStructure;
import model.Vertex;
import model.*;

import javax.media.opengl.GL;
import java.util.List;

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
                final MeshAttribute attribute = state.getMeshAttribute();

                if (state.getTransparent()) {
                    float alpha = 0.4f;
                    if (attribute == null) {
                        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                        gl.glColor4f(1f, 1f, 1f, alpha);
                        renderTriangles(gl, attribute,alpha,state.getColorMap());
                    }

                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                    gl.glColor4f(1f, 1, 1f, 0.4f);
                    renderTriangles(gl, attribute,alpha,state.getColorMap());

                } else {
                    float alpha = 1f;      
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                    gl.glColor4f(1f, 1, 1f, alpha);
                    renderTriangles(gl, attribute, alpha,state.getColorMap());
                }
            } else {
                gl.glColor4f(1f, 1f, 1f, 0.8f);
                renderVertices(gl);
            }


            gl.glEndList();
            prevState = state;
        }

        gl.glCallList(displayList);
    }

    private void renderVertices(GL gl) {
        gl.glBegin(GL.GL_POINTS);
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
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

                Vertex vertex = nextHalfEdge.getVertex();

                if (attribute != null) {
                    final float value = attribute.getValue(vertex,halfEdgeDataStructure);
                    final float[] color = colormap.getColor(value);
                    gl.glColor4f(color[0], color[1], color[2], alpha);
                }

                gl.glVertex3fv(vertex.getXyz(), 0);
                nextHalfEdge = nextHalfEdge.getNext();
                counter++;

                if (counter>4){
                    break;
                }
            } while (nextHalfEdge != firstHalfEdge);


            gl.glEnd();
        }
    }

    public void renderFace(GL gl) {
        final List<Vertex> list = halfEdgeDataStructure.getNeighbours(halfEdgeDataStructure.getVertex(1));

        for (Vertex vertex1 : list) {
            System.out.print(vertex1.getId() + " ");
        }
    }
}
