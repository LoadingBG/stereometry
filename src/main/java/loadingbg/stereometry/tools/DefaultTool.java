package loadingbg.stereometry.tools;

import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.components.StereoShape;

public final class DefaultTool implements Tool {
    @Override
    public String displayName() {
        return null;
    }

    @Override
    public boolean onMouseClick(final StereoShape shape, final Space space) {
        space.openMenu(shape);
        return false;
    }
}
