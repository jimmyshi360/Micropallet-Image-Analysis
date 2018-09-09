
import java.util.Scanner;

import java.io.*;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import java.awt.Color;

public class PictureTester
{
   public static boolean [][][][][] motherMatrix;
   public static String CoreFile;
   public static Picture DisplayPic;
   public static int startingRow = 155;
   public static int startingCol = 154;
   public static int border = 10;
   public static int well = 20;
   public static int dim = 16;
   public static int shiftDown = -92;
   public static int shiftRight = 8500;
   public static double fraction = 0.66;

	public static void main(String[] args)
	{
      Scanner Scan = new Scanner(System.in);
      boolean keepAsk = true;
            
		testImageProcessing();
      while(keepAsk)
      {
         searchImageProcessing();
         System.out.println("Is this the end of your searching and you do not need the data accumlated by the program anymore?");
         System.out.println("0 --> quit the program / anything other than 0 --> continue the program");
         if(Scan.next().equals("0"))
         {
            System.out.println("Are you sure you want to quit? (0 --> Yes / other things --> No)");
            if(Scan.next().equals("0"))
               keepAsk = false;
         }
      }
         
	}
   
   public static void testImageProcessing()
	{
      Scanner Scan = new Scanner(System.in);
  
      CoreFile = "2016.04.";
      int numChannel = 6;
      
      boolean keepGoing = true;

         
      for(int i = 1; i <= numChannel; i++)
      {
         String OpenName = CoreFile + "-" + i + ".jpg";
         System.out.println(OpenName);
         Picture Sample = new Picture(OpenName);
         while(keepGoing)
         {
            Sample.drawFrame(startingRow, startingCol, border, well, dim, shiftDown, shiftRight, fraction);
            System.out.println();
            System.out.println();
            Syste,.out.println
            System.out.println("Does the box fit? No -> 0 Yes -> 1");
            if(Scan.nextInt() == 1)
            {
               keepGoing = false;
               String name = "EDIT-" + i + ".jpg";
               Sample.write(name);
            }
         }
         if( i != 1)
         {
             Sample.drawFrame(startingRow, startingCol, border, well, dim, shiftDown, shiftRight, fraction);
             String name = "EDIT-" + i + ".jpg";
             Sample.write(name);
         }
         
         String TestName = CoreFile + "-" + i + "-" + "HC" + ".jpg";
         System.out.println(TestName);
         Sample = new Picture(TestName);
         Color color = Color.white;
		   JFrame frame = new JFrame();
		   frame.setAlwaysOnTop(true);
         String question = "Pick a color for " + TestName;
		   color = JColorChooser.showDialog(frame,question,color);
         System.out.println(color.toString());
         System.out.println("What is the range of pixels for the selected color should be registered as \"POSITIVE\" scanning " + TestName + "?");
         System.out.println("Enter the lower number first, add space, then the higher number");
         int [] list = new int [2];
         for(int j = 0; j < 2; j++)
         {
            list[j] = Scan.nextInt();
         }
         
         storeInfo((Sample.imageProcessing(startingRow, startingCol, border, well, dim, shiftDown, shiftRight, fraction, color.getRed(), color.getGreen(), color.getBlue(), list[0], list[1])), (i - 1));
         /*if(i == 3)
         storeInfo((Sample.imageProcessing(r, c, b, w, v, d, g, f, 0, 210, 210, 3, 17)), (i - 1));
         else if(i == 2)
         storeInfo((Sample.imageProcessing(r, c, b, w, v, d, g, f, 0, 155, 155, 1, 37)), (i - 1));
         else if(i == 1)
         storeInfo((Sample.imageProcessing(r, c, b, w, v, d, g, f, 0, 190, 190, 1, 37)), (i - 1));
         else if(i == 4)
         storeInfo((Sample.imageProcessing(r, c, b, w, v, d, g, f, 0, 95, 95, 1, 40)), (i - 1));
         else
         storeInfo((Sample.imageProcessing(r, c, b, w, v, d, g, f, 0, 160, 160, 1, 40)), (i - 1));*/
      }
	}
   
