import java.lang.*;
import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.lang.Math.*; //need for Math.random();
import java.util.*; //need this for lists

/**
 * This class runs the main president game. It contains all the logic.
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi
 * @version 18 December 2015
 */
public class PresidentCardGame
{
    private boolean gameOver, passed, playAgain, second;  //gameOver is true when all the players have played their cards ; passed stores if a given player is passed
    private int passNum, rank, win, numPlayers; //passNum records how many players have passed ; rank stores the rank of the current winning player ; win stores the number of players who have won
    private Card[] choice, oldChoice; //choice is the current played set of cards ; oldChoice is the previous set of played cards
    private java.util.List<Card> pile, trash;  // the pile includes the cards in play ; trash is all the discarded cards
    private int lastPlayed; //lastPlayed stores the number of the player who previously played (useful for passing)
    private Player[] player; //array of the players who are playing
    private Deck deck; //initial deck of cards
    private GUI gui;
    private String backName, cardBack;
    public Player[] player() {return player;}

    private void clearTable ()
    { //empties the pile into the trash and resets the players who have passed
        gui.clear();
        for (int j = 0; j < pile.size(); j++) //fills up trash
            trash.add(pile.get(j));
        pile.clear(); //empties pile
        gui.info("Pile was emptied!"); //prints to the GUI Label
        passNum = 0; //number of players that passed is zero
        for (int j = 0; j < player.length; j++) //sets all the players to not passed 
            player[j].pass(false);
        oldChoice = null; //no oldChoice
    }

    private void sleep (int time)
    {
        try { Thread.sleep(time); }
        catch (InterruptedException e) { System.out.println("interrupted"); }
    }

    private void setLead ()
    { //changes the order of the players
        Player[] temp = new Player[player.length]; //temporary players used to switch the players
        for (int i = 0; i < player.length; i++) //shifts all the players
            temp[i] = player[(i+lastPlayed)%player.length];
        player = temp; //reassigns the layer
    }

