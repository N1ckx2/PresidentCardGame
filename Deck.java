import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.lang.Math.*; //need for Math.random();
import java.util.*; //need this for lists
/**
 * This class is designed to hold a series of cards and manipulate these cards
 * 
 * @author @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi, Mr. Jay
 * @version 13 December 2015
 */
public class Deck
{
    protected java.util.List<Card> deck; //deck of cards

    public Deck () //constructor
    {
        deck = new ArrayList<Card>(52);
        for (int x = 0 ; x < 52 ; x++)  // for each card in standard deck
            deck.add(new Card (x)); // create card
    }
    
    public Deck (String back) //constructor with card back name
    {
        deck = new ArrayList<Card>(52);
        for (int x = 0 ; x < 52 ; x++)  // for each card in standard deck
            deck.add(new Card (x, back)); // create card
    }

    public Deck (Card[] c) //make a new deck with the cards in the array wit
    {
        deck = new ArrayList<Card>(c.length);
        for (int x = 0; x < c.length; x++)
            deck.add(c[x]);
    }

    public void show (Graphics g, int pos, boolean[] chosen)  // draws card face up or face down
    {
        try 
        {
            if (pos == 0) //main players
            {
                for (int c = 0 ; c < deck.size() ; c++)
                {
                    if (chosen[c]) //this makes the card stick out if it has been chosens
                        deck.get(c).show (g, c * 20 + 270, 483-40);
                    else
                        deck.get(c).show (g, c * 20 + 270, 483); //arranges the cards appropriately
                }
            }
            else if (pos == 1) //cpu 1
            {
                sideways();
                for (int c = 0 ; c < deck.size() ; c++)
                    deck.get(c).show (g, 20, c * 20 + 142); //arranges the cards appropriately
            }
            else if (pos == 2) //cpu 2
            {
                for (int c = 0 ; c < deck.size() ; c++)
                    deck.get(c).show (g, 510 - c * 20, 20); //arranges the cards appropriately
            }
            else if (pos == 3) //cpu 3
            {
                sideways();
                for (int c = 0 ; c < deck.size() ; c++)
                    deck.get(c).show (g, 755-20, 455 - 73 -  c * 20); //arranges the cards appropriately
            }
        } catch (NullPointerException n) {}; //just in case the cards they have not chosen any cards
    }

    public void sideways() //turns the cards sideways
    {
        for (int i = 0 ; i < deck.size() ; i++)
            deck.get(i).sideways();         //flips sideways
    }

    public java.util.List<Card> get() { return deck; } //getter method for deck

    public void shuffle () //shuffles the deck
    {
        for (int i = deck.size(); i > 1; i--)
            swap(i-1, (int) (Math.random()*i)); //swaps the cards
    }

    private void swap (int x, int y) //needed for the shuffle method (switches two arrays)
    {
        Card t = deck.get(x); 
        deck.set(x, deck.get(y));
        deck.set(y, t); //swaps the cards
    }

    public Card deal() { return deal(0); } //takes the top card off the deck

    public Card deal(int pos)
    {
        Card returner = deck.get(pos); //stores the card to be dealt
        try
        { //just in case the array is empty
            if (deck.size()>1)
                deck.remove(pos); //removes element
            else if (deck.size() == 1)
                deck = null;
        }
        catch (NullPointerException n) {};
        return returner;
    }

    public void deal(Card card) //deals the requested card
    {
        int cardNum = 0;
        for (int i = 0; i < deck.size(); i++)
            if (deck.get(i).equals(card))
                cardNum = i;
        deck.remove(cardNum);
    }

    public void deal(Card[] cards) //deals the cards that are in the array
    {
        for (int i = 0; i < cards.length; i++)
            deal(cards[i]);
    }

    public void add (Card card)
    {
        try
        { deck.add(card); }//adds this card to the end
        catch (NullPointerException n)
        {
            deck = new ArrayList<Card>(); 
            deck.add(card); //adds this card to the end
        }
    }

    public int[] search (Card card)
    {
        int numOccur = 0; //counts number of occurences
        for (int i = 0; i < deck.size(); i++) //goes through deck searching for number of occurence
            if (deck.get(i).equals(card))
                numOccur++;
        int[] positions = new int[numOccur];// array of the positions
        int pos = 0; //keeps track of which occurence number you are currently at
        for (int i = 0; i < deck.size(); i++) //goes through deck searching for number of occurence
            if (deck.get(i).equals(card))
                positions[pos++] = i; //records position then increments the position
        return positions;
    }

    public void quickSort() //calls the quickSort method with appropriate arguments
    {
        quickSort(0, deck.size()-1);
    }

    private void quickSort (int lowIndex, int highIndex)
    {
        if (deck == null || deck.size() == 0)
            return; //checks to make sure array isn't empty
        int low = lowIndex, high = highIndex;
        Card pivot = deck.get(low + (high-low)/2); //index is middle number
        while (low <= high)
        {
            while (deck.get(low).compareTo(pivot) == -1) //finds a card greater than the pivot
                low++;
            while (deck.get(high).compareTo(pivot) == 1) //finds a card that is less than the pivot
                high--;
            if (low <= high) //switches them if they should be switched
            {
                swap(low, high);
                low++;
                high--;
            }
        }
        //calls quicksort recursively
        if (lowIndex < high)
            quickSort(lowIndex, high);
        if (low < highIndex)
            quickSort(low, highIndex);
    }

    public int size()  //returns the side of the deck
    {
        try { return deck.size(); }
        catch (NullPointerException e) { return 0; }
    }

    public Card get(int i) //returns the card
    { 
        try { return deck.get(i); }
        catch (NullPointerException e) { return null; }
    }

    public Card[] get(int start, int end) //returns an array of cards in this index
    {
        Card[] card = new Card[end-start+1];
        for (int i = start; i <= end; i++) //iterates through required cards
            card[i-start] = deck.get(i);
        return card;
    }

    public void faceUp(boolean face) //flips the card appropriately
    {
        for (int i = 0 ; i < deck.size() ; i++)
            deck.get(i).flip(face);
    }
}