   private static void storeInfo(boolean[][][][] array, int level)
   {
      if(level == 0)
      {
         motherMatrix = new boolean [array.length][array[0].length][array[0][0].length][array[0][0][0].length][8];
         for(int rr = 0; rr < array.length; rr++)
         {
            for(int cc = 0; cc < array[0].length; cc++)
            {
               for(int r = 0; r < array[0][0].length; r++)
               {
                  for(int c = 0; c < array[0][0][0].length; c++)
                  {
                     motherMatrix[rr][cc][r][c][level] = array[rr][cc][r][c];
                  }
               }
            }
         }
      }
      else
      {
         for(int rr = 0; rr < array.length; rr++)
         {
            for(int cc = 0; cc < array[0].length; cc++)
            {
               for(int r = 0; r < array[0][0].length; r++)
               {
                  for(int c = 0; c < array[0][0][0].length; c++)
                  {
                     motherMatrix[rr][cc][r][c][level] = array[rr][cc][r][c];
                  }
               }
            }
         }
      }
      
      for(int rr = 0; rr < array.length; rr++)
      {
         for(int cc = 0; cc < array[0].length; cc++)
         {
            for(int r = 0; r < array[0][0].length; r++)
            {
               for(int c = 0; c < array[0][0][0].length; c++)
               {
                  motherMatrix[rr][cc][r][c][level] = array[rr][cc][r][c];
               }
            }
         }
      }
         
   }
   
   public static void searchImageProcessing()
   {
      //---Start Asking---//
      Scanner Scan = new Scanner(System.in);
      int[] inquiry;
      System.out.println("How many colors are in your combination of interest?");
      int num = Scan.nextInt();
      inquiry = new int[num];
      System.out.println("Enter all the numbers of interest with space in-between");
      for(int i = 0; i < num; i++)
      {
         inquiry[i] = Scan.nextInt() - 1;
      }
      
      print(inquiry);
            
   }
   
   private static void print(int [] inquiry)
   {
      int count = 0;
      String FileName = "Color Combination- ";
      boolean [][] displayMatrix = new boolean [motherMatrix[0][0].length][motherMatrix[0][0][0].length];
      
      for(int i = 0; i < inquiry.length; i++)
      {
         FileName = FileName + (inquiry[i] + 1) + " ";
      }
      String TextFileName = FileName + ".docx";
      String PicFileName = FileName + ".jpg";
      boolean newline;
      
      try
      {
         FileWriter table = new FileWriter(TextFileName);
         String Temp = CoreFile + "-DP.jpg";
         DisplayPic = new Picture(Temp);
         DisplayPic.drawFrame(startingRow, startingCol, border, well, dim, shiftDown, shiftRight, fraction);
         //look for booleans
      
         for(int rr = 0; rr < motherMatrix.length; rr++)
         {
            for(int cc = 0; cc < motherMatrix[0].length; cc++)
            {
               int counter = 0;
               for(int r = 0; r < motherMatrix[0][0].length; r++)
               {
                  for(int c = 0; c < motherMatrix[0][0][0].length; c++)
                  {
                     boolean markIt = true;
                     for(int i = 0; i < motherMatrix[0][0][0][0].length; i++)
                     {
                        boolean asked = false;
                        for(int j = 0; j < inquiry.length; j++)
                        {
                           if(inquiry[j] == i)
                           {
                              asked = true;
                           }
                        }
                        
                        if(asked)
                        {
                           if( ! motherMatrix[rr][cc][r][c][i])
                           {
                              markIt = false;
                           }
                        }
                        else
                        {
                           if(motherMatrix[rr][cc][r][c][i])
                           {
                              markIt = false;
                           }
                        }
                     }
                     if(markIt)
                     {
                        DisplayPic.drawTargets(rr, cc, r, c);
                        displayMatrix[r][c] = true;
                        counter++;
                     }
                     else 
                     {
                        displayMatrix[r][c] = false;
                     }
                  
                           
                  }
               }
               if(counter > 0)
               {
                  table.write("Location: (" + rr + ", " + cc + ")\n");
                  System.out.println("Location: (" + rr + ", " + cc + ")\n");
                  for(int r = 0; r <= displayMatrix.length; r++)
                  {
                     if(r == 0)
                     {
                           table.write("   A B C D E F G H I J K L M N \n");
                           System.out.println("   A B C D E F G H I J K L M N \n");
                     }
                     else
                     {
                        for(int c = 0; c <= displayMatrix[0].length; c++)
                        {
                          if(c == 0 )
                          {
                              if(r < 10){                       
                                 table.write(r + "  ");
                                 System.out.print(r + "  ");}
                              else{
                                 table.write(r + " ");
                                 System.out.print(r + " ");}
                          }
                          else
                          {  
                             String line = "";           
                             if(displayMatrix[r - 1][c - 1] == false)
                             {  
                                line = line + "- ";
                             }
                             else
                             {
                                line = line + "X ";
                                count++;
                             }
                             if(c == displayMatrix[0].length)
                             {
                                line = line + "\n";
                             }
                             table.write(line);
                             System.out.print(line);
                          }
                       }
                    }
                  }
                  table.write("Final Cell Count: " + count + "\n");
                  System.out.println("Final Cell Count: " + count);
                  System.out.println();
               }
            }
         }
         DisplayPic.write(PicFileName);
         table.close();
      }
      catch(IOException i)
      {
         System.out.println("Error: " + i.getMessage());
      }
      
      
   }

}

/* OUTRUN

*/