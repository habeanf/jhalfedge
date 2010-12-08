package contoller;

import attributes.*;
import model.HalfEdgeDataStructure;
import parser.HalfEdgeDataStructureGenerator;
import render.*;
import utils.InfoLogger;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * This class is responsible the handling the events and passing them to the display.
 * User: itamar
 * Date: Oct 30, 2010
 * Time: 10:14:56 AM
 */
public class Controller implements GLEventListener {
    private GLU glu = new GLU();

    // specific renders
    private MeshRenderer meshRenderer;
    private GridRenderer gridRenderer;


    public static final float WORLD_SIZE = 2f;
    private static final float freq = 1 / 20f;

    // lighting settings
    private boolean lightingEnabled;
    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {-20.0f, 0.0f, -10.0f, 1.0f};

    // display settings
    private boolean isSmooth = true;

    // current handlers state
    private float xrot;
    private boolean increaseX;
    private boolean decreaseX;
    private float yrot;
    private boolean increaseY;
    private boolean decreaseY;

    // default value for renderes
    private boolean enableGrid = false;


    private float z = -3.0f;            // Depth Into The Screen
    private boolean zoomIn;
    private boolean zoomOut;

    // need for mouse
    private int width = 640;
    private int height = 480;

    private RenderState state;

    private HalfEdgeDataStructure halfEdgeDataStructure = null;

    private static InfoLogger infoLogger = InfoLogger.getInfoLogger();

    int meshIterator = 0;


    public Controller() {
        gridRenderer = new GridRenderer(freq);

        File modelDirectory = new File("./Models");

        paths = new ArrayList<File>();
        Collections.addAll(paths, modelDirectory.listFiles());

        selectFile(paths.get(meshIterator));
    }

    public void display(GLAutoDrawable gLDrawable) {
        update();
        final GL gl = gLDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0f, 0.0f, this.z);
        gl.glRotatef(45.0f + xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(45.0f + yrot, 0.0f, 1.0f, 0.0f);

        if (isSmooth)
            gl.glShadeModel(GL.GL_SMOOTH);
        else
            gl.glShadeModel(GL.GL_FLAT);


        if (lightingEnabled)
            gl.glEnable(GL.GL_LIGHTING);
        else
            gl.glDisable(GL.GL_LIGHTING);


        meshRenderer.render(gl, state);

//        meshRenderer.renderFace(gl);

        if (enableGrid) {
            drawWireFrame(gl);
            gridRenderer.render(gl);
        }

        gl.glFlush();
    }

    private void drawWireFrame(GL gl) {
        gl.glDisable(GL.GL_LIGHTING);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
        BoxRenderer.drawBox(gl, 0, 0, 0, WORLD_SIZE);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
    }

    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
    }

    /**
     * set GL initial settings
     *
     * @param gLDrawable
     */
    public void init(GLAutoDrawable gLDrawable) {
        GL gl = gLDrawable.getGL();
        gl.glShadeModel(GL.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                            // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                                // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);    // Really Nice Perspective Calculations
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, this.lightAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, this.lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, this.lightPosition, 0);

        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        this.lightingEnabled = true;
    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        final GL gl = gLDrawable.getGL();

        if (height <= 0) // avoid a divide by zero error!
            height = 1;
        final float h = (float) width / (float) height;
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1, 100.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void update() {
        if (decreaseX)
            xrot -= 2f;
        if (increaseX)
            xrot += 2f;
        if (decreaseY)
            yrot -= 2f;
        if (increaseY)
            yrot += 2f;
        if (zoomIn)
            z += 0.1f;
        if (zoomOut)
            z -= 0.1f;
    }

    public void increaseXrot(boolean increase) {
        increaseX = increase;
    }

    public void decreaseXrot(boolean decrease) {
        decreaseX = decrease;
    }

    public void increaseYrot(boolean increase) {
        increaseY = increase;
    }

    public void decreaseYrot(boolean decrease) {
        decreaseY = decrease;
    }

    public void zoomIn(boolean zoomIn) {
        this.zoomIn = zoomIn;
    }

    public void zoomOut(boolean zoomOut) {
        this.zoomOut = zoomOut;
    }

    public void toggleGrid() {
        enableGrid = !enableGrid;
    }


    public void toggleTransparent() {
        state.toggleTransparent();
    }

    public void toggleLighting() {
        lightingEnabled = !lightingEnabled;
    }

    int prevMouseX;
    int prevMouseY;

    public void startDrag(Point point) {
        prevMouseX = point.x;
        prevMouseY = point.y;
    }

    public void drag(Point point) {
        int x = point.x;
        int y = point.y;

        float thetaX = 360f * ((float) (x - prevMouseX) / (float) width);
        float thetaY = 360f * ((float) (prevMouseY - y) / (float) height);

        prevMouseX = x;
        prevMouseY = y;

        xrot += thetaY;
        yrot += thetaX;
    }


    public void toggleShadeModel() {
        isSmooth = !isSmooth;
    }

    public void zoom(int wheelRotation) {
        z += wheelRotation / 2.0f;
    }

    public void setInfoLogger(InfoLogger infoLogger) {
        Controller.infoLogger = infoLogger;
    }

    public void toggleCloud() {
        state.toggleCloud();
    }

    public MeshAttribute setNoAttribute() {
        MeshAttribute attribute = null;
        state.setMeshAttribute(attribute);
        infoLogger.setAttribute("None");
        state.transperacy(true);
        return attribute;
    }

    public MeshAttribute setCentricityAttribute() {
        if (!state.isCalculatedCentricity()) {
            Centricity.calculate(halfEdgeDataStructure);
            state.setCalculatedCentricity(true);
        }

        MeshAttribute attribute = new Centricity();
        state.setMeshAttribute(attribute);
        state.transperacy(false);
        infoLogger.setAttribute(attribute.getName());
        return attribute;
    }

    public MeshAttribute setDistanceToCentroidAttribute() {
        if (!state.isCalculatedDistanceToCentroid()) {
            DistanceToCentroid.calculate(halfEdgeDataStructure);
            state.setCalculatedDistance(true);
        }

        MeshAttribute attribute = new DistanceToCentroid();
        state.setMeshAttribute(attribute);
        state.transperacy(false);
        infoLogger.setAttribute(attribute.getName());
        return attribute;
    }


    public MeshAttribute setGaussianCurvature() {
        if (!state.isCalculatedGaussian()) {
            GaussianCurvature.calculate(halfEdgeDataStructure);
            state.setCalculatedGaussian(true);
        }

        MeshAttribute attribute = new GaussianCurvature();
        state.setMeshAttribute(attribute);
        state.transperacy(false);
        infoLogger.setAttribute(attribute.getName());
        return attribute;
    }

    private List<File> paths;

    public void loadNewFile() {
        Frame f = new Frame();
        final FileDialog fd = new FileDialog(f, "Select mesh files", FileDialog.LOAD);
        fd.setVisible(true);

        File file = new File(fd.getDirectory(), fd.getFile());
        paths.add(file);
        meshIterator = paths.size()-1;
        selectFile(file);

    }

    public void selectFile(File file) {
        state = new RenderState();
        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get(file.getPath());
        meshRenderer = new MeshRenderer(halfEdgeDataStructure);
        infoLogger.setModelPath("Model path:" + file.getPath());
    }

    public void nextFile() {
        meshIterator++;
        if (paths.size() == meshIterator)
            meshIterator = 0;

        selectFile(paths.get(meshIterator));
    }

    public void prevFile() {
        meshIterator--;
        if (meshIterator == -1)
            meshIterator = paths.size() - 1;
        selectFile(paths.get(meshIterator));
    }
}
