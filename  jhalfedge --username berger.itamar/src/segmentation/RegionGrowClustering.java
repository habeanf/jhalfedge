package segmentation;

import model.Face;
import model.IFace;
import model.IMesh;
import model.IVertex;
import utils.InfoLogger;

import javax.swing.text.NumberFormatter;
import java.text.ParseException;
import java.util.*;

/**
 * User: itamar
 * Date: 1/16/11
 * Time: 10:46 PM
 */
public class RegionGrowClustering {

    private double threshold;

    private int clusterCounter = 0;

    private boolean enableRandom = true;

    private IMesh mesh;

    private InfoLogger infoLogger;

    private Map<Integer, IFace> faceMap = new HashMap<Integer, IFace>();
    private Random random = new Random();

    public RegionGrowClustering(IMesh mesh, double segmentationThreshold, InfoLogger infoLogger) {
        this.mesh = mesh;

        final Collection<IFace> allFaces = mesh.getAllFaces();
        for (IFace face : allFaces) {
            faceMap.put(face.getId(), face);
        }

        threshold = segmentationThreshold;

        this.infoLogger = infoLogger;
    }

    double totalAngle = 0.0;
    int faceAmountInTheCluster = 1;

    public void calculate() {
        PriorityQueue<IFace> queue = new PriorityQueue<IFace>(5, new Comparator<IFace>() {
            public int compare(IFace o1, IFace o2) {
                double avgAngle = totalAngle / faceAmountInTheCluster;

                final double angle1 = o1.calcDihedralAngle(o1, mesh.getFace(o1.getRelatedFaceId()));
                final double angle2 = o2.calcDihedralAngle(o2, mesh.getFace(o2.getRelatedFaceId()));

                if (Math.abs(angle1 - avgAngle) > Math.abs(angle2 - avgAngle)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        IFace nextSeed = selectSeed();

        int totalFaces = faceMap.size();

        NumberFormatter numberFormatter = new NumberFormatter();

        while (nextSeed != null) {
            try {
                infoLogger.setDebugRow("Calculating Segmentation (" + numberFormatter.valueToString(100 -  (faceMap.size()/(totalFaces * 100.0)) )+ "%) threshold = " + threshold);
            } catch (ParseException e) {
                System.out.println("Failed to show statistics");
            }
            int currentCluster = ++clusterCounter;
            totalAngle = 0.0;
            faceAmountInTheCluster = 1;
            nextSeed.setSegment(currentCluster);

            addRelatedFaceToQueue(queue, nextSeed);

            while (!queue.isEmpty()) {
                final IFace face = queue.poll();

                // verify that face is not already as part of any other cluster
                if (face.getSegment() <= 0) {
                    final double angle = face.calcDihedralAngle(face, mesh.getFace(face.getRelatedFaceId()));

                    double avgAngle = totalAngle / faceAmountInTheCluster;
                    // add to cluster
                    if (Math.abs(avgAngle - angle) >= threshold) {
                        face.setSegment(currentCluster);
                        faceAmountInTheCluster++;
                        totalAngle += angle;
                        faceMap.remove(face.getId());

                        // add the faces neighbours to queue
                        addRelatedFaceToQueue(queue, face);
                    }
                }
            }

            // select the next seed to start with
            nextSeed = selectSeed();
        }

    }

    private void addRelatedFaceToQueue(PriorityQueue<IFace> queue, IFace face) {
        for (IVertex vertex : face.getVertices()) {
            for (IFace adjFace : vertex.getFaces()) {
                // add related to queue
                if (adjFace.getSegment() <= 0 && adjFace.getRelatedFaceId() <= 0) {
                    adjFace.setRelatedFaceId(face.getId());
                    queue.add(adjFace);
                }
            }
        }
    }

    private IFace selectSeed() {
        if (enableRandom) {
            try {
                if (faceMap.size() == 0) {
                    return null;
                }

                int counter = 0;
                final int nextInt = random.nextInt(faceMap.size());
                for (Map.Entry<Integer, IFace> entry : faceMap.entrySet()) {
                    if (counter == nextInt) {
                        final Integer key = entry.getKey();
                        faceMap.remove(key);
                        return entry.getValue();
                    }
                    counter++;
                }
            } catch (Exception e) {
                enableRandom = false;
                selectSeed();
                e.printStackTrace();
            }


        } else {
            final Iterator<Map.Entry<Integer, IFace>> iterator = faceMap.entrySet().iterator();

            if (iterator.hasNext()) {
                final Map.Entry<Integer, IFace> next = iterator.next();
                faceMap.remove(next.getKey());
                return next.getValue();
            }
        }

        return null;
    }

    public int getClusterAmount() {
        return 5;
    }
}
