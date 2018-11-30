package applications;

import java.util.Iterator;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

public class Sheep extends RuleBasedSprite
{

  protected double speed;
  protected double x, y;
  protected double killSub = 300;
  protected KillerSheep ks;
  protected Bernstein b;

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

  @Override
  public void handleTick(int arg0)
  {

    x = b.getX() - killSub;
    y = b.getY();
    setLocation(x, y);

    // killSub -= 0.5;
    Iterator<Sprite> i;
    Sprite bernstein;

    i = antagonists.iterator();
    while (i.hasNext())
    {
      bernstein = i.next();
      killSub -= 2.3;

      if (intersects(bernstein))
      {
        ks.intersectWithBernstein();
      }
    }

  }

  public void setKillSubZero()
  {
    killSub = 0;
  }

  public double getX()
  {
    return x;

  }

  public double getY()
  {

    return y;
  }

}
