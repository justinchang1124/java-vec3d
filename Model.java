import java.util.ArrayList;

/**
 * A configuration of triangles that can be rendered. All triangles are fixed in
 * relation to each other, but the net model can be rotated and displaced.
 *
 * @author Justin C
 */
public abstract class Model
{
  /**
   * Master list of all models to be rendered.
   */
  public static ArrayList<Model> pipeline = new ArrayList<Model>();

  /**
   * A local tris that stores all triangles in relation to the localOrigin.
   */
  public ArrayList<Triangle> tris = new ArrayList<Triangle>();

  /**
   * A Vec3 representing the displacement of this Model from the global
   * origin.
   */
  public Vec3 origin;

  /**
   * Does this model experience shadows?
   */
  public boolean shadowBound;

  /**
   * Rotational orientations of the entire Model about respective axes.
   */
  public double rX, rY, rZ;

  /**
   * The current orientation matrix. Only updated when necessary.
   */
  private Matrix orientMatrix;

  /**
   * Constructs an empty Model with the specified origin and binding constants.
   */
  public Model(Vec3 o, boolean s)
  {
    origin = o;
    shadowBound = s;
  }

  /**
   * Copy constructor.
   */
  public Model(Model o)
  {
    this(new Vec3(o.origin), o.shadowBound);

    int len = o.tris.size();

    for (int i = 0; i < len; i++)
      tris.add(new Triangle(o.tris.get(i)));
  }

  /**
   * Updates the orientation matrix. Only called when necessary.
   */
  private void updateOrientMatrix()
  {
    orientMatrix = Matrix.rotate(1, 0, 0, rX).times(
    Matrix.rotate(0, 1, 0, rY).times(Matrix.rotate(0, 0, 1, rZ)));
  }

  /**
   * Orients a local vertex in the global coordinate system.
   */
  private Vec3 orient(Vec3 r)
  {
    return r.itimes(orientMatrix).add(origin);
  }

  /**
   * Orients a local triangle in the global coordinate system.
   */
  private Triangle orient(Triangle r)
  {
    return new Triangle(
        orient(r.v1), orient(r.v2), orient(r.v3), r.color, r.shadows);
  }

  /**
   * Adds the oriented version of another model to this model.
   */
  public void append(Model o)
  {
    o.updateOrientMatrix();
    for (int i = 0; i < o.tris.size(); i++)
      tris.add(o.orient(o.tris.get(i)));
  }

  /**
   * Adds this oriented model to a rendering pipeline.
   */
  public void addTo(ArrayList<Triangle> pipeline, boolean shadows)
  {
    updateOrientMatrix();

    if (shadowBound)
      for (int i = 0; i < tris.size(); i++)
        tris.get(i).shadows = shadows;

    for (Triangle t : tris)
      pipeline.add(orient(t));
  }

  /**
   * An abbreviated summary that acts as a string representation.
   */
  public String toString()
  {
    return "***MODEL***\nOrigin: " + origin + "\nRotation(x,y,z): " +
    new Vec3(rX, rY, rZ) + "\n";
  }

  /**
   * Updates this model. Can be used for movement.
   */
  public abstract void tick();
}
