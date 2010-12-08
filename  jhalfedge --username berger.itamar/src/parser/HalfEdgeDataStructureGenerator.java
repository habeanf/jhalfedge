package parser;

import model.Face;
import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vertex;

import java.io.FileReader;
import java.util.*;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 5:05:15 PM
 */
public class HalfEdgeDataStructureGenerator {

    private static int faceCounter = 0;



    public static void main(String[] args) {
        final long l1 = System.currentTimeMillis();

        final HalfEdgeDataStructure halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get( "C:\\Workspace\\ex2\\src\\Models\\Candil.obj");

        int halfCountFace =0 ;
        for (HalfEdge halfEdge : halfEdgeDataStructure.getAllHalfEdges()) {
            if (halfEdge.getFace() == null){
                halfCountFace++;
            }
        }
        System.out.println("halfCount " + halfCountFace);
        final long l2 = System.currentTimeMillis();

        System.out.println(l2 - l1);
    }


    public static HalfEdgeDataStructure get(String path) {

        Map<Edge, HalfEdge> halfEdgeMap = new HashMap<Edge, HalfEdge>();
        List<Face> faces = new ArrayList<Face>();
        List<int[]> faceIds = new ArrayList<int[]>();
        Map<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();

        float max = 0; //TODO: add comment

        MeshReader meshReader = getReader(path);
        try {
            FileReader fileReader = new FileReader(path);
            OBJLineIterator lineIterator = new OBJLineIterator(fileReader);

            meshReader.preProcess(lineIterator);

            while (lineIterator.hasNext()) {
                final String[] fields = lineIterator.nextLine().split("\\s+");
                if (meshReader.isVertex(fields)) {
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
            for (Vertex vertex : vertexMap.values()) {
                vertex.normalize(max);
            }

            for (int[] ids : faceIds) {
                HalfEdge halfEdge01 = getHalfEdgeFromMap(ids[0], ids[1],halfEdgeMap,vertexMap);
                HalfEdge halfEdge10 = getHalfEdgeFromMap(ids[1], ids[0],halfEdgeMap,vertexMap);
                HalfEdge halfEdge12 = getHalfEdgeFromMap(ids[1], ids[2],halfEdgeMap,vertexMap);
                HalfEdge halfEdge21 = getHalfEdgeFromMap(ids[2], ids[1],halfEdgeMap,vertexMap);
                HalfEdge halfEdge02 = getHalfEdgeFromMap(ids[0], ids[2],halfEdgeMap,vertexMap);
                HalfEdge halfEdge20 = getHalfEdgeFromMap(ids[2], ids[0],halfEdgeMap,vertexMap);

                halfEdge10.setOpp(halfEdge01);
                halfEdge01.setOpp(halfEdge10);
                halfEdge12.setOpp(halfEdge21);

                halfEdge21.setOpp(halfEdge12);
                halfEdge02.setOpp(halfEdge20);
                halfEdge20.setOpp(halfEdge02);

                halfEdge01.setNext(halfEdge12, true);
                halfEdge12.setNext(halfEdge20, true);
                halfEdge20.setNext(halfEdge01, true);

//                halfEdge02.setNext(halfEdge21,true);
//                halfEdge21.setNext(halfEdge10,true);
//                halfEdge10.setNext(halfEdge02,true);

                halfEdge01.setPrev(halfEdge20);
                halfEdge12.setPrev(halfEdge01);
                halfEdge20.setPrev(halfEdge12);

                halfEdge02.setPrev(halfEdge10);
                halfEdge21.setPrev(halfEdge02);
                halfEdge10.setPrev(halfEdge21);

                Face face = new Face(halfEdge01);
                faces.add(face);
                halfEdge01.setFace(face);
                halfEdge10.setFace(face);
                halfEdge12.setFace(face);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HalfEdgeDataStructure(halfEdgeMap.values(), faces, vertexMap);
    }

    private static HalfEdge getHalfEdgeFromMap(int from, int to, Map<Edge, HalfEdge> halfEdgeMap, Map<Integer, Vertex> vertexMap) {
        Edge edge = new Edge(from, to);
        HalfEdge halfEdge = halfEdgeMap.get(edge);
        if (halfEdge == null) {
            halfEdge = new HalfEdge(vertexMap.get(to));
            halfEdgeMap.put(edge, halfEdge);
        }

        return halfEdge;
    }

    private static float findmax(float max,Vertex vertex){
        final float[] xyz = vertex.getXyz();
        return Math.max(max,Math.max(Math.abs(xyz[0]),Math.max(Math.abs(xyz[1]),Math.abs(xyz[2]))));
    }


    public static class Edge {
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

    private static MeshReader  getReader(String path) {
        if (path.endsWith("obj")) {
            return new OBJReader();
        }
        return new OFFReader();
    }
}
