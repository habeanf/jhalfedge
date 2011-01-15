package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 8:48:45 PM
 */
public class Face implements IFace {
    private int id;
    private int segment;

    private List<IVertex> vertices;

    public Face(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void removeVertex(IVertex vertex) {
        final Iterator<IVertex> iterator = vertices.iterator();

        while (iterator.hasNext()){
            final IVertex next = iterator.next();

            if (next == vertex){
                iterator.remove();
            }
        }
    }

    public boolean isValidReplacement(IVertex fromVertex, IVertex toVertex) {
        return !vertices.contains(toVertex);
    }

    public boolean replaceVertex(IVertex fromVertex, IVertex toVertex) {
        List<IVertex> newVertices = new ArrayList<IVertex>();

        // in case the vertex already exists, no replacement should be done, this a valid state
        if (vertices.contains(toVertex)){
            return false;
        }

        for (IVertex vertex : vertices) {
            if (vertex == fromVertex){
                newVertices.add(toVertex);
            }else{
                newVertices.add(vertex);
            }
        }

        this.vertices = newVertices;

        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSegment() {
        return segment;
    }

    public List<IVertex> getVertices() {
        if (vertices == null) {
            return new ArrayList<IVertex>();
        }

        return vertices;
    }

    public void addVertices(List<IVertex> vertices) {
        if (this.vertices == null) {
            this.vertices = new ArrayList<IVertex>(vertices.size());
        }

        this.vertices.addAll(vertices);
    }

    public void setVertices(List<IVertex> vertices) {
        this.vertices = vertices;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Face face = (Face) o;

        if (id != face.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
