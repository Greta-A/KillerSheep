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
import visual.dynamic.described.Stage;
import visual.statik.sampled.BufferedImageOpFactory;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

public class KillerSheep extends JApplication
{

  public KillerSheep(int width, int height)
  {
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

    // content pane layout should be null
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);

    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

    // create the instructions panel
    instructions = new JLabel();
    instructions.setText(
        "Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
    instructions.setFont(new Font("Serif", Font.PLAIN, 20));
    instructions.setHorizontalAlignment(SwingConstants.CENTER);
    instructions.setBorder(border);
    instructions.setSize(500, 75);
    instructions.setLocation(350, 600);

    // setup for background grass image
    finder = ResourceFinder.createInstance(resources.Marker.class);
    ContentFactory factory = new ContentFactory(finder);
    opFactory = BufferedImageOpFactory.createFactory();

    /**
     * Content grass = factory.createContent("grass.png", 3); visualization = new Visualization();
     * grass.setLocation(0, 0); // grass.setBufferedImageOp(opFactory.createBlurOp(3));
     * visualization.add(grass);
     */

    // Set up the stage with the grass background
    Stage stage = new Stage(50);
    Content grass = factory.createContent("grass.png", 3, false);
    stage.add(grass);

    // Add Berrnstein
    Content bernstein = factory.createContent("b2.png", 4, false);
    Bernstein b = new Bernstein(bernstein, 3, 30.0, 450.0);
    stage.add(b);
    
    // Add paddock
    Content paddock = factory.createContent("paddock.png", 4, false);
    Paddock p = new Paddock(paddock, 900.0, 0.0);
    p.setScale(1.5);
    stage.add(p);

    // Make bernstein movable
    stage.addKeyListener(b);

    view = stage.getView();
    view.setBounds(0, 0, this.width, 600);
    contentPane.add(view);
    contentPane.add(instructions);
    stage.start();
  }

  public static void main(String[] args)
  {
    KillerSheep app = new KillerSheep(1200, 700);
    invokeInEventDispatchThread(app);
  }

}
