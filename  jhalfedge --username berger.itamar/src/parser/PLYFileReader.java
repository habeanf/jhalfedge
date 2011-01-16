package parser;

import model.Vertex;

/**
 * User: itamar
 * Date: 1/16/11
 * Time: 6:04 PM
 */
public class PLYFileReader implements IMeshFileReader {
    int counter = 1;
    int offset = 0;

    public boolean isVertex(String[] fields) {
        if (fields.length == 4) {
            if (fields[0].equals("v")) {
                return true;
            }
        } else if (fields.length == 3) {
            try {
                Float.parseFloat(fields[0]);
                Float.parseFloat(fields[1]);
                Float.parseFloat(fields[2]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public Vertex getVertex(String[] fields) {
        return new Vertex(counter++, Float.parseFloat(fields[1 - offset]), Float.parseFloat(fields[2 - offset]), Float.parseFloat(fields[3 - offset]));
    }

    public boolean isFace(String[] fields) {
        if (fields.length == 4) {
            if (fields[0].equals("f")) {
                return true;
            } else {
                try {
                    Float.parseFloat(fields[1]);
                    Float.parseFloat(fields[2]);
                    Float.parseFloat(fields[3]);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    }

    public int[] getFaceIds(String[] fields) {
        return new int[]{Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), Integer.parseInt(fields[3])};
    }

    public void preProcess(MeshLineIterator lineIterator) {
        final String next = lineIterator.next();

        if (next.equals("ply")) {
            counter = 0;
            offset = 1;
        }
    }
}
