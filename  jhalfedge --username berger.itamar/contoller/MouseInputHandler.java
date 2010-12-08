package contoller;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * This class enable the user to rotate the bounding box with mouse.
 * User: itamar
 * Date: Oct 30, 2010
 * Time: 10:14:56 AM
 */
public class MouseInputHandler extends MouseInputAdapter {
    private Controller Controller;

    public MouseInputHandler(Controller Controller) {
        this.Controller = Controller;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            Controller.startDrag(mouseEvent.getPoint());
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            Controller.drag(mouseEvent.getPoint());
        }
    }
}
