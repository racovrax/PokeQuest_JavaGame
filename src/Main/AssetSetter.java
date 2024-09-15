
package Main;
import entity.GYM_BOSS;
import entity.GYM_GOON;
import entity.NPC;
import object.OBJ_Pokeball;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        int mapNum = 0;
        int i = 0;
        /*OBJ_Pokeball pokeball = new OBJ_Pokeball(gp);
        pokeball.worldX = 50*gp.tileSize;
        pokeball.worldY = 50*gp.tileSize;
        gp.object[mapNum][i] = pokeball;
        i++;


        gp.object[mapNum][i] = new OBJ_Pokeball(gp);
        gp.object[mapNum][i].worldX = 54*gp.tileSize;
        gp.object[mapNum][i].worldY = 56*gp.tileSize;
        i++;*/


    }

    public void setNPC(){
        int mapNum = 0;
        int i = 0;
        mapNum = 2;
        gp.npc[mapNum][i] = new NPC(gp,"npc");
        gp.npc[mapNum][i].worldX = 28*gp.tileSize;
        gp.npc[mapNum][i].worldY = 24*gp.tileSize;
        i++;


    }

    public void setGYM_GOON() {
        int mapNum = 0;
        int i = 0;
        mapNum = 1;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 28*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 17*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 20*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 13*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 13*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 21*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 18*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 28*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 22*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 20*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 23*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 27*gp.tileSize;
        i++;

        mapNum = 4;

        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 31*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 62*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 37*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 72*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 27*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 44*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 41*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 49*gp.tileSize;
        i++;
        gp.gym_goon[mapNum][i] = new GYM_GOON(gp);
        gp.gym_goon[mapNum][i].worldX = 43*gp.tileSize;
        gp.gym_goon[mapNum][i].worldY = 41*gp.tileSize;
        i++;

    }

    public void setGYM_BOSS(){
        int mapNum = 0;
        int i = 0;
        mapNum = 1;
        gp.gym_boss[mapNum][i] = new GYM_BOSS(gp);
        gp.gym_boss[mapNum][i].worldX = 13*gp.tileSize;
        gp.gym_boss[mapNum][i].worldY = 14*gp.tileSize;
        i++;
    }
}
