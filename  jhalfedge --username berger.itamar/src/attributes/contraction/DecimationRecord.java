package attributes.contraction;

import java.util.List;

/**
 * User: itamar
 * Date: 1/13/11
 * Time: 9:13 PM
 */
public class DecimationRecord {
    // the vertex that is going to be deleted from the mesh (v2)
    public int deletedVertex;

    // the contracted to vertex (v1)
    public int otherVertex;

    // the first deleted triangle
    public int firstRemovedTriangleIndex = -1;
    public int[] firstTriangleVertices;

    // the second deleted triangle (optional)    
    public int secondRemovedTriangleIndex = -1;
    public int[] secondTriangleVertices;

    // the list of triangles that change one of their vertices from v2 to v1;
    public List<Integer> changedTrianglesIds;
    
}
