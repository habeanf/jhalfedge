package parser;

import org.apache.commons.io.LineIterator;

import java.io.Reader;

/**
 * User: itamar
 * Date: Nov 27, 2010
 * Time: 4:56:02 PM
 */
public class OBJLineIterator extends LineIterator{
    public OBJLineIterator(Reader reader) throws IllegalArgumentException {
        super(reader);
    }


    @Override
    protected boolean isValidLine(String line) {
        if (line.startsWith("#")) return false;

        else if (line.length() == 0) return false;

        return super.isValidLine(line);
    }
}
