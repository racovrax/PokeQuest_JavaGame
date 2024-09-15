package battle;

import Main.GamePanel;
import entity.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleState {
    private GamePanel gp;
    Graphics2D g;

    public int subState = 0; // 0 pentru meniul principal, 1 pentru meniul de atacuri

    public int command = 0;
    private String battleMessage = "";
    private List<String> abilityHistory = new ArrayList<>();
    private boolean battleEnded = false;

    private Random random = new Random();
    private boolean playerTurn = true;
    public Pokemon playerPokemon;
    public Pokemon enemyPokemon;
    private Image backgroundImage;
    private String bossPokemon;
    private boolean isBossBattle = false;

    private boolean lifeReduced = false;

    public BattleState(GamePanel gp) {
        this.gp = gp;

        loadBackgroundImage();
        loadRandomPokemon();
    }

    public void startBattle() {
        gp.stopMusic();
        gp.playMusic(2);
        gp.gameState = gp.transitionBattle;
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if(isBossBattle){
                    loadSpecificPokemon(bossPokemon);}
                else{*/
                /*loadRandomPokemon();}*/
                loadRandomPokemon();
                gp.gameState = gp.battleState;
                setupBattleConditions();
                System.out.println("Battle started!");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setBattleMessage(String message) {
        this.battleMessage = message;
    }

    private void setupBattleConditions() {
        playerTurn = true;
        battleMessage = ""; // Reset battle message
        abilityHistory.clear(); // Clear ability history
        battleEnded = false; // Reset battle ended flag
        lifeReduced = false;
    }

    public void update() {
        if (gp.gameState != gp.battleState) {
            return;
        }

        if (!playerTurn && !enemyPokemon.isFainted()) {
            enemyTurn();
        }

        if (enemyPokemon.isFainted()) {
            endBattle("Enemy Pokémon fainted! You win!");
        } else if (playerPokemon.isFainted() && !battleEnded) {
            endBattle("Your Pokémon fainted! You lose!");
        }
    }

    public void playerAttack(AttackType attackType) {
        if (!playerTurn || battleEnded) return;
        enemyPokemon.applyDamage(attackType.getDamage());
        abilityHistory.add("Player used " + attackType.getName() + "!");
        if (enemyPokemon.isFainted()) {
            endBattle("Enemy fainted! You win!");
            return;
        }
        playerTurn = false;
        enemyTurn();
    }

    private void enemyTurn() {
        List<AttackType> attackTypes = List.of(AttackType.values());
        AttackType chosenAttack = attackTypes.get(random.nextInt(attackTypes.size()));
        playerPokemon.applyDamage(chosenAttack.getDamage());
        abilityHistory.add("Enemy used " + chosenAttack.getName() + "!");
        if (playerPokemon.isFainted()) {
            endBattle("Your Pokémon fainted! You lose!");
            return;
        }
        playerTurn = true;
    }

    public void endBattle(String message) {
        if(battleEnded){
            return;
        }
        System.out.println(message);
        battleMessage = message; // Set battle message
        battleEnded = true;// Set battle ended flag

        if(message.contains("You lose!")){
            gp.player.life -=1;
            lifeReduced = true;
            System.out.println(gp.player.life);
        }

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gp.player.life == 0){
                    gp.gameState= gp.gameOverState;
                }else{
                transitionToPlayState();}
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void transitionToPlayState() {
        gp.gameState = gp.playState;
        gp.stopMusic();
        gp.playMusic(0);
    }

    private void cleanUpBattleUI() {
        gp.revalidate();
        gp.repaint();
    }

    public void resetBattleConditions() {
        playerTurn = true;
    }

    public void draw(Graphics2D g2) {
        this.g = g2;
        drawUI(g2);
    }

    private void drawUI(Graphics2D g2) {
        // Draw background image
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }

        // Coordonatele cercurilor verzi (ajustate pentru dimensiunea imaginii)
        int playerCircleX = 128; // Ajustează această valoare dacă e necesar
        int playerCircleY = 500; // Ajustează această valoare dacă e necesar
        int enemyCircleX = 810;  // Ajustează această valoare dacă e necesar
        int enemyCircleY = 200;   // Ajustează această valoare dacă e necesar

        // Draw player Pokémon
        if (playerPokemon != null && playerPokemon.getImage() != null) {
// Draw Pokémon image
            int playerX = playerCircleX - playerPokemon.getImage().getWidth(null) / 2;
            int playerY = playerCircleY - playerPokemon.getImage().getHeight(null) / 2;
            g2.drawImage(playerPokemon.getImage(), playerX, playerY, null);
            // Draw "Player" label above the image
            g2.setColor(Color.BLACK);
            g2.drawString("Player", playerCircleX - 25, playerCircleY - playerPokemon.getImage().getHeight(null) / 2 - 20);
            // Draw health bar
            drawHealthBar(g2, playerPokemon, playerCircleX, playerCircleY + playerPokemon.getImage().getHeight(null) / 2 + 10);

            }

        // Draw enemy Pokémon
        if (enemyPokemon != null && enemyPokemon.getImage() != null) {
            // Draw Pokémon image
            int enemyX = enemyCircleX - enemyPokemon.getImage().getWidth(null) / 2;
            int enemyY = enemyCircleY - enemyPokemon.getImage().getHeight(null) / 2;
            g2.drawImage(enemyPokemon.getImage(), enemyX, enemyY, null);
            // Draw "Enemy" label above the image
            g2.setColor(Color.BLACK);
            g2.drawString("Enemy", enemyCircleX - 25, enemyCircleY - enemyPokemon.getImage().getHeight(null) / 2 - 20);
            // Draw health bar
            drawHealthBar(g2, enemyPokemon, enemyCircleX, enemyCircleY + enemyPokemon.getImage().getHeight(null) / 2 + 10);
        }

        // Draw Battle UI (options and descriptions)
        drawBattleUI(g2);

        // Draw ability history
        drawAbilityHistory(g2);

        // Draw battle message
        drawBattleMessage(g2);
    }

    private void drawHealthBar(Graphics2D g2, Pokemon pokemon, int x, int y) {
        g2.setColor(Color.RED);
        int healthWidth = 100 * pokemon.getCurrentHitpoints() / pokemon.getMaxHitpoints();
        g2.fillRect(x - 50, y, healthWidth, 10);
        g2.setColor(Color.BLACK);
        g2.drawRect(x - 50, y, 100, 10);
    }

    private void loadBackgroundImage() {
        String backgroundPath = "images/BattleGrass.png";
        java.net.URL imgURL = getClass().getClassLoader().getResource(backgroundPath);
        if (imgURL != null) {
            backgroundImage = new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find background image: " + backgroundPath);
        }
    }

    private void loadRandomPokemon() {
        int playerPokemonIndex = random.nextInt(6); // Assuming you have 480 Pokémon images
        int enemyPokemonIndex = random.nextInt(350); // Assuming you have 500 Pokémon images

        String playerImagePath = "pokemons/back/" + playerPokemonIndex + ".png";
        String enemyImagePath = "pokemons/front/" + enemyPokemonIndex + ".png";


        playerPokemon = new Pokemon("PlayerPokemon", 50, playerImagePath); // Provide appropriate constructor parameters
        enemyPokemon = new Pokemon("EnemyPokemon", 50, enemyImagePath); // Provide appropriate constructor parameters
    }

    private void loadSpecificPokemon(String imagePath){
        playerPokemon = new Pokemon("PlayerPokemon", 50, "pokemons/back/1.png");
        enemyPokemon = new Pokemon("EnemyPokemon", 50, imagePath);
    }

    public void drawBattleUI(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(60f));

        int frameX = gp.tileSize * 2;
        int frameY = gp.screenHeight - gp.tileSize * 4; // Muta fereastra în partea de jos a ecranului
        int frameWidth = gp.screenWidth - gp.tileSize * 4;
        int frameHeight = gp.tileSize * 4;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        if (subState == 0) {
            drawMainMenu(frameX, frameY);
        } else if (subState == 1) {
            drawAttackMenu(frameX, frameY);
        }
    }

    private void drawMainMenu(int frameX, int frameY) {
        int buttonPaddingX = 220;
        int buttonPaddingY = 100;

        // Coordonatele inițiale pentru butoane
        int textX = frameX + 30;
        int textY = frameY + 70; // Ajustează pentru a centra textul în fereastră

        // Buton 1 - Attack
        String text = "Attack";
        g.drawString(text, textX, textY);
        if (command == 0) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 2 - Bag
        textX += buttonPaddingX;
        text = "Bag";
        g.drawString(text, textX, textY);
        if (command == 1) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 3 - Pokemon
        textY += buttonPaddingY;
        textX = frameX + 30; // Resetează la poziția inițială
        text = "Pokemon";
        g.drawString(text, textX, textY);
        if (command == 2) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 4 - Run
        textX += buttonPaddingX;
        text = "Run";
        g.drawString(text, textX, textY);
        if (command == 3) {
            g.drawString("*", textX - 20, textY);
        }
    }

    private void drawAttackMenu(int frameX, int frameY) {
        int textX;
        int textY;

        g.setFont(g.getFont().deriveFont(50f));

        int buttonPaddingX = 320;
        int buttonPaddingY = 50;

        String currD = "Choose an attack.";
        textX = gp.ui.getXCenteredString(currD);
        textY = frameY + gp.tileSize;
        g.drawString(currD, textX, textY);


        // Coordonatele inițiale pentru butoane
        textX = frameX + 30;
        textY = frameY + 70; // Ajustează pentru a centra textul în fereastră

        // Buton 1 - Basic Attack
        textY += buttonPaddingY;
        String text = "Basic Attack";
        g.drawString(text, textX, textY);
        if (command == 0) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 2 - Special Attack
        textX += buttonPaddingX;
        text = "Special Attack";
        g.drawString(text, textX, textY);
        if (command == 1) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 3 - Ultimate Attack
        textY += buttonPaddingY;
        textX = frameX + 30; // Resetează la poziția inițială
        text = "Ultimate Attack";
        g.drawString(text, textX, textY);
        if (command == 2) {
            g.drawString("*", textX - 20, textY);
        }

        // Buton 4 - Kick
        textX += buttonPaddingX;
        text = "Kick";
        g.drawString(text, textX, textY);
        if (command == 3) {
            g.drawString("*", textX - 20, textY);
        }
    }

    private void drawAbilityHistory(Graphics2D g2) {
        int x = gp.tileSize;
        int y = gp.tileSize * 2;
        int width = gp.tileSize * 5;
        int height = gp.tileSize * 5;

        // Draw background for ability history
        drawSubWindow(x - 10, y - 30, width, height);

        g2.setFont(g2.getFont().deriveFont(20f));
        g2.setColor(Color.WHITE);
        g2.drawString("Ability History:", x, y);
        y += 30;

        // Calculate the maximum number of lines that can fit in the history window
        int maxLines = (height - 60) / 30;

        while (abilityHistory.size() > maxLines) {
            abilityHistory.remove(0); // Remove the oldest message
        }

        for (String entry : abilityHistory) {
            g2.drawString(entry, x, y);
            y += 30;
        }
    }

    private void drawBattleMessage(Graphics2D g2) {
        if (!battleMessage.isEmpty()) {
            int x = gp.screenWidth / 2;
            int y = gp.screenHeight / 2;
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.setColor(Color.YELLOW);
            g2.drawString(battleMessage, x - g2.getFontMetrics().stringWidth(battleMessage) / 2, y);
        }
    }

    public void attemptToFlee(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;

        String currD = "Successfully fled the battle.";
        if (Math.random() < 0.2) {
            for (String line : currD.split("\n")) {
                g.drawString(line, textX, textY);
                textY += 32;
            }
            endBattle("Successfully fled the battle.");
        } else {
            currD = "Couldn't flee the battle.";
            for (String line : currD.split("\n")) {
                g.drawString(line, textX, textY);
                textY += 32;
            }
            enemyTurn();
        }
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 150); // Transparent black
        g.setColor(c);
        g.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect(x, y, width, height, 35, 35);
    }
    public void setBossPokemon(String bossPokemon){
        this.bossPokemon = bossPokemon;
        isBossBattle = true;
    }
}
