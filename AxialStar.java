import java.awt.Color;

/**
 * The original AxialStar model.
 *
 * @author Justin C
 */
public class AxialStar extends AnimatedModel
{
  public static final Color[] colorsAxial =
      { Color.green, Color.red, Color.blue, Color.magenta, Color.yellow, Color.orange };

  /**
   * Constructs an AxialStar.
   *
   * @param d
   * 6d is the length from the center of the star to one of its tips.
   */
  public AxialStar(Vec3 o, boolean s, double d)
  {
    super(o, s, 20, 128);

    // length of a star point, from center to tip
    double l = 8 * d;
    // width of a star point, from center to edge
    double w = 2 * d;
    // gap between parts of a star point
    double g = d / 10;

    double[][] coords = {
        { 0, 0, +l, +d, +g, +w, +g, +d, +w },
        { 0, 0, -l, +d, +g, -w, +g, +d, -w },
        { 0, +l, 0, +d, +w, +g, +g, +w, +d },
        { 0, -l, 0, +d, -w, +g, +g, -w, +d },
        { +l, 0, 0, +w, +d, +g, +w, +g, +d },
        { -l, 0, 0, -w, +d, +g, -w, +g, +d },
        { 0, 0, +l, -d, -g, +w, -g, -d, +w },
        { 0, 0, -l, -d, -g, -w, -g, -d, -w },
        { 0, +l, 0, -d, +w, -g, -g, +w, -d },
        { 0, -l, 0, -d, -w, -g, -g, -w, -d },
        { +l, 0, 0, +w, -d, -g, +w, -g, -d },
        { -l, 0, 0, -w, -d, -g, -w, -g, -d },
        { 0, 0, +l, +d, -g, +w, +g, -d, +w },
        { 0, 0, -l, +d, -g, -w, +g, -d, -w },
        { 0, +l, 0, +d, +w, -g, +g, +w, -d },
        { 0, -l, 0, +d, -w, -g, +g, -w, -d },
        { +l, 0, 0, +w, +d, -g, +w, +g, -d },
        { -l, 0, 0, -w, +d, -g, -w, +g, -d },
        { 0, 0, +l, -d, +g, +w, -g, +d, +w },
        { 0, 0, -l, -d, +g, -w, -g, +d, -w },
        { 0, +l, 0, -d, +w, +g, -g, +w, +d },
        { 0, -l, 0, -d, -w, +g, -g, -w, +d },
        { +l, 0, 0, +w, -d, +g, +w, -g, +d },
        { -l, 0, 0, -w, -d, +g, -w, -g, +d }
    };

    for (int i = 0; i < 24; i++)
      tris.add(new Triangle(coords[i], colorsAxial[i % 6]));
  }

  public void tick() {
    super.tick();
    rX = Rotation.rX;
    rY = Rotation.rY;
    rZ = Rotation.rZ;
  }
}
