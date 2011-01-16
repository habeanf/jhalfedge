package segmentation.clustering;

import model.IFace;
import model.IMesh;
import segmentation.cluster.Cluster;
import segmentation.cluster.ClusterPair;
import segmentation.cluster.DihedralCluster;

import java.util.PriorityQueue;

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
        this.mesh = mesh;

        // Initialize a priority queue Q of pairs
        PriorityQueue<ClusterPair> Q = new PriorityQueue<ClusterPair>();

        // Insert all valid element pairs to Q
        for(IFace face : mesh.getAllFaces())
            for(IFace faceNeighbor : face.getNeighbors()) {
                if (face.compareTo(faceNeighbor)>0) {
                    DihedralCluster clusterA = new DihedralCluster(face.getId(),face);
                    DihedralCluster clusterB = new DihedralCluster(faceNeighbor.getId(),faceNeighbor);
                    ClusterPair pair = new ClusterPair(clusterA,clusterB);
                    Q.add(pair);
                }
            }

        // Loop until Q is empty
        while(!Q.isEmpty()) {
            // Get the next pair (u,v) from Q
            ClusterPair nextPair = Q.poll();

            // If (u,v) can be merged
            if (nextPair.canMerge()) {
                // Merge (u,v) into w
                Cluster w = nextPair.merge();
                top = w.getHierarchy();
                // Insert all valid pairs of w to Q
                for (Cluster clusterNeighbor : w.getClusterNeighbors())
                    Q.add(new ClusterPair(w,clusterNeighbor));
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