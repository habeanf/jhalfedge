package segmentation.clustering;

import model.IFace;
import model.IMesh;
import segmentation.cluster.Cluster;
import segmentation.cluster.ClusterPair;
import segmentation.cluster.DihedralCluster;
import segmentation.cluster.DihedralProperty;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class HierarchicalClustering {
    Hierarchy top;
    PriorityQueue<Hierarchy> up,down;
    int numOfSegments = 0;
    IMesh mesh;

    public void getHierarchicalClustering(IMesh mesh) {
        Set<NumberPair> numpairs = new HashSet<NumberPair>();

        this.mesh = mesh;
        // Create a cluster for each face
        for(IFace face : mesh.getAllFaces()) {
            face.setCluster(new DihedralCluster(face.getId(),face));
        }

        // Initialize cluster neighbors of each cluster
        for(IFace face : mesh.getAllFaces()) {
            DihedralCluster cluster = (DihedralCluster)face.getCluster();
            Set<Cluster> clusterNeighbors = new HashSet<Cluster>();
            for (IFace neighbor : face.getNeighbors()) {
                clusterNeighbors.add(neighbor.getCluster());
            }

            cluster.setClusterNeighbors(clusterNeighbors);
        }

        // Initialize a priority queue Q of pairs
        PriorityQueue<ClusterPair> Q = new PriorityQueue<ClusterPair>();

        // Insert all valid element pairs to Q
        for(IFace face : mesh.getAllFaces()) {
            for(IFace faceNeighbor : face.getNeighbors()) {
                if (face.compareTo(faceNeighbor)>0) {
                    ClusterPair pair = new ClusterPair(face.getCluster(),faceNeighbor.getCluster());
                    Q.add(pair);
                    numpairs.add(new NumberPair(pair.clusterA.getId(),pair.clusterB.getId()));
                }
            }
        }


        // Loop until Q is empty
        while(!Q.isEmpty()) {
            // Get the next pair (u,v) from Q
            ClusterPair nextPair = Q.poll();

            // If (u,v) can be merged
            if (nextPair.canMerge()) {
                System.out.println("Merging pair (" + nextPair.clusterA.getId() + "," + nextPair.clusterB.getId() + ")" +
                " with combined dihedral: " + ((DihedralProperty)nextPair.getUnionProperty()).getDihedral());
                // Merge (u,v) into w
                Cluster w = nextPair.merge();
                top = w.getHierarchy();
                // Insert all valid pairs of w to Q
                for (Cluster clusterNeighbor : w.getClusterNeighbors()) {
                    NumberPair numberpair = new NumberPair(w.getId(),clusterNeighbor.getId());
                    if (!numpairs.contains(numberpair)) {
                        Q.add(new ClusterPair(w,clusterNeighbor));
                        numpairs.add(numberpair);
                    }
                }
            }
        }

        up = new PriorityQueue<Hierarchy>();
        down = new PriorityQueue<Hierarchy>();
        down.add(top);
        goDown();
    }

    public void goDown() {
        if (!down.isEmpty()) {
            // get next hierarchy
            Hierarchy current = down.poll();

            // apply changes to mesh
            applyHierarchy(current);

            // enqueue new hierarchies if they exist
            if (current.getFirst()!=null)
                down.add(current.getFirst());
            if (current.getSecond()!=null)
                down.add(current.getSecond());

            // remove parent if it exists
            if (current.getParent()!=null) {
                up.remove(current.getParent());

                // add this parent (if this parent's is null then it's the root node)
                up.add(current);
            }

            // update number of segments
            numOfSegments+=1;
        }
    }

    public void goUp() {
        if (!up.isEmpty()) {
            // get next hierarchy
            Hierarchy current = up.poll();

            // apply changes to mesh
            applyHierarchy(current);

            // enqueue self in down
            down.add(current);

            // see if brother node is is also in 'down', if yes add parent to 'up'
            // first, get brother
            Hierarchy parent = current.getParent();
            Hierarchy brother = parent.getFirst().equals(current)?parent.getSecond():parent.getFirst();
            if (down.contains(brother)) {
                // brother is in down queue, can enqueue parent
                up.add(parent);
            }

            // remove children from down (unless we're at the bottom level)
            if (current.getFirst()!=null)
                down.remove(current.getFirst());
            if (current.getSecond()!=null)
                down.remove(current.getSecond());

            // update number of segments
            numOfSegments-=1;
        }
    }

    public void applyHierarchy(Hierarchy current) {
       for (IFace face : current.getChangedFaces())
           face.setSegment(current.getId());
    }

    public int getNumOfSegments() {
        return numOfSegments;
    }
}