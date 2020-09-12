import java.awt.Color;

/**
 * A Model that represents a standard box with six faces.
 *
 * @author Justin Chang
 */
public class Box extends Model
{
  /**
   * Constructs a Box.
   *
   * @param xL
   * The x-axis length of the box.
   * @param yL
   * The y-axis length of the box.
   * @param zL
   * The z-axis length of the box.
   * @param c
   * An array of colors used for each face of the box.
   * The order of face coloring is x+, y+, z+, x-, y-, z-.
   */
  public Box(Vec3 o, boolean sB,
      double xL, double yL, double zL, Color[] c)
  {
    super(o, sB);

    if (c.length != 6)
      throw new IllegalArgumentException("A box must be created with 6 colors!");

    if (xL < 0 || yL < 0 || zL < 0)
      throw new IllegalArgumentException("A dimension of this box is negative!");

    double[] orient = { xL, yL, zL };
    Vec3[] v = {
        new Vec3(+xL / 2, 0, 0), new Vec3(0, +yL / 2, 0), new Vec3(0, 0, +zL / 2),
        new Vec3(-xL / 2, 0, 0), new Vec3(0, -yL / 2, 0), new Vec3(0, 0, -zL / 2) };

    // Constructs all faces based on the positions of their centers
    // (as in Vec3[] v).
    for (int i = 0; i < v.length; i++)
      append(new Rectangle(v[i], sB,
          orient[(i + 1) % 3], orient[(i + 2) % 3], c[i], (i + 1) % 3));
  }

  /**
   * Constructs the default version of a box: a cube.
   *
   * @param d
   * The length of a side of the cube.
   */
  public Box(Vec3 o, boolean sB, double d, Color[] c)
  {
    this(o, sB, d, d, d, c);
  }

  public void tick()
  {

  }
}
