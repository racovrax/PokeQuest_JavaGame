package entity;

import Main.CollisionChecker;
import Main.GamePanel;
import Main.KeyHandler;
import Main.UtilityTool;
import battle.Ability;
import database.DatabaseHelper;
import object.OBJ_Pokeball;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    private final List<Pokemon> pokemons;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int inventorySize = 20;


    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 22;
        solidArea.height = 22;

        pokemons = new ArrayList<>();
        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 49;
        worldY = gp.tileSize *44 ;
        speed = 4;
        direction = "down";

        pokeball = new OBJ_Pokeball(gp);

        maxLife = 6;
        life = maxLife;



    }


    public boolean isAlive(){
        return life > 0;
    }

    public void setItems(){
        inventory.add(pokeball);

    }

    public void getPlayerImage() {
        up1 = setup("/player/up1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/up2", gp.tileSize, gp.tileSize);
        up3 = setup("/player/up3", gp.tileSize, gp.tileSize);
        up4 = setup("/player/up4", gp.tileSize, gp.tileSize);
        down1 = setup("/player/down1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/down2", gp.tileSize, gp.tileSize);
        down3 = setup("/player/down3", gp.tileSize, gp.tileSize);
        down4 = setup("/player/down4", gp.tileSize, gp.tileSize);
        left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/left2",gp.tileSize, gp.tileSize);
        left3 = setup("/player/left3", gp.tileSize, gp.tileSize);
        left4 = setup("/player/left4", gp.tileSize, gp.tileSize);
        right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
        right3 = setup("/player/right3", gp.tileSize, gp.tileSize);
        right4 = setup("/player/right4",gp.tileSize, gp.tileSize);

    }

    public void update() {
        int currentSpeed = speed;
        if (keyH.shift) {
            currentSpeed = speed + 2;
        }
        if (keyH.up || keyH.down || keyH.left || keyH.right) {
            if (keyH.up) {
                direction = "up";
            }
            if (keyH.down) {
                direction = "down";
            }
            if (keyH.left) {
                direction = "left";
            }
            if (keyH.right) {
                direction = "right";
            }

            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            int objectIndex = gp.collisionChecker.checkObject(this, true);
            pickUpItem(objectIndex);

            int npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int gymGoonIndex = gp.collisionChecker.checkEntity(this, gp.gym_goon);
            interactNPC(gymGoonIndex);

            int gymBossIndex = gp.collisionChecker.checkEntity(this, gp.gym_boss);
            interactNPC(gymBossIndex);


            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= currentSpeed;
                        break;
                    case "down":
                        worldY += currentSpeed;
                        break;
                    case "left":
                        worldX -= currentSpeed;
                        break;
                    case "right":
                        worldX += currentSpeed;
                        break;
                }
            }

            gp.eventHandler.checkEvent();

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 4;
                } else if (spriteNum == 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2) {
        /*g2.setColor(Color.white);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);*/
        BufferedImage img = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1)
                    img = up1;
                else if (spriteNum == 2)
                    img = up2;
                else if (spriteNum == 3)
                    img = up3;
                else if (spriteNum == 4)
                    img = up4;
                break;
            case "down":
                if (spriteNum == 1)
                    img = down1;
                else if (spriteNum == 2)
                    img = down2;
                else if (spriteNum == 3)
                    img = down3;
                else if (spriteNum == 4)
                    img = down4;
                break;
            case "left":
                if (spriteNum == 1)
                    img = left1;
                else if (spriteNum == 2)
                    img = left2;
                else if (spriteNum == 3)
                    img = left3;
                else if (spriteNum == 4)
                    img = left4;
                break;
            case "right":
                if (spriteNum == 1)
                    img = right1;
                else if (spriteNum == 2)
                    img = right2;
                else if (spriteNum == 3)
                    img = right3;
                else if (spriteNum == 4)
                    img = right4;
                break;
        }
        g2.drawImage(img, screenX, screenY, null);

    }

    public void pickUpItem(int objectIndex) {
        if (objectIndex != 999) {
            gp.pokeballs++;
            gp.object[objectIndex] = null;
            System.out.println("Pokeballs: " + gp.pokeballs);
        }
    }

    public void interactNPC(int npcIndex) {
        if (npcIndex != 999) {
            if(gp.keyHandler.enter){
            gp.gameState = gp.dialogState;
            gp.npc[gp.currentMap][npcIndex].speak();
        }}
        gp.keyHandler.enter = false;
    }
    public List<Pokemon> getPokemons() {
        return pokemons;
    }
    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public void savePlayerState(){
        DatabaseHelper.savePlayer(1, worldX, worldY, direction);
    }
    public void loadPlayerState(){
        DatabaseHelper.loadPlayer(1, this);
    }
}


