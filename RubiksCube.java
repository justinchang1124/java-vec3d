import java.awt.Color;

/**
 * A single standard Rubik's Cube.
 *
 * @author Justin C
 */
public class RubiksCube extends Box
{
  /**
   * The list of colors for the faces of the cube.
   */
  public static final Color[] colorsRubiks =
      { Color.red, Color.yellow, Color.blue, Color.orange, Color.white, Color.green };

  /**
   * Constructs a RubiksCube.
   *
   * @param d
   * The length of an edge of the cube.
   */
  public RubiksCube(Vec3 o, double d)
  {
    super(o, true, d, colorsRubiks);
  }
}
