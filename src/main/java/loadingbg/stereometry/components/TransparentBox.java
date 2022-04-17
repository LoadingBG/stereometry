package loadingbg.stereometry.components;

import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

public class TransparentBox extends Parent {
    public TransparentBox(final Space space, final int width, final int height, final int depth, final Color color) {
        final Color alphaColor = Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness(), 0.10);
        final double halfWidth = width / 2.0;
        final double halfHeight = height / 2.0;
        final double halfDepth = depth / 2.0;

        final Point3D p1 = new Point3D(-halfWidth,  halfHeight, -halfDepth);
        final Point3D p2 = new Point3D( halfWidth,  halfHeight, -halfDepth);
        final Point3D p3 = new Point3D( halfWidth,  halfHeight,  halfDepth);
        final Point3D p4 = new Point3D(-halfWidth,  halfHeight,  halfDepth);
        final Point3D p5 = new Point3D(-halfWidth, -halfHeight, -halfDepth);
        final Point3D p6 = new Point3D( halfWidth, -halfHeight, -halfDepth);
        final Point3D p7 = new Point3D( halfWidth, -halfHeight,  halfDepth);
        final Point3D p8 = new Point3D(-halfWidth, -halfHeight,  halfDepth);

        getChildren().addAll(
            new Point(space, p1).withColor(color),
            new Point(space, p2).withColor(color),
            new Point(space, p3).withColor(color),
            new Point(space, p4).withColor(color),
            new Point(space, p5).withColor(color),
            new Point(space, p6).withColor(color),
            new Point(space, p7).withColor(color),
            new Point(space, p8).withColor(color),

            new Line(space, p1, p2).withColor(color),
            new Line(space, p4, p3).withColor(color),
            new Line(space, p5, p6).withColor(color),
            new Line(space, p8, p7).withColor(color),

            new Line(space, p1, p4).withColor(color),
            new Line(space, p2, p3).withColor(color),
            new Line(space, p5, p8).withColor(color),
            new Line(space, p6, p7).withColor(color),

            new Line(space, p1, p5).withColor(color),
            new Line(space, p2, p6).withColor(color),
            new Line(space, p3, p7).withColor(color),
            new Line(space, p4, p8).withColor(color),

            new Plane(space, p1, p2, p3, p4).withColor(alphaColor),
            new Plane(space, p5, p6, p7, p8).withColor(alphaColor),

            new Plane(space, p1, p2, p6, p5).withColor(alphaColor),
            new Plane(space, p4, p3, p7, p8).withColor(alphaColor),

            new Plane(space, p1, p5, p8, p4).withColor(alphaColor),
            new Plane(space, p2, p6, p7, p3).withColor(alphaColor)
        );
    }
}
