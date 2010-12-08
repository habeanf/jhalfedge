import contoller.Controller;
import contoller.InputHandler;
import contoller.MouseInputHandler;
import utils.GLDisplay;

/**
 * User: itamar
 * This is the main class that run the application. it links between the controller display and the event listeners
 * It is based on Nehe tutorial Runner files for running JOGL applications (version jogl-1.1.1-windows-i586)
 * Press F1 to see all keyboard shortcuts.
 *
 * The application assume that there exists a lib folder with the following files: gluegen-rt.jar,
 * jogl.jar and the following openGL dlls: gluegen-rt.dll , jogl.dll, jogl_awt.dll, jogl_cg.dll
 *
 * The program has been developed and tested under Win7 64bit, 4GB memory, core i5 750 2.66Ghz with ATI radeon 5970 and
 * ran smoothly with all configurations
 * 
 */
public class Main {
    public static void main(String[] args) {
        GLDisplay display = GLDisplay.createGLDisplay("3D Meshes processing");
        Controller Controller = new Controller();
        InputHandler keyboardInputHandler = new InputHandler(Controller, display);
        MouseInputHandler mouseInputHandler = new MouseInputHandler(Controller);

        display.addMouseListener(mouseInputHandler);
        display.addMouseMotionListener(mouseInputHandler);
        display.addGLEventListener(Controller);
        display.addKeyListener(keyboardInputHandler);
        display.start();
    }
}