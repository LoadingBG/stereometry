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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import loadingbg.stereometry.tools.DefaultTool;
import loadingbg.stereometry.tools.Tool;

public final class Space extends Group {
    private static final int MAX_ZOOM = 1000;

    private Tool currentTool = new DefaultTool();

    private final Group uiGroup = new Group();
    private final Group canvasGroup = new Group();

    private double anchorX;
    private double anchorY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorAngleX;
    private double anchorAngleY;

    private final DoubleProperty zoom = new SimpleDoubleProperty(canvasGroup.getTranslateZ());

    public Space(final int width, final int height) {
        uiGroup.getChildren().add(canvasGroup);
        uiGroup.getChildren().add(new AmbientLight());

        final SubScene scene = new SubScene(uiGroup, width, height, true, SceneAntialiasing.BALANCED);
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
        closeMenu();

        final VBox menu = new VBox();

        final HBox menuButtons = new HBox();
        menu.getChildren().add(menuButtons);

        final Label titleLabel = new Label(getTitle(shape));
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        menuButtons.getChildren().add(titleLabel);

        final Button close = new Button("X");
        close.setOnAction(event -> uiGroup.getChildren().remove(menu));
        menuButtons.getChildren().add(close);

        final VBox generalMenu = new VBox();

        final HBox colorChooser = new HBox();
        generalMenu.getChildren().add(colorChooser);

        final Label colorLabel = new Label("Color:");
        colorChooser.getChildren().add(colorLabel);

        final Label colorDisplay = new Label();
        colorDisplay.setMinWidth(10);
        colorDisplay.setBackground(new Background(new BackgroundFill(Color.color(shape.color().getRed(), shape.color().getGreen(), shape.color().getBlue()), null, null)));
        colorLabel.setLabelFor(colorChooser);
        colorChooser.getChildren().add(colorDisplay);

        menu.getChildren().addAll(
            new TitledPane("General", generalMenu)
        );

        uiGroup.getChildren().add(menu);
    }

    public void closeMenu() {
        uiGroup.getChildren().removeIf(VBox.class::isInstance);
    }

    private static String getTitle(final StereoShape shape) {
        return shape.getClass().getSimpleName();
    }

    public void setTool(final Tool tool) {
        currentTool = tool;
    }

    public void onClick(final StereoShape shape) {
        if (currentTool.onMouseClick(shape, this)) {
            setTool(new DefaultTool());
        }
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
