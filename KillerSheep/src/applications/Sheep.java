package applications;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

public class Sheep extends RuleBasedSprite
{

  protected double speed, x, y;

  public Sheep(TransformableContent content, double speed, double x, double y)
  {
    super(content);

    this.x = x;
    this.y = y;

    setLocation(x, y);
    this.speed = speed;
  }

  @Override
  public void handleTick(int arg0)
  {
    x = x + 0.09;
    setLocation(x, y);
  }

}
