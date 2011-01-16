package contoller;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This class enable the user to rotate the bounding box with mouse and zoom using the mouse wheel.
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
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            Controller.loadNewFile();
        }
        if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
            Controller.toggleAttributes();
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            Controller.drag(mouseEvent.getPoint());
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Controller.zoom(e.getWheelRotation());
    }
}
