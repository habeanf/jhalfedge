package contoller;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            Frame f = new Frame();
            final FileDialog fd = new FileDialog(f, "Select mesh files", FileDialog.LOAD);
            fd.setVisible(true);

            Controller.loadFile(fd.getDirectory(), fd.getFile());
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
