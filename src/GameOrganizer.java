/*Name: Chris Yang
This program allows the user to create a specified amount of Hands
and deal an appropriate amount of cards into each hand, depending on
the amount of packs of cards it is told to create.


*/

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//This class establishes the Card Identity of each card that is created

class CardIdentity
{
   enum Suit
   {
      clubs, diamonds, hearts, spades
   }

   private char value;
   private Suit suit;
   private static ArrayList<Character> legal_Values = new ArrayList<>()
   {
      {
         add('A');
         add('K');
         add('Q');
         add('J');
         add('T');
         add('2');
         add('3');
         add('4');
         add('5');
         add('6');
         add('7');
         add('8');
         add('9');
      }
   };

   public CardIdentity()
   {
      value = 'A';
      suit = Suit.spades;
   }

   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         return true;
      } else
      {
         return false;
      }
   }

   public Suit getSuit()
   {
      return suit;
   }

   public char getValue()
   {
      return value;
   }

   private static boolean isValid(char value, Suit suit)
   {
      return (legal_Values.contains(value));
   }

}

class Card extends CardIdentity
{
   private boolean cardError;

   public Card()
   {
      super();
      cardError = false;
   }

   public Card(char value, Suit suit)
   {
      super();
      set(value, suit);

   }

   @Override
   public boolean set(char value, Suit suit)
   {
      boolean valid = super.set(value, suit);
      cardError = !valid;
      return valid;
   }

   public String toString()
   {
      if (cardError)
      {
         return "**  illegal **";
      } else
      {
         return this.getValue() + " of " + this.getSuit();
      }
   }

   public boolean get_Error()
   {
      return cardError;
   }

   public boolean equals(Card card)
   {
      return ((this.getSuit() == card.getSuit()) && (this.getValue() == card.getValue())
            && (cardError == card.get_Error()));
   }
}

// This class creates the Hand class, which is able to receive cards from the
// deck class
// Along with other useful functions such as reset.

class Hand extends Card
{
   public final static int MAX_CARDS = 30;
   private Card[] myCards;
   private int numCards;

   public Hand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   public void resetHand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];

   }

   public boolean takeCard(Card card)
   {
      if (card.get_Error())
      {
         return !(numCards == MAX_CARDS);
      } else
      {
         if (numCards == MAX_CARDS)
         {
            return false;
         } else
         {
            myCards[numCards] = card;
            numCards++;
            return true;
         }
      }
   }

   public Card playCard()
   {
      if (numCards == 0)
      {
         return null;
      }
      numCards--;
      return myCards[numCards];
   }

   public String toString()
   {
      String string = "";
      for (int i = 0; i < numCards; i++)
      {
         string += myCards[i].toString() + ", ";

      }
      return string;
   }

   public int getNumCards()
   {

      return numCards;
   }

   public Card inspectCard(int k)
   {
      if (k < 0 || k > numCards - 1)
      {
         return new Card('L', Suit.spades);
      } else
      {
         return myCards[k - 1];
      }
   }
}

// This class creates a deck full of cards, and passes the cards on to the hand
// class

class Deck extends Card
{
   private final static int MAX_PACKS = 6;
   private final static int NUM_CARDS_PER_PACK = 52;
   private final static int MAX_CARDS_PER_DECK = MAX_PACKS * NUM_CARDS_PER_PACK;
   private static Card[] masterPack = new Card[NUM_CARDS_PER_PACK];
   private Card[] cards;
   private int numPacks;
   private int topCard;
   private boolean isMasterPackEmpty = true;

   public Card[] getMasterPack()
   {
      return masterPack;
   }

   public Deck(int numPacks)
   {
      this.numPacks = numPacks;
      topCard = NUM_CARDS_PER_PACK * this.numPacks;
      cards = new Card[topCard];
      if (isMasterPackEmpty)
      {
         allocateMasterPack();
         isMasterPackEmpty = false;
      }

      if (!(initializePack(numPacks)))
      {
         this.numPacks = 1;
         topCard = NUM_CARDS_PER_PACK * this.numPacks;
         cards = new Card[topCard];
         initializePack(this.numPacks);
      }

   }

   public Deck()
   {
      this.numPacks = 1;
      topCard = NUM_CARDS_PER_PACK * this.numPacks;
      cards = new Card[topCard];
      if (isMasterPackEmpty)
      {
         allocateMasterPack();
         isMasterPackEmpty = false;
      }
      initializePack(this.numPacks);

   }

