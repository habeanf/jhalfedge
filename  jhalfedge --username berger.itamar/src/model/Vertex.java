package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 7:22:11 PM
 */
public class Vertex implements IVertex{
    int id;
    float[] xyz;
    boolean isActive = true;

    Set<IFace> faces;


    public Vertex(int id,float x, float y, float z) {
        this.xyz = new float[]{x, y, z};
        this.id = id;
    }

    public Vertex(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id:" + id + " x: " + xyz[0] + " y: " + xyz[1] + " z: " + xyz[2];
    }

    public float[] getXyz() {
        return xyz;
    }

    public void setXyz(float[] xyz) {
        this.xyz = xyz;
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

    public Set<IFace> getFaces() {
        if (faces == null){
            return new HashSet<IFace>();
        }

        return faces;
    }

    public void addFace(IFace face) {
        if (faces == null){
            faces = new HashSet<IFace>();
        }

        faces.add(face);
    }

    public void removeFace(IFace face) {
        faces.remove(face);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void offset(float meanx, float meany, float meanz) {
           xyz[0] = xyz[0] - meanx;
           xyz[1] = xyz[1] - meany;
           xyz[2] = xyz[2] - meanz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex v = (Vertex) o;

        if (this.id != v.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
