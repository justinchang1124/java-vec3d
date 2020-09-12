import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Organizes mouse & keyboard inputs and ensures that every subclass has
 * initialize, tick, render, and input methods. 
 * 
 * @author Justin C
 */
public abstract class Controller
    implements KeyListener, MouseMotionListener, MouseWheelListener, MouseListener
{
  /**
   * The Virtual Keyboard key that was most recently interacted with.
   */
  public int keyCode;

  /**
   * (xM, yM) corresponds to the coordinates of the mouse.
   */
  public int xM, yM;

  /**
   * The number of times that the mouse has clicked. The interval that must pass
   * before this number resets to 0 depends on the system.
   */
  public int clicks;

  /**
   * The mouse button that was most recently interacted with.
   * (1 = left mouse button, 2 = scroll wheel button, 3 = right mouse button)
   */
  public int button;

  /**
   * Is -1 or 1, depending on the direction of the most recent wheel rotation.
   * The action that constitutes rotating the mouse wheel "up" or "down" depends
   * on the system.
   */
  public int wheel;

  public abstract void initialize();

  public abstract void tick(long frameCount);

  public abstract void render(Graphics g, long frameCount);

  public abstract void keyPressed();

  public abstract void keyTyped();

  public abstract void keyReleased();

  public abstract void wheelInput();

  /**
   * Manages all mouse input, subdivided into 7 events by the eventDescription.
   * 
   * @param eventDescription
   * Is -3 when the mouse enters a window. Is 3 when the mouse exits a window.
   * Is -2 when the mouse is moved. Is 2 when the mouse is dragged. Is -1 when
   * the mouse is pressed. Is 1 when the mouse is released. Is 0 when the mouse
   * is pressed and released.
   */
  public abstract void mouseInput(int eventDescription);

  /**
   * Detects if the mouse is within the rectangle that has a top-left corner of
   * (x, y) and dimensions (w, h).
   */
  public boolean mouseOver(int x, int y, int w, int h)
  {
    return xM > x && xM < x + w && yM > y && yM < y + h;
  }

  /*
   * All subsequent methods take a KeyEvent, MouseWheelEvent, or MouseEvent as a
   * parameter. The event's traits are used to adjust the fields of this class
   * accordingly and call the abstract method associated with the event.
   */
  public void keyPressed(KeyEvent e)
  {
    keyCode = e.getKeyCode();
    keyPressed();
  }

  public void keyTyped(KeyEvent e)
  {
    keyCode = e.getKeyCode();
    keyTyped();
  }

  public void keyReleased(KeyEvent e)
  {
    keyCode = e.getKeyCode();
    keyReleased();
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    wheel = e.getWheelRotation();
    wheelInput();
  }

  public void setMouseValues(MouseEvent e)
  {
    xM = e.getX();
    yM = e.getY();
    button = e.getButton();
    clicks = e.getClickCount();
  }

  public void mouseEntered(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(-3);
  }

  public void mouseExited(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(3);
  }

  public void mouseMoved(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(-2);
  }

  public void mouseDragged(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(2);
  }

  public void mousePressed(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(-1);
  }

  public void mouseReleased(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(1);
  }

  public void mouseClicked(MouseEvent e)
  {
    setMouseValues(e);
    mouseInput(0);
  }
}
