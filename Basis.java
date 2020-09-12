/**
 * Represents a basis in terms of the standard global orthonormal basis. Any
 * created Basis is final; this avoids inconsistencies in change-of-basis.
 *
 * @author Justin C
 */
public class Basis
{
  /**
   * The matrix representing the OpenGL camera convention.
   */
  public static final Matrix cameraZ = new Matrix(
      new double[][] {
          { 0, 0, 1 },
          { 1, 0, 0 },
          { 0, 1, 0 } });

  /**
   * The inverse of the matrix representing the OpenGL camera convention.
   */
  public static final Matrix cameraZ_inv = new Matrix(
      new double[][] {
          { 0, 1, 0 },
          { 0, 0, 1 },
          { 1, 0, 0 } });

  /**
   * ijk is created by concatenating the unit vectors that span this Basis, and
   * inv is ijk.inv().
   */
  private final Matrix ijk, inv;

  /**
   * The origin of this Basis in relation to the global origin.
   */
  private final Vec3 origin;

  /**
   * Constructs the global Basis.
   */
  public Basis()
  {
    this(new Matrix(), new Vec3());
  }

  /**
   * Constructs a Basis at "center" from the vectors represented by the columns
   * of "orient".
   */
  public Basis(Matrix orient, Vec3 center)
  {
    ijk = orient;
    inv = ijk.inv();
    origin = center;
  }

  /**
   * Constructs a Basis at "center" that is first rotated counterclockwise by
   * "theta" radians around the z-axis and then rotated counterclockwise by
   * "phi" radians around the newly positioned y-axis. The direction of the
   * z-axis can be used to represent the viewpoint of a camera with a pitch of
   * "phi" and a heading of "theta".
   *
   * In order to optimize this process for rendering, ijk is ignored and
   * the inverse is calculated by reversing the above steps.
   */
  public Basis(Vec3 center, double phi, double theta)
  {
    double angle = theta + Math.PI / 2;

    ijk = null;

    inv = cameraZ_inv.times(
    Matrix.rotate(0, 0, 1, -theta).times(
    Matrix.rotate(Math.cos(angle), Math.sin(angle), 0, -phi)
    ));

    origin = center;
  }

  /**
   * Copy constructor.
   */
  public Basis(Basis o)
  {
    this(o.ijk, o.origin);
  }

  /**
   * Returns whether ijk is an orthogonal matrix.
   * Note: not perfectly accurate
   */
  public boolean isOrthogonal()
  {
    Vec3 i = new Vec3(ijk, 0);
    Vec3 j = new Vec3(ijk, 1);
    Vec3 k = new Vec3(ijk, 2);

    return
    i.dot(j).lengthSquared() == 0 &&
    j.dot(k).lengthSquared() == 0 &&
    k.dot(i).lengthSquared() == 0;
  }

  /**
   * Returns whether ijk's column vectors are all unit vectors.
   */
  public boolean isUnit()
  {
    return
    new Vec3(ijk, 0).lengthSquared() == 1 &&
    new Vec3(ijk, 1).lengthSquared() == 1 &&
    new Vec3(ijk, 2).lengthSquared() == 1;
  }

  /**
   * Returns whether ijk is an orthonormal matrix.
   */
  public boolean isOrthoNormal()
  {
    return isOrthogonal() && isUnit();
  }

  /**
   * The local-to-global matrix transformation; L_G(o) = ijk * o + origin.
   */
  public Vec3 L_G(Vec3 o)
  {
    return o.itimes(ijk).add(origin);
  }

  /**
   * The inverse function of L_G(o).
   */
  public Vec3 G_L(Vec3 o)
  {
    return o.sub(origin).itimes(inv);
  }

  public String toString()
  {
    return "***BASIS***\nijk: "+ijk+"\ninv: "+inv +"\norigin: "+origin+"\n";
  }
}
