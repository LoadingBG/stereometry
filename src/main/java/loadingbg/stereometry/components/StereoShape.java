package loadingbg.stereometry.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Shape3D;

public abstract class StereoShape extends Parent {
    protected final List<Point3D> points;
    protected final Space space;
    protected final PhongMaterial material = new PhongMaterial(Color.BLACK);

    protected StereoShape(final Space space, final Point3D... points) {
        this.points = new ArrayList<>();
        this.space = space;
        this.points.addAll(Arrays.asList(points));
    }

    public StereoShape withColor(final Color newColor) {
        material.setDiffuseColor(newColor);
        return this;
    }

    protected final void onClick() {
        space.onClick(this);
    }

    public final Color color() {
        return material.getDiffuseColor();
    }

    protected final void prepareAndAdd(final Shape3D shape) {
        shape.setMaterial(material);
        shape.setCullFace(CullFace.NONE);
        shape.setOnMouseClicked(event -> onClick());
        getChildren().add(shape);
    }

    public abstract boolean containsPoint(final Point3D point);

    public abstract void addPoint(final Point3D point);

    public abstract boolean removePoint(final Point3D point);

    @Override
    public boolean equals(final Object o) {
        if (o instanceof StereoShape other) {
            return points.equals(other.points);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }
}
