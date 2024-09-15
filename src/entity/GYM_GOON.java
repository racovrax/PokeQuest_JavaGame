package entity;

import Main.GamePanel;

import java.util.Random;

public class GYM_GOON extends Entity {
    public boolean eventTriggered = false;

    public GYM_GOON(GamePanel gp) {
        super(gp);
        speed = 2;
        direction = "down";

        getPlayerImage();
    }

    public void getPlayerImage() {
        up1 = setup("/gym_goon/up1", gp.tileSize , gp.tileSize );
        up2 = setup("/gym_goon/up2", gp.tileSize , gp.tileSize );
        down1 = setup("/gym_goon/down1", gp.tileSize , gp.tileSize );
        down2 = setup("/gym_goon/down2", gp.tileSize , gp.tileSize );
        left1 = setup("/gym_goon/left1", gp.tileSize , gp.tileSize );
        left2 = setup("/gym_goon/left2", gp.tileSize , gp.tileSize );
        right1 = setup("/gym_goon/right1", gp.tileSize , gp.tileSize);
        right2 = setup("/gym_goon/right2", gp.tileSize , gp.tileSize);
    }

    public void setAction() {
        if (!eventTriggered) {
            followPlayer();
        }
        actionLockCounter++;
        if (actionLockCounter == 60) {
            Random random = new Random();
            int i = random.nextInt(10) + 1;
            if (i <= 4) {
                direction = "up";
            } else if (i == 5) {
                direction = "down";
            } else if (i > 5 && i <= 8) {
                direction = "left";
            } else if (i > 8) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    public void followPlayer() {
        if (!eventTriggered) {
            int playerX = gp.player.worldX;
            int playerY = gp.player.worldY;
            if (Math.abs(playerX - worldX) <= gp.tileSize * 4 && Math.abs(playerY - worldY) <= gp.tileSize * 4) {
                if (playerX < worldX) {
                    direction = "left";
                } else if (playerX > worldX) {
                    direction = "right";
                } else if (playerY < worldY) {
                    direction = "up";
                } else if (playerY > worldY) {
                    direction = "down";
                }
                collisionOn = false;
                gp.collisionChecker.checkTile(this);
                if (!collisionOn) {
                    switch (direction) {
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
                if (Math.abs(playerX - worldX) <= gp.tileSize && Math.abs(playerY - worldY) <= gp.tileSize) {
                    startBattle();
                }
            }
        }
    }

    public void startBattle() {
        eventTriggered = true;
        gp.battle.setBattleMessage("Un goon de la sală de sport te-a provocat la luptă!");
        gp.battle.resetBattleConditions();
        gp.gameState = gp.transitionBattle;
        gp.battle.startBattle();
    }

    @Override
    public String getDialog(){
        return "Halt! You shall not pass!";
    }
}
