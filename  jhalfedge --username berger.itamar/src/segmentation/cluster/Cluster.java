package segmentation.cluster;

import model.IFace;
import segmentation.clustering.Hierarchy;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/14/11
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Cluster implements Clusterable {
    // clusterable methods
    public abstract ClusterProperty compareProperty(Cluster cluster);

    // cluster methods
    public abstract ClusterProperty getClusterProperty();

    public abstract Set<Cluster> getClusterNeighbors();

    public abstract Set<IFace> getClusterElements();

    public abstract int compareTo(Cluster cluster);

    public abstract void combineWith(Cluster cluster);

    public abstract int getId();

    public abstract boolean canMerge(Cluster otherCluster);

    public abstract Hierarchy getHierarchy();
}
