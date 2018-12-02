package applications;

import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

/**
 * Paddock: Sprite class for the paddock.
 * 
 * @author Greta, Rain, Joelle
 *
 *         We abide by the JMU Honor Code
 */
public class Paddock extends RuleBasedSprite
{
  private static Random rng = new Random();
  private static final int INITIAL_LOCATION = 900;
  protected double x, y, maxX;

  /**
   * Paddock: constuctor that takes in the paddock content and randomizes location of the paddock.
   * 
   * @param content
   *          the image content of the paddock
   */
  public Paddock(TransformableContent content)
  {
    super(content);

    maxX = 900; // the max x bounds
    x = rng.nextDouble() * maxX;
    if (x > (int) maxX)
    {
      x = INITIAL_LOCATION;
    }
    setLocation(x, y);

  }

  /**
   * handleTick: required by metronome; sets location of the paddock.
   */
  @Override
  public void handleTick(int arg0)
  {

  }

  /**
   * getX: getter method to retrieve x-axis location of the paddock.
   * 
   * @return the value of the x-axis
   */
  public double getX()
  {
    return x;
  }

  /**
   * getY: getter method to retrieve y-axis location of the paddock.
   * 
   * @return the value of the y-axis
   */
  public double getY()
  {
    return y;
  }

  protected void updateLocation(int x, int y)
  {
    setLocation(x, y);
  }

}
