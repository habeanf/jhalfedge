package model;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 8:48:45 PM
 */
public class Face {
    public HalfEdge halfEdge;
    private int segment;

    public Face(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }
}
