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
 * To change this template use File | Settings | File Templates.
 */
public class HalfEdgeReader {
    Map<Edge, HalfEdge> halfEdgeMap;
    List<Face> faces;
    List<int[]> faceIds;
    Map<Integer, Vertex> vertexMap;

    public Edge addEdge(int fromVertex, int toVertex) {
        
        return new Edge(fromVertex,toVertex);
    }

    public Face addFace(List<HalfEdge> halfEdgeLoop) {
        return new Face(new HalfEdge(new Vertex(0,0,0,0)));
    }

    public HalfEdgeDataStructure get(String path, boolean normalizeVertices) {
        // initialize structures for reading from file
        vertexMap   = new HashMap<Integer,Vertex>();
        halfEdgeMap = new HashMap<Edge,HalfEdge>();
        faceIds     = new ArrayList<int[]>();

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

            // create face array
            faces = new ArrayList<Face>(faceIds.size());
            
            int[] curr_ids;
            for (int i=0;i<faceIds.size();++i) {
                curr_ids = faceIds.get(i);

                /* create edges by iterating over vertex ids pair-wise */

                // faceEdges will contain the halfe edges of the polygon created/retrieved by
                // addEdge
                List<HalfEdge> faceEdges = new ArrayList<HalfEdge>(curr_ids.length);
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
