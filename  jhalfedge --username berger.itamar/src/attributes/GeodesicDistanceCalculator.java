package attributes;

import attributes.graph.Dijkstra;
import attributes.graph.Node;
import model.*;

import java.util.*;

/**
 * The calculator first convert the vertices to a nodes, and hold for each node, its neigbours
 * and the distance from them. Then it runs a modified dijkstra algorithm for returning the shortest
 * path to each vertex.
 * In addition it create "Shortcuts paths" between vertices that has adjacent faces.
 * User: itamar
 * Date: Dec 9, 2010
 * Time: 1:19:38 AM
 */
public class GeodesicDistanceCalculator {
    HalfEdgeDataStructure halfEdgeDataStructure;

    private Map<Integer, Node> nodes;

    Dijkstra dijkstra = new Dijkstra();

    public GeodesicDistanceCalculator(HalfEdgeDataStructure halfEdgeDataStructure) {
        this.halfEdgeDataStructure = halfEdgeDataStructure;
        nodes = new HashMap<Integer, Node>();

        buildGraph();

    }

    /**
     * given a vertex, apply dijkstra algorithm for finding the shortest length to each vertex in the graph
     * @param vertex
     * @return
     */
    public float getGeodesicDistances(Vertex vertex) {
        // sum of all the shortest path to this vertex
        float sum = 0;

        final Node node = nodes.get(vertex.getId());
        if (node != null) {
            dijkstra.calcShortestPaths(nodes.values(), node);

            int counter = 0;
            for (Node node1 : nodes.values()) {
                if (node1.dist < Integer.MAX_VALUE - 1) {
                    counter++;
                    sum += node1.dist;
                }
            }

            // normalize
            if (counter > 0)
                sum /= counter;

            return sum;
        } else {
            return 0;
        }
    }


    /**
     * The function create the nodes that hold the graph, calculate the minimum distance to one vertex to his neigbour(=edge)
     */
    private void buildGraph() {
        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            nodes.put(vertex.getId(), new Node(vertex.getId()));
        }

        for (Node currentNode : nodes.values()) {
            Vertex vertex = halfEdgeDataStructure.getVertex(currentNode.id);

            Vector3D vertexVec = new Vector3D(vertex);

            final Set<Vertex> neighbours = halfEdgeDataStructure.getNeighbours(vertex);
            currentNode.neighbourDist = new HashMap<Node, Float>(neighbours.size());

            //for creating the shortcut path - get the vertices that needed to be attached.
            neighbours.addAll(createShortcutPaths(vertex));

            for (Vertex neighbour : neighbours) {
                Vector3D neighbourVec = new Vector3D(neighbour);
                currentNode.neighbourDist.put(nodes.get(neighbour.getId()), (float)neighbourVec.sub(vertexVec).length());
            }
        }
    }

    /**
     * The function add to the graph more "edges", by assigning each node a new neigbour nodes.
     * @param vertex
     * @return
     */
    private List<Vertex> createShortcutPaths(Vertex vertex) {
        List<Vertex> result = new ArrayList<Vertex>();

        int id = vertex.getId();
        final Set<Face> faceNeighbours = halfEdgeDataStructure.getFaceNeighbours(vertex);

        // iterate on the faces neighbours to the vertex and find adjacent vertices
        for (Face faceNeighbour : faceNeighbours) {
            final HalfEdge firsthalfEdge = faceNeighbour.getHalfEdge();
            HalfEdge nexthalfEdge = firsthalfEdge;

            do {
                if (nexthalfEdge.getVertex().getId() != id && nexthalfEdge.getNext().getVertex().getId() != id) {
                    final HalfEdge opp = nexthalfEdge.getOpp();

                    if ((opp != null) && (opp.getFace() != null && (opp.getNext() != null) && (opp.getNext().getNext() != null))) {
                        result.add(opp.getNext().getNext().getVertex());
                        break;
                    }
                }
                nexthalfEdge = nexthalfEdge.getNext();
            } while (firsthalfEdge != nexthalfEdge);


        }

        return result;
    }

     /**
     * Debug function for showing the geodesic distance from one vertex only
     * @param source
     */
    public void showGeodesicForVertex(Vertex source) {
        dijkstra.calcShortestPaths(nodes.values(), nodes.get(source.getId()));

        for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
            final Node node = nodes.get(vertex.getId());

            if (node.dist < Integer.MAX_VALUE - 1) {
                vertex.setCentricity(node.dist);
            }
        }
    }
}
