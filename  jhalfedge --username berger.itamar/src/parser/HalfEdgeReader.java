package parser;

import model.Face;
import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vertex;

import java.io.FileReader;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: habeanf
 * Date: Dec 8, 2010
 * Time: 7:20:34 PM
 * 
 * Based on code at http://www.cgafaq.info/wiki/Half_edge_implementation
 * Initial tutorial on Half Edge structure http://www.cgafaq.info/wiki/Half_edge_general
 */
public class HalfEdgeReader {
    Map<Edge, HalfEdge>                 halfEdgeMap;
    List<Face>                          faces;
    List<int[]>                         faceIds;
    Map<Integer, Vertex>                vertexMap;
    Map<Integer, ArrayList<HalfEdge>>   vertexFreeList;

    boolean loopEdges;
    boolean parallelEdges;

    public HalfEdge findFreeIncident(HalfEdge startingFrom, HalfEdge andBefore) {
        if (startingFrom == andBefore)
            return null;
        HalfEdge current = startingFrom;
        do {
            // current.left().empty()
            if (current.getFace() == null) {
                return current;
            }
            else {
                // current.next().pair()
                current = current.getNext().getOpp();
            }
        } while (current != andBefore);
        return null;
    }

    public boolean makeAdjacent(HalfEdge in, HalfEdge out) {
        return true;
    }

    public HalfEdge findFreeIncident(int vertexId) {
        List<HalfEdge> freeList = vertexFreeList.get(vertexId);
        
        for (HalfEdge edge : freeList) {
            if (edge.getFace()!=null) {
                return edge;
            }
        }
        return null;
    }

    public void installHalfEdge(int vertexId, HalfEdge newEdge) {
        List<HalfEdge> freeList = vertexFreeList.get(vertexId);
        Vertex         vertex   = vertexMap.get(vertexId);
        if (freeList.size() == 0) {
            vertex.setHalfEdge(newEdge);
            freeList.add(newEdge);
        }
        else {
            HalfEdge in     = findFreeIncident(vertexId);
            HalfEdge out    = in.getNext();

            in.setNext(newEdge, true);
            newEdge.setPrev(in);

            newEdge.setNext(out, true);
            out.setPrev(newEdge);
        }
    }

    public Edge addEdge(int fromVertex, int toVertex) {
        if (!loopEdges) {
            if (fromVertex == toVertex) {
                return null;
            }
        }

        Edge edge = new Edge(fromVertex,toVertex);

        if (!parallelEdges && halfEdgeMap.containsKey(edge)) {
            return edge;
        }

        // Allocate
        HalfEdge fromToHalf = new HalfEdge(vertexMap.get(fromVertex));
        HalfEdge toFromHalf = new HalfEdge(vertexMap.get(toVertex));


        // Initialize half edges
        fromToHalf.initialize(toFromHalf);
        toFromHalf.initialize(fromToHalf);

        // Install new half edges
        installHalfEdge(fromVertex,fromToHalf);
        installHalfEdge(toVertex,toFromHalf);

        return edge;
    }

    public Face addFace(ArrayList<HalfEdge> halfEdgeLoop) {
        // validity check
        if (halfEdgeLoop.size()==0)
            return null;

        // check edges are free and form a chain
        for(int i=0;i<halfEdgeLoop.size();++i) {
            HalfEdge current    = halfEdgeLoop.get(i);
            HalfEdge next       = halfEdgeLoop.get(i%(halfEdgeLoop.size()));

            if (current.getNext().getVertex()!=next.getVertex()) {
                return null;
            }
            if (current.getFace()!=null) {
                return null;
            }
        }

        // try to reorder the links to get a proper orientation
        for (int i=0;i<halfEdgeLoop.size();++i) {
            if (!makeAdjacent(halfEdgeLoop.get(i),halfEdgeLoop.get(i%(halfEdgeLoop.size()))))
                return null;
        }

        Face face = new Face(halfEdgeLoop.get(0));

        for (HalfEdge edge : halfEdgeLoop) {
            edge.setFace(face);
        }
        
        return face;
    }

    public HalfEdgeDataStructure get(String path, boolean normalizeVertices) {
        // initialize intermediate structures for reading from file
        vertexMap       = new HashMap<Integer,Vertex>();
        halfEdgeMap     = new HashMap<Edge,HalfEdge>();
        faceIds         = new ArrayList<int[]>();
        vertexFreeList  = new HashMap<Integer,ArrayList<HalfEdge>>();
        faces           = new ArrayList<Face>(faceIds.size());
        loopEdges       = true;
        parallelEdges   = true;

        // used for normalization vertices
        float   max = 0;

        // get reader
        MeshReader meshReader = getReader(path);
        try {
            FileReader      fileReader   = new FileReader(path);
            OBJLineIterator lineIterator = new OBJLineIterator(fileReader);

            meshReader.preProcess(lineIterator);

            while (lineIterator.hasNext()) {
                final String[] fields = lineIterator.nextLine().split("\\s+");
                if (meshReader.isVertex(fields)) {
                    // vertex constructor initializes vertex.half_ = null
                    final Vertex vertex = meshReader.getVertex(fields);
                    max = findmax(max,vertex);
                    vertexMap.put(vertex.getId(), vertex);
                    vertexFreeList.put(vertex.getId(), new ArrayList<HalfEdge>());
                } else if (meshReader.isFace(fields)) {
                    faceIds.add(meshReader.getFaceIds(fields));
                }
            }
            lineIterator.close();
            fileReader.close();

            // normalize vertices
            if(normalizeVertices) {
                for (Vertex vertex : vertexMap.values()) {
                    vertex.normalize(max);
                }
            }
            
            int[] curr_ids;
            for (int i=0;i<faceIds.size();++i) {
                curr_ids = faceIds.get(i);

                /* create edges by iterating over vertex ids pair-wise */

                // faceEdges will contain the halfe edges of the polygon created/retrieved by
                // addEdge
                ArrayList<HalfEdge> faceEdges = new ArrayList<HalfEdge>(curr_ids.length);
                for (int j=0;j<curr_ids.length;++j) {
                    int      vertexFrom  = curr_ids[j];
                    int      vertexTo    = curr_ids[(j+1)%curr_ids.length];

                    Edge     edge        = addEdge(vertexFrom,vertexTo);
                    HalfEdge half        = halfEdgeMap.get(edge);
                    
                    faceEdges.set(j,half);
                }

                // add face
                addFace(faceEdges);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new HalfEdgeDataStructure(halfEdgeMap.values(), faces, vertexMap);
    }
    
    public class Edge {
        int from;
        int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
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
    }

    private MeshReader getReader(String path) {
        if (path.endsWith("obj")) {
            return new OBJReader();
        }
        return new OFFReader();
    }

    private float findmax(float max,Vertex vertex){
        final float[] xyz = vertex.getXyz();
        return Math.max(max,Math.max(Math.abs(xyz[0]),Math.max(Math.abs(xyz[1]),Math.abs(xyz[2]))));
    }
}
