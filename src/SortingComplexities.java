import java.util.*;

class Heap<E extends Comparable<E>>
{

   private ArrayList<Integer> list = new ArrayList<>();

   public Heap()
   {

   }

   public Heap(int[] objects)
   {
      for (int i = 0; i < objects.length; i++)
      {
         add(objects[i]);
      }
   }

   public void add(int newObject)
   {
      list.add(newObject);
      int currentIndex = list.size() - 1;
      while (currentIndex > 0)
      {
         int parentIndex = (currentIndex - 1) / 2;
         if (list.get(currentIndex).compareTo(list.get(parentIndex)) > 0)
         {
            int temp = list.get(currentIndex);
            list.set(currentIndex, list.get(parentIndex));
            list.set(parentIndex, temp);

         } else
         {
            break;
         }
         currentIndex = parentIndex;
      }

   }

   public int remove()
   {
      if (list.size() == 0)
      {
         return 0;
      }
      int removedObject = list.get(0);
      list.set(0, list.get(list.size() - 1));
      list.remove(list.size() - 1);
      int currentIndex = 0;
      while (currentIndex < list.size())
      {
         int leftChildIndex = 2 * currentIndex + 1;
         int rightChildIndex = 2 * currentIndex + 2;

         if (leftChildIndex >= list.size())
            break;
         int maxIndex = leftChildIndex;
         if (rightChildIndex < list.size())
         {
            if (list.get(maxIndex).compareTo(list.get(rightChildIndex)) < 0)
            {
               maxIndex = rightChildIndex;
            }
         }

         if (list.get(currentIndex).compareTo(list.get(maxIndex)) < 0)
         {
            int temp = list.get(maxIndex);
            list.set(maxIndex, list.get(currentIndex));
            list.set(currentIndex, temp);
            currentIndex = maxIndex;
         } else
         {
            break;
         }
      }
      return removedObject;

   }
}

public class SortingComplexities
{

   public static void insertionSort(int[] list)
   {
      for (int i = 1; i < list.length; i++)
      {
         int currentElement = list[i];
         int k;
         for (k = i - 1; k >= 0 && list[k] > currentElement; k--)
         {
            list[k + 1] = list[k];
         }
         list[k + 1] = currentElement;
      }
   }

   public static void bubbleSort(int[] list)
   {

      for (int i = 0; i < list.length - 1; i++)
      {
         for (int j = 0; j + 1 < list.length; j++)
         {
            if (list[j] > list[j + 1])
            {
               int temp = list[j];
               list[j] = list[j + 1];
               list[j + 1] = temp;

            }
         }
      }
   }

   public static void mergeSort(int[] list)
   {
      if (list.length > 1)
      {
         int[] firstHalf = new int[list.length / 2];
         System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
         mergeSort(firstHalf);

         int secondHalfLength = list.length - list.length / 2;
         int[] secondHalf = new int[secondHalfLength];
         System.arraycopy(list, list.length / 2, secondHalf, 0, secondHalfLength);
         mergeSort(secondHalf);

         merge(firstHalf, secondHalf, list);
      }
   }

   public static void merge(int[] list1, int[] list2, int[] temp)
   {
      int current1 = 0;
      int current2 = 0;
      int current3 = 0;

      while (current1 < list1.length && current2 < list2.length)
      {
         if (list1[current1] < list2[current2])
            temp[current3++] = list1[current1++];
         else
            temp[current3++] = list2[current2++];
      }

      while (current1 < list1.length)
         temp[current3++] = list1[current1++];

      while (current2 < list2.length)
         temp[current3++] = list2[current2++];
   }

   public static void quickSort(int[] list)
   {
      quickSort(list, 0, list.length - 1);
   }

   private static void quickSort(int[] list, int first, int last)
   {
      if (last > first)
      {
         int pivotIndex = partition(list, first, last);
         quickSort(list, first, pivotIndex - 1);
         quickSort(list, pivotIndex + 1, last);
      }
   }

