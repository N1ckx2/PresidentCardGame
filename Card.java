import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.lang.Math.*; //need for Math.random();
import java.util.*; //need this for lists
import java.awt.image.BufferedImage;

/**
 * Used to create card objects
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi, Mr. Jay
 * @version 13 December 2015
 */
public class Card
{
    private int rank, value; //rank is 2-Ace, value is 1-52
    private char suit; //suit is one of the blow
    public static final char[] suits = {'♦', '♣', '♥', '♠'};
    public static final char[] ranks = {'3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A', '2'};
    private int left, right, up, down; //position variables
    private boolean faceup;
    private Image cardback; // shared image for back of card
    private static String cardBackName;
    private Image image;

    public Card (int cardNum)  // Creates card from 0-51
    {
        value = cardNum;
        rank = cardNum / 4 + 1; //this is to split up the cards and order based on suit
        suit = suits[cardNum % 4];  //determining the card information
        faceup = true;
        image = null;
        left = 0; right = 0; up = 0; down = 0;
        cardBackName = "Original";
        try
        {
            image = ImageIO.read (new File ("images\\cards\\" + (cardNum+5) + ".gif")); // load file into Image object
            cardback = ImageIO.read (new File ("images\\cardbacks\\"+cardBackName+".gif")); // load file into Image object
        }
        catch (IOException e) { System.out.println ("Card" + (cardNum+5) + "image file not found"); } 

    }
    
    public Card (int cardNum, String back)  // Creates card from 0-51 with a specified backgrounds
    {
        value = cardNum;
        rank = cardNum / 4 + 1; //this is to split up the cards and order based on suit
        suit = suits[cardNum % 4];  //determining the card information
        faceup = true;
        image = null;
        left = 0; right = 0; up = 0; down = 0;
        cardBackName = back;
        try
        {
            image = ImageIO.read (new File ("images\\cards\\" + (cardNum+5) + ".gif")); // load file into Image object
            cardback = ImageIO.read (new File ("images\\cardbacks\\"+cardBackName+".gif")); // load file into Image object
        }
        catch (IOException e) { System.out.println ("Card" + (cardNum+5) + "image file not found"); } 
    }
    

    public Card (Card card) //copy constructor
    {
        rank = card.rank;
        suit = card.suit;
        value = card.value;
        faceup = card.faceup;
        image = card.image;
    }

    public void flip () { faceup = false; } //makes card facedown
    public void flip (boolean b) { faceup = b; } //methods that flip the card
    public int getRank(){ return rank; }

    public char getSuit() { return suit; }

    public boolean getFaceup() { return faceup; } //getter/ accessor methods for these three data fields
    public int value() {return value;}

    public String toString() { return "" + ranks[rank-1] + suit; } //returns a string version of the card
    public Image getImage () {return image;} //returns image
    public int left() { return left; };

    public int right() { return right; };

    public int up() { return up; };

    public int down() { return down; };
    
    public void setCardBack(String name)
    { //method to select card back
        cardBackName = name;
        try { cardback = ImageIO.read (new File ("images\\cardbacks\\"+name+".gif")); } // load file into Image object
        catch (IOException e) { System.out.println ("Chosen card back not found"); } 
    }

    public void show (Graphics g, int x, int y)  // draws card face up or face down
    {
        if (faceup) 
            g.drawImage (image, x, y, null);
        else
            g.drawImage (cardback, x, y, null);
        up = y;
        down = y + 97;
        left = x;
        right = x + 73;
    }

    public void sideways()
    {
        try
        { cardback = ImageIO.read (new File ("images\\cardbacks\\" + cardBackName + "2.gif")); } // use the horizontal back for the card
        catch (IOException e) { System.out.println ("Chosen sideways card back not found"); }
    }

    public int compareTo(Card card) //compares the ranks of the cards
    {
        if (value < card.value)//this card is less than
            return -1;
        else if (value > card.value)//this card is greater than
            return 1;
        else //this card is equal to
            return 0;
    }

    public boolean equals (Card card) //checks if the cards are equal
    {
        if (card.value == value) //checks if the value is the same.
            return true;
        else
            return false;
    }
}