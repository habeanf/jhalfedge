package model;
import java.util.*;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 8:48:45 PM
 */
public class Face implements IFace {
    private int id;

    private List<IVertex> vertices;

    private Vector3D normal;

    private int relatedFaceId = -1;
    private int segment = -1;

    public Face(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public double calcDihedralAngle(IFace face1, IFace face2) {
        return face1.getNormal().calculateAngleTo(face2.getNormal());
    }

    public void removeVertex(IVertex vertex) {
        final Iterator<IVertex> iterator = vertices.iterator();

        while (iterator.hasNext()) {
            final IVertex next = iterator.next();

            if (next == vertex) {
                iterator.remove();
            }
        }
    }

    public boolean isValidReplacement(IVertex fromVertex, IVertex toVertex) {
        return !vertices.contains(toVertex);
    }

    public void setRelatedFaceId(int relatedFaceId) {
        this.relatedFaceId = relatedFaceId;
    }

    public int getRelatedFaceId() {
        return relatedFaceId;
    }

    public boolean replaceVertex(IVertex fromVertex, IVertex toVertex) {
        List<IVertex> newVertices = new ArrayList<IVertex>();

        // in case the vertex already exists, no replacement should be done, this a valid state
        if (vertices.contains(toVertex)) {
            return false;
        }

        for (IVertex vertex : vertices) {
            if (vertex == fromVertex) {
                newVertices.add(toVertex);
            } else {
                newVertices.add(vertex);
            }
        }

        this.vertices = newVertices;

        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSegment() {
        return segment;
    }

    public List<IVertex> getVertices() {
        if (vertices == null) {
            return new ArrayList<IVertex>();
        }

        return vertices;
    }

    public void addVertices(List<IVertex> vertices) {
        if (this.vertices == null) {
            this.vertices = new ArrayList<IVertex>(vertices.size());
        }

        this.vertices.addAll(vertices);
    }

    public void setVertices(List<IVertex> vertices) {
        this.vertices = vertices;
        calcNormal();
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public void calcNormal() {
        if (vertices.size() == 3) {
            Vector3D vp = new Vector3D(vertices.get(0).getXyz());
            Vector3D v = new Vector3D(vertices.get(1).getXyz());
            Vector3D vn = new Vector3D(vertices.get(2).getXyz());

            Vector3D vector1 = v.sub(vp);
            Vector3D vector2 = v.sub(vn);

            // normal is calculated as the cross product of the incoming and outgoing half edges on a vertex
            normal = vector1.calculateCrossProductWith(vector2).normalize();
        }
    }

    public Vector3D getNormal() {
        return normal;
    }

    public Set<IFace> getNeighbors() {
        Set<IFace> returnSet = new HashSet<IFace>();

        // return all faces of the this face's vertices
        for (IVertex vertex : vertices) {
            returnSet.addAll(vertex.getFaces());
        }

        // remove this face from the set
        returnSet.remove(this);

        return returnSet;
    }

    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }
}
