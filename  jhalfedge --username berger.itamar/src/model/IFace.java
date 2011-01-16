package model;

import segmentation.cluster.Cluster;
import segmentation.cluster.DihedralProperty;

import java.util.List;
import java.util.Set;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 3:43 PM
 */
public interface IFace {
    void setCluster(Cluster cluster);

    Cluster getCluster();

    Set<IFace> getNeighbors();

    public int compareTo(IFace otherFace);

    public DihedralProperty compareProperty(IFace otherFace);

    int getSegment();

    void setSegment(int cluster);

    List<IVertex> getVertices();

    void addVertices(List<IVertex> vertices);

    int getId();

    void removeVertex(IVertex vertex);

    boolean replaceVertex(IVertex vertex, IVertex toVertex);

    public Vector3D getNormal();

    boolean isValidReplacement(IVertex fromVertex, IVertex toVertex);

}
