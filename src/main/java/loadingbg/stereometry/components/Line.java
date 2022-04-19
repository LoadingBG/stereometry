package loadingbg.stereometry.components;

import java.util.AbstractMap;
import java.util.Comparator;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class Line extends StereoShape {
    public Line(final Space space, final Point3D p1, final Point3D p2) {
        super(space, p1, p2);

        prepareAndAdd(generateCylinder());
    }

    private Cylinder generateCylinder() {
        final AbstractMap.SimpleImmutableEntry<Point3D, Point3D> pair = points
            .stream()
            .flatMap(p1 -> points.stream()
                .map(p2 -> new AbstractMap.SimpleImmutableEntry<>(p1, p2)))
            .max(Comparator.comparingDouble(p -> p.getValue().subtract(p.getKey()).magnitude()))
            .orElseThrow();

        final Point3D p1 = pair.getKey();
        final Point3D p2 = pair.getValue();
        final Point3D diff = p2.subtract(p1);
        final Point3D axisOfRotation = diff.crossProduct(Rotate.Y_AXIS);
        final double angle = Math.acos(diff.normalize().dotProduct(Rotate.Y_AXIS));
        final Point3D mid = p1.midpoint(p2);

        getTransforms().addAll(
            new Translate(mid.getX(), mid.getY(), mid.getZ()),
            new Rotate(-Math.toDegrees(angle), axisOfRotation)
        );

        final Cylinder cylinder = new Cylinder(1, diff.magnitude());
        cylinder.setOnMouseClicked(event -> onClick());
        return cylinder;
    }

    @Override
    public boolean containsPoint(Point3D point) {
        final Point3D p1 = points.get(0);
        final Point3D p2 = points.get(1);
        final Point3D p2p1 = p2.subtract(p1);
        final Point3D pp1 = point.subtract(p1);
        return Math.abs(p2p1.dotProduct(pp1)) / (p2p1.magnitude() * pp1.magnitude()) >= 1.0 - 1e-10;
    }

    @Override
    public void addPoint(Point3D point) {
        points.add(point);
    }

    @Override
    public boolean removePoint(Point3D point) {
        points.remove(point);
        return points.size() < 2;
    }
}
