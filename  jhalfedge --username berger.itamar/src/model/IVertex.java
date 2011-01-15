package model;

import java.util.Set;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 3:42 PM
 */
public interface IVertex {
    float[] getXyz();

    void normalize(float max);

    void offset(float meanx, float meany, float meanz);

    int getId();

    Set<IFace> getFaces();

    void addFace(IFace face);

    void removeFace(IFace face);

    boolean isActive();

    void setActive(boolean isActive);
}
