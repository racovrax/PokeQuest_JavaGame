package entity;

import Main.GamePanel;
import Main.UtilityTool;
import database.DatabaseHelper;
import object.OBJ_Pokeball;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {
    public GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public String[] dialoguri;
    protected int dialogIndex = 0;

    public BufferedImage up1,up2,up3,up4,down1,down2,down3,down4,left1,left2,left3,left4,right1,right2,right3,right4;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public int solidAreaDefaultX,solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    int dialoguriNum = 0;
    public BufferedImage image, image2,image3;
    public String name;
    public boolean collision = false;
    public Entity pokeball;
    public String description = "";

    public int maxLife;
    public int life;
    public int currMap;

    public Entity(GamePanel gp){
        this.gp = gp;
        this.solidArea = new Rectangle(0,0,48,48);

    }

    public void setDialogue(String[] dialoguri){
        this.dialoguri = dialoguri;

    }


    public String getDialog() {
        return "";
    }

    public void setAction(){}
    public void speak(){
        if(dialoguri[dialoguriNum] != null){
            dialoguriNum= 0;
        }
        gp.ui.currentDialog = dialoguri[dialoguriNum];
        dialoguriNum++;

        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;

        }
    }

    public void update(){
        setAction();
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkObject(this, false);
        gp.collisionChecker.checkPlayer(this);

        if(!collisionOn){
            switch (direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum ==1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            /*else if(spriteNum == 3){
                spriteNum = 4;
            }
            else if(spriteNum == 4){
                spriteNum = 1;*/
            //}
            spriteCounter = 0;
        }

    }
    public void draw(Graphics2D g2) {

        BufferedImage img = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY) {
            switch (direction){
                case "up":
                    if(spriteNum == 1)
                        img = up1;
                    else if(spriteNum == 2)
                        img = up2;
                    else if(spriteNum == 3)
                        img = up3;
                    else if(spriteNum == 4)
                        img = up4;
                    break;
                case "down":
                    if(spriteNum == 1)
                        img = down1;
                    else if(spriteNum == 2)
                        img = down2;
                    else if(spriteNum == 3)
                        img = down3;
                    else if(spriteNum == 4)
                        img = down4;
                    break;
                case "left":
                    if (spriteNum == 1)
                        img = left1;
                    else if(spriteNum == 2)
                        img = left2;
                    else if(spriteNum == 3)
                        img = left3;
                    else if(spriteNum == 4)
                        img = left4;
                    break;
                case "right":
                    if (spriteNum == 1)
                        img = right1;
                    else if(spriteNum == 2)
                        img = right2;
                    else if(spriteNum == 3)
                        img = right3;
                    else if(spriteNum == 4)
                        img = right4;
                    break;
            }
            g2.drawImage(img, screenX, screenY, null);
        }
    }
    public BufferedImage setup(String imagePath,int width,int height){ {
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage img = null;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath+".png")));
            img = utilityTool.scaleImage(img, width,height);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error: Image not found - " + imagePath);
        }
        return img;
    }}
        public void saveEntityState(int id, String type) {
            DatabaseHelper.saveEntity(id, worldX, worldY, type);
        }

        public void loadEntityState(int id) {
            DatabaseHelper.loadEntity(id, this);
        }



}



