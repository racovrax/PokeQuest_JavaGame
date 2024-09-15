package Main;

import entity.Entity;
import object.Life;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g;
    Font pixelFont;
    public boolean messageOn = false;
    public String message = "";
       public int commandNum = 0;

       public String currentDialog = "";


    private Image titleScreen;
    BufferedImage life;
    BufferedImage lifeLost;
    BufferedImage lifehalf;
    private JButton start;
    private JButton exit;
    private BufferedImage pokeQuestLogo;
    private final BufferedImage pokeball;

    public String[] dialoguri= {"Halt","Who goes there?","You are not ready to face me yet!"};
    public int dialoguriNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    public int commaNum = 0;
    int c = 0;

    int subState = 0;

    int endGameTextY;
    private int endGameTextSpeed = 2;

    // Constructorul clasei UI
    public UI(GamePanel gp) throws IOException {
        this.gp = gp;
        LoadFont();
        try {
            pokeball = ImageIO.read(getClass().getResource("/images/pokeball_icon.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Entity orb = new Life(gp);
        life = orb.image;
        lifeLost = orb.image2;
        lifehalf = orb.image3;
    }

    public void LoadFont(){
        try (InputStream is = getClass().getResourceAsStream("/font/PixelFont.ttf")) {
            assert is != null;
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f); // Set font size to 20f
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }}

    // Metoda draw care desenează interfața utilizatorului
    public void draw(Graphics2D g) throws IOException {
        this.g = g;

        g.setFont(pixelFont);
        g.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen(); // Desenează ecranul titlului
        }

        if (gp.gameState == gp.playState) {
            drawGameUI(); // Desenează interfața utilizatorului în timpul jocului
            drawLife();
        }
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen(); // Desenează ecranul de pauză
            drawLife();
        }
        if(gp.gameState== gp.dialogState){
            drawDialog(); // Desenează ecranul de dialog
            drawLife();
        }
        if(gp.gameState== gp.inventoryState){
            drawInventory(g); // Desenează ecranul de inventar
            drawLife();
        }
        if(gp.gameState == gp.optionsState){
            drawOptionsScreen();
            drawLife();
        }
        if(gp.gameState == gp.transitionState){
            drawTransitionScreen();
        }
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        if(gp.gameState == gp.selectionState){
            drawChoosePokemonScreen();
        }
        if(gp.gameState == gp.endGameState){
            drawEndGameScreen();
        }
    }

    public void drawLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // Desenează inimioarele de viață
        while (i < gp.player.maxLife/2) {
            g.drawImage(lifeLost, x, y, gp.tileSize, gp.tileSize, null);
            i++;
            x += gp.tileSize;

        }
        //reset x and y
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;


       //deseneaza viata curenta
        while(i<gp.player.life){
            g.drawImage(lifehalf,x,y,null);
            i++;
            if(i<gp.player.life){
                g.drawImage(life,x,y,gp.tileSize,gp.tileSize,null);
            }
            i++;
            x+=gp.tileSize;


        }

    }
    public void drawEndGameScreen(){

    g.setColor(Color.BLACK);
        g.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(pixelFont);
    String message = "THE END?";
    int x = getXCenteredString(message);
    int y = endGameTextY;
        g.drawString(message, x, y);

    message = "You have completed the game!";
    x = getXCenteredString(message);
    y += gp.tileSize * 2;
        g.drawString(message, x, y);

    message = "Thank you for playing!";
    x = getXCenteredString(message);
    y += gp.tileSize * 2;
        g.drawString(message, x, y);

        message = "To be continued...";
        x = getXCenteredString(message);
        y += gp.tileSize * 2;
        g.drawString(message, x, y);

    // Move the text upward
    endGameTextY -= endGameTextSpeed;

    // Reset text position when it goes off-screen
        if (endGameTextY < -gp.tileSize * 6) {
        endGameTextY = gp.getHeight();
    }

    // Desenarea meniului de Game Over
        g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));

    message = "EXIT";
    x = getXCenteredString(message);
    y += gp.tileSize * 2;
        g.drawString(message, x, y);
        g.drawString(".", x - gp.tileSize, y);

}

    public void drawTransitionScreenFight(Graphics2D g2) {
        String text = "A new foe has appeared!";
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        int x = getXCenteredString(text);
        int y = gp.screenHeight / 2;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }




    // Metoda care desenează interfața utilizatorului în timpul jocului
    public void drawDialog() {
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.getWidth()-(gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g.setFont(pixelFont);
        x+= gp.tileSize;
        y+= gp.tileSize;
        if (dialoguriNum < dialoguri.length) {
            String line = dialoguri[dialoguriNum];
            g.drawString(line, x, y);
        }

    }
    public void drawTransitionScreen(){
        c++;
        g.setColor(new Color(0,0,0,c*5));
        g.fillRect(0,0,gp.getWidth(),gp.getHeight());

        if(c ==50) {
            c = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eventHandler.tempMap;
            gp.player.worldX = gp.eventHandler.tempCol * gp.tileSize;
            gp.player.worldY = gp.eventHandler.tempRow * gp.tileSize;
            gp.eventHandler.previousEventX = gp.player.worldX;
            gp.eventHandler.previousEventY = gp.player.worldY;
        }

    }

    public void drawSubWindow(int x,int y,int width,int height){
        Color c = new Color(0,0,0,220);
        g.setColor(c);
        g.fillRoundRect(x,y,width,height,40,40);

        c = new Color(255,255,255);
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect(x+5,y+5,width-10,height-10,30,30);

    }
    private void drawGameUI() {

        int pokeballx = gp.getWidth() - 100;
        int pokebally = 10;
        int imagesize = 24;
        g.drawImage(pokeball, pokeballx, pokebally, imagesize, imagesize, null); // Desenează iconița Pokeball

        String pokeballCount = String.valueOf(gp.pokeballs);
        g.drawString(pokeballCount, pokeballx + imagesize + 5, pokebally + imagesize - 5); // Desenează numărul de Pokebile

    }

    // Metoda care desenează ecranul titlului
    public void drawTitleScreen() {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        // Desenarea titlului "PokeQuest"
        g.setFont(g.getFont().deriveFont(Font.BOLD, 96f));
        String message = "PokeQuest";
        int x = getXCenteredString(message);
        int y = gp.tileSize * 3;
        g.setColor(Color.gray);
        g.drawString(message, x + 5, y + 5);
        g.setColor(Color.white);
        g.drawString(message, x, y);

        // Desenarea logoului PokeQuest
        try {
            pokeQuestLogo = ImageIO.read(getClass().getResource("/images/logo-removebg-preview (1).png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int logoWidth = gp.tileSize * 4;
        int logoHeight = gp.tileSize * 4;
        int logoX = (gp.getWidth() - logoWidth) / 2;
        int logoY = y + gp.tileSize * 4;
        g.drawImage(pokeQuestLogo, logoX, logoY, logoWidth, logoHeight, null);

        // Desenarea meniului
        g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));

        message = "NEW GAME";
        x = getXCenteredString(message);
        y += gp.tileSize * 2;
        g.drawString(message, x, y);
        if (commandNum == 0) {
            g.drawString(".", x - gp.tileSize, y);
        }

        message = "LOAD GAME";
        x = getXCenteredString(message);
        y += gp.tileSize;
        g.drawString(message, x, y);
        if (commandNum == 1) {
            g.drawString(".", x - gp.tileSize, y);
        }

        message = "EXIT";
        x = getXCenteredString(message);
        y += gp.tileSize;
        g.drawString(message, x, y);
        if (commandNum == 2) {
            g.drawString(".", x - gp.tileSize, y);
        }
    }

    // Metoda care desenează ecranul de pauză
    public void drawPauseScreen() {
        g.setFont(pixelFont);
        String message = "Paused";
        int x = getXCenteredString(message);
        int y = gp.getHeight() / 2;
        g.drawString(message, x, y);
    }

    // Metoda care returnează coordonata x pentru a centra un șir de caractere pe ecran
    public int getXCenteredString(String message) {
        int x;
        int length = (int) g.getFontMetrics().getStringBounds(message, g).getWidth();
        x = gp.getWidth() / 2 - length / 2;
        return x;
    }

    public void drawGameOverScreen() {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(pixelFont);
        String message = "Game Over";
        int x = getXCenteredString(message);
        int y = gp.getHeight() / 2;
        g.drawString(message, x, y);

        // Desenarea meniului de Game Over
        g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));

        message = "RETRY";
        x = getXCenteredString(message);
        y += gp.tileSize * 2;
        g.drawString(message, x, y);
        if (commandNum == 0) {
            g.drawString(".", x - gp.tileSize, y);
        }

        message = "EXIT";
        x = getXCenteredString(message);
        y += gp.tileSize;
        g.drawString(message, x, y);
        if (commandNum == 1) {
            g.drawString(".", x - gp.tileSize, y);
        }
    }




    public void drawInventory(Graphics2D g){
        int frameX = gp.tileSize*12;
        int frameY =gp.tileSize*2;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        final int slotXstart = frameX+20;
        final int slotYstart = frameY+20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;

        for(int i = 0;i<gp.player.inventory.size();i++){
            g.drawImage(gp.player.inventory.get(i).down1,slotX,slotY,gp.tileSize,gp.tileSize,null);

            slotX+=slotSize;

           if(i==4||i==9||i==14){
               slotX = slotXstart;
               slotY+=slotSize;
           }


        }

        int cursorX = slotXstart + (slotSize*slotCol);
        int cursorY = slotYstart + (slotSize*slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        this.g.setColor(Color.white);
        this.g.setStroke(new BasicStroke(3));
        this.g.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;

        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g.setFont(pixelFont);
        g.setColor(Color.white);

        int itemIndex =getItemIndexOnSlot();
        if(itemIndex<gp.player.inventory.size()){
            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

            for(String line:gp.player.inventory.get(itemIndex).description.split("\n")){
                g.drawString(line,textX,textY);
                textY+=32;
            }
        }

    }
    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawOptionsScreen() throws IOException {
        g.setColor(Color.BLACK);
        g.setFont(pixelFont);

        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0:
                options_top(frameX, frameY);
                break;
            case 1:
                options_fullScreen(frameX, frameY);
                break;
            case 2:
                options_control(frameX, frameY);
                break;
            case 3:
                options_endGame(frameX, frameY); break;
        }

        gp.keyHandler.enter = false;
    }

    public void options_top(int frameX, int frameY) throws IOException {
        int textX;
        int textY;

        String text = "Options";
        textX = getXCenteredString(text);
        textY = frameY + gp.tileSize;
        g.drawString(text, textX, textY);

        // Full Screen
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g.drawString("Full Screen", textX, textY);
        if (commaNum == 0) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                if (!gp.fullScreenOn) {
                    gp.fullScreenOn = true;
                } else {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        // Music
        textY += gp.tileSize;
        g.drawString("Music", textX, textY);
        if (commaNum == 1) {
            g.drawString("-", textX - 25, textY);
        }

        // Sound
        textY += gp.tileSize;
        g.drawString("Sound", textX, textY);
        if (commaNum == 2) {
            g.drawString("-", textX - 25, textY);
        }

        // Control
        textY += gp.tileSize;
        g.drawString("Control", textX, textY);
        if (commaNum == 3) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                subState = 2;
                commaNum = 0;
            }
        }

        // Exit
        textY += gp.tileSize;
        g.drawString("Exit", textX, textY);
        if (commaNum == 4) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                subState = 3;
                commaNum = 0;
            }
        }


        // Back
        textY += (int) (gp.tileSize * 1.5);
        g.drawString("Back", textX, textY);
        if (commaNum == 5) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                gp.gameState = gp.playState;
            }
        }



        textX = (int) (frameX + (gp.tileSize * 4.5));
        textY = frameY + gp.tileSize * 2 + 28;
        g.setStroke(new BasicStroke(3));
        g.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn) {
            g.fillRect(textX, textY, 24, 24);
        }

        textY += gp.tileSize;
        g.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24* gp.music.volumeScale;
        g.fillRect(textX, textY, volumeWidth, 24);

        textY += gp.tileSize;
        g.drawRect(textX, textY, 120, 24);
        volumeWidth = 24* gp.se.volumeScale;
        g.fillRect(textX, textY, volumeWidth, 24);

        gp.config.saveConfig();
    }

    public void options_endGame(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialog = "Are you sure you want to \nend the game?";

        for (String line : currentDialog.split("\n")) {
            g.drawString(line, textX, textY);
            textY += 40;
        }

        //Yes

        String text = "Yes";
        textX = getXCenteredString(text);
        textY += gp.tileSize * 3;
        g.drawString(text, textX, textY);
        if(commaNum == 0){
            g.drawString("-", textX - 25, textY);
            if(gp.keyHandler.enter){
                subState = 0;
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.playMusic(1);
            }
        }

        //no

        text = "No";
        textX = getXCenteredString(text);
        textY += gp.tileSize;
        g.drawString(text, textX, textY);
        if(commaNum == 1){
            g.drawString("-", textX - 25, textY);
            if(gp.keyHandler.enter){
                subState = 0;
                commaNum = 4;
            }
        }


    }

    public void options_control(int frameX, int frameY){
        int textX;
        int textY;

        String text = "Control";
        textX = getXCenteredString(text);
        textY = frameY + gp.tileSize;

        g.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize ;
        g.drawString("Up", textX, textY); textY += gp.tileSize;
        g.drawString("Down", textX, textY); textY += gp.tileSize;
        g.drawString("Left", textX, textY); textY += gp.tileSize;
        g.drawString("Right", textX, textY); textY += gp.tileSize;
        g.drawString("Inventory", textX, textY); textY += gp.tileSize;
        g.drawString("Pause", textX, textY); textY += gp.tileSize;
        g.drawString("Options", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;

        g.drawString("W", textX, textY); textY += gp.tileSize;
        g.drawString("S", textX, textY); textY += gp.tileSize;
        g.drawString("A", textX, textY); textY += gp.tileSize;
        g.drawString("D", textX, textY); textY += gp.tileSize;
        g.drawString("I", textX, textY); textY += gp.tileSize;
        g.drawString("P", textX, textY); textY += gp.tileSize;
        g.drawString("ESC", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g.drawString("Back", textX, textY);
        if (commaNum == 0) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                subState = 0;
                commaNum = 3;
            }
        }


    }

    public void options_fullScreen(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialog = "The change will take \neffect after restarting \nthe game.";

        for (String line : currentDialog.split("\n")) {
            g.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 9;
        g.drawString("Back", textX, textY);
        if (commaNum == 0) {
            g.drawString("-", textX - 25, textY);
            if (gp.keyHandler.enter) {
                subState = 0;
            }
        }
    }

    public void drawChoosePokemonScreen(){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,gp.getWidth(),gp.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(pixelFont);
        String message = "Choose your Pokemon!";
        int x = getXCenteredString(message);
        int y = gp.getHeight()/4;
        g.drawString(message,x,y);


        g.setFont(g.getFont().deriveFont(Font.BOLD,48f));

        message = "Bulbasaur";
        x = getXCenteredString(message);
        y += gp.tileSize*2;
        g.drawString(message,x,y);
        if(commandNum == 0){
            g.drawString(".",x-gp.tileSize,y);
        }

        message = "Charmander";
        x = getXCenteredString(message);
        y += gp.tileSize;
        g.drawString(message,x,y);
        if(commandNum == 1){
            g.drawString(".",x-gp.tileSize,y);
        }

        message = "Squirtle";
        x = getXCenteredString(message);
        y += gp.tileSize;
        g.drawString(message,x,y);
        if(commandNum == 2){
            g.drawString(".",x-gp.tileSize,y);
        }


        }
}
