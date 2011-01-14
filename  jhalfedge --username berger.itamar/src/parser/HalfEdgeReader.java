package parser;

import model.*;

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

    /** Ctor
     *
     * @param loopEdges boolean - accept loop edges
     * @param parallelEdges boolean - accept parallel edges (inactive - will always reject)
     */
    public HalfEdgeReader(boolean loopEdges, boolean parallelEdges) {
        this.loopEdges      = loopEdges;
        this.parallelEdges  = parallelEdges;
    }

    /** looks for a half edge on a vertex (startingFrom.next().getVertex()) with no face to it's "left" side
     *
     *
     * @param startingFrom halfEdge to startfrom
     * @param andBefore free half edge before this halfEdge
     * @return  a free half edge if exists, null otherwise
     */
    public HalfEdge findFreeIncident(HalfEdge startingFrom, HalfEdge andBefore) {
        if (startingFrom == andBefore)
            return null;
        HalfEdge current = startingFrom;
        // go through all half edges until a free one is found, else return null
        do {
            // current.left().empty()
            if (current.getFace() == null) {
                return current;
            }
            else {
                // current.next().pair()
                current = current.getNext().getOpp();
            }
        } while (!current.equals(andBefore));
        return null;
    }

    /** makes two half edges adjacent, so in.next()==out.  if the half edges cannot be made adjacent returns false else true
     *
     * @param in starting edge
     * @param out ending edge
     * @return true if succeeded, else false
     */
    public boolean makeAdjacent(HalfEdge in, HalfEdge out) {
        // skip over trivial case
        if (in.getNext().equals(out)) {
            return true;
        }

        // reserve next and prev
        HalfEdge b = in.getNext();
        HalfEdge d = out.getPrev();

        // find a free half edge for switch off
        HalfEdge g = findFreeIncident(out.getOpp(), in);

        if (g==null) {
            // none found, in/out cannot be made adjacent
            return false;
        }

        // switch off in and out by using free half edge to switch off
        HalfEdge h = g.getNext();

        in.setNext(out,true);
        out.setPrev(in);

        g.setNext(b,true);
        b.setPrev(g);

        d.setNext(h,true);
        h.setPrev(d);

        return true;
    }

    /** find a free half edge on a vertex
     *
     * @param vertexId vertex to search on
     * @return free half edge if exists else null
     */
    public HalfEdge findFreeIncident(int vertexId) {
        List<HalfEdge> freeList = vertexFreeList.get(vertexId);

        for (HalfEdge edge : freeList) {
            if (edge.getFace()==null) {
                return edge;
            }
        }
        return null;
    }

    /** verifies the existence of a free half edge, useful for testing multiple half edges
     *  before attempting to apply an operation to the data structures
     *  this way they are kept in tact (invariant)
     *
     * @param vertexId vertex to search on
     * @return true if a free half edge exists, false otherwise
     */
    public boolean testFailHalfEdge(int vertexId) {
        List<HalfEdge> freeList = vertexFreeList.get(vertexId);
        Vertex         vertex   = vertexMap.get(vertexId);
        if (freeList.size() == 0) {
            return false;
        }
        else {
            if (findFreeIncident(vertexId)!=null)
                return false;
        }
        return true;

    }

    /** updates data structure with one new half edges
     *
     * @param vertexId affected vertex of half edge
     * @param newEdge edge to add
     */
    public void installHalfEdge(int vertexId, HalfEdge newEdge) {
        List<HalfEdge> freeList = vertexFreeList.get(vertexId);
        Vertex         vertex   = vertexMap.get(vertexId);

        if (freeList.size() == 0) {
            // empty vertex
            vertex.setHalfEdge(newEdge);
        }
        else {
            // split a current edge1->edge2 pair
            HalfEdge in     = findFreeIncident(vertexId);
            HalfEdge out    = in.getNext();

            in.setNext(newEdge, true);
            newEdge.setPrev(in);

            newEdge.getOpp().setNext(out, true);
            out.setPrev(newEdge.getOpp());
        }
        // update vertex's free list
        freeList.add(newEdge.getOpp());
    }

    /** adds a full edge to the data structure
     *  might fail if edge cannot be added - usually do to corrupt input or non-manifold data
     *
     * @param fromVertex first vertex of edge
     * @param toVertex second vertex of edge
     * @return full edge if succeeded, null otherwise
     */
    public Edge addEdge(int fromVertex, int toVertex) {
        if (!loopEdges) {
            if (fromVertex == toVertex) {
                return null;
            }
        }

        Edge edge = new Edge(vertexMap.get(fromVertex),vertexMap.get(toVertex));

        // return existing edge
        if (halfEdgeMap.containsKey(edge)) {
            return edge;
        }

        // Allocate
        HalfEdge fromToHalf = new HalfEdge(vertexMap.get(fromVertex));
        HalfEdge toFromHalf = new HalfEdge(vertexMap.get(toVertex));


        // Initialize half edges
        fromToHalf.initialize(toFromHalf);
        toFromHalf.initialize(fromToHalf);

        // make sure the dge can be added
        if (testFailHalfEdge(fromVertex) || testFailHalfEdge(toVertex)) {
            return null;    
        }

        // Install new half edges
        installHalfEdge(fromVertex,fromToHalf);
        installHalfEdge(toVertex,toFromHalf);

        // add a map so that opposite edge of other faces can find these edges
        halfEdgeMap.put(edge,fromToHalf);
        halfEdgeMap.put(new Edge(vertexMap.get(toVertex),vertexMap.get(fromVertex)),toFromHalf);

        return edge;
    }

    /** add a new face consisting of a closed loop of half edges to the data structure
     *  if the half edge loop cannot be added, an exception is thrown describing the reason
     *
     * @param halfEdgeLoop list of half edges (should be a loop)
     * @return face object if succeeded, will throw exception otherwise
     * @throws Exception reason face cannot be added
     */
    public Face addFace(ArrayList<HalfEdge> halfEdgeLoop) throws Exception {
        // validity check
        if (halfEdgeLoop.size()==0)
            return null;

        // check edges are free and form a chain
        for(int i=0;i<halfEdgeLoop.size();++i) {
            HalfEdge current    = halfEdgeLoop.get(i);
            HalfEdge next       = halfEdgeLoop.get((i+1)%(halfEdgeLoop.size()));

            if (!current.getNext().getVertex().equals(next.getVertex())) {
                throw new Exception("Half-edges not in a loop");
            }
            if (current.getFace()!=null) {
                throw new Exception("Non free faces founds");
            }
        }

        // try to reorder the links to get a proper orientation
        for (int i=0;i<halfEdgeLoop.size();++i) {
            if (!makeAdjacent(halfEdgeLoop.get(i),halfEdgeLoop.get((i+1)%(halfEdgeLoop.size()))))
                throw new Exception("Unadjacent edges");
        }

        // create face and update all half edges
        Face face = new Face(halfEdgeLoop.get(0));

        for (HalfEdge edge : halfEdgeLoop) {
            edge.setFace(face);
        }

        faces.add(face);

        return face;
    }

    /**Process OBJ and OFF files and creates a half edge data structure to store the data in the files
     * faces which introduce are corrupt or create a non-manifold are silently rejected, see commented code for
     * cases
     *
     * @param path path to file
     * @param normalizeVertices should normalize vertices or not (in case user has a unit box)
     * @return HalfEdgeDataStructure containing file data
     */
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
        int i=0;

        // get reader
        MeshReader meshReader = getReader(path);
        try {
            FileReader      fileReader   = new FileReader(path);
            OBJLineIterator lineIterator = new OBJLineIterator(fileReader);

            meshReader.preProcess(lineIterator);

            // read mesh from file
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

            // normalization and other preprocessing
            postProcessVertices(normalizeVertices, max);


            // main processing loop
            int[] curr_ids = null;
            for (i=0;i<faceIds.size();++i) {
                curr_ids = faceIds.get(i);

                /* create edges by iterating over vertex ids pair-wise */

                // faceEdges will contain the half edges of the polygon created/retrieved by
                // addEdge
                //LinkedList<HalfEdge> faceEdgeList = new LinkedList<HalfEdge>();
                boolean skipFace = false;
                ArrayList<HalfEdge> faceEdges = new ArrayList<HalfEdge>(curr_ids.length);
                for (int j=0;j<curr_ids.length;++j) {
                    int      vertexFrom  = curr_ids[j];
                    int      vertexTo    = curr_ids[(j+1)%curr_ids.length];

                    Edge     edge        = addEdge(vertexFrom,vertexTo);
                    if (edge==null) {
                        //System.out.println("Face #" + i + ", Edge " + j + " discarded.");
                        skipFace = true;
                        break;
                        //throw new Exception("Face #" + i + ", Edge #" + j + " not loaded.");
                    }
                    else {
                        HalfEdge half    = halfEdgeMap.get(edge);
                        faceEdges.add(j,half);
                    }
                }


                if (!skipFace) {
                    try {
                        Face face = addFace(faceEdges);
                    }
                    catch (Exception e)
                    {
                        //System.out.println("Face #" + i + " discarded due to " + e.getMessage());
                    }
                }

            }
        }
        catch (Exception e) {
            //System.out.println("Failed on face #" + i);
            e.printStackTrace();
        }

        return new HalfEdgeDataStructure(halfEdgeMap.values(), faces, vertexMap, halfEdgeMap);
    }

    /** process some vertex data early
     *
     * @param normalizeVertices boolean should normalize
     * @param max maximum value for normalization (say max=largest vertex for unit box)
     */
    private void postProcessVertices(boolean normalizeVertices, float max) {
        // normalize vertices
        float[] mean = {0,0,0};
        for (Vertex vertex : vertexMap.values()) {
            if (normalizeVertices){
                vertex.normalize(max);
            }
            final float[] xyz = vertex.getXyz();
            mean[0]+=xyz[0];
            mean[1]+=xyz[1];
            mean[2]+=xyz[2];
        }

        // calculate average
        float meanx = mean[0]/vertexMap.values().size();
        float meany = mean[1]/vertexMap.values().size();
        float meanz = mean[2]/vertexMap.values().size();
        for (Vertex vertex : vertexMap.values()) {
            vertex.offset(meanx,meany,meanz);
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
