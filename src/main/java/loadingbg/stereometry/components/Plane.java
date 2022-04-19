package loadingbg.stereometry.components;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import loadingbg.stereometry.Utils;
import org.fxyz3d.shapes.primitives.helper.delaunay.Triangle3D;

public class Plane extends StereoShape {
    private final MeshView view = new MeshView();

    public Plane(final Space space, final Point3D... points) {
        super(space, points);
        updateView();
        view.setOnMouseClicked(event -> onClick());
        prepareAndAdd(view);
    }

    private void updateView() {
        final TriangleMesh triMesh = new TriangleMesh();
        for (final Triangle3D triangle : Utils.triangulate(points.stream().map(org.fxyz3d.geometry.Point3D::convertFromJavaFXPoint3D).toList())) {
            final org.fxyz3d.geometry.Point3D[] trianglePoints = Utils.counterClockwiseTriangle(triangle.getP0(), triangle.getP1(), triangle.getP2());
            triMesh.getPoints().addAll(
                trianglePoints[0].getX(), trianglePoints[0].getY(), trianglePoints[0].getZ(),
                trianglePoints[1].getX(), trianglePoints[1].getY(), trianglePoints[1].getZ(),
                trianglePoints[2].getX(), trianglePoints[2].getY(), trianglePoints[2].getZ()
            );
        }

        triMesh.getTexCoords().addAll(0, 0);

        for (int i = 0; i < triMesh.getPoints().size() / 3; i += 3) {
            triMesh.getFaces().addAll(i, 0, i + 1, 0, i + 2, 0);
        }

        view.setMesh(triMesh);
    }

    @Override
    public boolean containsPoint(final Point3D point) {
        return Utils.determinant(new double[][] {
            new double[] { points.get(0).getX(), points.get(1).getX(), points.get(2).getX(), point.getX() },
            new double[] { points.get(0).getY(), points.get(1).getY(), points.get(2).getY(), point.getY() },
            new double[] { points.get(0).getZ(), points.get(1).getZ(), points.get(2).getZ(), point.getZ() },
            new double[] { 1, 1, 1, 1 },
        }) == 0;
    }

    @Override
    public void addPoint(Point3D point) {
        points.add(point);
        updateView();
    }

    @Override
    public boolean removePoint(Point3D point) {
        points.remove(point);
        return points.size() < 3;
    }
}
