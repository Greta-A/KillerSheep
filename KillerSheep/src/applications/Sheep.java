package applications;

import java.util.Iterator;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

/**
 * Sheep: sprite class for Sheep.
 * @author Greta, Rain, Joelle
 *
 * We abide by the JMU Honor Code
 */
public class Sheep extends RuleBasedSprite
{
  protected double speed;
  protected double x, y;
  // controls the speed at which the sheep follow Bernstein
  protected double killSub = 300;
  protected KillerSheep ks;
  protected Bernstein b;

  /**
   * Sheep: constructor that creates the Sheep object, its speed, and location.
   * @param content the image content that contains the Sheep
   * @param speed the initial speed of the sheep
   * @param x the initial location of the sheep on the x-axis
   * @param y the initial location of the sheep on the y-axis
   * @param ks the KillerSheep class object
   * @param b the Bernstein sprite
   */
  public Sheep(TransformableContent content, double speed, double x, double y, KillerSheep ks,
      Bernstein b)
  {
    super(content);

    this.x = x;
    this.y = y;

    setLocation(x, y);
    this.speed = speed;
    this.ks = ks;
    this.b = b;
  }

  /**
   * handleTick: required by Metronome; updates speed of Sheep, and checks if the sheep 
   *   intersected with antagonist Bernstein.
   */
  @Override
  public void handleTick(int arg0)
  {
	  // update Sheep location to constantly follow Bernstein
    x = b.getX() - killSub;
    y = b.getY();
    setLocation(x, y);

    Iterator<Sprite> i;
    Sprite bernstein;

    i = antagonists.iterator();
    while (i.hasNext())
    {
      bernstein = i.next();
      killSub -= 2.0;

      if (intersects(bernstein))
      {
    	  // invoke main class method to interact with content pane and stage
        ks.intersectWithBernstein();
      }
    }
  }
}
