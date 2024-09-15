package tile;



import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {
    public GamePanel gp;
    //vectorul de dale
    public Tile[] tile;
    //matricea de numere de dale
    public int[][][] mapTileNum;
    //ArrayList<String> filenames = new ArrayList<>();
    //ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[100];

        mapTileNum = new int[gp.MaxMap][gp.MaxWorldRow][gp.MaxWorldCol];//initializarea matricii hartii
        getTileImage();//incarcam imaginile dalelor
        loadMap("/maps/route_1.txt", 0);
        loadMap("/maps/gym.txt", 1);
        loadMap("/maps/laborator.txt", 2);
        loadMap("/maps/house.txt", 3);
        loadMap("/maps/final.txt", 4);
        loadMap("/maps/end.txt", 5);


    }

    //metoda de incarcare a dalelor
    public void getTileImage() {
        setup(0, "000", false);
        setup(1, "001", true);
        setup(2, "002", true);
        setup(3, "003", false);
        setup(4, "004", false);
        setup(5, "005", false);
        setup(6, "006", false);
        setup(7, "007", false);
        setup(8, "008", false);
        setup(9, "009", false);
        setup(10, "010", true);
        setup(11, "011", true);
        setup(12, "012", true);
        setup(13, "013", false);
        setup(14, "014", false);
        setup(15, "015", true);
        setup(16, "016", true);
        setup(17, "017", true);
        setup(18, "018", true);
        setup(19, "019", true);
        setup(20, "020", true);
        setup(21, "021", true);
        setup(22, "022", true);
        setup(23, "023", true);
        setup(24, "024", true);
        setup(25, "025", true);
        setup(26, "026", true);
        setup(27, "027", true);
        setup(28, "028", true);
        setup(29, "029", true);
        setup(30, "030", false);
        setup(31, "031", true);
        setup(32, "032", true);
        setup(33, "033", true);
        setup(34, "034", false);
        setup(35, "035", true);
        setup(36, "036", true);
        setup(37, "037", true);
        setup(38, "038", false);
        setup(39, "039", false);
        setup(40, "040", true);
        setup(41, "041", false);
        setup(42, "042", false);
        setup(43, "043", false);
        setup(44, "044", false);
        setup(45, "045", true);
        setup(46, "046", true);
        setup(47, "047", true);
        setup(48, "048", false);
        setup(49, "049", true);
    }

    //metoda de configurare a dalelor
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool utilityTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(STR."/Tiles/\{imageName}.png")));
            tile[index].image = utilityTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadMap(String filename, int map) {
        try {
            InputStream in = getClass().getResourceAsStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            int col = 0;
            int row = 0;
//citirea datelor din fisierul de harta si salvarea lor in matrice
            while (col < gp.MaxWorldCol && row < gp.MaxWorldCol) {
                String line = br.readLine();
                while (col < gp.MaxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][row][col] = num;
                    col++;
                }
                if (col == gp.MaxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metoda de desenare a hartii
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        // Parcurgerea matricei si desenarea dalelor
        while (worldCol < gp.MaxWorldCol && worldRow < gp.MaxWorldRow) {
            try {
                int tileNum = mapTileNum[gp.currentMap][worldRow][worldCol];
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Verificam daca dala se afla in zona vizibila a ecranului si o desenam
                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                    if (tile[tileNum] != null) {
                        g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                    } else {
                        System.out.println("Tile at index " + tileNum + " is null.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error drawing tile at [" + worldRow + ", " + worldCol + "]: " + e.getMessage());
                e.printStackTrace();
            }

            worldCol++;

            if (worldCol == gp.MaxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

