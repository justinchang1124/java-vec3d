import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Implements the initialize, tick, render, and input methods of a specified
 * Controller. It ensures that the tick/render loop operates on an individual
 * Thread, that a JFrame is constructed, and that this component is linked to
 * the JFrame and appropriate input listeners.
 *
 * @author Justin C
 */
public class Loader extends Canvas implements Runnable
{
  /**
   * The thread that will manage the execution of the tick/render methods.
   */
  private Thread thread;

  /**
   * Stores whether or not the tick/render loop should continue running.
   */
  private boolean running;

  /**
   * The Controller whose initialize, tick, render, and input methods will be
   * called.
   */
  private Controller control;

  /**
   * The number of rendering operations that have been successfully executed
   * within the last second.
   */
  private int frameRate;

  /**
   * The number of nanoseconds that are intended to elapse per tick.
   */
  private double nsPerTick;

  /**
   * Calls control's initialize() method, adds all input listeners, ensures that
   * all events are visible to this component, constructs an unadjustable
   * JFrame, links the JFrame to this component, and starts the tick/render
   * loop.
   */
  public Loader(int width, int height, String title, double tickPerSec, Controller c)
  {
    nsPerTick = 1000000000 / tickPerSec;
    control = c;

    control.initialize();

    addKeyListener(control);
    addMouseListener(control);
    addMouseMotionListener(control);
    addMouseWheelListener(control);

    setFocusable(true);
    setFocusTraversalKeysEnabled(false);

    JFrame frame = new JFrame(title);

    // The buffer of (6, 40) is due to the way
    // the screen is drawn on the window.
    Dimension d = new Dimension(width, height);

    frame.setPreferredSize(d);
    frame.setMinimumSize(d);
    frame.setMaximumSize(d);
    frame.setResizable(false);

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);

    start();
  }

  /**
   * Executes run() on a new Thread and enables the continual iteration of the
   * tick/render loop.
   */
  public synchronized void start()
  {
    thread = new Thread(this);
    thread.start();
    running = true;
  }

  /**
   * Terminates the current Thread and prevents any further iterations of the
   * tick/render loop.
   */
  public synchronized void stop()
  {
    try
    {
      thread.join();
      running = false;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Automatically executes on "thread".
   *
   * ticks:
   * the number of ticks that must be called before the program proceeds.
   *
   * frames:
   * The number of times that the program has rendered in this iteration.
   */
  public void run()
  {
    double ticks = 0;
    int frames = 0;

    long lastNano = System.nanoTime();
    long lastMill = System.currentTimeMillis();

    while (running)
    {
      long nowNano = System.nanoTime();
      double ticksElapsed = (nowNano - lastNano) / nsPerTick;
      lastNano = nowNano;
      ticks += ticksElapsed;

      while (ticks >= 1)
      {
        control.tick(frameRate);
        ticks--;
      }

      render();
      frames++;

      // Restarts the frame count and updates the framerate every second.
      long nowMill = System.currentTimeMillis();
      if (nowMill - lastMill >= 1000)
      {
        lastMill += 1000;
        frameRate = frames;
        frames = 0;
      }
    }

    // Allows "running = false" to immediately terminate this thread.
    stop();
  }

  /**
   * Creates a buffer for the program's graphics and passes it, along with the
   * frameCount, to central.render. Proceeds to release the system resources
   * used by g and make the buffer strategy visible.
   *
   * Code attributed to:
   * RealTutsGML at "https://www.youtube.com/user/RealTutsGML"
   */
  private void render()
  {
    BufferStrategy bs = getBufferStrategy();

    if (bs == null)
    {
      createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();

    control.render(g, frameRate);

    g.dispose();
    bs.show();
  }
}
