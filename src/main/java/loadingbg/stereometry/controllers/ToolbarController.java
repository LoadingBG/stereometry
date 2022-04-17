package loadingbg.stereometry.controllers;

import javafx.scene.paint.Color;
import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.components.TransparentBox;

public final class ToolbarController {
    private Space space;

    public void setSpace(final Space newSpace) {
        space = newSpace;
    }

    public void cubePreset() {
        space.clearShapes();
        space.addShape(new TransparentBox(space, 100, 100, 100, Color.BLUE));
    }
}
