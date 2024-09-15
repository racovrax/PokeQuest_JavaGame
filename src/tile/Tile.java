package tile;

import java.awt.image.BufferedImage;

public class Tile {
    // Imaginea asociată dalei
    public BufferedImage image;
    // Proprietate care indică dacă dala are coliziuni sau nu
    public boolean collision = false;

    // Metoda pentru a obține imaginea dalei
    public BufferedImage getImage() {
        return image;
    }

    // Metoda pentru a seta imaginea dalei și a returna obiectul curent (pentru a permite stilizarea)
    public Tile setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    // Metoda pentru a verifica dacă dala are coliziuni
    public boolean isCollision() {
        return collision;
    }

    // Metoda pentru a seta proprietatea de coliziuni a dalei și a returna obiectul curent (pentru a permite stilizarea)
    public Tile setCollision(boolean collision) {
        this.collision = collision;
        return this;
    }
}