   private static int partition(int[] list, int first, int last)
   {
      int pivot = list[first];
      int low = first + 1;
      int high = last;

      while (high > low)
      {

         while (low <= high && list[low] <= pivot)
            low++;

         while (low <= high && list[high] > pivot)
            high--;

         if (high > low)
         {
            int temp = list[high];
            list[high] = list[low];
            list[low] = temp;
         }
      }

      while (high > first && list[high] >= pivot)
         high--;

      if (pivot > list[high])
      {
         list[first] = list[high];
         list[high] = pivot;
         return high;
      } else
      {
         return first;
      }
   }

   public static <E extends Comparable<E>> void heapSort(int[] list)
   {
      Heap<Integer> heap = new Heap<>();
      for (int i = 0; i < list.length; i++)
      {
         heap.add(list[i]);
      }
      for (int i = list.length - 1; i >= 0; i--)
      {
         list[i] = heap.remove();
      }

   }

   public static void radixSort(int[] list)
   {
      int max = max(list);

      for (int exp = 1; max / exp > 0; exp *= 10)
      {
         countSort(list, exp);
      }

   }

   public static void countSort(int[] list, int exp)
   {
      int[] tempList = new int[list.length];
      int[] count = new int[10];
      for (int i = 0; i < list.length; i++)
      {
         count[(list[i] / exp) % 10]++;
      }
      for (int i = 1; i < 10; i++)
      {
         count[i] = count[i - 1] + count[i];

      }
      for (int i = list.length - 1; i >= 0; i--)
      {
         tempList[count[(list[i] / exp) % 10] - 1] = list[i];
         count[(list[i] / exp) % 10]--;
      }
      for (int i = 0; i < list.length; i++)
      {
         list[i] = tempList[i];
      }

   }

   public static int getKey(int num, int exp)
   {
      return (num / exp) % 10;
   }

   public static int max(int[] list)
   {
      if (list.length == 0)
      {
         return 0;
      }
      int max = list[0];
      for (int i = 0; i < list.length; i++)
      {
         if (list[i] > max)
         {
            max = list[i];
         }
      }
      return max;
   }

   public static int[] generateList(int len)
   {
      int[] list = new int[len];
      for (int i = 0; i < list.length; i++)
      {
         list[i] = (int) (Math.random() * 301);
      }
      return list;
   }

   public static void main(String[] args)
   {

      System.out.println("Array Size       |    Insertion Sort     |      Bubble Sort      |"
            + "    Merge Sort      |       Quick Sort      |      Heap Sort      |       Radix Sort          |");
      for (int i = 50000; i <= 300000; i += 50000)
      {
         System.out.print(i + "                   ");
         long startTime = System.nanoTime();
         insertionSort(generateList(i));
         long endTime = System.nanoTime();
         long executionTime = endTime - startTime;
         System.out.print(executionTime + "               ");
         startTime = System.nanoTime();
         bubbleSort(generateList(i));
         endTime = System.nanoTime();
         executionTime = endTime - startTime;
         System.out.print(executionTime + "               ");
         startTime = System.nanoTime();
         mergeSort(generateList(i));
         endTime = System.nanoTime();
         executionTime = endTime - startTime;
         System.out.print(executionTime + "               ");
         startTime = System.nanoTime();
         quickSort(generateList(i));
         endTime = System.nanoTime();
         executionTime = endTime - startTime;
         System.out.print(executionTime + "               ");
         startTime = System.nanoTime();
         heapSort(generateList(i));
         endTime = System.nanoTime();
         executionTime = endTime - startTime;
         System.out.print(executionTime + "               ");
         startTime = System.nanoTime();
         radixSort(generateList(i));
         endTime = System.nanoTime();
         executionTime = endTime - startTime;
         System.out.println(executionTime + "             ");
      }

   }
}
