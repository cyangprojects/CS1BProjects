// @Author Chris Yang
// This program can interpret BarcodeImages, as well as turn a string into a BarcodeImage

//This interface has the common methods implemented by the DataMatrix class
interface BarcodeIO
{
   // boolean scan(BarcodeImage bc);
   boolean readText(String text);

   boolean generateImageFromText();

   boolean translateImageToText();

   void displayTextToConsole();

   void displayImageToConsole();
}
//This class creates and stores BarcodeImages, which can be interpreted by the DataMatrix class
class BarcodeImage implements Cloneable
{

   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] imageData;

   BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }

   BarcodeImage(String[] str_data)
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      if (checkSize(str_data)) {
         
         int colIndex = 0;
         // str starts at the second to last value and ends at the second value because
         // the first and last values are skipped
         for (int str = (str_data.length - 2); str >= 1; str--)
         {
            // same reasoning with this for loop for each string
            int widIndex = 1;
            for (int str_index = 1; str_index < str_data[str].length() - 1; str_index++)
            {
               if (str_data[str].charAt(str_index) == '*')
               {
                  setPixel((MAX_HEIGHT - colIndex), widIndex, true);
               } else
               {
                  setPixel((MAX_HEIGHT - colIndex), widIndex, false);
               }
               widIndex++;
            }
            colIndex++;
         }

   }
   }
   private boolean checkSize(String[] data)
   {
      return !(data.length - 2 > MAX_HEIGHT || data[0].length() - 2 > MAX_WIDTH || data == null);
     
   }

   public boolean getPixel(int row, int col)
   {
      try
      {
         return imageData[row - 1][col - 1];

      } catch (Exception IndexOutOfBounds)
      {
         return false;
      }
   }

   public boolean setPixel(int row, int col, boolean value)
   {
      try
      {
         imageData[row - 1][col - 1] = value;
         return true;
      } catch (Exception IndexOutOfBounds)
      {
         return false;
      }

   }

   public void displayToConsole()
   {
      for (int i = 0; i < MAX_HEIGHT; i++)
      {
         for (int j = 0; j < MAX_WIDTH; j++)
         {
            System.out.print(imageData[i][j] + " ");

         }
         System.out.println();
      }
   }
   
   @Override
   public Object clone(){
        try
      {
         BarcodeImage copy = (BarcodeImage)super.clone();
         copy.imageData = this.imageData;
         for (int row = 0; row < imageData.length; row++) {
            copy.imageData[row] = this.imageData[row].clone();
         }
         return copy;
      } catch (CloneNotSupportedException e)
      {
         e.printStackTrace();
      }
      return null;

         
   }
      
   }

//This class interprets BarcodeImages as well turns strings into BarcodeImages
//DataMatrix implements the interface BarcodeIO
class DataMatrix implements BarcodeIO
{

   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;

   DataMatrix()
   {
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      text = "";
   }

   DataMatrix(BarcodeImage image)
   {
      this();
      scan(image);
   }

   DataMatrix(String text)
   {
      this();
      readText(text);
   }
   public BarcodeImage getImage() {
      return image;
   }
   public void scan(BarcodeImage image) {
     try {
      this.image = (BarcodeImage) image.clone();
      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();
     } catch (Exception e) {
        
     }
   }
   private int computeSignalHeight() {
      int height = 0;
      for (int i = 1; i <= BarcodeImage.MAX_HEIGHT; i++) {
         if (image.getPixel(i, 1)) {
            height++;
         }
      }
      return height;
   }
   private int computeSignalWidth() {
      int width = 0;
      for (int i = 1; i < BarcodeImage.MAX_WIDTH; i++) {
         if (image.getPixel(BarcodeImage.MAX_HEIGHT, i)) {
            width++;
         }
      }
      return width;
   }
   public int getActualWidth() {
      return actualWidth;
   }
   public int getActualHeight() {
      return actualHeight;
   }

