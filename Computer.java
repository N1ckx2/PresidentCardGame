import java.lang.*;
import java.util.*;
/**
 * Is the AI for the game
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi
 * @version 18 December 2015
 */
public class Computer extends Player
{
    private static int num; //to name the CPUs appropriately

    public Computer(Deck h, int pos)
    {
        super(h, pos); //player constructor
        num++; //increments the number of computers
        name = "CPU " + num; //named computer 
        hand.faceUp(false);//all the computer cards should be facedown
    }

    public static void num (int n) { num = n; } //allows modification of the number of computers

    public Card[] choose(Card[] old) //choose method
    {
        Card[] cards = new Card[1]; //creates an empty array
        boolean series = true; //assumes cards are a proper series
        try //this try catch is just in case the old choice is null, which happens at the beginning of each round
        {
            cards = new Card[old.length]; // number of played cards must always equal number of previously played cards
            if (old.length == 1) //for singles, just play the lowest possible single the computer can
            {
                int i = 0;
                for (i = 0; i < hand.size()-1 && hand.size() > 5; i+=2) //this block of code is used to prevent the computer from breaking doubles, triples, and quadruples 
                {
                    if (old[0].compareTo(hand.get(i)) == -1 && hand.get(i).getRank() != hand.get(i+1).getRank()) 
                    {
                        cards[0] = hand.deal(i);
                        i = hand.size();
                    }
                }
                for (; i < hand.size(); i++) //this chooses lowest possible single
                {
                    if (old[0].compareTo(hand.get(i)) == -1)
                    {
                        cards[0] = hand.deal(i);
                        i = hand.size();
                    }
                }
            }
            else if (old.length > 1 && old.length < 5) //for doubles triples and quadruples
            {
                for (int i = 0; i < hand.size()-old.length; i++) //checks the hand
                {
                    if (old[old.length-1].compareTo(hand.get(i)) == -1) //makes sure that current card is higher than the highest card played by the previous player
                    {
                        for (int j = 1; j < old.length; j++)
                        {
                            if (hand.get(j+i).getRank() != hand.get(i).getRank()) //checks to make sure there is a series of those cards (for doubles, triples, etc)
                                series = false;
                        }
                        if (series) //if it is a successfuly series, the ocmputer will deal these cards 
                        {
                            for (int x = 0; x < old.length; x++)
                                cards[x] = hand.deal(i);
                            i = hand.size(); //this is used to end the loop
                        }
                    }
                    series = true;
                }
            }
            if (cards[0] == null) //if the computer cannot play anything, it will pass
                return old;
        } catch (NullPointerException e) //when the computer is first to play for that round, it will always try to play doubles, triples, and quadruples as soon as it can
        {
            try
            {

                for (int i = 4; i > 1; i--) //quadruples first, then triples, then doubles
                {
                    for (int x = 0 ; x < hand.size() - (hand.size()/3) - i; x++) // cycles through hand but prevents the computer from playing doubles that are too high
                    {
                        for (int j = 0; j < i; j++) //checks for a series
                        {
                            if (hand.get(j+x).getRank() != hand.get(x).getRank())
                                series = false;
                        }
                        if (series) // if it successfully finds a series, it will play them
                        {
                            cards = new Card[i];
                            for (int y = 0; y < cards.length; y++)
                                cards[y] = hand.deal(x);
                            i = -1; //to exit loop
                            x = hand.size(); //to exit loop
                            return cards; 
                        }
                        series = true;
                    }
                }

                cards = new Card[1]; //if no series, it will just play the lowest card it can
                cards[0] = new Card(hand.deal());
                return cards;

            }
            catch (NullPointerException e2) //just in case the player has just won, it will prevent a null pointer to the hand
            {}
        }
        return cards;
    }
}