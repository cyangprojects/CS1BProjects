import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Button;
import java.awt.color.*;
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

class GUICard
{

   private static Image[][] imageCards = new Image[14][4]; // 14 = A thru K (+ joker)
   private static ImageView[][] imageCardViews = new ImageView[14][4];
   private static Image imageBack;
   private static ImageView imageCardBack;
   private static boolean imagesNotLoaded = true;

   private static String cardlValsConvertAssist = "23456789TJQKAX";
   private static String suitValsConvertAssist = "CDHS";
   private static Card.Suit suitConvertAssist[] =
   { Card.Suit.clubs, Card.Suit.diamonds, Card.Suit.hearts, Card.Suit.spades };

   static char turnIntIntoCardValueChar(int k)
   {

      if (k < 0 || k > 13)
         return '?';
      return cardlValsConvertAssist.charAt(k);
   }

   // turns 0 - 3 into 'C', 'D', 'H', 'S'
   static char turnIntIntoCardSuitChar(int k)
   {
      if (k < 0 || k > 3)
         return '?';
      return suitValsConvertAssist.charAt(k);
   }

   static void loadCardImages()
   {
      if (imagesNotLoaded)
      {
         int intVal;
         int intSuit;
         String imageFileName;
         for (intSuit = 0; intSuit < 4; intSuit++)
            for (intVal = 0; intVal < 14; intVal++)
            {
               imageFileName = "images/" + turnIntIntoCardValueChar(intVal) + turnIntIntoCardSuitChar(intSuit) + ".gif";
               imageCards[intVal][intSuit] = new Image(imageFileName);
               imageCardViews[intVal][intSuit] = new ImageView(imageCards[intVal][intSuit]);

            }
         imagesNotLoaded = false;
      }

   }

   static public Image getImage(Card card)
   {
      loadCardImages(); // will not load twice, so no worries.
      return imageCards[valueAsInt(card)][suitAsInt(card)];
   }

   static int valueAsInt(Card card)
   {
      return cardlValsConvertAssist.indexOf(card.getValue());
   }

   static int suitAsInt(Card card)
   {
      switch (card.getSuit())
      {
      case clubs:
         return 0;
      case diamonds:
         return 1;
      case hearts:
         return 2;
      case spades:
         return 3;
      default:
         return 4;

      }
   }

   static Card.Suit turnIntIntoSuit(int k)
   {
      switch (k)
      {
      case 0:
         return Card.Suit.clubs;
      case 1:
         return Card.Suit.diamonds;
      case 2:
         return Card.Suit.hearts;
      case 3:
         return Card.Suit.spades;
      default:
         return Card.Suit.spades;
      }

   }
}

public class CardGUI extends Application
{
   // static for the 57 images and their corresponding labels
   // normally we would not have a separate label for each card, but
   // if we want to display all at once using labels, we need to.

   final int NUM_CARDS_PER_HAND = 7;
   final int NUM_PLAYERS = 2;
   Image[] humanImages = new Image[NUM_CARDS_PER_HAND];
   ImageView[] humanViews = new ImageView[NUM_CARDS_PER_HAND];
   Image[] computerImages = new Image[NUM_CARDS_PER_HAND];
   ImageView[] computerViews = new ImageView[NUM_CARDS_PER_HAND];
   Image[] playedImages = new Image[NUM_CARDS_PER_HAND];
   ImageView[] playedViews = new ImageView[NUM_CARDS_PER_HAND];
   Label[] playLabelText = new Label[NUM_PLAYERS];
   Button[] humanBtn = new Button[NUM_CARDS_PER_HAND];
   public static void main(String[] args)
   {

      launch(args);
   }

   static Card generateRandomCard()
   {
      GUICard guiCard = new GUICard();
      return new Card(guiCard.turnIntIntoCardValueChar((int) (Math.random() * 14)),
            guiCard.turnIntIntoSuit((int) (Math.random() * 4)));
   }
   
   private void moveCard(Image cTemp, Image hTemp, Pane pPane){
      pPane.getChildren().clear();
      playedImages[0] = hTemp;
      playedViews[0] = new ImageView(playedImages[0]);
      playedImages[1] = cTemp;
      playedViews[1] = new ImageView(playedImages[1]);
      VBox playerVbox = new VBox();
      VBox computerVbox = new VBox();
      playLabelText[0] = new Label("        You");
      playLabelText[1] = new Label("   Computer");
      playerVbox.getChildren().addAll(playedViews[0], playLabelText[0]);
      computerVbox.getChildren().addAll(playedViews[1], playLabelText[1]);
      pPane.getChildren().addAll(computerVbox, playerVbox);
   }

   public void start(Stage primaryStage)
   {

      BorderPane pane = new BorderPane();
      Scene scene = new Scene(pane, 750, 600);
      primaryStage.setTitle("Card Table");
      pane.setStyle("-fx-border-color: blue");

      HBox computerPane = new HBox(15);
      HBox humanPane = new HBox(15);

      computerPane.setStyle("-fx-border-color: blue");
      humanPane.setStyle("-fx-border-color: blue");
      FlowPane playedPane = new FlowPane(200, 100);
      playedPane.setAlignment(Pos.CENTER);
      playedPane.setStyle("-fx-border-color: blue");

      GUICard guiCard = new GUICard();
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         Card card = generateRandomCard();
         humanImages[i] = guiCard.getImage(card);
         humanViews[i] = new ImageView(humanImages[i]);
         humanBtn[i] = new Button();
         humanBtn[i].setGraphic(humanViews[i]);
         final Image hTemp = humanImages[i];
         humanBtn[i].setOnAction(e -> {
           final Image cTemp = guiCard.getImage(generateRandomCard());
           moveCard(cTemp, hTemp, playedPane);           
                  });
      }
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         Card card = generateRandomCard();
         computerImages[i] = guiCard.getImage(card);
         computerViews[i] = new ImageView(new Image("images/BK.gif"));
      }

      computerPane.setPadding(new Insets(0,0,0,75));
      humanPane.getChildren().addAll(humanBtn);
      computerPane.getChildren().addAll(computerViews);
      moveCard(new Image("images/XC.gif"), new Image("images/XC.gif"), playedPane);
      // and two random cards in the play region (simulating a computer/hum ply)
     
      pane.setTop(computerPane);
      pane.setCenter(playedPane);
      pane.setBottom(humanPane);
      //
      // show everything to the user
      primaryStage.setScene(scene);
      primaryStage.show();

   }

}