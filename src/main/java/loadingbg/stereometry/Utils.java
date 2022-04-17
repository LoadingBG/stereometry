package loadingbg.stereometry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.helper.delaunay.DelaunayMesh;
import org.fxyz3d.shapes.primitives.helper.delaunay.Triangle3D;
import org.fxyz3d.shapes.primitives.helper.delaunay.jdt.Point;

public final class Utils {
    private Utils() {
    }

    public static double determinant(double[][] matrix) {
        final int size = matrix.length;
        if (size == 1) {
            return matrix[0][0];
        }
        double det = 0;
        for (int col = 0; col < size; ++col) {
            final double[][] subMatrix = new double[size - 1][size - 1];
            for (int i = 1; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    if (j > col) {
                        subMatrix[i - 1][j - 1] = matrix[i][j];
                    } else if (j < col) {
                        subMatrix[i - 1][j] = matrix[i][j];
                    }
                }
            }
            det += (col % 2 == 0 ? 1 : -1) * matrix[0][col] * determinant(subMatrix);
        }
        return det;
    }

    public static Point3D[] counterClockwiseTriangle(final org.fxyz3d.geometry.Point3D p1,
                                                     final org.fxyz3d.geometry.Point3D p2,
                                                     final org.fxyz3d.geometry.Point3D p3) {
        final double det = determinant(new double[][]{
            new double[]{p1.getX(), p2.getX(), p3.getX()},
            new double[]{p1.getY(), p2.getY(), p3.getY()},
            new double[]{p1.getZ(), p2.getZ(), p3.getZ()},
        });
        if (det > 0) {
            return new Point3D[]{p1, p2, p3};
        }
        return new Point3D[]{p3, p2, p1};
    }

    public static List<Triangle3D> triangulate(final List<Point3D> points) {
        if (points.stream().map(Point3D::getX).distinct().count() == 1) {
            return triangulateMapped(points, p -> new Point3D(p.getY(), p.getX(), p.getZ()));
        } else if (points.stream().map(Point3D::getZ).distinct().count() == 1) {
            return triangulateMapped(points, p -> new Point3D(p.getX(), p.getZ(), p.getY()));
        }
        return triangulateMapped(points, UnaryOperator.identity());
    }

    private static List<Triangle3D> triangulateMapped(final List<Point3D> points, final UnaryOperator<Point3D> mapper) {
        final List<Point3D> preparedPoints = points.stream().map(mapper).toList();
        final DelaunayMesh mesh = new DelaunayMesh(preparedPoints);
        final List<Triangle3D> triangles = mesh.getTriangle3DList();
        final List<org.fxyz3d.geometry.Point3D> normalized = mesh.getNormalizedPoints();
        final List<org.fxyz3d.geometry.Point3D> reference = mesh.getDataPoints();

        final List<Triangle3D> unnormalized = new ArrayList<>();
        for (final Triangle3D tri : triangles) {
            unnormalized.add(Triangle3D.of(
                mapper.apply(reference.get(normalized.indexOf(tri.getP0()))),
                mapper.apply(reference.get(normalized.indexOf(tri.getP1()))),
                mapper.apply(reference.get(normalized.indexOf(tri.getP2())))
            ));
        }
        return unnormalized;
    }
}
