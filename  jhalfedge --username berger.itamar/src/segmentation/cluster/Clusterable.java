package segmentation.cluster;

import model.IFace;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/14/11
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Clusterable {
    public void setCluster(Cluster cluster);

    public Cluster getCluster();

    public Set<IFace> getNeighbors();

    public int getId();

    // determines some order relation (used to enqueue each pair once)
    public int compareTo(Clusterable clusterable);
}
