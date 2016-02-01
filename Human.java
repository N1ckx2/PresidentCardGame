import java.lang.*;
import java.util.*;
/**
 * Is a type of player that is controlled by real people
 * 
 * @author Nicholas Vadivelu, Frederick Ngo, Danyal Abbasi
 * @version 17 December 2015
 */
public class Human extends Player
{  
    private GUI gui;

    public Human(Deck h, String n, int pos, GUI g)
    {
        super(h, pos);
        gui = g;
        name = n;
    }

    protected Card[] validate (Card[] old, Card[] nue)
    { //ensures that the played hand is the right length and is high enough
        boolean series = true; //assumes they did play a valid card if series
        for (int i = 0; i < nue.length-1; i++) 
            if (nue[i].getRank() != nue[i+1].getRank()) //makes sure that all the cards are the same rank 
                series = false; //if they aren't, false
        try
        {
            /*if (old.length > 1 && nue.length > 1)
            {
            Arrays.sort(old);
            Arrays.sort(nue);
            }*/
            if (old.length != nue.length || old[old.length-1].compareTo(nue[old.length-1]) == 1 || !series)
            {
                gui.alert("Invalid choice! Please choose again");
                nue = choose (old); //if they aren't the same length, or if the cards are lower, or if they are not a series, make the user rechoose
            }
            else 
                hand.deal(nue); //otherwise play the cards and remove them from the hand
        }
        catch (NullPointerException e) 
        {
            if (!series)
                nue = choose(old);
            else
                hand.deal(nue);
        } //just in case this is on a new round, and so oldChoice would be null

        return nue;
    }

    public Card[] choose(Card[] old) 
    {
        int[] choice = gui.getChoice();
        Card[] cards = new Card[choice.length]; //declared array of cards that will be returned

        if (choice[0] != -1) //-1 means pass, so as long they don't pass
        {
            cards = new Card[choice.length];
            for (int i = 0 ; i < choice.length; i++) //fill card array with the cards that the user chose
                cards[i] = new Card(hand.get(choice[i]-1));
            return validate(old, cards);
        }
        else //otherwise,return the oldChoice to indicate passing
            return old;
    }

    //The code below would be used for text based input

    /*
    public Card[] choose(Card[] old) 
    {
    Scanner scan = new Scanner(System.in); //scanner used to get text input
    java.util.List<Integer> card = new ArrayList(); //used to get input
    Card[] cards; //declared array of cards that will be returned

    //Prompt user to choose a card and record the user's choice:      
    System.out.println(); 
    for (int i = 0; i < hand.size(); i++)
    System.out.print(hand.get(i) + "(" + (i+1) + ") "); //print out card and the number used to choose that card
    System.out.print("PASS(-1)");
    System.out.print("\n" + name + " Choose a card: ");
    while (scan.hasNextInt()) //while there is another integer.
    card.add(scan.nextInt());

    if (card.get(0) != -1) //-1 means mass, so as long they don't pass
    {
    cards = new Card[card.size()];
    for (int i = 0 ; i < card.size(); i++) //fill card array with the cards that the user chose
    cards[i] = new Card(hand.get(card.get(i)-1));
    return validate(old, cards);
    }
    else //otherwise,return the oldChoice to indicate passing
    return old;
    }
     */
}