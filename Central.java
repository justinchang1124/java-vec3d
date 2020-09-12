import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The goal of this project is to explore 3D rendering in Java.
 *
 * @author Justin C
 */
public class Central extends Controller
{
  /**
   * Stores whether shadows is enabled for all shadow-bound Models.
   */
  public static boolean shadows;

  /**
   * Used to count the number of triangles being rendered.
   */
  private int trisCount, updateTris, pipelineSize;

  /**
   * Stores GUI info.
   */
  private boolean paused, showHUD;

  /**
   * Resets constants.
   */
  public void reset()
  {
    shadows = false;
    paused = false;
    showHUD = true;
    Writer.reset();
    Movement.reset();
    Rotation.reset();
  }

  public void initialize()
  {
    System.out.println(Arguments.INSTRUCTIONS);
    Arguments.initialize();
    reset();
  }

  public void tick(long frameCount)
  {
    // Triangle counter.
    updateTris %= 60;
    updateTris++;
    if (updateTris == 60)
      trisCount = pipelineSize;

    if (paused)
      return;

    // Camera controls.
    if (mouseOver(0, 0, 200, Writer.HEIGHT))
      Rotation.slowL();
    if (mouseOver(Writer.WIDTH - 200, 0, 200, Writer.HEIGHT))
      Rotation.slowR();
    if (mouseOver(0, 0, Writer.WIDTH, 200))
      Rotation.slowU();
    if (mouseOver(0, Writer.HEIGHT - 200, Writer.WIDTH, 200))
      Rotation.slowD();

    if (mouseOver(0, 0, 100, Writer.HEIGHT))
      Rotation.fastL();
    if (mouseOver(Writer.WIDTH - 100, 0, 100, Writer.HEIGHT))
      Rotation.fastR();
    if (mouseOver(0, 0, Writer.WIDTH, 100))
      Rotation.fastU();
    if (mouseOver(0, Writer.HEIGHT - 100, Writer.WIDTH, 100))
      Rotation.fastD();

    for (Model p : Model.pipeline)
      p.tick();

    for (AnimatedModel p : AnimatedModel.pipeline)
      p.tick();

    // All other tick methods.
    Rotation.tick();
    Movement.tick(Rotation.heading);
  }

  public void render(Graphics g, long frameCount)
  {
    Graphics2D g2 = (Graphics2D) g;

    // Draws the background.
    g2.setColor(Color.black);
    g2.fillRect(0, 0, Writer.WIDTH, Writer.HEIGHT);

    // The list of all triangles to be rendered.
    ArrayList<Triangle> pipeline = new ArrayList<Triangle>();

    for (Model p : Model.pipeline)
      p.addTo(pipeline, shadows);

    for (AnimatedModel p : AnimatedModel.pipeline)
      p.addTo(pipeline, shadows);

    // Gets the number of triangle to be rendered.
    pipelineSize = pipeline.size();

    // Draws all entities.
    g2.drawImage(
        Triangle.getImage(pipeline,
            new Basis(Movement.pos, Rotation.pitch, Rotation.heading),
            Writer.WIDTH, Writer.HEIGHT,
            Rotation.near, Rotation.far, Rotation.zoom), 0, 0, null);

    if (showHUD)
    {
      // A text array that stores HUD info.
      String[] textBox = {
          "FPS: " + frameCount,
          "Position: " + Movement.pos,
          "Shadows: " + shadows,
          "Heading / Pitch: " + Matrix.round(Rotation.heading, 3) +
              " / " + Matrix.round(Rotation.pitch, 3),
          "View Length / Zoom: " + Matrix.round(Rotation.near, 3) +
              " / " + Matrix.round(Rotation.zoom, 3),
          "Triangle Count: " + trisCount
      };

      // Draws the HUD.
      for (int i = 0; i < textBox.length; i++)
        Writer.showText(g2, textBox[i], 10, 20 * i + 20);

      // Draws the center pointer.
      g2.setColor(Color.white);
      g2.fillRect(Writer.WIDTH / 2 - 3, Writer.HEIGHT / 2 - 3, 8, 8);
      g2.setColor(Color.black);
      g2.fillRect(Writer.WIDTH / 2 - 2, Writer.HEIGHT / 2 - 2, 6, 6);
    }

    if (paused)
    {
      // Pause GUI.
      g2.setColor(Color.white);
      g2.fillRect(Writer.WIDTH / 2 - 200, 400, 400, 128);
      g2.setColor(Color.gray);
      g2.fillRect(Writer.WIDTH / 2 - 190, 410, 380, 108);
      Writer.setMainFont("times new roman", 0, 80);
      Writer.showText(g2, "PAUSED", 490);
      Writer.setMainFont("times new roman", 0, 14);
    }
  }

  public void keyPressed()
  {
    Movement.pressKeys(keyCode);
  }

  public void keyTyped()
  {

  }

  public void keyReleased()
  {
    Movement.releaseKeys(keyCode);
    Rotation.releaseKeys(keyCode);

    if (keyCode == KeyEvent.VK_E)
      showHUD = !showHUD;
    if (keyCode == KeyEvent.VK_CONTROL)
      paused = !paused;
  }

  public void wheelInput()
  {
    Rotation.scroll(wheel);
  }

  public void mouseInput(int eventMouse)
  {
    if (clicks <= 0 || eventMouse != 1)
      return;

    if (!paused)
    {
      // Left clicking in renderer.
      if (button == 1)
        reset();

      // Right clicking in renderer.
      if (button == 3)
        shadows = !shadows;
    }
  }

  public static void main(String[] args)
  {
    new Loader(Writer.WIDTH, Writer.HEIGHT, "3D Rendering in Java", 60, new Central());
  }
}
