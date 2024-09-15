
package object;
import Main.GamePanel;
import entity.Entity;

public class OBJ_Pokeball extends Entity {
    public OBJ_Pokeball(GamePanel gp){
        super(gp);
        name = "Pokeball";
        direction = "down";
        down1 = setup("/images/pokeball_icon",gp.tileSize,gp.tileSize);
        collision = false;
        description = "A pokeball\nthat can catch\na pokemon";

    }

    @Override
    public String getDialog() {
        return null;
    }
}
