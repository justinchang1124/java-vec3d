import java.awt.Color;
import java.util.ArrayList;

/**
 * A Model that represents a spherical configuration.
 *
 * @author Justin C
 */
public class Sphere extends AnimatedModel
{
  /**
   * The radius of the sphere.
   */
  public final double r;

  /**
   * The number of inflation iterations performed.
   */
  private int inflate;

  /**
   * Constructs a Sphere. rB and sB are binding constants and inflate is the
   * number of times that the sphere is inflated.
   */
  public Sphere(Vec3 o, boolean sB,
      double radius, Color color, int inflate)
  {
    super(o, sB, 1, 128);

    if (radius < 0)
      throw new IllegalArgumentException("Sphere cannot have a negative radius!");

    r = radius;

    double[][] coords = {
        { +r, +r, +r, -r, -r, +r, -r, +r, -r },
        { +r, +r, +r, -r, -r, +r, +r, -r, -r },
        { -r, +r, -r, +r, -r, -r, +r, +r, +r },
        { -r, +r, -r, +r, -r, -r, -r, -r, +r } };

    for (int i = 0; i < coords.length; i++)
      tris.add(new Triangle(coords[i], color));

    inflate(inflate);
  }

  /**
   * Inflates the current tris k times.
   */
  public void inflate(int k)
  {
    for (int i = 0; i < k; i++)
      inflate();
  }

  public void tick()
  {
    super.tick();
    rY = Rotation.rY;
  }

  /**
   * Inflates the current tris once.
   */
  public void inflate()
  {
    ArrayList<Triangle> newTris = new ArrayList<Triangle>();

    for (Triangle t : tris)
    {
      // The midpoints of the sides.
      Vec3 m1 = new Vec3(
          t.v1.v[0][0] + t.v2.v[0][0], t.v1.v[1][0] + t.v2.v[1][0], t.v1.v[2][0] + t.v2.v[2][0]);
      Vec3 m2 = new Vec3(
          t.v2.v[0][0] + t.v3.v[0][0], t.v2.v[1][0] + t.v3.v[1][0], t.v2.v[2][0] + t.v3.v[2][0]);
      Vec3 m3 = new Vec3(
          t.v1.v[0][0] + t.v3.v[0][0], t.v1.v[1][0] + t.v3.v[1][0], t.v1.v[2][0] + t.v3.v[2][0]);

      // Scales them so that all midpoints are "r" away from the center of the sphere.
      m1 = m1.unit().dot(r);
      m2 = m2.unit().dot(r);
      m3 = m3.unit().dot(r);
      t.v1 = t.v1.unit().dot(r);
      t.v2 = t.v2.unit().dot(r);
      t.v3 = t.v3.unit().dot(r);

      // Adds these triangles to the "inflated" tris.
      newTris.add(new Triangle(t.v1, m1, m3, t.color, false));
      newTris.add(new Triangle(t.v2, m1, m2, t.color, false));
      newTris.add(new Triangle(t.v3, m2, m3, t.color, false));
      newTris.add(new Triangle(m1, m2, m3, t.color, false));
    }

    // Reassigns the tris.
    tris = newTris;
    inflate++;
  }

  /**
   * Gets the number of inflations performed.
   */
  public int getInflate()
  {
    return inflate;
  }
}
