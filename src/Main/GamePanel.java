package Main;

import battle.BattleState;
import entity.Entity;
import entity.Player;
import entity.Pokemon;
import factory.EntityFactory;

import factory.SimpleEntityFactory;

import tile.TileManager;

import javax.swing.*;
import java.awt.*;

import database.DatabaseHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {
    // Setările ecranului

    final int originalTileSize = 16; // Dimensiunea originală a unui tile (16x16)
    final int scale = 3; // Factorul de scalare (3x)
    public final int tileSize = originalTileSize * scale; // Dimensiunea finală a unui tile (48x48 pixeli)
    public final int maxScreenCol = 20; // Numărul maxim de coloane de tile-uri pe ecran
    public final int maxScreenRow = 12; // Numărul maxim de rânduri de tile-uri pe ecran
    public final int screenWidth = tileSize * maxScreenCol; // Lățimea ecranului (760 pixeli)
    public int screenHeight = tileSize * maxScreenRow; // Înălțimea ecranului (576 pixeli)

    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    public boolean fullScreenOn = false;
    // Setările lumii
    public int MaxWorldCol = 100;
    public int MaxWorldRow = 100;
    public final int MaxMap = 10;
    public int currentMap = 0;
    public int pokeballs = 0;

    int FPS = 60; // Cadre pe secundă
    public TileManager tm = new TileManager(this); // Managerul de tile-uri
    public KeyHandler keyHandler = new KeyHandler(this); // Handler-ul pentru tastatură
    Sound music = new Sound(); // Sunetul de fundal
    Sound se = new Sound(); // Sunetul de efect
    public UI ui = new UI(this); // Interfața utilizatorului
    public EventHandler eventHandler = new EventHandler(this); // Handler pentru evenimente

    Config config = new Config(this);
    Thread gameThread; // Thread-ul jocului

    public CollisionChecker collisionChecker = new CollisionChecker(this); // Verificatorul de coliziuni
    public AssetSetter assetSetter = new AssetSetter(this); // Setările de resurse
    public Player player = new Player(this, keyHandler); // Jucătorul
    public Entity[][] object = new Entity[MaxMap][10]; // Obiectele din joc
    ArrayList<Entity> entityList = new ArrayList<>();
    public Entity[][] npc = new Entity[MaxMap][10]; // Entitățile din joc
    public Entity[][] gym_goon = new Entity[MaxMap][20];
    public Entity[][] gym_boss = new Entity[MaxMap][10];

    public final EntityFactory entityFactory = new SimpleEntityFactory(this);

    public String playerName = "";
    public boolean choosingName = false;
    public Entity currentEntity;
    //public final ObjectFactory objectFactory;
    // Starea jocului
    public int gameState;
    public final int titleState = 0; // Starea de titlu
    public final int playState = 1; // Starea de joc
    public final int pauseState = 2; // Starea de pauză
    public final int battleState = 3; // Starea de luptă
    public final int dialogState = 4; // Starea de dialog

    public final int optionsState = 5;

    public final int inventoryState = 6;
    public final int gameOverState = 7;
    public final int selectionState = 10;

    public final int transitionState = 8;
    public final int transitionBattle = 9;
    public final int endGameState = 11;
    public BattleState battle;// Starea de luptă
    //public OptionsState options; // Starea de opțiuni

    private JFrame frame;

    // Constructorul clasei
    public GamePanel() throws IOException {
        // Inițializarea panelului de joc
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Inițializarea stării de luptă
        battle = new BattleState(this);

        //options = new OptionsState(this);
        /*objectFactory = new ObjectFactory() {
            @Override
            public SuperObject createPokeball() {
                return null;
            }

            @Override
            public SuperObject createPotion() {
                return null;
            }
        };*/
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    // Metoda pentru inițializarea jocului
    public void setupGame() {
        player = entityFactory.createPlayer();
        npc[currentMap][0] = entityFactory.createNPC();
        gym_goon[currentMap][0] = entityFactory.createGYM_GOON();
        gym_boss[currentMap][0] = entityFactory.createGYM_BOSS();
        //SuperObject pokeball = objectFactory.createPokeball();
        assetSetter.setObject();
        assetSetter.setNPC();
        assetSetter.setGYM_GOON();
        assetSetter.setGYM_BOSS();
        gameState = titleState; // Setarea stării jocului la starea de titlu
        playMusic(1); // Redarea muzicii de fundal

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn) {
            setFullScreen();
        }
    }


    public void setFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    // Metoda pentru pornirea thread-ului jocului
    public void StartGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Logica de actualizare și desenare a jocului în bucla principală a thread-ului
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                try {
                    drawTempScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                drawToScreen();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Metoda pentru actualizarea stării jocului
    public void update() {
        if (gameState == playState) {
            player.update();
            for (Entity c : npc[currentMap]) {
                if (c != null) {
                    c.update();
                }
            }
            for (Entity c : gym_goon[currentMap]) {
                if (c != null) {
                    c.update();
                }
            }

            for (Entity c : gym_boss[currentMap]) {
                if (c != null) {
                    c.update();
                }
            }
            eventHandler.checkForEncounters();
            eventHandler.checkEvent();
        }
        if (gameState == pauseState) {
            // Pauză
        }
        if (gameState == battleState) {
            battle.update();//TBA
        }
        if (gameState == optionsState) {
            //any updates
        }
    }

    public void drawTempScreen() throws IOException {
        if (gameState == titleState) {
            ui.draw(g2); // Desenarea ecranului de titlu
        } else {
            tm.draw(g2); // Desenarea hărții și a tile-urilor
            entityList.add(player);
            for (Entity entity : npc[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }
            for (int i = 0; i < object[1].length; i++) {
                if (object[currentMap][i] != null) {
                    entityList.add(object[currentMap][i]);
                }
            }
            for (Entity entity : gym_goon[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }
            for (Entity entity : gym_boss[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            entityList.sort(Comparator.comparingInt(o -> o.worldY));


            for (Entity entity : entityList) {
                entity.draw(g2);
            }
            entityList.clear();

            ui.draw(g2); // Desenarea interfeței utilizatorului
        }

        if (gameState == battleState) {
            battle.draw(g2); // Desenarea stării de luptă
        }


       /* if(gameState == optionsState){
            removeAll();
            //add(options.getOptionsPanel());
            revalidate();
            repaint();
        }*/
        if (gameState == inventoryState) {
            ui.drawInventory(g2);
        }
        if (gameState == transitionBattle) {
            ui.drawTransitionScreenFight(g2);
        }

    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void goToOptions() {
        gameState = optionsState;
    }

   /* public void returnToGame(){
        gameState = playState;
        remove(options.getOptionsPanel());
        revalidate();
        repaint();
    }*/

    public void toggleFullScreen() {
        if (frame != null) {
            if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }

    // Metoda pentru redarea muzicii de fundal
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    // Metoda pentru oprirea muzicii de fundal
    public void stopMusic() {
        music.stop();
    }

    // Metoda pentru redarea sunetului de efect
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
    /*public void setVolume(int volume) {
        music.setVolume(volume);
        se.setVolume(volume);
    }*/


    private int getPokeballsCount() {
        return pokeballs;
    }

    private int getCurrentMap() {
        return currentMap;
    }

    public void saveGame() {
        // Save player state
        player.savePlayerState();

        // Save NPCs
        for (int i = 0; i < npc[currentMap].length; i++) {
            if (npc[currentMap][i] != null) {
                npc[currentMap][i].saveEntityState(i, "NPC");
            }
        }

        // Save gym goons
        for (int i = 0; i < gym_goon[currentMap].length; i++) {
            if (gym_goon[currentMap][i] != null) {
                gym_goon[currentMap][i].saveEntityState(i, "GYM_GOON");
            }
        }

        // Save gym bosses
        for (int i = 0; i < gym_boss[currentMap].length; i++) {
            if (gym_boss[currentMap][i] != null) {
                gym_boss[currentMap][i].saveEntityState(i, "GYM_BOSS");
            }
        }

        System.out.println("Game saved.");
    }

    public void loadGame() {
        // Load player state
        player.loadPlayerState();

        // Load NPCs
        for (int i = 0; i < npc[currentMap].length; i++) {
            if (npc[currentMap][i] != null) {
                npc[currentMap][i].loadEntityState(i);
            }
        }

        // Load gym goons
        for (int i = 0; i < gym_goon[currentMap].length; i++) {
            if (gym_goon[currentMap][i] != null) {
                gym_goon[currentMap][i].loadEntityState(i);
            }
        }

        // Load gym bosses
        for (int i = 0; i < gym_boss[currentMap].length; i++) {
            if (gym_boss[currentMap][i] != null) {
                gym_boss[currentMap][i].loadEntityState(i);
            }
        }

        System.out.println("Game loaded.");
    }





}