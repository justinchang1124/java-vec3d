/**
 * Defines an m by n matrix as a rectangular array of numbers with m rows and n
 * columns and supports numerous operations on Matrix classes.
 *
 * @author Justin C
 */
public class Matrix
{
  /**
   * This Matrix has final dimensions (m, n).
   */
  public final int m, n;

  /**
   * The entries of this Matrix. The size of a Matrix may not be changed once it
   * is created, but individual entries are mutable.
   */
  public final double[][] v;

  /**
   * Constructs a zero Matrix.
   */
  public Matrix(int row, int col)
  {
    if (row < 1 || col < 1)
      throw new IllegalArgumentException(
      "error: dimensions of a Matrix cannot be nonpositive!");

    m = row;
    n = col;

    v = new double[m][n];
  }

  /**
   * Constructs an identity Matrix.
   */
  public Matrix(int dim)
  {
    this(dim, dim);

    for (int i = 0; i < m; i++)
      v[i][i] = 1;
  }

  /**
   * Constructs a Matrix from an array.
   */
  public Matrix(double[][] o)
  {
    this(o.length, o[0].length);

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        v[i][j] = o[i][j];
  }

  /**
   * Constructs a Matrix from a column.
   */
  public Matrix(double[] o)
  {
    this(o.length, 1);

    for (int i = 0; i < m; i++)
      v[i][0] = o[i];
  }

  /**
   * Constructs a 3x3 Identity Matrix.
   */
  public Matrix()
  {
    this(3);
  }

  /**
   * Forces d to show numPlaces decimal places.
   */
  public static String round(double d, int numPlaces)
  {
    double places = Math.pow(10, numPlaces);
    String target = "" + Math.round(d * places) / places;

    int numZeroesMissing = numPlaces - target.length() + target.indexOf('.');

    for (int i = 0; i <= numZeroesMissing; i++)
      target += "0";

    return target;
  }

  /**
   * Returns a copy of this matrix to prevent mutability.
   * Essentially a less verbose copy constructor.
   */
  public Matrix copy()
  {
    return new Matrix(v);
  }

  /**
   * Returns (this + o).
   */
  public Matrix add(Matrix o)
  {
    if (o.m != m || o.n != n)
      throw new IllegalArgumentException(
      "Cannot perform \"add\" on Matrix:" + this + ", Matrix:" + o + ".");

    Matrix r = copy();

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        r.v[i][j] += o.v[i][j];

    return r;
  }

  /**
   * Returns (this * o) where every entry of (this) is pairwise multiplied
   * with the corresponding entry of (o).
   */
  public Matrix dot(Matrix o)
  {
    if (o.m != m || o.n != n)
      throw new IllegalArgumentException(
      "Cannot perform \"dot\" on Matrix:" + this + ", Matrix:" + o + ".");

    Matrix r = copy();

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        r.v[i][j] *= o.v[i][j];

    return r;
  }

  /**
   * Returns (this * o) where o is a scalar.
   */
  public Matrix dot(double o)
  {
    Matrix r = copy();

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        r.v[i][j] *= o;

    return r;
  }

  /**
   * Returns the conventional product of two matrices.
   */
  public Matrix times(Matrix o)
  {
    if (n != o.m)
      throw new IllegalArgumentException(
      "Cannot perform \"times\" on Matrix:" + this + ", Matrix:" + o + ".");

    Matrix r = new Matrix(m, o.n);

    for (int i = 0; i < m; i++)
      for (int j = 0; j < o.n; j++)
        for (int k = 0; k < n; k++)
          r.v[i][j] += v[i][k] * o.v[k][j];

    return r;
  }

  /**
   * Returns the transpose of this matrix.
   */
  public Matrix transpose()
  {
    Matrix r = new Matrix(n, m);

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        r.v[j][i] = v[i][j];

    return r;
  }

  /**
   * Returns the determinant of this matrix.
   */
  public double determinant()
  {
    if (m != n)
      throw new IllegalArgumentException(
      "This Matrix is not a square; no determinant is defined!");

    // For the two simplest cases, I wrote the formula directly.
    if (m == 1)
      return v[0][0];

    if (m == 2)
      return (v[0][0] * v[1][1]) - (v[0][1] * v[1][0]);

    // recursively computing the determinant
    double sum = 0;

    for (int i = 0; i < n; i += 2)
      sum += v[0][i] * subMatrix(0, i).determinant();

    for (int i = 1; i < n; i += 2)
      sum -= v[0][i] * subMatrix(0, i).determinant();

    return sum;
  }

  /**
   * Returns a Matrix with dimensions (m-1, n-1). Every entry is the same, but
   * one row and one column have been omitted.
   */
  public Matrix subMatrix(int row, int col)
  {
    Matrix r = new Matrix(m - 1, n - 1);

    for (int i = 0; i < row; i++)
    {
      for (int j = 0; j < col; j++)
        r.v[i][j] = v[i][j];

      for (int j = col+1; j < n; j++)
        r.v[i][j-1] = v[i][j];
    }

    for (int i = row+1; i < m; i++)
    {
      for (int j = 0; j < col; j++)
        r.v[i-1][j] = v[i][j];

      for (int j = col+1; j < n; j++)
        r.v[i-1][j-1] = v[i][j];
    }

    return r;
  }

