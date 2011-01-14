package segmentation;

import attributes.MeshAttribute;
import model.Face;
import model.HalfEdge;
import model.HalfEdgeDataStructure;
import model.Vertex;
import model.Vector3D;
import parser.HalfEdgeNormalCreator;

/**
 * User: itamar
 * Date: 1/12/11
 * Time: 5:37 PM
 */
public class PartSegmentation implements MeshAttribute {
    private int clusterAmount = 0;

    public String getName() {
        return "Part Based Segmentation";
    }

    public boolean doFaceRendering() {
        return true;
    }

    public int getClustersAmount() {
        return clusterAmount;
    }

    public void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
        /* TRY #1: USE DIHEDRAL MEASURE WITH HIERARCHICAL CLUSTERING */
        // calculate normals, needed for dihedral calculation
        HalfEdgeNormalCreator normalCreator = new HalfEdgeNormalCreator(halfEdgeDataStructure);
        normalCreator.calcNormals();

        // calculate dihedral for each edge
        for (HalfEdge halfEdge : halfEdgeDataStructure.getAllHalfEdges()) {
            if (halfEdge.getDihedralAngle()<0.0 && halfEdge.getOpp()!=null) {
                float[] curEdgeNormal = halfEdge.getCornerNormal();
                float[] oppEdgeNormal = halfEdge.getCornerNormal();

                double dihedralAngle = (new Vector3D(curEdgeNormal)).calculateAngleTo(new Vector3D(oppEdgeNormal));

                halfEdge.setDihedralAngle(dihedralAngle);
                halfEdge.getOpp().setDihedralAngle(dihedralAngle);
            }
        }

        // assign an id to each face
        clusterAmount = 5;
        for (Face face : halfEdgeDataStructure.getAllFaces()) {
            final float[] xyz = face.getHalfEdge().getVertex().getXyz();
            float value = (float) (Math.sqrt(xyz[0] * xyz[0]) + (xyz[1] * xyz[1]) + (xyz[2] * xyz[2]));
            face.setSegment((int) (value*clusterAmount));
        }

    }

    // ignore
    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return 0;
    }
}

