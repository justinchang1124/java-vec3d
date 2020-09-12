import java.awt.Color;

/**
 * A demonstration of how the Painter's Algorithm fails in 3D rendering, but the
 * z-Buffer method does not, through a pinwheel structure.
 *
 * @author Justin Chang
 */
public class Pinwheel extends AnimatedModel
{
  /**
   * Constructs a PainterStar.
   *
   * @param rBase
   * The radius of the base of the pinwheel.
   * @param rTip
   * The radius of the tips of the pinwheel.
   * @param height
   * The height of this pinwheel.
   * @param edgeCount
   * The number of edges on this pinwheel.
   * @param disp
   * The displacement of each tip from the starting Vec3 of each triangle.
   * @param colors
   * The list of colors that this pinwheel will be colored with, in alternation.
   */
  public Pinwheel(Vec3 o, boolean s,
      double rBase, double rTip, double height, int edgeCount, double disp, Color[] colors)
  {
    super(o, s, 4, 255);

    // Precomputes the constant for polar coordinates.
    double increment = 2 * Math.PI / edgeCount;

    for (int i = 0; i < edgeCount; i++)
    {
      double sin1 = rBase * Math.sin(i * increment);
      double cos1 = rBase * Math.cos(i * increment);
      double sin2 = rBase * Math.sin((i + 0.5) * increment);
      double cos2 = rBase * Math.cos((i + 0.5) * increment);
      double sin3 = rTip * Math.sin((i + disp) * increment);
      double cos3 = rTip * Math.cos((i + disp) * increment);

      tris.add(new Triangle(
          new double[] { cos1, sin1, 0, cos2, sin2, 0, cos3, sin3, height },
          colors[i % colors.length]));
    }
  }

  public void tick() {
    super.tick();
    rZ = -Rotation.rZ;
  }
}
