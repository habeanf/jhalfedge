package render;

import model.Face;
import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vertex;

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
            if (state.getTransparent()) {
//            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                gl.glColor4f(1f, 1, 1f, 0.5f);
            } else {
                gl.glColor4f(1f, 1, 1f, 1f);

            }

            gl.glBegin(GL.GL_TRIANGLES);
            for (Face face : halfEdgeDataStructure.getAllFaces()) {
                HalfEdge firstHalfEdge = face.getHalfEdge();
                HalfEdge nextHalfEdge = firstHalfEdge;

                do {
                    Vertex vertex = nextHalfEdge.getVertex();
                    gl.glVertex3fv(vertex.getXyz(), 0);
                    nextHalfEdge = nextHalfEdge.getNext();

                } while (nextHalfEdge != firstHalfEdge);
            }
            gl.glEnd();
            gl.glEndList();
            prevState = state;
        }

        gl.glCallList(displayList);
    }

    public void renderFace(GL gl) {
        final List<Vertex> list = halfEdgeDataStructure.getNeighbours(halfEdgeDataStructure.getVertex(1));

        for (Vertex vertex1 : list) {
            System.out.print(vertex1.getId() + " ");
        }

        System.out.println();
        //To change body of created methods use File | Settings | File Templates.
    }
}
