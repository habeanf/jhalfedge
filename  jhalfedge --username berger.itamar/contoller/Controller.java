package contoller;

import colormaps.ColorMapFactory;
import colormaps.ValueRedColorMap;
import colormaps.IColorMap;
import model.HalfEdgeDataStructure;
import parser.HalfEdgeDataStructureGenerator;
import render.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.*;

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



    // current color ColorMaps
    private IColorMap sliceColorMap;

    public static final float WORLD_SIZE = 2f;
    private static final float freq = 1 / 20f;

    // lighting settings
    private boolean lightingEnabled;
    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {-20.0f, 0.0f, -10.0f, 1.0f};

    // display settings
    private boolean isSmooth = true;
    private boolean isTransparent = true;

    // current handlers state
    private float xrot;
    private boolean increaseX;
    private boolean decreaseX;
    private float yrot;
    private boolean increaseY;
    private boolean decreaseY;

    // default value for renderes
    private boolean enableGrid = false;


    private float z = -5.0f;			// Depth Into The Screen
    private boolean zoomIn;
    private boolean zoomOut;

    // need for mouse
    private int width = 640;
    private int height = 480;
    private boolean isIsoColorMap = false;

    RenderState state;

    public Controller() {
        sliceColorMap = ColorMapFactory.getNextColorMap();

        gridRenderer = new GridRenderer(freq);

//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\Armadillo_f40000.obj");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\BEAR_KLA.off");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\cheetah.off");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\Dinopet_stripped.off");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\gargoyle.off");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\Candil.obj");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\Candil.obj");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\elk_48k.obj");
        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\homer.obj");
//        halfEdgeDataStructure = HalfEdgeDataStructureGenerator.get("C:\\Workspace\\ex2\\src\\Models\\sample.obj");

        meshRenderer = new MeshRenderer(halfEdgeDataStructure);

        state = new RenderState();
    }

    private HalfEdgeDataStructure halfEdgeDataStructure = null;

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

        drawWireFrame(gl);

        if (lightingEnabled)
            gl.glEnable(GL.GL_LIGHTING);
        else
            gl.glDisable(GL.GL_LIGHTING);


        meshRenderer.render(gl,state);

//        meshRenderer.renderFace(gl);

        if (enableGrid) {
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
        glu.gluPerspective(45.0f, h, 1.0, 60.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void update() {
        if (decreaseX)
            xrot -= 4f;
        if (increaseX)
            xrot += 4f;
        if (decreaseY)
            yrot -= 4f;
        if (increaseY)
            yrot += 4f;
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

    public void switchColorMap() {
        sliceColorMap = ColorMapFactory.getNextColorMap();
    }

    public void toggleShadeModel() {
        isSmooth = !isSmooth;
    }

    public void toggleIsoColorMapping() {
        isIsoColorMap = !isIsoColorMap;
    }
}