   @Override
   public boolean generateImageFromText()
   {
      image = new BarcodeImage();
      char[] textArray = text.toCharArray();
      for (int row = BarcodeImage.MAX_HEIGHT; row >= BarcodeImage.MAX_HEIGHT - 10; row--) {
         image.setPixel(row, 1, true);
      }
      for (int col = 1; col <= textArray.length; col++) {
         if ((col % 2) == 1) {
            image.setPixel(BarcodeImage.MAX_HEIGHT - 9, col, true);
         }
         
      }
      int counter = 0;
      for (int col = 2; col <= textArray.length + 1; col++) {
         writeChartoCol(col, textArray[counter]);
         counter++;
         
        
      }
      this.actualHeight = computeSignalHeight() - 1;
      this.actualWidth = computeSignalWidth() + 1;
      
      return true;
      
      
   }
   public boolean writeChartoCol(int col, char character) {
      int value = (int) character;
      char[] binary = Integer.toBinaryString(value).toCharArray();
      image.setPixel(BarcodeImage.MAX_HEIGHT, col, true);
      int counter = 1;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= BarcodeImage.MAX_HEIGHT - binary.length ; i--) {
         if(binary[binary.length - counter] == '1') {
            image.setPixel(i, col, true);
         }
         counter++;
      }
      return true;
      
   }

   @Override
   public boolean translateImageToText()
   {
      String text = "";
      for (int i = 2; i < actualWidth; i++) {
         text += readCharFromCol(i);
      }
      this.text = text;
      return true;
   }
   private char readCharFromCol(int col) {
      int value = 0;
      int multiplier = 0;
      for (int i = BarcodeImage.MAX_HEIGHT - 1; i > BarcodeImage.MAX_HEIGHT - (actualHeight - 1) ;  i--) {
         if(image.getPixel(i, col)) {
            value+= Math.pow(2, multiplier);
         }
         multiplier++;
      }
      return  (char) value;
   }
   @Override
   public void displayTextToConsole()
   {
      System.out.println(text);

   }

   @Override
   public void displayImageToConsole()
   {
      for (int i = 0; i < actualWidth + 2; i++) {
         System.out.print('-');
      }
      System.out.println();
      for (int row = BarcodeImage.MAX_HEIGHT - actualHeight + 1; row <= BarcodeImage.MAX_HEIGHT; row++) {
         System.out.print('|');
         for (int col = 1; col <= actualWidth; col++) {
            if (image.getPixel(row, col)) {
               System.out.print('*');
            }else {
               System.out.print(' ');
            }
         }
         System.out.print('|');
         System.out.println();
      }
      for (int i = 0; i < actualWidth + 2; i++) {
         System.out.print('-');
      }
      System.out.println();

   }

   @Override
   public boolean readText(String text)
   {
      this.text = text;
      return true;
   }

}

public class BarcodeReader
{
   public static void main(String[] args)
   {
      String[] sImageIn =
         {
            "------------------------------",
            "|* * * * * * * * * * * * * * *|",
            "|*                            |",
            "|**** *** *******  ***** *****|",
            "|* **************** ********* |",
            "|** *  *   * *      *  * * * *|",
            "|***          **          * * |",
            "|* **   *  ** ***  * * * **  *|",
            "|* *   *   * **    **    **** |",
            "|**** * * ******** * **  ** **|",
            "|*****************************|",
            "------------------------------"
         };      
               
            
         
         String[] sImageIn_2 =
         {
               "-------------------------------",
               "|* * * * * * * * * * * * * * *|",
               "|*                           *|",
               "|*** ** ******** ** ***** *** |",
               "|*  **** ***************** ***|",
               "|* *  *    *      *  *  *  *  |",
               "|*       ** **** *          **|",
               "|*    * ****  **    * * * *** |",
               "|***    ***       * **    * **|",
               "|*** *   **  *   ** * **   *  |",
               "|*****************************|",
               "-------------------------------"
               
         };
        
         BarcodeImage bc = new BarcodeImage(sImageIn);
         DataMatrix dm = new DataMatrix(bc);
        
         // First secret message
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
      //   
         // second secret message
         bc = new BarcodeImage(sImageIn_2);
         dm.scan(bc);
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         dm.readText("What a great resume builder this is!");
         dm.generateImageFromText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         dm.displayImageToConsole();
         dm.displayTextToConsole();
      }
     
   }

