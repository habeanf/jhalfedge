package model;

import segmentation.cluster.Cluster;
import segmentation.cluster.Clusterable;
import segmentation.cluster.DihedralProperty;

import java.util.*;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 8:48:45 PM
 */
public class Face implements IFace, Clusterable {
    private int id;
    private int segment = 4;

    private List<IVertex> vertices;

    private Cluster cluster;

    private Vector3D normal;

    public Face(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void removeVertex(IVertex vertex) {
        final Iterator<IVertex> iterator = vertices.iterator();

        while (iterator.hasNext()){
            final IVertex next = iterator.next();

            if (next == vertex){
                iterator.remove();
            }
        }
    }

    public boolean isValidReplacement(IVertex fromVertex, IVertex toVertex) {
        return !vertices.contains(toVertex);
    }

    public boolean replaceVertex(IVertex fromVertex, IVertex toVertex) {
        List<IVertex> newVertices = new ArrayList<IVertex>();

        // in case the vertex already exists, no replacement should be done, this a valid state
        if (vertices.contains(toVertex)){
            return false;
        }

        for (IVertex vertex : vertices) {
            if (vertex == fromVertex){
                newVertices.add(toVertex);
            }else{
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
        calcNormal();
    }

    public void setVertices(List<IVertex> vertices) {
        this.vertices = vertices;
        calcNormal();
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Face face = (Face) o;

        if (id != face.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void applyClustering() {
        segment = cluster.getId();
    }

    public void calcNormal() {
        assert(vertices.size()==3);
        Vector3D vp         = new Vector3D(vertices.get(0).getXyz());
        Vector3D v          = new Vector3D(vertices.get(1).getXyz());
        Vector3D vn         = new Vector3D(vertices.get(2).getXyz());

        Vector3D vector1    = v.sub(vp);
        Vector3D vector2    = v.sub(vn);

        // normal is calculated as the cross product of the incoming and outgoing half edges on a vertex
        normal              = vector1.calculateCrossProductWith(vector2).normalize();
    }

    public Vector3D getNormal() {
        return normal;
    }

    /* Clusterable methods */
    public int compareTo(IFace otherFace) {
        if (this.getSegment()>otherFace.getSegment())
            return 1;
        else
            return -1;
    }


    public int compareTo(Clusterable otherClusterable) {
        return compareTo((IFace)otherClusterable);
    }

    public DihedralProperty compareProperty(IFace otherFace) {
        return new DihedralProperty(normal.calculateAngleTo(((Face)otherFace).getNormal()));
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Set<IFace> getNeighbors() {
        Set<IFace> returnSet = new HashSet<IFace>();

        // return all faces of the this face's vertices
        for(IVertex vertex : vertices) {
            returnSet.addAll(vertex.getFaces());
        }

        // remove this face from the set
        returnSet.remove(this);

        return returnSet;
    }
}