  /**
   * Returns a Matrix with dimensions (m, n). The entry at row i, col j is the
   * determinant of the submatrix created by removing row i and col j,
   * multiplied by (-1)^(i+j).
   */
  public Matrix cofactor()
  {
    Matrix r = new Matrix(m, n);

    for (int i = 0; i < m; i+=2)
    {
      for (int j = 0; j < n; j+=2)
        r.v[i][j] += subMatrix(i, j).determinant();

      for (int j = 1; j < n; j+=2)
        r.v[i][j] -= subMatrix(i, j).determinant();
    }

    for (int i = 1; i < m; i+=2)
    {
      for (int j = 0; j < n; j+=2)
        r.v[i][j] -= subMatrix(i, j).determinant();

      for (int j = 1; j < n; j+=2)
        r.v[i][j] += subMatrix(i, j).determinant();
    }

    return r;
  }

  /**
   * Returns the inverse of this Matrix. The cases for 1x1, 2x2, and 3x3
   * matrices have already been precomputed.
   */
  public Matrix inv()
  {
    if (m != n)
      throw new IllegalArgumentException(
      "This Matrix is not square; no inverse is defined!");

    if (m == 1)
    {
      Matrix r = new Matrix(m, n);
      r.v[0][0] = 1 / v[0][0];
      return r;
    }

    if (m == 2)
    {
      Matrix r = new Matrix(m, n);
      r.v[0][0] = v[1][1];
      r.v[0][1] = -v[0][1];
      r.v[1][0] = -v[1][0];
      r.v[1][1] = v[0][0];
      return r.dot(1 / (v[0][0]*v[1][1] - v[0][1]*v[1][0]));
    }

    if (m == 3)
    {
      Matrix r = new Matrix(m, n);

      r.v[0][0] = v[1][1] * v[2][2] - v[1][2] * v[2][1];
      r.v[0][1] = v[0][2] * v[2][1] - v[0][1] * v[2][2];
      r.v[0][2] = v[0][1] * v[1][2] - v[0][2] * v[1][1];
      r.v[1][0] = v[1][2] * v[2][0] - v[1][0] * v[2][2];
      r.v[1][1] = v[0][0] * v[2][2] - v[0][2] * v[2][0];
      r.v[1][2] = v[0][2] * v[1][0] - v[0][0] * v[1][2];
      r.v[2][0] = v[1][0] * v[2][1] - v[1][1] * v[2][0];
      r.v[2][1] = v[0][1] * v[2][0] - v[0][0] * v[2][1];
      r.v[2][2] = v[0][0] * v[1][1] - v[0][1] * v[1][0];

      return r.dot(1 / (
      v[0][0]*v[1][1]*v[2][2] +
      v[0][1]*v[1][2]*v[2][0] +
      v[0][2]*v[1][0]*v[2][1] -
      v[0][0]*v[1][2]*v[2][1] -
      v[0][1]*v[1][0]*v[2][2] -
      v[0][2]*v[1][1]*v[2][0]));
    }

    // A generalized formula for computing an inverse.
    return cofactor().transpose().dot(1 / determinant());
  }

  /**
   * Rotates axis counterclockwise by theta around Vertex(l,m,n).
   *
   * Mathematics attributed to:
   * https://en.wikipedia.org/wiki/Transformation_matrix
   */
  public static Matrix rotate(double l, double m, double n, double theta)
  {
    double cos = Math.cos(theta);
    double sin = Math.sin(theta);
    double cas = 1 - cos;

    Matrix r = new Matrix();

    r.v[0][0] = l * l * cas + cos;
    r.v[0][1] = m * l * cas - n * sin;
    r.v[0][2] = n * l * cas + m * sin;
    r.v[1][0] = l * m * cas + n * sin;
    r.v[1][1] = m * m * cas + cos;
    r.v[1][2] = n * m * cas - l * sin;
    r.v[2][0] = l * n * cas - m * sin;
    r.v[2][1] = m * n * cas + l * sin;
    r.v[2][2] = n * n * cas + cos;

    return r;
  }

  /**
   * Returns the reduced row echelon form of this Matrix.
   */
  public Matrix rref()
  {
    Matrix r = copy();

    // The index of the first row that has not yet been fixed.
    int minPivot = 0;

    for (int col = 0; col < n && minPivot < m; ++col)
    {
      // The index of the largest unfixed possible pivot in the column.
      int largestPivot = minPivot;
      for (int i = minPivot + 1; i < m; ++i)
        if (Math.abs(r.v[i][col]) > Math.abs(r.v[largestPivot][col]))
          largestPivot = i;

      // If the largest pivot is 0, next column.
      if (Math.abs(r.v[largestPivot][col]) < 0.00001)
        continue;

      // Swaps the largest pivot into the first unfixed position.
      for (int j = 0; j < n; ++j)
      {
        double temp = r.v[largestPivot][j];
        r.v[largestPivot][j] = r.v[minPivot][j];
        r.v[minPivot][j] = temp;
      }

      // Turns the pivot row's leading entry into a 1.
      double largestPivotInverse = 1 / r.v[minPivot][col];
      for (int j = 0; j < n; ++j)
        r.v[minPivot][j] *= largestPivotInverse;

      // Removes the leading entries of all other rows.
      for (int i = 0; i < m; ++i)
        if (i != minPivot)
        {
          double t = r.v[i][col];
          for (int j = 0; j < n; ++j)
            r.v[i][j] -= t * r.v[minPivot][j];
        }

      minPivot++;
    }

    return r;
  }

  /**
   * Creates a string representation of this Matrix.
   * Example: a 3x3 identity matrix will be represented as
   * "{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}"
   */
  public String toString()
  {
    String r = "";
    int n1 = n-1;

    for (int i = 0; i < m; i++)
    {
      r += "{";

      for (int j = 0; j < n1; j++)
        r += v[i][j] + ", ";

      r += v[i][n1] + "}";
    }

    return "{" + r + "}";
  }
}
