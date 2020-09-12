import java.awt.Color;

/**
 * A list of all entities to be added to the engine upon starting the program.
 *
 * @author Justin Chang
 */
public class Arguments
{
  /**
   * Instructions for user input.
   */
  public static final String INSTRUCTIONS =

      "Instructions: " + "\n" +
          "Controls for Movement: WASD, Space, Shift" + "\n" +
          "Controls for Camera: Mouse Movement" + "\n" +
          "Controls for Near Plane: Mouse Wheel" + "\n" +
          "Controls for Zoom: O/P" + "\n" +
          "Controls for Rotation: FGHJKL" + "\n" +
          "E = Toggle Heads-Up Display" + "\n" +
          "Left Click = Reset All" + "\n" +
          "Right Click = Toggle Shadows" + "\n" +
          "Left Control = Toggle Pause";

  /**
   * Adds all intended entities.
   */
  public static void initialize()
  {
    Model.pipeline.add(new RubiksCube(new Vec3(0, -2000, 0), 400));
    AnimatedModel.pipeline.add(new Sphere(new Vec3(0, 2000, 0), true, 200, Color.magenta, 5));

    AnimatedModel.pipeline.add(new AxialStar(
        new Vec3(-2000, 0, 0), true, 100));

    AnimatedModel.pipeline.add(new AxialStar(
        new Vec3(2000, 0, 0), false, 100));

    AnimatedModel.pipeline.add(new Pinwheel(
        new Vec3(0, 0, 1000), false, 1200, 800, 225, 12, 3.75,
        new Color[] { Color.red, Color.green, Color.blue }));
  }
}
