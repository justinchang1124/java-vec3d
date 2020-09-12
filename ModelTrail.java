import java.awt.Color;

/**
 * A Model whose colors' RGB value linearly fades over time towards black. The
 * transparency of each color linearly fades over time from a specified starting
 * point alpha_0, given by the AnimatedModel this ModelTrail is based on.
 *
 * @author Justin Chang
 */
public class ModelTrail extends Model
{
  /**
   * The current remaining life span of the trail in ticks.
   */
  private int currentLifeSpan;

  /**
   * The original life span of the trail in ticks.
   */
  private final int originalLifeSpan;

  /**
   * The starting transparency of the trail, out of 255.
   */
  private final int startingAlpha;

  /**
   * Creates a ModelTrail for an AnimatedModel.
   */
  public ModelTrail(AnimatedModel am)
  {
    super(am);
    startingAlpha = am.alpha_0;
    originalLifeSpan = am.lifeSpan;
    currentLifeSpan = originalLifeSpan;

    // Aligns to given rotation values.
    rX = am.rX;
    rY = am.rY;
    rZ = am.rZ;
  }

  /**
   * Causes the color and transparency to decay.
   */
  public void tick()
  {
    if (currentLifeSpan > 0)
      for (int i = 0; i < tris.size(); i++)
      {
        int r = tris.get(i).color.getRed();
        int g = tris.get(i).color.getGreen();
        int b = tris.get(i).color.getBlue();

        // Linearly fading factors, albeit arbitrary.
        double shade = (double) currentLifeSpan / (currentLifeSpan + 1);
        double fade = (double) currentLifeSpan / originalLifeSpan;

        tris.get(i).color = new Color(
            (int) (r * shade),
            (int) (g * shade),
            (int) (b * shade),
            (int) (startingAlpha * fade));
      }

    currentLifeSpan--;
  }

  /**
   * Returns whether or not the trail is no longer visible.
   */
  public boolean hidden()
  {
    return currentLifeSpan <= 0;
  }
}
