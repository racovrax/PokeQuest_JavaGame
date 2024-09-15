package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    // Metoda pentru redimensionarea unei imagini
    public static BufferedImage scaleImage(BufferedImage img, int width, int height){
        // Se creează o nouă imagine cu dimensiunile dorite
        BufferedImage newImage = new BufferedImage(width, height, img.getType());
        // Se obține un obiect Graphics2D pentru noua imagine
        Graphics2D g = newImage.createGraphics();
        // Se desenează imaginea originală redimensionată în noua imagine
        g.drawImage(img, 0, 0, width, height, null);
        // Se eliberează resursele obiectului Graphics2D
        g.dispose();

        // Se returnează noua imagine redimensionată
        return newImage;
    }
}
