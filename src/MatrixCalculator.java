import java.util.Scanner;

class Complex implements Cloneable, Comparable<Complex>
{

   private double a;
   private double b;

   public Complex(double a, double b)
   {
      this.a = a;
      this.b = b;
   }

   public Complex(double a)
   {
      this.a = a;
      b = 0.0;
   }

   public Complex()
   {
      a = 0.0;
      b = 0.0;
   }

   public Complex add(Complex second)
   {
      return new Complex(a + second.getRealPart(), b + second.getImaginaryPart());
   }

   public Complex subtract(Complex second)
   {
      return new Complex(a - second.getRealPart(), b - second.getImaginaryPart());
   }

   public Complex multiply(Complex second)
   {
      double newA = (a * second.getRealPart()) - (b * second.getImaginaryPart());
      double newB = (b * second.getRealPart()) + (a * second.getImaginaryPart());
      return new Complex(newA, newB);
   }

   public Complex divide(Complex second)
   {
      double newA = ((a * second.getRealPart()) + (b * second.getImaginaryPart()))
            / ((second.getRealPart() * second.getRealPart()) + (second.getImaginaryPart() * second.getImaginaryPart()));
      double newB = ((b * second.getRealPart()) - (a * second.getImaginaryPart()))
            / ((second.getRealPart() * second.getRealPart()) + (second.getImaginaryPart() * second.getImaginaryPart()));
      return new Complex(newA, newB);
   }

   public double abs()
   {
      return Math.sqrt((a * a) + (b * b));
   }

   @Override
   public String toString()
   {
      if (b == 0)
      {
         return "    0.0      ";
      }
      return "(" + a + " + " + b + "i)";
   }

   public Object clone()
   {
      try
      {
         return (Complex) super.clone();
      } catch (CloneNotSupportedException e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public double getRealPart()
   {
      return a;
   }

   public double getImaginaryPart()
   {
      return b;
   }

   @Override
   public int compareTo(Complex o)
   {
      if (this.abs() > o.abs())
      {
         return 1;
      } else if (this.abs() < o.abs())
      {
         return -1;
      } else
      {
         return 0;
      }
   }

}

abstract class GenericMatrix<E extends Object>
{
   /** Abstract method for adding two elements of the matrices */
   protected abstract E add(E o1, E o2);

   /** Abstract method for multiplying two elements of the matrices */
   protected abstract E multiply(E o1, E o2);

   /** Abstract method for defining zero for the matrix element */
   protected abstract E zero();

   /** Add two matrices */
   public E[][] addMatrix(E[][] matrix1, E[][] matrix2)
   {
      // Check bounds of the two matrices

      if ((matrix1.length != matrix2.length) || (matrix1[0].length != matrix2[0].length))
      {
         throw new RuntimeException("The matrices do not have the same size");
      }

      E[][] result = (E[][]) new Object[matrix1.length][matrix1[0].length];

      // Perform addition
      for (int i = 0; i < result.length; i++)
         for (int j = 0; j < result[i].length; j++)
         {
            result[i][j] = add(matrix1[i][j], matrix2[i][j]);
         }

      return result;

   }

   public E[][] multiplyMatrix(E[][] matrix1, E[][] matrix2)
   {
      if ((matrix1.length != matrix2.length) || (matrix1[0].length != matrix2[0].length))
      {
         throw new ArrayIndexOutOfBoundsException("The matrices do not have the same size");
      }

      E[][] result = (E[][]) new Object[matrix1.length][matrix1[0].length];

      // Perform addition
      for (int i = 0; i < result.length; i++)
         for (int j = 0; j < result[i].length; j++)
         {
            result[i][j] = zero();

            result[i][j] = add(result[i][j], multiply(matrix1[i][j], matrix2[i][j]));
         }

      return result;
   }

   /** Print matrices, the operator, and their operation result */
   public static void printResult(

         Object[][] m1, Object[][] m2, Object[][] m3, char op)
   {
      try
      {
         for (int i = 0; i < m1.length; i++)
         {
            for (int j = 0; j < m1[0].length; j++)
               System.out.print(" " + m1[i][j]);

            if (i == m1.length / 2)
               System.out.print("  " + op + "  ");
            else
               System.out.print("     ");

            for (int j = 0; j < m2.length; j++)
               System.out.print(" " + m2[i][j]);

            if (i == m1.length / 2)
               System.out.print("  =  ");
            else
               System.out.print("     ");

            for (int j = 0; j < m3.length; j++)
               System.out.print(m3[i][j] + " ");

            System.out.println();
         }
      } catch (ArrayIndexOutOfBoundsException e)
      {
         System.out.print("The matrices are not the same size");
      }

   }
}

class ComplexMatrix extends GenericMatrix<Complex>
{

   @Override
   protected Complex add(Complex o1, Complex o2)
   {
      return o1.add(o2);
   }

   @Override
   protected Complex multiply(Complex o1, Complex o2)
   {
      return o1.multiply(o2);

   }

   @Override
   protected Complex zero()
   {
      // TODO Auto-generated method stub
      return new Complex();
   }

}

public class MatrixCalculator
{
   public static void main(String[] args)
   {

      ComplexMatrix compMatrix = new ComplexMatrix();
      Complex[][] matrix1 = new Complex[3][3];
      matrix1[0][0] = new Complex(3.0, 1.0); // focus
      matrix1[0][1] = new Complex(4, 5);
      matrix1[0][2] = new Complex(3, 2);
      matrix1[1][0] = compMatrix.zero();
      matrix1[1][1] = new Complex(1, 2);
      matrix1[1][2] = new Complex(3, 2);
      matrix1[2][0] = new Complex(5, 1);
      matrix1[2][1] = new Complex(3, 4);
      matrix1[2][2] = new Complex(3, 4);

      Complex[][] matrix2 = new Complex[3][3];
      matrix2[0][0] = new Complex(3.0, 1.0); // focus
      matrix2[0][1] = new Complex(4, 5);
      matrix2[0][2] = new Complex(3, 2);
      matrix2[1][0] = new Complex(1, 2);
      matrix2[1][1] = compMatrix.zero();
      matrix2[1][2] = new Complex(3, 2);
      matrix2[2][0] = new Complex(5, 1);
      matrix2[2][1] = new Complex(3, 4);
      matrix2[2][2] = new Complex(3, 4);

      Complex c1 = new Complex(3.0, 1.0);
      Complex c2 = new Complex(3.0, 1.0);
      System.out.println("Multiply:");
      compMatrix.printResult(matrix1, matrix2, compMatrix.multiplyMatrix(matrix1, matrix2), '*');
      System.out.println("Add:");
      compMatrix.printResult(matrix1, matrix2, compMatrix.addMatrix(matrix1, matrix2), '+');

      Complex[][] matrix3 = new Complex[2][2];
      matrix3[0][0] = new Complex(3.0, 1.0); // focus
      matrix3[0][1] = new Complex(4, 5);
      matrix3[1][0] = new Complex(1, 2);
      matrix3[1][1] = compMatrix.zero();

      compMatrix.printResult(matrix1, matrix3, compMatrix.multiplyMatrix(matrix1, matrix2), '*');

   }

}
