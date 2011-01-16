package model;


import java.util.List;

/**
 * User: itamar
 * Date: 1/15/11
 * Time: 3:43 PM
 */
public interface IFace{
    int getSegment();

    void setSegment(int cluster);

    List<IVertex> getVertices();

    void addVertices(List<IVertex> vertices);

    int getId();

    void removeVertex(IVertex vertex);

    boolean replaceVertex(IVertex vertex, IVertex toVertex);

    public Vector3D getNormal();

    boolean isValidReplacement(IVertex fromVertex, IVertex toVertex);

    void setRelatedFaceId(int faceId);

    int getRelatedFaceId();

    double calcDihedralAngle(IFace face1, IFace face2);
}
