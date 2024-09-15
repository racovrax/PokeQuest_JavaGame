package factory;

import Main.GamePanel;
import entity.Entity;
import entity.NPC;
import entity.Player;
import entity.Pokemon;

public interface EntityFactory {
    NPC createNPC();
    Player createPlayer();
    Pokemon createPokemon();

    Entity createEntity(String type, int worldX, int worldY);

    Entity createGYM_GOON();

    Entity createGYM_BOSS();
}

