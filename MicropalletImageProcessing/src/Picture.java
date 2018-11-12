import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; 
import javax.imageio.ImageIO;
import java.io.*;

public class Picture extends SimplePicture
{
      
      private String fileName;
      private BufferedImage bufferedImage;
      private String extension;
      private String title;
      private boolean[][][][] isPresent;
      private Pixel[][] image;
      private int wellLength;
      private int borderLength;
      private int wellborder;
      private int StartingRow;
      private int StartingCol;
      private int valueRl;
      private int valueBl;
      private int valueGl;
      private int valueRu;
      private int valueBu;
      private int valueGu;
      private int hit1;
      private int hit2;
      private int dimension;
      private int numPixShiftDown;
      private int numPixShiftRight;
      private double fraction;
      private int jumpingNum = 1;

   public Picture(int H, int W)
   {
      super(H, W);
   }
	public Picture(String fileName)
	{
		
		// let the parent class handle this fileName
      super(fileName);
	}
   
   public Picture(String fileName, int limit)
   {
      super(fileName, limit);
   }
   
	////////////////////// methods ///////////////////////////////////////
   
	public boolean[][][][] imageProcessing(int startingRow, int startingCol, int endingRow, int endingCol, int well, int border, int dim, double frac, int rrr, int gg, int bb, int redUpp, int greenUpp, int blueUpp, int h1, int h2)
	{

	   image = this.getPixels2D();
      StartingRow = startingRow;
      StartingCol = startingCol;
      borderLength = border;
      wellLength = well;
      wellborder = well + border;
      dimension = dim;
      numPixShiftDown = endingRow - startingRow;
      numPixShiftRight = ((int)((double) numPixShiftDown / (endingCol - startingCol) * image.length) * -1) - 1;
      fraction = frac;
      valueRl = rrr;
      valueBl = bb;
      valueGl = gg;
      valueRu = redUpp;
      valueBu = blueUpp;
      valueGu = greenUpp;
      hit1 = h1;
      hit2 = h2;
      
      jumpingNum = (int)Math.sqrt((double) h1);
      
      if(jumpingNum < 1)
      {
         jumpingNum = 1;
      }
      
      int R = (int)((image.length - StartingRow - Math.abs(numPixShiftDown)) / (dimension * (wellborder + fraction)));
      int C = (int)((image[0].length - StartingCol - Math.abs(numPixShiftRight)) / (dimension * (wellborder + fraction)));

      
      isPresent = new boolean[R][C][dim][dim];
      for(int ROW = 0; ROW < R; ROW++)
      {
         for(int COL = 0; COL < C; COL++)
         {
            for(int Row = 0; Row < dim; Row++)
            {
               for(int Col = 0; Col < dim; Col++)
               {
                  isPresent[ROW][COL][Row][Col] = false;
               }
            }
         }
      }
      
      for(int rr = 0; rr < R; rr++)
      {
         for(int cc = 0; cc < C; cc++)
         {
            for(int r = 0; r < dim; r++)
            {
               for(int c = 0; c < dim; c++)
               {
                  isPresent[rr][cc][r][c] = scan(rr, cc, r, c);
               
               }
            }
         }
      }
      
      return isPresent;
   }
   private boolean scan(int rr, int cc ,int r, int c)
   {
      int SR = (int)((rr * dimension + r) * (wellborder + fraction)) + StartingRow;
      int SC = (int)((cc * dimension + c) * (wellborder + fraction)) + StartingCol;
      SR = SR + (int)((((double)(SC ) )/ image[0].length) * numPixShiftDown);
      SC = SC + (int)((((double)(SR ) )/ image.length) * numPixShiftRight);
      int ER = SR + wellLength;
      int EC = SC + wellLength;
      
      int counter = 0;
      outer:
      for(int row = SR; row <= ER; row += jumpingNum)
      {
         for(int col = SC; col <= EC; col += jumpingNum)
         {
            if (((image[row][col].getBlue() > valueBl) && (image[row][col].getGreen() > valueGl) && (image[row][col].getRed() > valueRl)) && ((image[row][col].getBlue() < valueBu) && (image[row][col].getGreen() < valueGu) && (image[row][col].getRed() < valueRu)))
            {
               counter++; 
               SR = row - jumpingNum + 1;
               SC = col - jumpingNum + 1;
               break outer;      
            }
         }
      }     
      
      if(counter == 0)
      {
         return false;
      }
      
      counter = 0;
      for(int row = SR; row <= ER; row += 1)
      {
         for(int col = SC; col <= EC; col += 1)
         {
            if (((image[row][col].getBlue() > valueBl) && (image[row][col].getGreen() > valueGl) && (image[row][col].getRed() > valueRl)) && ((image[row][col].getBlue() < valueBu) && (image[row][col].getGreen() < valueGu) && (image[row][col].getRed() < valueRu)))

            {
               counter++;       
            }
         }
      }     
      return ((counter >= hit1)&&(counter <= hit2));

   }
   
