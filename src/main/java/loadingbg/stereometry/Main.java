package loadingbg.stereometry;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.components.TransparentBox;
import loadingbg.stereometry.tools.PointPointTool;

public final class Main extends Application {
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setScene(createScene());
        primaryStage.setTitle("Stereometry");
        primaryStage.show();
    }

    private Scene createScene() {
        final VBox layout = new VBox();

        final Space space = new Space(1400, 800);
        layout.getChildren().add(createToolbar(space));
        layout.getChildren().add(space);

        final Scene scene = new Scene(layout);
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, space::onMousePressed);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, space::onMouseDragged);
        scene.addEventHandler(ScrollEvent.SCROLL, space::onScroll);
        return scene;
    }

    private static MenuBar createToolbar(final Space space) {
        final MenuItem cubePreset = new MenuItem("Cube");
        cubePreset.setOnAction(event -> {
            space.clearShapes();
            space.closeMenu();
            space.addShape(new TransparentBox(space, 100, 100, 100, Color.BLUE));
        });

        final Menu presetsMenu = new Menu("Presets");
        presetsMenu.getItems().addAll(cubePreset);

        final Menu toolsMenu = new Menu("Tools");
        List.of(new PointPointTool()).forEach(tool -> {
            final MenuItem toolItem = new MenuItem(tool.displayName());
            toolItem.setOnAction(event -> space.setTool(tool));
            toolsMenu.getItems().add(toolItem);
        });

        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
            presetsMenu,
            toolsMenu
        );
        return menuBar;
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
