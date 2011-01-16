package segmentation.cluster;

import model.IFace;
import segmentation.clustering.Hierarchy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 12:49 PM
 */
public class DihedralCluster extends Cluster {
    int id;
    Set<IFace> clusterElements;
    Set<IFace> neighboringElements;
    Set<Cluster> neighbors;
    DihedralProperty totalDihedral;
    Hierarchy hier;

    public DihedralCluster(int id, IFace face) {
        this.id = id;
        face.setCluster(this);
        this.clusterElements = new HashSet<IFace>();
        this.clusterElements.add(face);
        this.totalDihedral = new DihedralProperty(0.0);
        this.neighboringElements = face.getNeighbors();
        this.hier = new Hierarchy(null,null,id,clusterElements,totalDihedral);
    }

    public DihedralCluster(int id, IFace face, Hierarchy hier) {
        this.id = id;
        this.clusterElements = new HashSet<IFace>();
        this.clusterElements.add(face);
        this.totalDihedral = new DihedralProperty(0.0);
        this.neighboringElements = face.getNeighbors();
        this.hier = hier;
    }

    public Hierarchy getHierarchy() {
        return hier;
    }

    public void setCluster(Cluster cluster) {
        combineWith(cluster);
    }

    public Cluster getCluster() {
        return this;
    }

    public Set<IFace> getNeighbors() {
        return neighboringElements;
    }

    public int compareTo(Clusterable otherCluster) {
         return (new Integer(id)).compareTo(otherCluster.getId());
    }

    public int compareTo(Cluster otherCluster) {
         return (new Integer(id)).compareTo(otherCluster.getId());
    }

    /**When comparing to clusters, their average dihedral is the comparison value
     *
     * @param dihedralPropertyCluster
     * @return
     */
    @SuppressWarnings({"JavaDoc"})
    @Override
    public DihedralProperty compareProperty(Cluster dihedralPropertyCluster) {
        // add internal dihedrals
        int bothElements = this.clusterElements.size()+dihedralPropertyCluster.getClusterElements().size();

        // add dihedral between the two clusters
        ClusterPair pair = new ClusterPair(this,dihedralPropertyCluster);
        Double bothDihedrals = ((DihedralProperty)pair.getUnionProperty()).getDihedral();

        // averages out dihedral angles
        return new DihedralProperty(bothDihedrals/bothElements);
    }

    @Override
    public DihedralProperty getClusterProperty() {
        return new DihedralProperty(totalDihedral.getDihedral()/clusterElements.size());
    }

    @Override
    public Set<Cluster> getClusterNeighbors() {
        return neighbors;
    }

    public void setClusterNeighbors(Set<Cluster> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public Set<IFace> getClusterElements() {
        return clusterElements;
    }

    public boolean canMerge(Cluster dihedralPropertyCluster) {
        return neighbors.contains(dihedralPropertyCluster);
    }

    @Override
    public void combineWith(Cluster dihedralPropertyCluster) {
        // validate that these are indeed neighbors
        assert(canMerge(dihedralPropertyCluster));

        // update total dihedral
        ClusterPair pair = new ClusterPair(this,dihedralPropertyCluster);
        this.totalDihedral = (DihedralProperty)pair.getUnionProperty();

        // update list of neighboring elements
        neighboringElements.addAll(dihedralPropertyCluster.getNeighbors());
        neighboringElements.removeAll(clusterElements);
        neighboringElements.removeAll(dihedralPropertyCluster.getClusterElements());


        // update all elements of incoming cluster with their new cluster
        for (IFace element : dihedralPropertyCluster.getClusterElements()) {
            element.setCluster(this);
        }

        // update set of elements
        clusterElements.addAll(dihedralPropertyCluster.getClusterElements());


        // update the list of neighbors
        neighbors.addAll(dihedralPropertyCluster.getClusterNeighbors());
        neighbors.remove(this);
        neighbors.remove(dihedralPropertyCluster);

        Hierarchy newhier = new Hierarchy(getHierarchy(),
                                          dihedralPropertyCluster.getHierarchy(),
                                          getId(),
                                          new HashSet<IFace>(getClusterElements()),
                                          new DihedralProperty(getClusterProperty()));

        hier.setParent(newhier);
        dihedralPropertyCluster.getHierarchy().setParent(newhier);
        this.hier = newhier;
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getElementIds() {
        Set<Integer> elementIds = new HashSet<Integer>();
        for (IFace element : clusterElements)
            elementIds.add(element.getId());

        return elementIds;
    }

}
