package loadingbg.stereometry.components;

import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;

public final class Point extends StereoShape {
    public Point(final Space space, final Point3D point) {
        super(space, point);
        translateXProperty().set(point.getX());
        translateYProperty().set(point.getY());
        translateZProperty().set(point.getZ());

        prepareAndAdd(new Sphere(3));
    }

    @Override
    public boolean containsPoint(Point3D point) {
        return point.equals(points.get(0));
    }

    @Override
    public void addPoint(Point3D point) {
        // Cannot add point
    }

    @Override
    public boolean removePoint(Point3D point) {
        return true;
    }
}
