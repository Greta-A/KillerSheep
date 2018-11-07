package applications;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.JApplication;
import io.ResourceFinder;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.BufferedImageOpFactory;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

public class KillerSheep extends JApplication implements ActionListener
{
  // variable declarations
  private JPanel contentPane;
  private JButton start;
  private JLabel instructions;
  private Stage stage;

  public KillerSheep(int width, int height)
  {
    super(width, height);
  }

  @Override
  public void init()
  {

    // content pane layout should be null
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);

    // Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

    // Start Button
    start = new JButton("Start!");
    start.setFont(new Font("Serif", Font.PLAIN, 20));
    start.setHorizontalAlignment(SwingConstants.CENTER);
    start.setSize(100, 60);
    start.setLocation(700, 615);

    // create the instructions panel
    instructions = new JLabel();
    instructions.setText("Bernstein is only one click away! \u2192");
    instructions.setFont(new Font("Serif", Font.PLAIN, 20));
    instructions.setHorizontalAlignment(SwingConstants.CENTER);
    // instructions.setBorder(border);
    instructions.setSize(880, 85);
    instructions.setLocation(110, 610);

    ResourceFinder finder = ResourceFinder.createInstance(resources.Marker.class);
    ContentFactory factory = new ContentFactory(finder);
    BufferedImageOpFactory opFactory = BufferedImageOpFactory.createFactory();

    // Set up the stage with the grass background
    stage = new Stage(50);
    Content grass = factory.createContent("grass.png", 3, false);
    stage.add(grass);

    // Add Bernstein
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

    VisualizationView view = stage.getView();
    view.setBounds(0, 0, this.width, 600);
    contentPane.add(view);
    contentPane.add(instructions);
    contentPane.add(start);
    stage.start();

    // start.addActionListener(this);

  }

  public static void main(String[] args)
  {
    KillerSheep app = new KillerSheep(1200, 700);
    invokeInEventDispatchThread(app);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == start)
    {
      instructions.setText(
          "Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
      // instructions.setSize(990, 85);
      start.setVisible(false);
      stage.start();
    }

  }

}
