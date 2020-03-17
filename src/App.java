import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App
{
    public static void main(String[] args)
    {
        BufferedImage img = init();
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
}
