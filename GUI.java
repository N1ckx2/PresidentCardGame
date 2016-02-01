import java.lang.*;
import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.lang.Math.*; //need for Math.random();
import java.util.*; //need this for lists
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * This class controls the user's interactions
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi
 * @version 16 December 2015
 */
class GUI extends JFrame implements MouseListener, ActionListener //GUI that will be used for this game
{
    private PresidentCardGame game; //local copy of the card game to refer to
    private Player[] player; //creates a local reference of the player array for easy access
    private JButton b_enterChoice, b_pass; //b_enter is to Play cards; b_pass is to pass on the turn
    private int[] cards; //this is used to store user input when they select their cards
    private boolean didChoose, showing, isBeginning; //didChoose is true when user has selected a card for their turn; showing is true when the table has cards on it; isBeginning is true when the beginning display is up
    private DrawArea table; //this is the jpanel containing all the graphocs
    private Card[] choice; //local copy of the current cards played
    private JLabel info; //information on the bottom jpanel about what is going on in game (used in case graphics error)
    private boolean[] chosen = {false, false, false, false, false, false, false, false, false, false, false, false, false}; //this array is used to control which cards are sticking out and which cards in their initial position
    private JPanel content; //panels to place board and buttons, respectively
    private JToolBar toolbar;
    private JComboBox<String> backgrounds, cardBacks;
    private String backgroundName;
    
    public boolean isBeginning() { return isBeginning; }; //returns whether or not start screen is up