    public PresidentCardGame() //contrustor to initalize the variables
    {
        playAgain = true;
        numPlayers = 4;
        second = false;
        player = new Player[numPlayers]; //new player with the appropriate size
        backName = "Original Blue";
        cardBack = "Original";
        
        while (playAgain)
        {
            gui = new GUI(this, second, backName);
            gui.setVisible(true);
            deck = new Deck(cardBack); //deck of 52
            gameOver = false; //game is active
            passNum = 0; //nobody passed 
            rank = 1; //current rank is 1
            win = 0; //nobody won
            lastPlayed = 0; //nobody played yet
            pile = new ArrayList<Card>(); //initalize pile 
            trash = new ArrayList<Card>(); //initalize trash
            passed = false; //just initial value
            deck.shuffle();
            deck.shuffle(); //shuffle twice because once isn't good enough
            oldChoice = null; //no card was played before

            //Creates the desired number of opponents
            Computer.num(0); //resets the numbering system for the CPUs

            player[0] = new Human (new Deck(deck.get(0, (int)(52/player.length)-1)), "You", 0, gui); //creates one human player "The Coolest"
            //player[0] = new Computer (new Deck(deck.get(0, (int)(52/player.length)-1)), 0); //uncomment this to have 4 computer players
            for (int i = 1; i < player.length; i++) //creates the appropriate number of computers
                player[i] = new Computer (new Deck(deck.get((52/player.length)*i, (int)(52/player.length)*(i+1)-1)), i);

            for (int i = 0 ; i < player.length; i++)
            {
                if(second)
                {
                    for(int j =0 ; j < player.length; j++)
                    {
                        if(player[j].standing == 4) //makes bum the first to play
                        {
                            lastPlayed = j;
                            setLead();
                        }
                    }
                }
                else if (player[i].hand().get(0).value() == 0) //makes the 3 of diamonds the first to play
                {
                    lastPlayed = i;
                    setLead();
                }
            }

            while (gui.isBeginning()) //waits for the beginning screen to end.
                sleep(15);

            //sleep(3000);
            while (!gameOver) //game continues until everyone is done
            {
                for (int i = 0; i < player.length; i++) //cycles through all the players
                {
                    if (!player[i].win() && !player[i].pass()) //if the player did not yet win nor did they pass
                    {
                        player[i].turn(true); //it becomes the player's turn
                        sleep(900); //waits so it looks better visually
                        choice = player[i].choose(oldChoice); //makes the player choose a card

                        //if the player returns the same thing as oldChoice, it means they passed
                        gui.repaint();
                        try { passed = choice[0].equals(oldChoice[0]); } //checks if they player passed 
                        catch (NullPointerException e)  { passed = false; } //just in case it is the first turn and oldChoice is null

                        if (passed)
                        {
                            gui.info(player[i].toString() + " passed!"); //tells user that this player passed 
                            player[i].pass(true); 
                            passNum++; //number of players that passed increases
                            if (passNum == player.length-rank) //if everyone but one player passed:
                            {
                                player[i].turn(false); //no longer player's turn
                                clearTable(); //empties
                                setLead(); //changes the lead player based on who last played
                                i = -1;
                            } else
                                player[i].turn(false);

                        }
                        else //if they played cards
                        {
                            oldChoice = choice; //store their choice
                            try
                            {
                                //System.out.print(player[i]+  " played "); //outputs what they played
                                String info = player[i].toString() +  " played "; // information about their hand
                                gui.showCards(i, choice); //shows the info on the jLbale
                                for (int j = 0; j < choice.length; j++)
                                {
                                    //System.out.print(choice[j] + " ");
                                    info = info + choice[j].toString()  + " ";
                                }
                                gui.info(info);
                                //System.out.println();
                                for (int j = 0; j < choice.length; j++) //add these cards to the pile
                                    pile.add(choice[j]);
                            } catch (NullPointerException e) {}; //just in case they won

                            if (player[i].hand.size() == 0) //if their hand is empty they win
                            {
                                player[i].win(true); //they won
                                player[i].standing(rank); //records their standing
                                rank++; //increases the rank that the next player will receive
                                win++;

                                gui.info (player[i].toString() + " won!"); //displays that they won
                                if (win == player.length-1) //if everyone has an empty hand, the game ends
                                {
                                    gameOver = true; 
                                    for (int x = 0 ; x < player.length; x++)
                                    {
                                        player[x].win(true); 
                                        if (player[x].standing() == 0)
                                            player[x].standing(rank);//this ensures that all the players have won so the game ends without errors
                                    }

                                }
                                clearTable(); //empties the table
                            } else
                            {
                                lastPlayed = i; //sets which player played last in case everyone else passes
                            }
                        }
                    }
                    if (i > -1)
                        player[i].turn(false);
                    gui.repaint();
                }
            }
            
            String[] rankings = { "President: ",
                                  "Vice President: ", 
                                  "Neutral: ", 
                                  "Bum: " };
            String endgame = "<html><p>Game Over! The rankings are: </p><p></p>";// starts string that shows rankings

            int checker = 1;
            for (int i = 0; i < player.length; i++)
            {
                if (player[i].standing() == checker)
                {
                    endgame = endgame +  "<p>" + rankings[checker-1] + player[i] + "</p>"; //adds all the players to the rankings in order
                    checker++;
                    i = -1;
                }
            }
            endgame = endgame + "</html>";
            gui.alert(endgame); //gives you a popup menu
            playAgain = gui.question("Do you want to play again?"); //question option pane asking if you want to play agains
            // playAgain = true; //uncomment for consntant replays
            backName = gui.getBackName();
            cardBack = gui.getCardBack(); //stores the selected backgrounds
            gui.dispose(); //clears the gui in preperation for the next game.
            gui.setVisible(false);
            second = true;
        }
    }

    public static void main (String[] args)
    {
        PresidentCardGame game = new PresidentCardGame();
    }
}