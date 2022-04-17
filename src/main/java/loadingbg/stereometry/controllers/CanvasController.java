package loadingbg.stereometry.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class CanvasController {
    @FXML
    private SubScene scene;
    @FXML
    private Group canvas;
    @FXML
    private Group root;
    @FXML
    private Camera camera;
    @FXML
    private Box outerBox;

    private double anchorX;
    private double anchorY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorAngleX;
    private double anchorAngleY;

    public void initialize() {
        canvas.translateXProperty().bind(scene.widthProperty().divide(2));
        canvas.translateYProperty().bind(scene.heightProperty().divide(2));

        final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateX.angleProperty().bind(angleX);
        final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        rotateY.angleProperty().bind(angleY);
        canvas.getTransforms().addAll(rotateX, rotateY);
    }

    public void mousePressed(final MouseEvent event) {
        anchorX = event.getSceneX();
        anchorY = event.getSceneY();
        anchorAngleX = angleX.get();
        anchorAngleY = angleY.get();
    }

    public void mouseDragged(final MouseEvent event) {
        angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
        angleY.set(anchorAngleY + anchorX - event.getSceneX());
    }

    public void scroll(final ScrollEvent event) {
        canvas.translateZProperty().set(canvas.getTranslateZ() - 5 * event.getDeltaY());
    }

    public Group root() {
        return root;
    }
}
