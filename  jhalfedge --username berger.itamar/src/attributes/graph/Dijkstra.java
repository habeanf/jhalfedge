package attributes.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * An implementation of dijkstra algorithm. based on: http://en.wikipedia.org/wiki/Dijkstra's_algorithm
 * User: itamar
 * Date: Dec 10, 2010
 * Time: 6:26:04 PM
 */

public class Dijkstra {
    public void calcShortestPaths(Collection<Node> nodes, Node source) {
        PriorityQueue<Node> queue = new PriorityQueue<Node>(nodes.size(), new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                if (o1.dist > o2.dist) {
                    return 1;
                } else if (o1.dist < o2.dist) {
                    return -1;
                }

                return 0;
            }
        });
        for (Node node : nodes) {
            if (node.id != source.id) {
                node.dist = Integer.MAX_VALUE;
            } else {
                node.dist = 0;
            }
            queue.add(node);
        }

        while (!queue.isEmpty()) {
            Node minNode = queue.poll();

            if (minNode.dist == Integer.MAX_VALUE) {
                break;
            }

            if (minNode.getNeighbours() != null) {
                for (Map.Entry<Node, Float> neigbourNodeEntry : minNode.getNeighbours().entrySet()) {
                    final Node neigbourNode = neigbourNodeEntry.getKey();
                    final float dist = neigbourNodeEntry.getValue();

                    float alternativeDist = minNode.dist + dist;

                    if (neigbourNode != null && alternativeDist < neigbourNode.dist) {
                        queue.remove(neigbourNode);
                        neigbourNode.dist = alternativeDist;
                        queue.add(neigbourNode);
                    }
                }
            }
        }

    }
}