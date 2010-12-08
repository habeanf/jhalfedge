package parser;

import model.Vertex;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 4:33:41 PM
 */
public class OBJReader implements MeshReader{
        int counter = 1;

    public boolean isVertex(String[] fields) {
        return fields[0].equals("v");
    }

    public Vertex getVertex(String[] fields) {
        return new Vertex(counter++, Float.parseFloat(fields[1]),Float.parseFloat(fields[2]),Float.parseFloat(fields[3]));
    }

    public boolean isFace(String[] fields) {
        return fields[0].equals("f");
    }

    public int[] getFaceIds(String[] fields) {
        return new int[]{Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),Integer.parseInt(fields[3])};
    }

    public void preProcess(OBJLineIterator lineIterator) {
    }
}
