package parser;

import model.Vertex;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 7:16:41 PM
 */
public class OFFFileReader implements IMeshFileReader {
    int counter = 0;

    public boolean isVertex(String[] fields) {
        return fields.length == 3;
    }

    public Vertex getVertex(String[] fields) {
        return new Vertex(counter++,Float.parseFloat(fields[0]),Float.parseFloat(fields[1]),Float.parseFloat(fields[2]));
    }

    public boolean isFace(String[] fields) {
        return fields.length == 4;
    }

    public int[] getFaceIds(String[] fields) {
        return (new int[]{Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),Integer.parseInt(fields[3])});        
    }

    public void preProcess(OBJLineIterator lineIterator) {
        lineIterator.nextLine();
        if (lineIterator.hasNext())
            lineIterator.nextLine();
    }
}
