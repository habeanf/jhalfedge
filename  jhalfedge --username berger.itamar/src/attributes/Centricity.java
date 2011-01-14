package attributes;

import contoller.InputHandler;
import model.HalfEdgeDataStructure;
import model.Vertex;
import render.RenderState;
import utils.InfoLogger;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Calculate the cenricity attribute of a mesh by using the GeodesicDistanceCalculator.
 * User: itamar
 * Date: Dec 8, 2010
 * Time: 5:03:17 PM
 */
public class Centricity implements MeshAttribute {
    public String getName() {
        return "Centricity";
    }

    public float getValue(Vertex vertex, HalfEdgeDataStructure halfEdgeDataStructure) {
        return vertex.getCentricity();
    }

    public boolean doFaceRendering() {
        return false;
    }

    public int getClustersAmount() {
        return 0;
    }

    public void calculate(HalfEdgeDataStructure halfEdgeDataStructure) {
    }

    public static void calculate(final HalfEdgeDataStructure halfEdgeDataStructure, final InfoLogger infoLogger, final RenderState state) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    GeodesicDistanceCalculator geodesicDistanceCalculator = new GeodesicDistanceCalculator(halfEdgeDataStructure);

                    int counter = 0;
                    float size = halfEdgeDataStructure.getVertexes().size();
                    NumberFormatter formater = new NumberFormatter();
                    formater.setFormat(new DecimalFormat());

                    // calculate the centricity value for each vertex
                    for (Vertex vertex : halfEdgeDataStructure.getVertexes()) {
                        vertex.setCentricity(geodesicDistanceCalculator.getGeodesicDistances(vertex));
                        try {
                            infoLogger.setDebugRow("Calculating centricity: " + formater.valueToString(counter++ / size*100) + "% done");
                        } catch (ParseException e) {
                        }
                    }

                    state.setCalculatedCentricity(true);

                    MeshAttribute attribute = new Centricity();
                    state.setMeshAttribute(attribute);
                    state.transperacy(false);
                    infoLogger.setAttribute(attribute.getName());
                    infoLogger.setDebugRow("");
                    InputHandler.keyboardLock = false;
                }
            });

            InputHandler.keyboardLock = true;
            thread.start();
    }

    /**
     * For debuging - show the geodesic distance from one point only
     * @param halfEdgeDataStructure
     */
    public static void calculateOnePointOnly(HalfEdgeDataStructure halfEdgeDataStructure) {
        GeodesicDistanceCalculator geodesicDistanceCalculator = new GeodesicDistanceCalculator(halfEdgeDataStructure);

        geodesicDistanceCalculator.showGeodesicForVertex(halfEdgeDataStructure.getVertex((int) (Math.random() *
                halfEdgeDataStructure.getVertexes().size() + 1)));
    }
}
