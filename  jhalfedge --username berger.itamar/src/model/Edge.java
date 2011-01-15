package model;

/**
 * User: itamar
 * Date: 1/14/11
 * Time: 3:16 PM
 */
public class Edge implements Comparable<Edge> {
    Vertex from;
    Vertex to;

    float weight;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public Edge getOpp() {
        return new Edge(to,from);
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

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
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

    public void updateWeight() {
       Vector3D fromVec = new Vector3D(from);
       Vector3D toVec = new Vector3D(to);

       weight = (float) fromVec.sub(toVec).length();
    }
}