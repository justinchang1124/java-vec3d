import java.awt.event.KeyEvent;

/**
 * A series of static methods for drawing objects in 3D.
 *
 * @author Justin C
 */
public class Rotation
{
  /**
   * Camera speeds.
   */
  public static final double V_CAM_SLOW = 0.02, V_CAM_FAST = 0.03, V_ZOOM = 5;

  /**
   * Rotational speed.
   */
  public static final double V_ROTATE = 0.03;

  /**
   * near and far are the respective clipping planes.
   */
  public static double near, far;

  /**
   * Heading and pitch govern player view.
   */
  public static double heading, pitch;

  /**
   * Zoom is the zoom factor.
   */
  public static double zoom = 1;

  /**
   * Stores the x, y, z radians of rotation for use by Models.
   */
  public static double rX, rY, rZ;

  /**
   * Stores the x, y, z rotation speeds for use by Models.
   */
  public static int vX, vY, vZ;

  /**
   * Don't let anyone instantiate this class.
   */
  private Rotation()
  {

  };

  /**
   * Increments all rotational matrices by the appropriate amount.
   */
  public static void tick()
  {
    rX += vX * V_ROTATE;
    rY += vY * V_ROTATE;
    rZ += vZ * V_ROTATE;
  }

  /**
   * Manage the pitch and heading. left, right, up, down
   */
  public static void slowL()
  {
    heading += V_CAM_SLOW;
  }

  public static void slowR()
  {
    heading -= V_CAM_SLOW;
  }

  public static void fastL()
  {
    heading += V_CAM_FAST;
  }

  public static void fastR()
  {
    heading -= V_CAM_FAST;
  }

  public static void slowU()
  {
    if (2*pitch < Math.PI)
      pitch += V_CAM_SLOW;
  }

  public static void slowD()
  {
    if (2*pitch > -Math.PI)
      pitch -= V_CAM_SLOW;
  }

  public static void fastU()
  {
    if (2*pitch < Math.PI)
      pitch += V_CAM_FAST;
  }

  public static void fastD()
  {
    if (2*pitch > -Math.PI)
      pitch -= V_CAM_FAST;
  }

  public static void scroll(int wheel)
  {
    if (near >= 0 || wheel >= 0)
      near += V_ZOOM * wheel;
  }

  /**
   * Resets constants.
   */
  public static void reset()
  {
    near = 400;
    far = 20000;
    zoom = 1;
    heading = 0;
    pitch = 0;
    rX = 0;
    rY = 0;
    rZ = 0;
    vX = 0;
    vY = 0;
    vZ = 0;
  }

  /**
   * Takes in a keyCode from a Virtual Keyboard key that has been released and
   * adjusts the appropriate constants.
   */
  public static void releaseKeys(int keyCode)
  {
    if (keyCode == KeyEvent.VK_O)
      zoom /= 1.1;
    if (keyCode == KeyEvent.VK_P)
      zoom *= 1.1;
    if (keyCode == KeyEvent.VK_F)
      vZ++;
    if (keyCode == KeyEvent.VK_G)
      vZ--;
    if (keyCode == KeyEvent.VK_H)
      vY++;
    if (keyCode == KeyEvent.VK_J)
      vY--;
    if (keyCode == KeyEvent.VK_K)
      vX++;
    if (keyCode == KeyEvent.VK_L)
      vX--;
  }
}
