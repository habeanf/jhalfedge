package attributes.contraction;

import model.Face;
import model.IFace;
import model.IVertex;
import model.Vertex;

import java.util.List;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:13 PM
 */
public class DecimationRecord {
    // the vertex that is going to be deleted from the mesh (v2)
    public int deletedVertexId;

    // the contracted to vertex (v1)
    public int otherVertexId;

    // the first deleted triangle
    public int firstRemovedTriangleId;
    public int[] firstTriangleVerticesIds;

    // the second deleted triangle (optional)    
    public int secondRemovedTriangleId;
    public int[] secondTriangleVerticesIds;

    // the list of triangles that change one of their vertices from v2 to v1;
    public List<Integer> changedTrianglesIds;
}
