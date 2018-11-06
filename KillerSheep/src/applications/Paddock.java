package applications;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

public class Paddock extends RuleBasedSprite
{
	protected double x, y;
	
	public Paddock(TransformableContent content, double x, double y) {
		super(content);

		this.x = x;
		this.y = y;
		
		setLocation(x, y);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleTick(int arg0) 
	{
		setLocation(x,y);
		
	}

}
