/**
 * A Re^3 vector that supports vector operations. Compatible with the OpenGL
 * convention.
 *
 * @author Justin C
 */
public class Vec3 extends Matrix implements Comparable<Vec3>
{
  /**
   * Constructs a Vec3 at (0, 0, 0).
   */
  public Vec3()
  {
    super(3, 1);
  }

  /**
   * Constructs a Vec3 at (x, y, z).
   */
  public Vec3(double x, double y, double z)
  {
    super(new double[] { x, y, z });
  }

  /**
   * Constructs a Vec3 from column "col" of Matrix o.
   */
  public Vec3(Matrix o, int col)
  {
    this(o.v[0][col], o.v[1][col], o.v[2][col]);
  }

  /**
   * Constructs a Vec3 from a Matrix. Also a copy constructor.
   */
  public Vec3(Matrix o)
  {
    this(o, 0);
  }

  /**
   * Returns the square of the length of the line segment from
   * (0, 0, 0) to this Vec3.
   */
  public double lengthSquared()
  {
    return v[0][0] * v[0][0] + v[1][0] * v[1][0] + v[2][0] * v[2][0];
  }

  /**
   * Returns the length of the line segment from (0, 0, 0) to this Vec3.
   */
  public double length()
  {
    return Math.sqrt(lengthSquared());
  }

  /**
   * Returns (Vec3) (this.add(o)).
   */
  public Vec3 add(Vec3 o)
  {
    return new Vec3(super.add(o));
  }

  /**
   * Returns (Vec3) (this.add(-o)).
   */
  public Vec3 sub(Vec3 o)
  {
    return this.add(o.dot(-1));
  }

  /**
   * Returns (Vec3) (this.dot(o)).
   */
  public Vec3 dot(Vec3 o)
  {
    return new Vec3(super.dot(o));
  }

  /**
   * Returns (Vec3) (this.dot(o)).
   */
  public Vec3 dot(double o)
  {
    return new Vec3(super.dot(o));
  }

  /**
   * Returns (Vec3) (o.times(this)).
   */
  public Vec3 itimes(Matrix o)
  {
    return new Vec3(o.times(this));
  }

  /**
   * Returns a unit Vec3 parallel to the given Vec3.
   */
  public Vec3 unit()
  {
    return this.dot(1 / length());
  }

  /**
   * Returns the cross product "this x o".
   */
  public Vec3 cross(Vec3 o)
  {
    return new Vec3(
        v[1][0] * o.v[2][0] - v[2][0] * o.v[1][0],
        v[2][0] * o.v[0][0] - v[0][0] * o.v[2][0],
        v[0][0] * o.v[1][0] - v[1][0] * o.v[0][0]
    );
  }

  /**
   * Overwrite Object.equals
   */
  public boolean equals(Object o)
  {
    if (o == null)
      return false;

    Vec3 other = (Vec3) o;
    return other.v[0][0] == v[0][0] &&
        other.v[1][0] == v[1][0] &&
        other.v[2][0] == v[2][0];
  }

  /**
   * Make Vec3 objects comparable.
   */
  public int compareTo(Vec3 o)
  {
    return (int) (o.v[2][0] - v[2][0]);
  }

  /**
   * Return the string representation of the Vec3.
   */
  public String toString()
  {
    return "(" +
      Matrix.round(v[0][0], 3)+", "+
      Matrix.round(v[1][0], 3)+", "+
      Matrix.round(v[2][0], 3)+")";
  }
}
