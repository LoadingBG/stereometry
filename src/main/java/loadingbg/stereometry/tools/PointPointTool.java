package loadingbg.stereometry.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import loadingbg.stereometry.components.Line;
import loadingbg.stereometry.components.Point;
import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.components.StereoShape;

public final class PointPointTool implements Tool {
    private Point firstPoint;

    @Override
    public String displayName() {
        return "Create line from two points";
    }

    @Override
    public boolean onMouseClick(final StereoShape shape, final Space space) {
        if (!(shape instanceof Point point)) {
            new Alert(Alert.AlertType.ERROR, "This tool can only work with points.", ButtonType.OK).showAndWait();
            return false;
        }
        if (firstPoint == null) {
            firstPoint = point;
            return false;
        }
        space.addShape(new Line(space, firstPoint.point(), point.point()));
        firstPoint = null;
        return true;
    }
}
