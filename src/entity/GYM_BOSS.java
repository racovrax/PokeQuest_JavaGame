package entity;

import Main.GamePanel;
import battle.BattleState;

import java.util.Random;

public class GYM_BOSS extends Entity {
    public boolean eventTriggered = false;
    private boolean defeated = false;

    public GYM_BOSS(GamePanel gp) {
        super(gp);
        maxLife = 300;

        speed = 2;
        direction = "down";

        getPlayerImage();
    }

    public void getPlayerImage() {
       down1 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
         down2 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
       up1 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
       up2 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
       left1 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
         left2 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
       right1 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);
         right2 = setup("/gym_goon/geodude",gp.tileSize*3,gp.tileSize*3);

    }

    public String getDialog() {
        return "You are not ready to face me yet!";
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
        gp.battle.setBossPokemon("pokemons/front/74.png");
        gp.battle.resetBattleConditions();
        gp.gameState = gp.transitionBattle;
        gp.battle.startBattle();
    }




}
