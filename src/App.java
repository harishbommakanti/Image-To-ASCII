import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class App
{
    private static int width;
    private static int height;
    public static void main(String[] args)
    {
        BufferedImage img = init();
        width = img.getWidth();
        height = img.getHeight();
        //System.out.println(width + " " + height);

        //generates a 3d matrix with a rgb array at each row/col
        String[][] rgbMatrix = generateRGBMatrix(img);
        //testing the rgbTupleMatrix
        /*for (String[] arr : rgbMatrix)
            for (String str : arr)
                System.out.println(str);*/

        int[][] brightnessMatrix = generateBrightness(rgbMatrix);
        //testing the generateBrightness method
        /*for (int[] arr : brightnessMatrix)
            for (int brightness : arr)
                System.out.println(brightness);*/

        char[][] characterMatrix = convertBrightnessToASCII(brightnessMatrix);
        //testing that the brightness conversion worked
        /*for (char[] i:characterMatrix)
            for (char j:i)
                System.out.println(j);*/

        //printOut("images/beforeRotate.txt",characterMatrix);

        //now, for some reason, need to flip 90 degrees clockwise and rotate horizontally
        characterMatrix = rotate90degCW(characterMatrix);

        //now, print out results
        printOut("images/afterRotate.txt", characterMatrix);
    }

    private static BufferedImage init()
    {
        Scanner s = new Scanner(System.in);
        File file=null;
        String filename="";
        BufferedImage image=null;

        //prompts for directory name until its non null
        while (image == null)
        {
            System.out.println("Enter the directory of the file you wish to convert to ASCII.");
            filename = s.nextLine().trim();
            file = new File(filename);
            try{
                image = ImageIO.read(file);
            } catch (IOException e)
            {
                System.out.println("File directory doesn't exist. Make sure its the right directory and you include the appropriate suffix (.jpg...)");
            }
        }

        return image;
    }

    private static String[][] generateRGBMatrix(BufferedImage img)
    {
        String[][] rgbMatrix = new String[width][height];
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                Color c = new Color(img.getRGB(w,h));
                String colorString = c.getRed()+" "+c.getGreen()+" "+c.getBlue();
                rgbMatrix[w][h] = colorString;
            }
        }

        return rgbMatrix;
    }

    private static int[][] generateBrightness(String[][] mat)
    {
        int[][] result = new int[width][height]; //assume its a rectangular image

        for (int i = 0; i < mat.length; i++)
        {
            for (int j = 0; j < mat[i].length; j++)
            {
                String rgb = mat[i][j];
                String[] rgbVals = rgb.split(" ");
                int average = (int)(.21*Integer.parseInt(rgbVals[0])+.72*Integer.parseInt(rgbVals[1])+.07*Integer.parseInt(rgbVals[2])); //formula to determine the brightness of current pixel
                result[i][j] = average;
            }
        }
        return result;
    }

    private static char[][] convertBrightnessToASCII(int[][] matrix)
    {
        char[][] result = new char[width][height];

        String characterArray = "`^\\\",:;Il!i~+_-?][}{1)(|\\\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$"; //grabbed from internet to simulate brightness
        for (int i=0;i<matrix.length;i++)
        {
            for (int j=0;j<matrix[0].length;j++)
            {
                //j is the brightness at the current pixel, need to convert that to an index for the character in the characterArray string

                //0 should map to index of 0, 255 should map to index 67, 128 should map to 35. fraction = index/67, fractions should match up
                double fraction = (double)matrix[i][j]/255;
                //fraction = index/67, fractions should match up
                int index = (int)(fraction*67);
                result[i][j] = characterArray.charAt(index);
            }
        }
        return result;
    }

    private static char[][] rotate90degCW(char[][] matrix)
    {
        char[][] result = new char[height][width];

        for (int w = 0; w < width; w++)
        {
            for (int h = 0; h < height; h++)
            {
                result[h][width-w-1]=matrix[w][h];
            }
        }

        return result;
    }

    private static void printOut(String filename, char[][] characterMatrix)
    {
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter(new File(filename)));
        } catch (IOException e){}

        for (int i = 0; i < characterMatrix.length; i++)
        {
            for (int j = 0; j < characterMatrix[0].length; j++)
            {
                pw.print(characterMatrix[i][j]);
            }
            pw.println();
        }
        pw.close();
        System.out.printf("Image to ASCII successfull! check for a %s file to see the results.\n",filename);
    }
}
