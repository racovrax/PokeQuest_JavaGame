package factory;

import Main.GamePanel;
import entity.*;

public class SimpleEntityFactory implements EntityFactory {
    private GamePanel gp;

    public SimpleEntityFactory(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public NPC createNPC() {
        return new NPC(gp,"npc");
    }
    public GYM_GOON createGYM_GOON() {
        return new GYM_GOON(gp);
    }

    @Override
    public Entity createGYM_BOSS() {
        return new GYM_BOSS(gp);
    }

    @Override
    public Player createPlayer() {
        return new Player(gp, gp.keyHandler);
    }

    @Override
    public Pokemon createPokemon() {
        // Assumed constructor, replace with actual if different
        return new Pokemon("Default",5,"pokemons/default.png");
    }

    @Override
    public Entity createEntity(String type, int worldX, int worldY) {
        return null;
    }
}
