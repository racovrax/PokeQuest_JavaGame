package Main;

import battle.BattleState;
import entity.GYM_GOON;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.Random;

public class EventHandler {
    GamePanel gp;
    public int previousEventX, previousEventY;
    EvenRect[][][] eventRect;
    private boolean hasMovedSinceLastEncounter = true;
    private boolean touch = true;
    int tempMap,tempCol,tempRow;
    private boolean selectPokemon = true;
    private boolean endScreenFlag = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EvenRect[gp.MaxMap][gp.MaxWorldRow][gp.MaxWorldCol];
        int col = 0;
        int row = 0;
        int map = 0;
        while (map<gp.MaxMap && row < gp.MaxWorldRow &&col < gp.MaxWorldCol) {
                eventRect[map][row][col] = new EvenRect();
                eventRect[map][row][col].x = 50;
                eventRect[map][row][col].y = 50;
                eventRect[map][row][col].width = 2;
                eventRect[map][row][col].height = 2;
                eventRect[map][row][col].eventRectDefaultX = eventRect[map][row][col].x;
                eventRect[map][row][col].eventRectDefaultY = eventRect[map][row][col].y;
                col++;
                if(col == gp.MaxWorldCol){
                    col = 0;
                    row++;
                }

                if(row == gp.MaxWorldRow){
                    row = 0;
                    map++;
                }
            }

        }


    public void checkEvent() {
        int xDistance = Math.abs((gp.player.worldX - previousEventX));
        int yDistance = Math.abs((gp.player.worldY - previousEventY));
        int distance = Math.max(xDistance, yDistance);

        if(distance > gp.tileSize){
            touch = true;
        }
        if (touch) {
            if (hit(0, 50, 6, "any")) {
                teleport(1,35,32);
                gp.stopMusic();
                gp.playMusic(3);
            }
            else if(hit(0,48,43,"any")){
                teleport(3, 51, 51);
                gp.stopMusic();
                gp.playMusic(4);
            }
            else if(hit(3,24,31,"any")){
                teleport(0,58,44);
                gp.stopMusic();
                gp.playMusic(0);
            }
            else if(hit(1, 14, 13, "any")){
                teleport(4,9,60);
                gp.stopMusic();
                gp.playMusic(8);
            }
            else if(hit(0, 56, 50, "any")){
                teleport(2,25,29);
                gp.stopMusic();
                gp.playMusic(4);

            } else if (hit(2, 25, 28, "any")) {
                teleport(0, 56, 51);
                gp.stopMusic();
                gp.playMusic(0);

            }
            else if(hit(3,46,39,"any") && selectPokemon){
                triggerChoosePokemon();
                selectPokemon = false;
            }
            else if(hit(4,47,39,"any")){
                teleport(5,49,93);
                gp.stopMusic();
                gp.playMusic(7);
            }
            else if ((hit(5, 49, 44, "any") || hit(5, 48, 44, "any") || hit(5, 50, 44, "any")) && endScreenFlag) {
                gp.gameState = gp.endGameState;
                gp.ui.endGameTextY = gp.getHeight(); // Reset text position for animation
            }
        }
    }


    private void triggerChoosePokemon(){
        gp.gameState = gp.selectionState;
        selectPokemon = true;
        gp.stopMusic();
        gp.playMusic(5);
    }

    public boolean hit(int map,int col, int row, String reqDirection) {
        boolean hit = false;
        if(map == gp.currentMap){
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][row][col].x = col * gp.tileSize + eventRect[map][row][col].x;
            eventRect[map][row][col].y = row * gp.tileSize + eventRect[map][row][col].y;

            if (gp.player.solidArea.intersects(eventRect[map][row][col])) {
                if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                    hit = true;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][row][col].x = eventRect[map][row][col].eventRectDefaultX;
            eventRect[map][row][col].y = eventRect[map][row][col].eventRectDefaultY;

        }

        return hit;
    }

    public void teleport(int map,int col, int row) {
        gp.gameState= gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;

        touch = false;
    }

    public void checkForEncounters() {
        int x = gp.player.worldX / gp.tileSize;
        int y = gp.player.worldY / gp.tileSize;
        int tileNum = gp.tm.mapTileNum[gp.currentMap][y][x];
        if (tileNum == 14 && hasMovedSinceLastEncounter) {
            if (Math.random() < 0.6) {  // 60% chance to encounter a wild Pokémon
                triggerBattle();
            }
            hasMovedSinceLastEncounter = false;
        } else if (tileNum != 14) {
            hasMovedSinceLastEncounter = true; // Ensure the player has moved off the high grass tile
        }
    }

    private void triggerBattle() {
        System.out.println("Battle triggered.");
        if (gp.battle == null) {
            gp.battle = new BattleState(gp);
        }
        gp.gameState = gp.battleState; // Change game state to battle state
        gp.battle.startBattle();
    }

    }

