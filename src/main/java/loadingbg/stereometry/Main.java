package loadingbg.stereometry;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.controllers.ToolbarController;

public final class Main extends Application {
    @Override
    public void start(final Stage primaryStage) throws IOException {
        primaryStage.setScene(createScene());
        primaryStage.setTitle("Stereometry");
        primaryStage.show();
    }

    private Scene createScene() throws IOException {
        final VBox layout = new VBox();

        final Group spaceLayout = new Group();
        final Space space = new Space(spaceLayout, 1400, 800);
        spaceLayout.getChildren().add(space);

        layout.getChildren().add(createToolbar(space));
        layout.getChildren().add(spaceLayout);

        final Scene scene = new Scene(layout);
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, space::onMousePressed);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, space::onMouseDragged);
        scene.addEventHandler(ScrollEvent.SCROLL, space::onScroll);
        return scene;
    }

    private static MenuBar createToolbar(final Space space) throws IOException {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/toolbar.fxml"));
        final MenuBar box = loader.load();
        final ToolbarController controller = loader.getController();
        controller.setSpace(space);
        return box;
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
