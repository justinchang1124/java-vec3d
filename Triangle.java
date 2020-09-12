import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * A triangle that acts as the building block for all Models.
 *
 * @author Justin C
 */
public class Triangle
{
  /**
   * The three vertices of the triangle.
   */
  public Vec3 v1, v2, v3;

  /**
   * The color of the triangle.
   */
  public Color color;

  /**
   * Whether this triangle will be rendered with shadows.
   */
  public boolean shadows;

  /**
   * Constructs a Triangle from 3 vertices, a color, and a shadows boolean.
   */
  public Triangle(Vec3 o1, Vec3 o2, Vec3 o3, Color c, boolean s)
  {
    v1 = o1;
    v2 = o2;
    v3 = o3;
    color = c;
    shadows = s;
  }

  /**
   * Copy constructor.
   */
  public Triangle(Triangle o)
  {
    this(new Vec3(o.v1), new Vec3(o.v2), new Vec3(o.v3), o.color, o.shadows);
  }

  /**
   * Constructs a Triangle from a double[] of length 9, a color, and a shadows
   * boolean.
   */
  public Triangle(double[] xyz, Color c, boolean s)
  {
    if (xyz.length != 9)
      throw new IllegalArgumentException(
          "Constructing Triangle requires an array of length 9!");

    v1 = new Vec3(xyz[0], xyz[1], xyz[2]);
    v2 = new Vec3(xyz[3], xyz[4], xyz[5]);
    v3 = new Vec3(xyz[6], xyz[7], xyz[8]);
    color = c;
    shadows = s;
  }

  /**
   * Constructs a triangle that, by default, has shadows turned off.
   */
  public Triangle(Vec3 o1, Vec3 o2, Vec3 o3, Color color)
  {
    this(o1, o2, o3, color, false);
  }

  /**
   * Constructs a triangle that, by default, has shadows turned off.
   */
  public Triangle(double[] xyz, Color color)
  {
    this(xyz, color, false);
  }

  /**
   * Performs a specific linear transformation using precomputed coefficients.
   *
   * @param a
   * A linear transformation coefficient.
   * @param b
   * A linear transformation coefficient.
   * @param c
   * A linear transformation coefficient.
   */
  public static void linearTransform(Vec3 v0, double a, double b, double c)
  {
    v0.v[0][0] *= a / v0.v[2][0];
    v0.v[1][0] *= -a / v0.v[2][0];
    v0.v[2][0] = v0.v[2][0] * b + c;
  }

  /**
   * Generates a BufferedImage with dimension (w,h) from the camera aligned at
   * b with near clipping plane n, far clipping plane f, and zoom factor z.
   */
  public static BufferedImage getImage(ArrayList<Triangle> pipeline,
  Basis b, int w, int h, double n, double f, double z)
  {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    int len = w * h;
    double[] zib = new double[len];
    for (int i = 0; i < len; i++)
      // This essentially clips the far plane.
      zib[i] = -0.25;

    double xy_1 = -n * z;
    double z_1 = 2 / (f - n);
    double z_0 = 2 * n / (f - n) - 2;

    for (Triangle t : pipeline)
      t.draw(b, n, xy_1, z_1, z_0, img, zib);

    return img;
  }

  /**
   * String representation.
   */
  public String toString()
  {
    return "***TRIANGLE***\nVertices: "+v1+v2+v3+"\nColor: "+color+", "+
    (shadows ? "shaded\n" : "unshaded\n");
  }

  /**
   * Draws this triangle by creating and rendering the necessary Triangle_NDCs.
   *
   * @param basis
   * The camera basis that this Triangle will be rendered with.
   * @param n
   * The near clipping plane of the viewport.
   * @param a
   * A precomputed linear transformation coefficient.
   * @param b
   * A precomputed linear transformation coefficient.
   * @param c
   * A precomputed linear transformation coefficient.
   * @param img
   * The image that this triangle will be drawn on.
   * @param zib
   * The z-inverse buffer for the image that the triangle will be drawn on.
   */
  public void draw(Basis basis, double n, double a, double b, double c,
      BufferedImage img, double[] zib)
  {
    // Orients the vertices with respect to this basis.
    Vec3[] v = { basis.G_L(v1), basis.G_L(v2), basis.G_L(v3) };

    // Sorts the vertices from closest to farthest.
    Arrays.sort(v);
    Vec3 v1 = v[0];
    Vec3 v2 = v[1];
    Vec3 v3 = v[2];

    // Gets the color of this triangle.
    int rgb = color.getRGB();

    if (shadows)
    {
      Vec3 normal = v2.sub(v1).cross(v3.sub(v1));
      double cosZ = Math.abs(normal.unit().v[2][0]);
      // 2.4 produces a good shading effect based on testing values
      double log_shade = Math.pow(cosZ, 1 / 2.4);

      int rr = (int) (((rgb >> 16) & 0xFF) * log_shade);
      int gg = (int) (((rgb >> 8) & 0xFF) * log_shade);
      int bb = (int) ((rgb & 0xFF) * log_shade);

      rgb = (255 << 24) | (rr << 16) | (gg << 8) | bb;
    }

    // Counts the number of vertices that must be clipped.
    int numClips = 0;

    if (v1.v[2][0] >= -n)
      numClips++;
    if (v2.v[2][0] >= -n)
      numClips++;
    if (v3.v[2][0] >= -n)
      numClips++;

    if (numClips == 3)
      return;

    // Appropriately clips the vertices.
    if (numClips == 1)
    {
      Vec3 vec01 = v2.sub(v1);
      Vec3 vec02 = v3.sub(v1);
      Vec3 v1p = v2.add(vec01.dot(-(v2.v[2][0] + n) / vec01.v[2][0]));
      Vec3 v2p = v3.add(vec02.dot(-(v3.v[2][0] + n) / vec02.v[2][0]));

      linearTransform(v2, a, b, c);
      linearTransform(v3, a, b, c);
      linearTransform(v1p, a, b, c);
      linearTransform(v2p, a, b, c);

      (new Triangle_NDC(v2, v1p, v2p, rgb)).render(img, zib);
      (new Triangle_NDC(v2p, v3, v2, rgb)).render(img, zib);
      return;
    }

    if (numClips == 2)
    {
      Vec3 vec20 = v1.sub(v3);
      Vec3 vec21 = v2.sub(v3);
      v2 = v3.add(vec21.dot((-v3.v[2][0] - n) / vec21.v[2][0]));
      v1 = v3.add(vec20.dot((-v3.v[2][0] - n) / vec20.v[2][0]));
    }

    linearTransform(v1, a, b, c);
    linearTransform(v2, a, b, c);
    linearTransform(v3, a, b, c);

    (new Triangle_NDC(v1, v2, v3, rgb)).render(img, zib);
  }
}
