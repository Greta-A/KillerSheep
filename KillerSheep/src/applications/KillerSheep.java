package applications;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import app.JApplication;
import io.ResourceFinder;
import visual.Visualization;
import visual.VisualizationView;
import visual.statik.sampled.BufferedImageOpFactory;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

public class KillerSheep extends JApplication
{

	public KillerSheep(int width, int height) {
		super(width, height);
	}

	@Override
	public void init() 
	{
		// variable declarations
		JPanel contentPane;
		JLabel instructions;
		ResourceFinder finder;
		BufferedImageOpFactory opFactory;
		Visualization visualization;
		VisualizationView view;
		
		//content pane layout should be null
		contentPane = (JPanel)getContentPane();
		contentPane.setLayout(null);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		// create the instructions panel
		instructions = new JLabel();
		instructions.setText("Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
		instructions.setFont(new Font("Serif", Font.PLAIN, 20));
		instructions.setHorizontalAlignment(SwingConstants.CENTER);
		instructions.setBorder(border);
		instructions.setSize(800, 75);
		instructions.setLocation(200, 700);
		
		//setup for background grass image
		finder = ResourceFinder.createInstance(resources.Marker.class);
		ContentFactory factory = new ContentFactory(finder);
		opFactory = BufferedImageOpFactory.createFactory();
		
		Content grass = factory.createContent("grass.png", 3);
		visualization = new Visualization();
		grass.setLocation(0, 0);
		grass.setBufferedImageOp(opFactory.createBlurOp(3));
		visualization.add(grass);
		
		//add everything to contentPane
		view = visualization.getView();
		view.setSize(1200,700);
		contentPane.add(view);
		contentPane.add(instructions);
		
	}
	
	public static void main(String[] args)
	{
		KillerSheep app = new KillerSheep(1200,800);
		invokeInEventDispatchThread(app);
	}

}
