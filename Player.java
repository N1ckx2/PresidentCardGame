import java.lang.*;
import java.util.*;
/**
 * Abstract class Player - outline for all players of President
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi
 * @version 15 December 2015
 */
public abstract class Player 
{
    protected Deck hand; //each hand is a mini deck
    protected boolean win, pass, turn; //win or passes
    protected int standing; //their rank when they win
    protected String name; //their name

    public boolean win() { return win; } //getter for win
    public void win(boolean w) { win = w; } //setter for win
    public boolean pass() { return pass; } //accessor for pass
    public void pass(boolean p) { pass = p; } //mutator for pass
    public Deck hand() { return hand; } //allows you to manipulate hand
    public void standing (int r)  { standing = r; } //changes the player's standing
    public int standing() {return standing;} //returns the standing of the player
    public String toString() { return name; } //displays player name
    public void turn (boolean b) { turn = b; } //setter method for the turn
    public boolean turn () { return turn; }; //getter method for the turn

    public Player (Deck h, int pos) // constructor
    {
        hand = h;
        hand.quickSort(); //sorts the deck
        win = false; 
        pass = false; //initializes variables
        turn = false;
    }

    public abstract Card[] choose(Card[] old); //this method is used to choose the card, different for humans and computers

    public String showCards() //returns a string version of the cards in the hand
    {
        String string = "";
        for (int i = 0; i < hand.size(); i++)
            string = string + hand.get(i) + " "; //iterates through all the card and adds it to the string
        return string;
    }

}