package loadingbg.stereometry.reference;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public final class Main extends Application {
    private static final double WIDTH = 1400;
    private static final double HEIGHT = 800;

    private double anchorX;
    private double anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Box box = prepareBox();

        SmartGroup group = new SmartGroup();
        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1200);
        group.getChildren().add(box);
        group.getChildren().addAll(prepareLightSource());
        group.getChildren().add(new AmbientLight(Color.GRAY));

        Camera camera = new PerspectiveCamera();

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        initMouseControl(group, scene, primaryStage);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W -> group.translateZProperty().set(group.getTranslateZ() + 100);
                case S -> group.translateZProperty().set(group.getTranslateZ() - 100);
                case Q -> group.rotateByX(10);
                case E -> group.rotateByX(-10);
                case NUMPAD6 -> group.rotateByY(10);
                case NUMPAD4 -> group.rotateByY(-10);
            }
        });

        primaryStage.setTitle("Stereometry");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                light.setRotate(light.getRotate() + 1);
            }
        };
        timer.start();
    }

    private final PointLight light = new PointLight();
    private Node[] prepareLightSource() {
//        AmbientLight light = new AmbientLight();
//        light.setColor(Color.AQUA);
//        return light;
        light.setColor(Color.AQUA);
        light.getTransforms().add(new Translate(0, -50, 100));
        light.setRotationAxis(Rotate.X_AXIS);

        Sphere sphere = new Sphere(2);
        sphere.getTransforms().addAll(light.getTransforms());
        sphere.rotateProperty().bind(light.rotateProperty());
        sphere.rotationAxisProperty().bind(light.rotationAxisProperty());

        return new Node[] { light, sphere };
    }

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial(Color.YELLOW);
        material.setSpecularColor(Color.WHITE);
        Box box = new Box(100, 20, 50);
        box.setMaterial(material);
        return box;
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        group.getTransforms().addAll(xRotate, yRotate);
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double movement = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + movement);
        });
    }

    public static void main(final String[] args) {
        launch(args);
    }

    private static final class SmartGroup extends Group {
        private Rotate r;
        private Transform t = new Rotate();

        void rotateByX(final int angle) {
            r = new Rotate(angle, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            getTransforms().clear();
            getTransforms().add(t);
        }

        void rotateByY(final int angle) {
            r = new Rotate(angle, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            getTransforms().clear();
            getTransforms().add(t);
        }
    }
}
