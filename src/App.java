import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class App
{
    public static void main(String[] args)
    {
        BufferedImage img = init();

        //generates a 3d matrix with a rgb array at each row/col
        int[][][] rgbTupleMatrix = generate2dMatrix(img);
        //testing the rgbTupleMatrix
        /*for (int[][] twoDmatrix : rgbTupleMatrix)
            for (int[] oneDmatrix : twoDmatrix)
                System.out.println(Arrays.toString(oneDmatrix)); */

        int[][] brightnessMatrix = generateBrightness(rgbTupleMatrix);
        //testing the generateBrightness method
        /*for (int[] arr : brightnessMatrix)
            for (int brightness : arr)
                System.out.println(brightness);*/

        char[][] characterMatrix = convertBrightnessToASCII(brightnessMatrix);
        //testing that the brightness conversion worked
        /*for (char[] i:characterMatrix)
            for (char j:i)
                System.out.println(j);*/

        //now, print out results
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter(new File("output.txt")));
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
        System.out.println("Image to ASCII successfull! check for a 'output.txt' file to see the results.");
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

    private static int[][][] generate2dMatrix(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][][] rgbMatrix = new int[width][height][3];

        for (int row = 0; row < width; row++)
        {
            for (int col=0; col<height; col++)
            {
                Color c = new Color(img.getRGB(row,col));
                int[] pixelRGBMatrix = {c.getRed(),c.getGreen(),c.getBlue()};
                rgbMatrix[row][col] = pixelRGBMatrix;
            }
        }

        return rgbMatrix;
    }

    private static int[][] generateBrightness(int[][][] mat)
    {
        int[][] result = new int[mat.length][mat[0].length]; //assume its a rectangular image

        for (int i = 0; i < mat.length; i++)
        {
            for (int j = 0; j < mat[i].length; j++)
            {
                int[] rgb = mat[i][j];
                int average = (int)(.21*rgb[0]+.72*rgb[1]+.07*rgb[2]); //formula to determine the brightness of current pixel
                result[i][j] = average;
            }
        }
        return result;
    }

    private static char[][] convertBrightnessToASCII(int[][] matrix)
    {
        char[][] result = new char[matrix.length][matrix[0].length];

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
}
