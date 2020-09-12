import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextLayout;

/**
 * A set of static methods used to draw text on Graphics.
 * The default font is: "standard, size 14, times new roman"
 * The default color is: "Color.white"
 *
 * @author Justin C
 */
public class Writer
{
  /**
   * The width/height of the screen being drawn to.
   */
  public static final int WIDTH = 800, HEIGHT = 800;

  /**
   * The current font being used for the text.
   */
  private static Font font = new Font("times new roman", 0, 14);

  /**
   * The current color being used for the text.
   */
  public static Color color = Color.white;

  /**
   * Don't let anyone instantiate this class!
   */
  private Writer()
  {

  }

  /**
   * Resets constants.
   */
  public static void reset(){
    setMainFont("times new roman", 0, 14);
    color = Color.white;
  }

  /**
   * Returns a String[] containing all available fonts for this platform.
   *
   * All code is attributed to:
   * Alvin Alexander at "https://alvinalexander.com/
   * blog/post/jfc-swing/swing-faq-list-fonts-current-platform"
   */
  public static String[] allFonts()
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return ge.getAvailableFontFamilyNames();
  }

  /**
   * Draws s on g at the coordinates (x, y).
   *
   * @param s
   * A String that takes up only one line.
   */
  public static void showText(Graphics g, String s, int x, int y)
  {
    g.setFont(font);
    g.setColor(color);
    g.drawString(s, x, y);
  }

  /**
   * Draws s on g such that s is horizontally centered
   * at the elevation y on g.
   */
  public static void showText(Graphics g, String s, int y)
  {
    showText(g, s, xCenter(g, s), y);
  }

  /**
   * Draws s on g such that s is horizontally and
   * vertically centered on g.
   */
  public static void showText(Graphics g, String s)
  {
    showText(g, s, HEIGHT / 2);
  }

  /**
   * Draws s on g such that the center of s is
   * (WIDTH + xDisplace, y).
   */
  public static void showText(int xDisplace, Graphics g, String s, int y)
  {
    int x = xCenter(g, s) + xDisplace;
    if (x < 0)
      throw new IllegalArgumentException(
          "The x-displacement sets the text off of the screen!");

    showText(g, s, x, y);
  }

  /**
   * Returns an integer x_c such that g.drawString(s, x_c, y)
   * is horizontally centered. If x_true is the actual center,
   * then Math.abs(x_c - x_true) <= 0.5 is always satisfied.
   *
   * Code is attributed to:
   * MadProgrammer at "https://stackoverflow.com/questions/23729944/"
   */
  public static int xCenter(Graphics g, String s)
  {
    Graphics2D g2d = (Graphics2D) g;
    TextLayout txt = new TextLayout(s, font, g2d.getFontRenderContext());
    double xDist = txt.getBounds().getWidth();
    return (int) Math.round((WIDTH - xDist) / 2);
  }

  /**
   * Sets the font used by this class.
   */
  public static void setMainFont(String name, int style, int size)
  {
    font = new Font(name, style, size);
  }
}
