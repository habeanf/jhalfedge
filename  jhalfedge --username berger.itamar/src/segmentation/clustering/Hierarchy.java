package segmentation.clustering;

import model.IFace;
import segmentation.cluster.ClusterProperty;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Hierarchy implements Comparable<Hierarchy> {
    public Hierarchy first;

    public Hierarchy second;

    public Hierarchy parent;

    public int clusterId;

    public Set<IFace> changedFaces;

    public ClusterProperty propertyValue;

    public Hierarchy(Hierarchy first, Hierarchy second, int clusterId, Set<IFace> changedFaces, ClusterProperty propertyValue) {
        this.parent = null;
        this.first = first;
        this.second = second;
        this.clusterId = clusterId;
        this.changedFaces = changedFaces;
        this.propertyValue = propertyValue;
    }

    public Hierarchy getFirst() {
        return first;
    }

    public Hierarchy getSecond() {
        return second;
    }

    public Hierarchy getParent() {
        return parent;
    }

    public Set<IFace> getChangedFaces() {
        return changedFaces;
    }

    public void setParent(Hierarchy hier) {
        this.parent = hier;
    }

    public int getId() {
        return clusterId;
    }

    public boolean equal(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hierarchy hier = (Hierarchy) o;

        if (hier.getId() != clusterId) return false;
        if (hier.changedFaces.size() != changedFaces.size()) return false;

        return true;
    }

    public int compareTo(Hierarchy otherHierarchy) {
        return propertyValue.compareTo(otherHierarchy.propertyValue);
    }
}
