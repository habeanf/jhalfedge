package model;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 8:48:45 PM
 */
public class Face {
    public HalfEdge halfEdge;
    public float[] normal;

    public Face(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public float[] getNormal() {
        return normal;
    }

    public void setNormal(float[] normal) {
        this.normal = normal;
    }
}