   public boolean initializePack(int numPacks)
   {
      boolean validNum;
      if (numPacks < 1 || numPacks > MAX_PACKS)
      {
         validNum = false;
      } else
      {
         validNum = true;
         for (int i = 0; i < numPacks; i++)
         {
            for (int j = 0; j < NUM_CARDS_PER_PACK; j++)
            {
               cards[(i * NUM_CARDS_PER_PACK) + j] = masterPack[j];
            }
         }
      }
      return validNum;
   }

   private static void allocateMasterPack()
   {
      int index = 0;
      for (Suit suit : Suit.values())
      {
         masterPack[index] = new Card('A', suit);
         index++;
         masterPack[index] = new Card('K', suit);
         index++;
         masterPack[index] = new Card('Q', suit);
         index++;
         masterPack[index] = new Card('J', suit);
         index++;
         masterPack[index] = new Card('T', suit);
         index++;
         char value = '9';
         for (int i = 2; i < 10; i++)
         {
            masterPack[index] = new Card(value, suit);
            value--;
            index++;
         }

      }
   }

   public void shuffle()
   {
      for (int i = 0; i < topCard; i++)
      {
         int randInt = (int) (Math.random() * topCard);
         Card temp = cards[i];
         cards[i] = cards[randInt];
         cards[randInt] = temp;
      }
   }

   public Card dealCard()
   {
      topCard--;
      return new Card(cards[topCard].getValue(), cards[topCard].getSuit());
   }

   public int getNumCards()
   {
      return topCard;
   }

   public Card inspectCard(int k)
   {
      try
      {
         return new Card(cards[k - 1].getValue(), cards[k - 1].getSuit());
      } catch (Exception IndexOutOfBoundsException)
      {
         return new Card('L', Suit.spades);
      }
   }
}

public class GameOrganizer extends Hand
{
   public static void main(String[] args)
   {
      Deck deck = new Deck(2);
      int numCardsInDeck = deck.getNumCards();
      System.out.println("Two unshuffled packs:");
      for (int i = 0; i < numCardsInDeck; i++)
      {
         System.out.println("Deal: " + deck.dealCard());
      }
      System.out.println();
      deck = new Deck(2);
      deck.shuffle();
      System.out.println("Two shuffled packs:");
      numCardsInDeck = deck.getNumCards();
      for (int i = 0; i < numCardsInDeck; i++)
      {
         System.out.println("Deal: " + deck.dealCard());
      }
      System.out.println();
      deck = new Deck();
      System.out.println("One unshuffled pack:");
      numCardsInDeck = deck.getNumCards();
      for (int i = 0; i < numCardsInDeck; i++)
      {
         System.out.println("Deal: " + deck.dealCard());
      }
      System.out.println();
      deck = new Deck();
      System.out.println("One shuffled pack:");
      deck.shuffle();
      numCardsInDeck = deck.getNumCards();
      for (int i = 0; i < numCardsInDeck; i++)
      {
         System.out.println("Deal: " + deck.dealCard());
      }

      Scanner keyboard = new Scanner(System.in);
      System.out.println("Enter the number of players between 1 and 10:");
      int userChoice;
      userChoice = keyboard.nextInt();
      Hand[] hands = new Hand[userChoice];
      for (int i = 0; i < userChoice; i++)
      {
         hands[i] = new Hand();
      }
      Deck new_Deck = new Deck();
      int num_Extra_Cards = new_Deck.getNumCards() % userChoice;
      for (int j = 0; j < num_Extra_Cards; j++)
      {
         hands[j].takeCard(new_Deck.dealCard());
      }
      while (new_Deck.getNumCards() > 0)
      {
         for (int i = 0; i < userChoice; i++)
         {
            hands[i].takeCard(new_Deck.dealCard());

         }
      }
      System.out.println("Here are our hands, from unshuffled deck:");
      for (int i = 0; i < userChoice; i++)
      {
         System.out.println("Hand: " + hands[i]);
      }

      for (int i = 0; i < userChoice; i++)
      {
         hands[i] = new Hand();
      }
      new_Deck = new Deck();
      new_Deck.shuffle();
      num_Extra_Cards = new_Deck.getNumCards() % userChoice;
      for (int j = 0; j < num_Extra_Cards; j++)
      {
         hands[j].takeCard(new_Deck.dealCard());
      }
      while (new_Deck.getNumCards() > 0)
      {
         for (int i = 0; i < userChoice; i++)
         {
            hands[i].takeCard(new_Deck.dealCard());

         }
      }
      System.out.println("Here are our hands, from shuffled deck:");
      for (int i = 0; i < userChoice; i++)
      {
         System.out.println("Hand: " + hands[i]);
      }

   }

}
