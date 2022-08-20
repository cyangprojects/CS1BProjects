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
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.awt.color.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

//This program is a 24 point card game. The goal is to use the value of the all of the cards displayed and attempt to reach 24. 
//You can use addition (+), subtraction (-), multiplication (*), division (/), and parenthesis. The rules are that all of the values
//displayed need to be used once and only once.

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

   private static String cardlValsConvertAssist = "A23456789TJQKX";
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
      return cardlValsConvertAssist.indexOf(card.getValue()) + 1;
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

public class TwentyFourGame extends Application
{

   int[] validValues = new int[4];

   public static void main(String[] args)
   {

      launch(args);
   }

   public void start(Stage primaryStage)
   {
      BorderPane pane = new BorderPane();
      Scene scene = new Scene(pane, 650, 350);
      primaryStage.setTitle("24 Point Card Game");
      primaryStage.setScene(scene);
      primaryStage.show();

      HBox cardPane = new HBox();
      cardPane.setSpacing(10);
      cardPane.setAlignment(Pos.CENTER);
      setCardPane(cardPane);

      HBox topPane = new HBox();
      Button shuffleBT = new Button("Shuffle");
      Label topLabel = new Label("");
      topPane.setAlignment(Pos.BASELINE_RIGHT);
      topPane.setSpacing(10);
      topPane.getChildren().addAll(topLabel, shuffleBT);
      shuffleBT.setOnAction(e ->
      {
         topLabel.setText("");
         setCardPane(cardPane);
      });

      HBox bottomPane = new HBox();
      TextField tf = new TextField();
      Label bottomLabel = new Label("Enter an expression:");
      Button verifyBT = new Button("Verify");
      bottomPane.setAlignment(Pos.BOTTOM_CENTER);
      bottomPane.getChildren().addAll(bottomLabel, tf, verifyBT);
      bottomPane.setSpacing(10);

      verifyBT.setOnAction(e ->
      {
         String text = tf.getText();
         try
         {
            int expression = evaluateExpression(text);
            if (!validateExpression(text, validValues))
            {
               topLabel.setText("The numbers in the expression don't match the numbers in the set");

            } else
            {
               if (expression == 24)
               {
                  topLabel.setText("Correct");
               } else
               {
                  topLabel.setText("Incorrect");
               }
            }

         } catch (Exception ex)
         {
            topLabel.setText("Invalid Entry");
         }
      });

      pane.setTop(topPane);
      pane.setCenter(cardPane);
      pane.setBottom(bottomPane);

   }

   private void setCardPane(HBox pane)
   {
      pane.getChildren().clear();
      GUICard guiCard = new GUICard();
      Deck deck = new Deck();
      deck.shuffle();
      int index = 0;
      // usedCards contain 4 unique cards
      for (int i = 0; i < 4; i++)
      {
         Card tempCard = deck.dealCard();
         validValues[index] = (guiCard.valueAsInt(tempCard)) + 1;
         index++;
         ImageView IV = new ImageView(guiCard.getImage(tempCard));
         IV.setFitHeight(240);
         IV.setFitWidth(150);
         pane.getChildren().add(IV);
      }

   }

   private static boolean validateExpression(String expression, int[] validValues)
   {
      expression = insertBlanks(expression);
      String[] tokens = expression.split(" ");
      Stack<Integer> userNums = new Stack<>();

      for (String token : tokens)
      {
         if (token.length() == 0) // Blank space
            continue;
         if (isNumeric(token))
         {
            userNums.push(Integer.valueOf(token));
         }
      }
      if (userNums.size() != 4)
      {
         return false;
      }

      ArrayList<Integer> validNums = new ArrayList<>();
      for (int i = 0; i < validValues.length; i++)
      {
         validNums.add(validValues[i]);
      }

      for (int i = 0; i < userNums.size(); i++)
      {
         int num = userNums.pop();
         int index = validNums.indexOf(num);

         if (index != -1)
         {
            validNums.remove(index);

         } else
         {
            return false;
         }

      }
      return true;
   }

   private static boolean isNumeric(String strNum)
   {
      if (strNum == null)
      {
         return false;
      }
      try
      {
         double d = Double.parseDouble(strNum);
      } catch (NumberFormatException nfe)
      {
         return false;
      }
      return true;
   }

   private static int evaluateExpression(String expression)
   {
      // Create operandStack to store operands
      Stack<Integer> operandStack = new Stack<>();
      // Create operatorStack to store operators
      Stack<Character> operatorStack = new Stack<>();

      // Insert blanks around (, ), +, -, /, and *
      expression = insertBlanks(expression);

      // Extract operands and operators
      String[] tokens = expression.split(" ");

      // Phase 1: Scan tokens
      for (String token : tokens)
      {
         if (token.length() == 0) // Blank space
            continue; // Back to the while loop to extract the next token
         else if (token.charAt(0) == '+' || token.charAt(0) == '-')
         {
            // Process all +, -, *, / in the top of the operator stack
            while (!operatorStack.isEmpty() && (operatorStack.peek() == '+' || operatorStack.peek() == '-'
                  || operatorStack.peek() == '*' || operatorStack.peek() == '/'))
            {
               processAnOperator(operandStack, operatorStack);
            }

            // Push the + or - operator into the operator stack
            operatorStack.push(token.charAt(0));
         } else if (token.charAt(0) == '*' || token.charAt(0) == '/')
         {
            // Process all *, / in the top of the operator stack
            while (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/'))
            {
               processAnOperator(operandStack, operatorStack);
            }

            // Push the * or / operator into the operator stack
            operatorStack.push(token.charAt(0));
         } else if (token.trim().charAt(0) == '(')
         {
            operatorStack.push('('); // Push '(' to stack
         } else if (token.trim().charAt(0) == ')')
         {
            // Process all the operators in the stack until seeing '('
            while (operatorStack.peek() != '(')
            {
               processAnOperator(operandStack, operatorStack);
            }

            operatorStack.pop(); // Pop the '(' symbol from the stack
         } else
         { // An operand scanned
            // Push an operand to the stack
            operandStack.push(Integer.valueOf(token));
         }
      }

      // Phase 2: process all the remaining operators in the stack
      while (!operatorStack.isEmpty())
      {
         processAnOperator(operandStack, operatorStack);
      }

      // Return the result
      return operandStack.pop();
   }

   private static void processAnOperator(Stack<Integer> operandStack, Stack<Character> operatorStack)
   {
      char op = operatorStack.pop();
      int op1 = operandStack.pop();
      int op2 = operandStack.pop();
      if (op == '+')
         operandStack.push(op2 + op1);
      else if (op == '-')
         operandStack.push(op2 - op1);
      else if (op == '*')
         operandStack.push(op2 * op1);
      else if (op == '/')
         operandStack.push(op2 / op1);
   }

   private static String insertBlanks(String s)
   {
      String result = "";

      for (int i = 0; i < s.length(); i++)
      {
         if (s.charAt(i) == '(' || s.charAt(i) == ')' || s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*'
               || s.charAt(i) == '/')
            result += " " + s.charAt(i) + " ";
         else
            result += s.charAt(i);
      }

      return result;
   }

}
