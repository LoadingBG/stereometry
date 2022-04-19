package loadingbg.stereometry.tools;

import loadingbg.stereometry.components.Space;
import loadingbg.stereometry.components.StereoShape;

public interface Tool {
    String displayName();

    boolean onMouseClick(final StereoShape shape, final Space space);
}
