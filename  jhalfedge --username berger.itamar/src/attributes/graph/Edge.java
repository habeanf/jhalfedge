package attributes.graph;

/**
 * User: itamar
 * Date: Dec 10, 2010
 * Time: 5:51:13 PM
 */

public class Edge implements Comparable<Edge> {

    final Node from, to;
    final float weight;

    public Edge(final Node argFrom, final Node argTo, final float argWeight) {
        from = argFrom;
        to = argTo;
        weight = argWeight;
    }

    public int compareTo(final Edge argEdge) {
        if (weight > argEdge.weight) {
            return 1;
        }
        if (weight < argEdge.weight) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
        if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}