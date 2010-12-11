package attributes.graph;

import java.util.*;

/**
 * User: itamar
 * Date: Dec 10, 2010
 * Time: 5:51:06 PM
 */
    public class Node {

        public final int id;
        public float dist;

        public Map<Node,Float> neighbourDist;

        public Node(final int id) {
            this.id = id;
        }

        public Map<Node,Float> getNeighbours(){
            return neighbourDist;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}