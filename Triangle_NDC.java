import java.awt.image.BufferedImage;

/**
 * A triangle whose vertices have been expressed in Normalized Device
 * Coordinates and undergone all necessary transformations.
 *
 * Also supports highlighting the foremost Triangle_NDC.
 *
 * @author Justin Chang
 */
public class Triangle_NDC
{
  /**
   * The three vertices of the triangle. Will not be changed!
   */
  private Vec3 v1, v2, v3;

  /**
   * The color value of this triangle.
   */
  private final int rgb;

  /**
   * Constructs a Triangle_NDC from 3 vertices and an integer representing rgb.
   */
  public Triangle_NDC(Vec3 o1, Vec3 o2, Vec3 o3, int c)
  {
    v1 = o1;
    v2 = o2;
    v3 = o3;
    rgb = c;
  }

  /**
   * Returns twice the signed area of the triangle formed by the vertices v1,
   * v2, v3. Orienting vertices in counterclockwise order returns a positive
   * value.
   */
  public static double edge(Vec3 v1, Vec3 v2, Vec3 v3)
  {
    return (v3.v[0][0] - v1.v[0][0]) * (v2.v[1][0] - v1.v[1][0])
        - (v3.v[1][0] - v1.v[1][0]) * (v2.v[0][0] - v1.v[0][0]);
  }

  /**
   * Uses linear interpolation to blend a foreground and background color.
   *
   * @param t
   * The color value of the transparent foremost color.
   * @param o
   * The color value of the opaque background color.
   */
  public static int composite(int t, int o)
  {
    int alpha = (t >> 24) & 0xff;

    int rT = (t >> 16) & 0xff;
    int gT = (t >> 8) & 0xff;
    int bT = (t >> 0) & 0xff;

    int rB = (o >> 16) & 0xff;
    int gB = (o >> 8) & 0xff;
    int bB = (o >> 0) & 0xff;

    int r = (rT - rB) * alpha / 255 + rB;
    int g = (gT - gB) * alpha / 255 + gB;
    int b = (bT - bB) * alpha / 255 + bB;

    return (255 << 24) | (r << 16) | (g << 8) | b;
  }

  /**
   * Converts this triangle to a string representation.
   */
  public String toString()
  {
    return "***TRIANGLE_NDC***\nVertices: "+v1+v2+v3+"\nRGB: "+rgb+"\n";
  }

  /**
   * Renders this triangle on a BufferedImage.
   *
   * Consider a Triangle t with vertices v1, v2, v3. Every point v0 can be
   * expressed as v0 = v1 * b1 + v2 * b2 + v3 * b3, where (b1, b2, b3) are the
   * unique barycentric coordinates of v.
   *
   * Let [n1/n2/n3] be the area of the triangle formed by v_n1, v_n2, v_n3.
   * Then (b1, b2, b3) = ([230]/[231], [310]/[312], [120]/[123]). This implies
   * that v0 is contained within t if and only if b1 >= 0, b2 >= 0, b3 >= 0.
   *
   * Consider a vertex v_n with a corresponding vertex attribute with weight
   * w_n. Then w0 = w1 * b1 + w2 * b2 + w3 * b3, by barycentric interpolation.
   *
   * _i = inverse, _c = center, _s = start increment.
   * _* is used to avoid operators in the rendering loop.
   *
   * Note: The idea to use addition to minimize in-loop operations is attributed
   * to https://fgiesen.wordpress.com/2013/02/10/optimizing-the-basic-rasterizer/
   *
   * @param zib
   * Corresponds to the inverses of the filled depths of each pixel. zib stands
   * for zInverseBuffer. Depth is measured along the z-axis that faces the
   * camera, so further objects have a higher zib.
   */
  public void render(BufferedImage img, double[] zib)
  {
    // The width and height of the image.*
    int w = img.getWidth();
    int h = img.getHeight();

    // The center of the screen, displaced by (0.5, 0.5) for symmetry.
    Vec3 center = new Vec3(w / 2.0 + 0.5, h / 2.0 + 0.5, 0);

    v1 = v1.add(center);
    v2 = v2.add(center);
    v3 = v3.add(center);

    // Minimize rendering operations by finding rectangular bounds that this
    // triangle can be inscribed inside. (left, right, bottom, top)
    double xMin = Math.min(v1.v[0][0], Math.min(v2.v[0][0], v3.v[0][0]));
    double xMax = Math.max(v1.v[0][0], Math.max(v2.v[0][0], v3.v[0][0]));
    double yMin = Math.min(v1.v[1][0], Math.min(v2.v[1][0], v3.v[1][0]));
    double yMax = Math.max(v1.v[1][0], Math.max(v2.v[1][0], v3.v[1][0]));

    // If the triangle is out-of-frame, return the original img.
    if (xMin > w - 1 || xMax < 0 || yMin > h - 1 || yMax < 0)
      return;

    // The inverses of the z-coordinates.*
    double z1_i = 1 / v1.v[2][0];
    double z2_i = 1 / v2.v[2][0];
    double z3_i = 1 / v3.v[2][0];

    // Convert the rectangle bounds into integer pixel coordinates.
    int minX = (int) Math.max(0, Math.ceil(xMin));
    int maxX = (int) Math.min(w - 1, Math.floor(xMax));
    int minY = (int) Math.max(0, Math.ceil(yMin));
    int maxY = (int) Math.min(h - 1, Math.floor(yMax));

    // Top-left corner of the bounding rectangle, where rasterization begins.
    Vec3 start = new Vec3(minX, minY, 0);

    // Inverse of twice the area of the triangle.
    double area_inv = 1 / edge(v1, v2, v3);

    // Constants for incrementing start's (y, z) coordinates.
    double x1_s = (v3.v[1][0] - v2.v[1][0]) * area_inv;
    double x2_s = (v1.v[1][0] - v3.v[1][0]) * area_inv;
    double x3_s = (v2.v[1][0] - v1.v[1][0]) * area_inv;
    double y1_s = (v2.v[0][0] - v3.v[0][0]) * area_inv;
    double y2_s = (v3.v[0][0] - v1.v[0][0]) * area_inv;
    double y3_s = (v1.v[0][0] - v2.v[0][0]) * area_inv;
    double b1_s = edge(v2, v3, start) * area_inv;
    double b2_s = edge(v3, v1, start) * area_inv;
    double b3_s = edge(v1, v2, start) * area_inv;

    // For every pixel on the screen ...
    for (int x = minX; x <= maxX; x++)
    {
      double b1 = b1_s;
      double b2 = b2_s;
      double b3 = b3_s;

      for (int y = minY; y <= maxY; y++)
      {
        // If the triangle contains the pixel ...
        if (b1 >= 0 && b2 >= 0 && b3 >= 0)
        {
          // Use interpolation to find the depth; the vertex attribute is the
          // depth inverse because all points have been scaled by 1/v[0][0].
          double depth_i = b1 * z1_i + b2 * z2_i + b3 * z3_i;
          int zIndex = y * w + x;

          if (zib[zIndex] > depth_i)
          {
            // Composites the final color, with transparency, over the image.
            img.setRGB(x, y, composite(rgb, img.getRGB(x, y)));
            zib[zIndex] = depth_i;
          }
        }
        b1 += y1_s;
        b2 += y2_s;
        b3 += y3_s;
      }
      b1_s += x1_s;
      b2_s += x2_s;
      b3_s += x3_s;
    }
  }
}