    public GUI(PresidentCardGame g, boolean sec, String backName)
    {
        // 1... Create/initialize components
        isBeginning = !sec; //won't show start screen when second round
        game = g; //a local copy of the game
        player = game.player(); //local copy of the players in game
        backgroundName = backName; //name of background file
        
        didChoose = false; 
        showing = false; //initialize booleans
        content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for pane
        toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.setFloatable(false);
        table = new DrawArea (800, 600); // Area for cards to be displayed
        table.addMouseListener(this);
        table.setLayout (new BorderLayout ());

        cards = new int[1];
        cards[0] = 1;//initializing cards array which wil be changed later

        info = new JLabel(); //information jlabel at the bottom
        b_enterChoice = new JButton("                  Start Game                  ");
        b_enterChoice.addActionListener(this);
       

        toolbar.add(info);
        toolbar.add(b_enterChoice);//adding elements to bottom bar
        
        if (!isBeginning || sec)
            startGame();

        content.add (toolbar, "South"); // Input area
        content.add (table, "North"); // Deck display area
        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setResizable(false); //don't want user to resize or else images will not be aligned
        setTitle ("Deck GUI Lists");
        setSize (858, 665);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null); // Center window.
    }
    
    public void startGame()
    {
        repaint();
        isBeginning = false;
        info.setPreferredSize(new Dimension(175,20));// Width, Heigh
        b_enterChoice.setText("        Play        "); //changes the functionality of the button;
        b_pass = new JButton ("        Pass        "); //creates pass button
        backgrounds = new JComboBox<String>(new String[] {"Original Blue", "Blue Waves", "Cracked Grey", "Grey Bricks", "Metal Gears", "Brown Spikes", "Simple Green", "Rough Red"});
        cardBacks = new JComboBox<String>(new String[]   {"Original", "Mystic Grey", "Bicycle Blue", "Bicycle Red", "Bicycle Black", "Blue Gears", "Grey Dragon", "Elegant Green" });
        
        //add listeners
        b_pass.addActionListener(this); //the two buttons for user input
        backgrounds.addItemListener(new ItemListener()
        { 
            public void itemStateChanged(ItemEvent e) 
            {
                table.setBackground((String) backgrounds.getSelectedItem()); 
                repaint();
            }
        });
        
        cardBacks.addItemListener(new ItemListener() //allows user to set to set the item background
        { 
            public void itemStateChanged(ItemEvent e) 
            {
                for (int i = 0 ; i < 4 ; i++)
                    for (int j = 0 ; j < player[i].hand().size() ; j++)
                        player[i].hand().get(j).setCardBack((String) cardBacks.getSelectedItem());  //set all the card backs
                repaint();
            }
        });
        
        //items to toolbars
        toolbar.add(b_pass);
        toolbar.addSeparator();
        toolbar.add(new JLabel("Background: "));
        toolbar.add(backgrounds);
        toolbar.addSeparator();
        toolbar.add(new JLabel("Card Back: "));
        toolbar.add(cardBacks);
    }

    private void sleep (int time) //this method is for easy access to thread.sleep
    {
        try { Thread.sleep(time); }
        catch (InterruptedException e) { System.out.println("interrupted"); }
    }

    public int[] getChoice() //this method waits until the user selects cards on their turn
    {
        while(!didChoose) //sleeps until the user chooses
            sleep(15);
        didChoose = false;
        return cards; 
    }
    
    public String getBackName() { return (String) backgrounds.getSelectedItem(); }
    public String getCardBack() { return (String) cardBacks.getSelectedItem(); }

    public void showCards (int pos, Card[] choice) //shows the cards that are played
    {
        showing = true;
        this.choice = choice;
        repaint();
    }

    public void clear () { showing = false;} //clears the play area 

    public void info(String text) { info.setText(text); } //allows the PresidentCardGame class to easily display information

    public void alert(String text) { JOptionPane.showMessageDialog (null, text); } //allows main class to easily alert user

    public boolean question (String text)  //used for play again text so user can choose to play again
    {
        int n = JOptionPane.showConfirmDialog(null, text, "Play Again?", JOptionPane.YES_NO_OPTION); 
        return JOptionPane.YES_OPTION == n;
    }

    //-------------------------------Mouse Related Events ---------------------------------------------------------
    public void mouseClicked(MouseEvent e) //checks which card is clicked
    {
        if (isBeginning) //makes the start screen disapear
            startGame();
        else
        {
            for (int i = 0; i < player[0].hand().size(); i++) //checks which card is being clicked
            {
                if (e.getX() > player[0].hand().get(i).left() && e.getX() < player[0].hand().get(i).right()-(73-20) && e.getY() > player[0].hand().get(i).up() && e.getY() < player[0].hand().get(i).down())
                    chosen[i] = !chosen[i];
                else if (i == player[0].hand().size()-1 && e.getX() > player[0].hand().get(i).left() && e.getX() < player[0].hand().get(i).right() && e.getY() > player[0].hand().get(i).up() && e.getY() < player[0].hand().get(i).down())
                    chosen[i] = !chosen[i];
                repaint();
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}  //these methods are not needed but must be overwritten when implemented

    // -------------------------------------------- ActionListener
    public void actionPerformed (ActionEvent e)
    {
        if (e.getActionCommand().equals("                  Start Game                  "))
            startGame();
        else if (e.getSource() == b_enterChoice) 
        {
            boolean isValid = false;
            java.util.List<Integer> nums = new ArrayList<Integer>();
            for (int i = 0 ; i < chosen.length; i++) //sees which cards were chosen
            {
                if (chosen[i])
                    nums.add(i+1);
                isValid = isValid || chosen[i]; //prevents blank input
            }
            if (nums.size() > 4)
                isValid = false;
            if (isValid) //if the selection was not blank or it wasn't too long
            {
                cards = buildIntArray(nums); //creates an array from the arraylist
                didChoose = true; //so the getChoice method can continue
                for (int i = 0 ; i < chosen.length; i++) //makes all the selections false again
                    chosen[i] = false;
            } else
            {
                alert("Invalid choice! Please choose again.");
            }
        } else if (e.getSource() == b_pass) //allow sthe user to pass
        {
            cards = new int[1];
            cards[0] = -1;
            didChoose = true;
            for (int i = 0 ; i < chosen.length; i++)
                chosen[i] = false;
        }
    }

    private int[] buildIntArray(java.util.List<Integer> integers) //creates an int array from an integer arraylist
    {
        int[] ints = new int[integers.size()];
        int i = 0;
        for (Integer n : integers)
            ints[i++] = n;
        return ints;
    }

    class DrawArea extends JPanel  //where all the graphics are 
    {
        private Image background, openning, layout; //all the required images
        private Image[] won, passed, turn; 
        public DrawArea (int width, int height) //constructor
        {
            this.setPreferredSize (new Dimension (width, height)); 
            won = new Image[4];
            passed = new Image[4];
            turn = new Image[4];
            try //imports all the images
            { 
                background = ImageIO.read (new File ("images\\backgrounds\\" + backgroundName + ".jpg"));
                openning = ImageIO.read (new File ("images\\opening.jpg"));
                layout = ImageIO.read (new File ("images\\layout.png"));
                for (int i = 0 ; i < 4; i++)
                {
                    won[i] = ImageIO.read (new File ("images\\signals\\" + i + "won.png"));
                    passed[i] = ImageIO.read (new File ("images\\signals\\" + i + "passed.png"));
                    turn[i] = ImageIO.read (new File ("images\\signals\\" + i + "turn.png"));
                }
            } // load file into Image object
            catch (IOException e) { alert ("GUI Image not found"); }
        } // size
        
        public void setBackground (String name)
        {
            try { background = background = ImageIO.read (new File ("images\\backgrounds\\" + name + ".jpg")); }
            catch (IOException e) { alert ("GUI Image not found"); }
        }

        public void paintComponent (Graphics g)
        {

            if (isBeginning) //start screen
                g.drawImage(openning, 0, 0, null);
            else 
            {
                g.drawImage(background, 0, 0, null); //background
                g.drawImage(layout, 0, 0, null);//player cards
                for (int i = 0 ; i < player.length; i ++) //shows all of the cards and status
                {
                    player[i].hand().show(g, i, chosen);
                    //the following statements show if the player is passed, has won, or is currently playing
                    if (player[i].win())
                        g.drawImage(won[i], 0, 0, null);
                    else if (player[i].pass())
                        g.drawImage(passed[i], 0, 0, null);
                    else if (player[i].turn())
                        g.drawImage(turn[i], 0, 0, null);
                }
                if (showing) //shows the cards that were last played 
                {
                    for (int i = 0 ; i < choice.length ; i++)
                    {
                        choice[i].flip(true);
                        choice[i].show(g, 363 + 20*i, 251);
                    }
                }
            }
        }
    }
}