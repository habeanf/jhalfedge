package parser;

import org.apache.commons.io.LineIterator;

import java.io.Reader;

/**
 * Line Iterator for reading mesh files
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 4:56:02 PM
 */
public class MeshLineIterator extends LineIterator{
    public MeshLineIterator(Reader reader) throws IllegalArgumentException {
        super(reader);
    }


    @Override
    protected boolean isValidLine(String line) {
        if (line.startsWith("#")) return false;

        else if (line.length() == 0) return false;

        return super.isValidLine(line);
    }
}
