package model;

import utils.Vector3D;

/**
 * User: itamar
 * Date: 1/14/11
 * Time: 3:16 PM
 */
public class Edge implements IEdge, Comparable<IEdge> {
    int from;
    int to;

    double weight;

    public Edge(IVertex from, IVertex to) {

        if (from.getId() > to.getId()){
            this.from = from.getId();
            this.to = to.getId();
        }
        else{
            this.from = to.getId();
            this.to = from.getId();
        }

        updateWeight(from, to);
    }


    public int getFromId() {
        return from;
    }

    public int getToId() {
        return to;
    }

    public int compareTo(final IEdge argEdge) {
        if (weight > argEdge.getWeight()) {
            return 1;
        }
        if (weight < argEdge.getWeight()) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != edge.from) return false;
        if (to != edge.to) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }

    private void updateWeight(IVertex from, IVertex to) {
       Vector3D fromVec = new Vector3D(from);
       Vector3D toVec = new Vector3D(to);

       weight = fromVec.sub(toVec).length();
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "from: " + from + " to: " + to;
    }
}