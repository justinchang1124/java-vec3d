import java.util.ArrayList;

/**
 * A Model with the ability to support a trailing animation.
 *
 * @author Justin Chang
 */
public class AnimatedModel extends Model
{
  /**
   * Master list of all animated models to be rendered.
   */
  public static ArrayList<AnimatedModel> pipeline = new ArrayList<AnimatedModel>();

  /**
   * The number of ticks for which this animation trails. As an example,
   * lifeSpan = 20 implies that this model will be followed by 20 fading copies.
   */
  public int lifeSpan;

  /**
   * The starting transparency of the fade operation.
   */
  public int alpha_0;

  /**
   * All model trails corresponding to this AnimatedModel.
   */
  private ArrayList<ModelTrail> trails = new ArrayList<ModelTrail>();

  /**
   * Constructs an empty AnimatedModel at the specified origin. Sets the
   * specified binding constants and animation constants.
   */
  public AnimatedModel(Vec3 o, boolean s, int life, int alpha)
  {
    super(o, s);
    lifeSpan = life;
    alpha_0 = alpha;
  }

  /**
   * Copy constructor.
   */
  public AnimatedModel(AnimatedModel o)
  {
    super(o);
    lifeSpan = o.lifeSpan;
    alpha_0 = o.alpha_0;
  }

  /**
   * Updates the trails of this animated model.
   */
  public void tick()
  {
    if (lifeSpan > 0 && alpha_0 > -1 && alpha_0 < 256)
    {
      // Store a new trail at this location.
      trails.add(new ModelTrail(this));

      for (int j = 0; j < trails.size(); j++)
      {
        // Decay the lifespan of each trail.
        ModelTrail mt = trails.get(j);
        mt.tick();

        // Remove invisible trails.
        if (mt.hidden())
        {
          trails.remove(mt);
          j--;
        }
      }
    }
  }

  // Adds the model and all corresponding trails to the pipeline.
  public void addTo(ArrayList<Triangle> pipeline, boolean shadows)
  {
    super.addTo(pipeline, shadows);
    for (ModelTrail mt : trails)
      mt.addTo(pipeline, shadows);
  }
}
