package attributes.contraction;

import model.Face;
import model.Vertex;

import java.util.List;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:13 PM
 */
public class DecimationRecord {
    // the vertex that is going to be deleted from the mesh (v2)
    public Vertex deletedVertex;

    // the contracted to vertex (v1)
    public Vertex otherVertex;

    // the first deleted triangle
    public Face firstRemovedTriangle;
    public List<Integer> firstTriangleVertices;

    // the second deleted triangle (optional)    
    public Face secondRemovedTriangle;
    public List<Integer> secondTriangleVertices;

    // the list of triangles that change one of their vertices from v2 to v1;
    public List<Face> changedTriangles;
    
}
