package Main;

import battle.AttackType;
import entity.Pokemon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean up, down, left, right, shift, enter, space;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Play state
        if (gp.gameState == gp.playState) {
            handlePlayState(keyCode);
        }

        // Pause state
        else if (gp.gameState == gp.pauseState) {
            handlePauseState(keyCode);
        }

        // Dialog state
        else if (gp.gameState == gp.dialogState) {
            handleDialogState(keyCode);
        }

        // Inventory state
        else if (gp.gameState == gp.inventoryState) {
            handleInventoryState(keyCode);
        }

        // Options state
        else if (gp.gameState == gp.optionsState) {
            handleOptionsState(keyCode);
        }

        // Title state
        else if (gp.gameState == gp.titleState) {
            handleTitleState(keyCode);
        }

        // Battle state
        if (gp.gameState == gp.battleState) {
            handleBattleState(keyCode);
        }
        if(gp.gameState == gp.gameOverState){
            handleGameOverState(keyCode);
        }
        if(gp.gameState == gp.selectionState){
            handleChoosePokemon(keyCode,e);
        }
        if(gp.gameState == gp.endGameState){
            handleEndGame(keyCode);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            up = false;
        }
        if (code == KeyEvent.VK_A) {
            left = false;
        }
        if (code == KeyEvent.VK_S) {
            down = false;
        }
        if (code == KeyEvent.VK_D) {
            right = false;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shift = false;
        }
    }

    private void handlePlayState(int keyCode) {
        if (keyCode == KeyEvent.VK_W) {
            up = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            left = true;
        }
        if (keyCode == KeyEvent.VK_S) {
            down = true;
        }
        if (keyCode == KeyEvent.VK_D) {
            right = true;
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shift = true;
        }
        if (keyCode == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = true;
        }
        if (keyCode == KeyEvent.VK_I) {
            gp.gameState = gp.inventoryState;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
        }
    }

    private void handlePauseState(int keyCode) {
        if (keyCode == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    private void handleDialogState(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            if (gp.ui.dialoguriNum < gp.ui.dialoguri.length - 1) {
                gp.ui.dialoguriNum++; // Move to the next dialog line
            } else {
                gp.ui.dialoguriNum = 0; // Reset dialog index
                gp.gameState = gp.battleState;
                gp.music.stop();
                gp.playMusic(2);
            }
        }
    }

    private void handleInventoryState(int keyCode) {
        if (keyCode == KeyEvent.VK_I) {
            gp.gameState = gp.playState;
        }
        if (keyCode == KeyEvent.VK_W) {
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
            }
        }
        if (keyCode == KeyEvent.VK_A) {
            if (gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            if (gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
            }
        }
        if (keyCode == KeyEvent.VK_D) {
            if (gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
            }
        }
    }

    private void handleOptionsState(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter = true;
        }

        int maxCommNum = switch (gp.ui.subState) {
            case 0 -> 6;
            case 3 -> 1;
            default -> 0;
        };

        if (keyCode == KeyEvent.VK_W) {
            gp.ui.commaNum--;
            if (gp.ui.commaNum < 0) {
                gp.ui.commaNum = maxCommNum;
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            gp.ui.commaNum++;
            if (gp.ui.commaNum > maxCommNum) {
                gp.ui.commaNum = 0;
            }
        }
        if (keyCode == KeyEvent.VK_A) {
            if (gp.ui.subState == 0) {
                if (gp.ui.commaNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                } else if (gp.ui.commaNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.se.checkVolume();
                }
            }
        }
        if (keyCode == KeyEvent.VK_D) {
            if (gp.ui.subState == 0) {
                if (gp.ui.commaNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                } else if (gp.ui.commaNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.se.checkVolume();
                }
            }
        }
    }

    private void handleChoosePokemon(int keyCode, KeyEvent e) {
            if (keyCode == KeyEvent.VK_1) {
                gp.battle.playerPokemon = new Pokemon("Bulbasaur", 50, "pokemons/back/1.png");
                gp.gameState = gp.playState;
            } else if (keyCode == KeyEvent.VK_2) {
                gp.battle.playerPokemon = new Pokemon("Charmander", 50, "pokemons/back/4.png");
                gp.gameState = gp.playState;
            } else if (keyCode == KeyEvent.VK_3) {
                gp.battle.playerPokemon = new Pokemon("Squirtle", 50, "pokemons/back/7.png");
                gp.gameState = gp.playState;
            }

        }

    private void handleTitleState(int keyCode) {
        if (keyCode == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
                gp.playSE(6);
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
                gp.playSE(6);
            }
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.stopMusic();
                gp.playMusic(0);
            }
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.optionsState;
            }
            if (gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }
    }

    private void handleGameOverState(int keyCode){
        if (keyCode == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
                gp.playSE(6);
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
                gp.playSE(6);
            }
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.player.life = 6;
                gp.stopMusic();
                gp.playMusic(0);
            }
            if (gp.ui.commandNum == 1) {
                System.exit(0);
            }
        }
    }

    private void handleBattleState(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            gp.battle.endBattle("SKIPPED");
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            if (gp.battle.subState == 0) {
                if (gp.battle.command == 0) {
                    gp.battle.subState = 1;
                    gp.battle.command = 0;
                } else {
                    executeCommand();
                }
            } else if (gp.battle.subState == 1) {
                executeAttackCommand();
                gp.battle.subState = 0;
            }
        }

        if (keyCode == KeyEvent.VK_W) {
            gp.battle.command--;
            if (gp.battle.command < 0) {
                gp.battle.command = 3;
                gp.playSE(6);
            }
        }

        if (keyCode == KeyEvent.VK_S) {
            gp.battle.command++;
            if (gp.battle.command > 3) {
                gp.battle.command = 0;
                gp.playSE(6);
            }
        }

        if (keyCode == KeyEvent.VK_A && gp.battle.subState == 0) {
            if (gp.battle.command % 2 == 1) {
                gp.battle.command--;
            }
        }

        if (keyCode == KeyEvent.VK_D && gp.battle.subState == 0) {
            if (gp.battle.command % 2 == 0) {
                gp.battle.command++;
            }
        }
    }
    private void handleEndGame(int keyCode){ {
        if(keyCode == KeyEvent.VK_ENTER){
            System.exit(0);
        }
    }
      }

    private void executeCommand() {
        switch (gp.battle.command) {
            case 0 -> gp.battle.subState = 1; // Intră în sub-starea de atacuri
            case 1 -> System.out.println("Bag selected");
            case 2 -> System.out.println("Pokemon selected");
            case 3 -> gp.battle.attemptToFlee(gp.tileSize * 2, gp.screenHeight - gp.tileSize * 4);
        }
    }

    private void executeAttackCommand() {
        switch (gp.battle.command) {
            case 0 -> gp.battle.playerAttack(AttackType.BASIC_ATTACK);
            case 1 -> gp.battle.playerAttack(AttackType.SPECIAL_ATTACK);
            case 2 -> gp.battle.playerAttack(AttackType.ULTIMATE_ATTACK);
            case 3 -> gp.battle.playerAttack(AttackType.KICK);
        }
        gp.battle.subState = 0; // Revine la meniul principal după atac
    }


}