   public void drawFrame(int startingRow, int startingCol, int endingRow, int endingCol, int well, int border, int dim, double frac)
   {
      image = this.getPixels2D();
      StartingRow = startingRow;
      StartingCol = startingCol;
      borderLength = border;
      wellLength = well;
      wellborder = well + border;
      dimension = dim;
      numPixShiftDown = endingRow - startingRow;
      numPixShiftRight = ((int)((double) numPixShiftDown / (endingCol - startingCol) * image.length) * -1) - 1;
      fraction = frac;
      
      int R = (int)((image.length - StartingRow - Math.abs(numPixShiftDown)) / (dimension * (wellborder + fraction)));
      int C = (int)((image[0].length - StartingCol - Math.abs(numPixShiftRight)) / (dimension * (wellborder + fraction)));
      
       class WorkerTask implements Runnable {
    	   int rr;
    	   WorkerTask(int rr)
    	   {
    		   this.rr=rr;
    	   }
    	   public void run() {
 			  
 			  for(int cc = 0; cc < C; cc++)
 		         {
 		            for(int r = 0; r < dim; r++)
 		            {
 		               for(int c = 0; c < dim; c++)
 		               {
 		                  draw(rr, cc, r, c);
 		               }
 		            }
 		         }
 		  }
    	   
       }
       //for(int rr = 0; rr < R; rr++)
       //{
      // new Thread(new WorkerTask(rr)).start();
       //}

      for(int rr = 0; rr < R; rr++)
      {
         for(int cc = 0; cc < C; cc++)
         {
            for(int r = 0; r < dim; r++)
            {
               for(int c = 0; c < dim; c++)
               {
                  draw(rr, cc, r, c);
               }
            }
         }
      }
   }
   
   public void drawTargets(int rr, int cc, int r, int c)
   {
      int SR = (int)((rr * dimension + r) * (wellborder + fraction)) + StartingRow; //+ (numPixShiftDown / numWellWide * rr * 10);
      int SC = (int)((cc * dimension + c) * (wellborder + fraction)) + StartingCol; //(numPixShiftRight / numWellHigh * cc * 10) + StartingCol;
      SR = SR + (int)((((double)(SC ) )/ image[0].length) * numPixShiftDown);
      SC = SC + (int)((((double)(SR ) )/ image.length) * numPixShiftRight);
      int ER = SR + wellLength;
      int EC = SC + wellLength;
      
      for(int Col = SC; Col <= EC; Col++)
      {
         image[SR][Col].setColor(Color.RED);
         image[ER][Col].setColor(Color.RED);
      }
      
      for(int Row = SR; Row <= ER; Row++)
      {
         image[Row][SC].setColor(Color.RED);
         image[Row][EC].setColor(Color.RED);
      }
   }
   
   private void draw(int rr, int cc, int r, int c)
   {
      int SR = (int)((rr * dimension + r) * (wellborder + fraction)) + StartingRow; //+ (numPixShiftDown / numWellWide * rr * 10);
      int SC = (int)((cc * dimension + c) * (wellborder + fraction)) + StartingCol; //(numPixShiftRight / numWellHigh * cc * 10) + StartingCol;
      SR = SR + (int)((((double)(SC ) )/ image[0].length) * numPixShiftDown);
      SC = SC + (int)((((double)(SR ) )/ image.length) * numPixShiftRight);
      int ER = SR + wellLength;
      int EC = SC + wellLength;
      
      if((r == 0) && (c == 0))
      {
         for(int Col = SC; Col <= EC + borderLength; Col++)
         {
            image[SR][Col].setColor(Color.RED);
         }
         for(int Row = SR; Row <= ER + borderLength; Row++)
         {
            image[Row][SC].setColor(Color.RED);
         }
      } 
      else if(r == 0)
      {
         for(int Col = SC; Col <= EC + borderLength; Col++)
         {
            image[SR][Col].setColor(Color.RED);
         }
      }
      else if(c == 0)
      {
         for(int Row = SR; Row <= ER + borderLength; Row++)
         {
            image[Row][SC].setColor(Color.RED);
         }
      }
      
      if(rr == 0 && cc == 0)
      {
         for(int Row = SR; Row <= ER; Row += 1)
         {
            for(int Col = SC; Col <= EC; Col += 1)
            {
               if(Row == SR || Row == ER || Col == SC || Col == EC){
                  Color nice = new Color(image[Row][Col].getRed()/2, (image[Row][Col].getGreen()+255)/2,image[Row][Col].getBlue()/2);
                  image[Row][Col].setColor(nice);
               }
            }
         }
      } 
   }
} 