import java.awt.event.KeyEvent;

/**
 * A set of static methods that govern the movement of a user avatar
 * through the traditional WASD keyboard setup. SPACE and SHIFT are
 * used to ascend and descend.
 *
 * @author Justin Chang
 */
public class Movement
{
  /**
   * The absolute magnitude of the user's velocity.
   */
  public static final double vel = 10;

  /**
   * The player's current position.
   */
  public static Vec3 pos;

  /**
   * A set of private integers used to help with a smooth keyboard navigation
   * system.
   */
  private static int x1, x2, y1, y2, z1, z2;

  // Don't let anyone instantiate this class.
  private Movement(){

  }

  /**
   * Resets the player's position to the origin.
   */
  public static void reset()
  {
    pos = new Vec3();
  }

  /**
   * Standard movement controls. (Example: A 2D side scroller.)
   */
  public static void tick()
  {
    tick(0);
  }

  /**
   * Moves the player in a direction based on the camera. (Ex: A 3D landscape).
   *
   * @param h
   * The player's clockwise orientation from the x-axis in radians.
   */
  public static void tick(double h)
  {
    int netX = x1 - x2;
    int netY = y1 - y2;
    int netZ = z1 - z2;

    // Constant used to minimize calculations.
    double h_n = h + Math.PI / 2;

    double newX = pos.v[0][0] + (netX * Math.cos(h) + netY * Math.cos(h_n)) * vel;
    double newY = pos.v[1][0] + (netX * Math.sin(h) + netY * Math.sin(h_n)) * vel;
    double newZ = pos.v[2][0] + netZ * vel;

    // The intended destination of the player.
    Vec3 end = new Vec3(newX, newY, newZ);

    pos = end;
  }

  /**
   * Adjusts fields if the player presses a key.
   */
  public static void pressKeys(int keyCode)
  {
    if (keyCode == KeyEvent.VK_SHIFT)
      z2 = 1;
    if (keyCode == KeyEvent.VK_W)
      x2 = 1;
    if (keyCode == KeyEvent.VK_A)
      y2 = 1;
    if (keyCode == KeyEvent.VK_S)
      x1 = 1;
    if (keyCode == KeyEvent.VK_D)
      y1 = 1;
    if (keyCode == KeyEvent.VK_SPACE)
      z1 = 1;
  }

  /**
   * Adjusts fields if the player releases a key.
   */
  public static void releaseKeys(int keyCode)
  {
    if (keyCode == KeyEvent.VK_SHIFT)
      z2 = 0;
    if (keyCode == KeyEvent.VK_W)
      x2 = 0;
    if (keyCode == KeyEvent.VK_A)
      y2 = 0;
    if (keyCode == KeyEvent.VK_S)
      x1 = 0;
    if (keyCode == KeyEvent.VK_D)
      y1 = 0;
    if (keyCode == KeyEvent.VK_SPACE)
      z1 = 0;
  }
}
