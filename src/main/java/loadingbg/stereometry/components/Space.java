package loadingbg.stereometry.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public final class Space extends Group {
    private static final int MAX_ZOOM = 1000;

    private final Group superGroup = new Group();
    private final Group canvasGroup = new Group();
    private final Group superLayout;

    private double anchorX;
    private double anchorY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorAngleX;
    private double anchorAngleY;

    private final DoubleProperty zoom = new SimpleDoubleProperty(canvasGroup.getTranslateZ());

    public Space(final Group superLayout, final int width, final int height) {
        this.superLayout = superLayout;

        superGroup.getChildren().add(canvasGroup);
        superGroup.getChildren().add(new AmbientLight());

        final SubScene scene = new SubScene(superGroup, width, height, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GRAY);

        final Camera camera = new PerspectiveCamera();
        camera.setNearClip(0.01);
        camera.setFarClip(100);
        scene.setCamera(camera);

        canvasGroup.translateXProperty().bind(scene.widthProperty().divide(2));
        canvasGroup.translateYProperty().bind(scene.heightProperty().divide(2));
        canvasGroup.translateZProperty().bind(zoom);

        final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateX.angleProperty().bind(angleX);
        final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        rotateY.angleProperty().bind(angleY);
        canvasGroup.getTransforms().addAll(rotateX, rotateY);

        getChildren().add(scene);
    }

    public void addShape(final Node shape) {
        canvasGroup.getChildren().add(shape);
    }

    public void addAllShapes(final Node... shapes) {
        canvasGroup.getChildren().addAll(shapes);
    }

    public void clearShapes() {
        angleX.set(0);
        angleY.set(0);
        zoom.set(0);
        canvasGroup.getChildren().clear();
    }

    public void openMenu(final StereoShape shape) {
        // TODO: finish
        System.out.println("Opening popup for shape " + shape);

        final VBox menu = new VBox();

        final HBox menuButtons = new HBox();
        menu.getChildren().add(menuButtons);

        final Pane emptyPane = new Pane();
        HBox.setHgrow(emptyPane, Priority.ALWAYS);
        menuButtons.getChildren().add(emptyPane);

        final Button close = new Button("X");
        close.setOnAction(event -> superLayout.getChildren().remove(menu));
        menuButtons.getChildren().add(close);

        final Accordion properties = new Accordion();
        menu.getChildren().add(properties);

        final VBox generalMenu = new VBox();

        properties.getPanes().addAll(
            new TitledPane("General", generalMenu)
        );

        superLayout.getChildren().add(menu);
        superLayout.layout();
        //System.out.println(superLayout.getRight());
    }

    public void onMousePressed(final MouseEvent event) {
        anchorX = event.getSceneX();
        anchorY = event.getSceneY();
        anchorAngleX = angleX.get();
        anchorAngleY = angleY.get();
    }

    public void onMouseDragged(final MouseEvent event) {
        final double newAngleX = anchorAngleX - (anchorY - event.getSceneY());
        if (newAngleX >= -90 && newAngleX <= 90) {
            angleX.set(newAngleX);
        }
        angleY.set(anchorAngleY + anchorX - event.getSceneX());
    }

    public void onScroll(final ScrollEvent event) {
        final double newZ = zoom.get() - 5 * event.getDeltaY();
        if (newZ > -MAX_ZOOM && newZ < MAX_ZOOM) {
            zoom.set(newZ);
        }
    }
}
