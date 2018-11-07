package applications;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import app.JApplication;
import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import io.ResourceFinder;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

public class KillerSheep extends JApplication implements ActionListener
{
  // variable declarations
  private JPanel contentPane;
  private static JButton start;
  private static JLabel instructions;
  private JTextArea blank;
  private static Stage stage;

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

    blank = new JTextArea();
    blank.setSize(this.width, 600);
    blank.setEditable(false);

    ResourceFinder finder = ResourceFinder.createInstance(resources.Marker.class);
    ContentFactory factory = new ContentFactory(finder);

    // Sheep Sound
    BufferedSoundFactory sf = new BufferedSoundFactory(finder);
    BufferedSound baaing;

    try
    {
      AudioInputStream stream;
      stream = AudioSystem.getAudioInputStream(finder.findURL("sheep-sound.wav"));
      baaing = sf.createBufferedSound(stream);

      auditory.sampled.Content c = baaing;
      BoomBox box = new BoomBox(c);
      box.start(true);

    }
    catch (IOException | UnsupportedAudioFileException | NullPointerException
        | LineUnavailableException e)
    {
      e.printStackTrace();
    }

    // Set up the stage with the grass background
    stage = new Stage(1);
    Content grass = factory.createContent("grass.png", 3, false);
    stage.add(grass);

    // Add Bernstein
    Content bernstein = factory.createContent("b2.png", 4, false);
    Bernstein b = new Bernstein(bernstein, 3, 30.0, 450.0);

    // Add paddock
    Content paddock = factory.createContent("paddock.png", 4, false);
    Paddock p = new Paddock(paddock, 900.0, 0.0);
    p.setScale(1.5);

    // Make bernstein movable
    stage.addKeyListener(b);

    b.addAntagonist(p);

    stage.add(b);
    stage.add(p);

    VisualizationView view = stage.getView();
    view.setBounds(0, 0, this.width, 600);
    contentPane.add(blank);
    contentPane.add(view);
    contentPane.add(instructions);
    contentPane.add(start);
    stage.start();
    stage.stop();
    start.addActionListener(this);

  }

  public static void main(String[] args)
  {
    KillerSheep app = new KillerSheep(1200, 700);
    invokeInEventDispatchThread(app);

  }

  public static void intersectWithPaddock()
  {
    instructions.setText("You Win!");
    start.setVisible(false);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == start)
    {
      stage.start();
      instructions.setText(
          "Use the arrow keys to direct Bernstein safely into the paddock! Avoid the angry, killer sheep!");
      instructions.setSize(990, 85);
      start.setVisible(false);
      blank.setVisible(false);
    }

  }

}
