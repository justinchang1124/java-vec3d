import java.awt.Color;

/**
 * Represents a rectangle aligned with the axes.
 *
 * @author Justin C
 */
public class Rectangle extends Model
{
  /**
   * Constructs a Rectangle.
   *
   * @param d1
   * The length of one side of the Rectangle.
   *
   * @param d2
   * The length of another adjacent side of the Rectangle.
   *
   * @param orientation
   * orientation = 0: d1 is along x-axis, d2 is along y-axis.
   * orientation = 1: d1 is along y-axis, d2 is along z-axis.
   * orientation = 2: d1 is along z-axis, d2 is along x-axis.
   *
   * orientation = 0: parallel to the XY plane.
   * orientation = 1: parallel to the YZ plane.
   * orientation = 2: parallel to the ZX plane.
   */
  public Rectangle(Vec3 o, boolean sB,
      double d1, double d2, Color color, int orientation)
  {
    super(o, sB);

    if (d1 < 0 || d2 < 0 || orientation < 0 || orientation > 2)
      throw new IllegalArgumentException("A dimension of this rectangle is negative!");

    double[][] coords = null;

    if (orientation == 0)
      coords = new double[][] {
          { -d1 / 2, -d2 / 2, 0, +d1 / 2, -d2 / 2, 0, -d1 / 2, +d2 / 2, 0 },
          { +d1 / 2, +d2 / 2, 0, +d1 / 2, -d2 / 2, 0, -d1 / 2, +d2 / 2, 0 } };

    if (orientation == 1)
      coords = new double[][] {
          { 0, -d1 / 2, -d2 / 2, 0, +d1 / 2, -d2 / 2, 0, -d1 / 2, +d2 / 2 },
          { 0, +d1 / 2, +d2 / 2, 0, +d1 / 2, -d2 / 2, 0, -d1 / 2, +d2 / 2 } };

    if (orientation == 2)
      coords = new double[][] {
          { -d2 / 2, 0, -d1 / 2, +d2 / 2, 0, -d1 / 2, -d2 / 2, 0, +d1 / 2 },
          { +d2 / 2, 0, +d1 / 2, +d2 / 2, 0, -d1 / 2, -d2 / 2, 0, +d1 / 2 } };

    for (int i = 0; i < coords.length; i++)
      tris.add(new Triangle(coords[i], color));
  }

  /**
   * Constructs a square of length d.
   */
  public Rectangle(Vec3 o, boolean sB,
      double d, Color color, int orientation)
  {
    this(o, sB, d, d, color, orientation);
  }

  public void tick()
  {

  }
}
