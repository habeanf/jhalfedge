package segmentation.cluster;

import model.IFace;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterPair implements Comparable<ClusterPair> {
    public Cluster clusterA,clusterB;

    public ClusterPair(Cluster clusterA, Cluster clusterB) {
        this.clusterA = clusterA;
        this.clusterB = clusterB;
    }

    public ClusterProperty getIntersectionProperty() {
        Cluster primaryCluster,secondaryCluster;

        if (clusterA.getNeighbors().size()>clusterB.getNeighbors().size()) {
            primaryCluster = clusterB;
            secondaryCluster = clusterA;
        } else {
            primaryCluster = clusterA;
            secondaryCluster = clusterB;
        }

        int numOfDihedrals = 0;
        Double totalDihedral = 0.0;

        // find all neighboring pairs
        Set<IFace> clusterNeighbors = primaryCluster.getNeighbors();

        for(IFace face : clusterNeighbors) {
            if(face.getCluster().equals(secondaryCluster)) {
                Set<IFace> secondaryFaceNeighbors = face.getNeighbors();
                for(IFace face2 : secondaryFaceNeighbors) {
                    if (face2.getCluster().equals(primaryCluster)) {
                        // face and face2 are neighbors with an edge on the boundary of our two clusters
                        numOfDihedrals+=1;
                        totalDihedral+=face2.compareProperty(face).getDihedral();
                    }
                }
            }
        }

        if(numOfDihedrals>0)
            return new DihedralProperty(totalDihedral/numOfDihedrals);
        else
            return new DihedralProperty(0.0);
    }

    public ClusterProperty getUnionProperty() {
        int bothElements = clusterA.getClusterElements().size()+
                           clusterB.getClusterElements().size();
        return new DihedralProperty((
                                    ((DihedralProperty)clusterA.getClusterProperty()).getDihedral()+
                                    ((DihedralProperty)clusterB.getClusterProperty()).getDihedral()+
                                    ((DihedralProperty)getIntersectionProperty()).getDihedral())/bothElements);
    }

    public int compareTo(ClusterPair clusterPair) {
        return getUnionProperty().compareTo(clusterPair.getUnionProperty());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusterPair pair = (ClusterPair) o;

        if (clusterA.getId() != pair.clusterA.getId()) return false;
        if (clusterB.getId() != pair.clusterB.getId()) return false;

        return true;
    }

    public boolean canMerge() {
        return clusterA.canMerge(clusterB);
    }

    public Cluster merge() {
        clusterA.combineWith(clusterB);
        return clusterA;
    }

}
