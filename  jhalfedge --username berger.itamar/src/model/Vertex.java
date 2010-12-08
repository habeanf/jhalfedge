package model;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 7:22:11 PM
 */
public class Vertex {
    int id;
    float[] xyz;
    HalfEdge halfEdge;

    private float centricity;
    private float distance;
    private float gaussianCurvature;

    public Vertex(int id,float x, float y, float z) {
        this.xyz = new float[]{x, y, z};
        this.id = id;
    }

    @Override
    public String toString() {
        return "x: " + xyz[0] + " y: " + xyz[1] + " z: " + xyz[2];
    }

    public float[] getXyz() {
        return xyz;
    }

    public void setXyz(float[] xyz) {
        this.xyz = xyz;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public void setHalfEdge(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }

    public void normalize(float max) {
        if (max > 0.0) {
            xyz[0] = xyz[0] / max;
            xyz[1] = xyz[1] / max;
            xyz[2] = xyz[2] / max;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getCentricity() {
        return centricity;
    }

    public void setCentricity(float centricity) {
        this.centricity = centricity;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getGaussianCurvature() {
        return gaussianCurvature;
    }

    public void setGaussianCurvature(float gaussianCurvature) {
        this.gaussianCurvature = gaussianCurvature;
    }
}
