package object;

import Main.GamePanel;
import Main.UtilityTool;
import com.badlogic.gdx.Game;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Life extends Entity {
    GamePanel gp;
    public Life(GamePanel gp) throws IOException {
        super(gp);
        this.gp = gp;
        name = "life";
        image = ImageIO.read(getClass().getResourceAsStream("/images/life.png"));
        image2 = ImageIO.read(getClass().getResourceAsStream("/images/lifelost.png"));
        image3 = ImageIO.read(getClass().getResourceAsStream("/images/lifehalf.png"));
        image = UtilityTool.scaleImage(image, 48, 48);
        image2 = UtilityTool.scaleImage(image2, 48, 48);
        image3 = UtilityTool.scaleImage(image3, 48, 48);

    }

}
