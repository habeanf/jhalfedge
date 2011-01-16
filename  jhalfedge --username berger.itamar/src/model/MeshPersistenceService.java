package model;

import parser.*;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 3:52 PM
 */
public class MeshPersistenceService {

    private boolean normalizeVertices = true;

    public IMesh loadMeshFromFile(String path) {

        // initialize intermediate structures for reading from file
        Map<Integer, IVertex> vertexMap = new HashMap<Integer, IVertex>();
        Map<Integer, IFace> faceMap = new HashMap<Integer, IFace>();

        IMesh mesh = new IndexFacedSetMesh(vertexMap, faceMap);

        // used for normalization vertices
        float max = 0;
        int faceCounter = 1;

        // get reader
        IMeshFileReader meshFileReader = getReader(path);
        try {
            FileReader fileReader = new FileReader(path);
            MeshLineIterator lineIterator = new MeshLineIterator(fileReader);

            meshFileReader.preProcess(lineIterator);

            // read mesh from file
            while (lineIterator.hasNext()) {
                final String[] fields = lineIterator.nextLine().split("\\s+");
                if (meshFileReader.isVertex(fields)) {
                    // vertex constructor initializes vertex.half_ = null
                    final Vertex vertex = meshFileReader.getVertex(fields);
                    max = findmax(max, vertex);
                    vertexMap.put(vertex.getId(), vertex);
                } else if (meshFileReader.isFace(fields)) {
                    addFace(mesh, faceCounter, meshFileReader.getFaceIds(fields), true);

                    faceCounter++;
                }
            }
            lineIterator.close();
            fileReader.close();

            // normalization and other preprocessing
            postProcessVertices(vertexMap, normalizeVertices, max);

            return mesh;

        } catch (Exception e) {
            System.out.println("Failed to load file from path: " + path);
        }


        return null;
    }

    private IMeshFileReader getReader(String path) {
        if (path.toLowerCase().endsWith("obj")) {
            return new OBJFileReader();
        } else if (path.toLowerCase().endsWith("obj")) {
            return new OFFFileReader();
        } else
            return new PLYFileReader();
    }

    private float findmax(float max, Vertex vertex) {
        final float[] xyz = vertex.getXyz();
        return Math.max(max, Math.max(Math.abs(xyz[0]), Math.max(Math.abs(xyz[1]), Math.abs(xyz[2]))));
    }


    /**
     * process some vertex data early
     *
     * @param normalizeVertices boolean should normalize
     * @param max               maximum value for normalization (say max=largest vertex for unit box)
     */
    private void postProcessVertices(Map<Integer, IVertex> vertexMap, boolean normalizeVertices, float max) {
        // normalize vertices
        float[] mean = {0, 0, 0};
        for (IVertex vertex : vertexMap.values()) {
            if (normalizeVertices) {
                vertex.normalize(max);
            }
            final float[] xyz = vertex.getXyz();
            mean[0] += xyz[0];
            mean[1] += xyz[1];
            mean[2] += xyz[2];
        }

        // calculate average
        float meanx = mean[0] / vertexMap.values().size();
        float meany = mean[1] / vertexMap.values().size();
        float meanz = mean[2] / vertexMap.values().size();
        for (IVertex vertex : vertexMap.values()) {
            vertex.offset(meanx, meany, meanz);
        }
    }

    public void removeFace(IMesh mesh, int faceId) {
        if (faceId > 0) {
            mesh.removeFace(faceId);
        }
    }

    public boolean changeFaceVertices(IMesh mesh, int faceId, int from, int to) {
        return mesh.changeFaceVertices(faceId, from, to);
    }

    public boolean isValidReplacment(IMesh mesh, Integer faceId, int from, int to) {
        return mesh.isValidReplacment(faceId, from, to);
    }

    public void removeVertex(IMesh mesh, int vertexId) {
        mesh.removeVertex(vertexId);
    }

    public void addVertex(IMesh mesh, int vertexId) {
        mesh.addVertex(vertexId);
    }

    public void addFace(IMesh mesh, int faceId, int[] verticesIds, boolean calcNormals) {
        mesh.addFace(faceId, verticesIds, calcNormals);
    }


}
