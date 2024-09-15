package battle;

public class Ability {
    private String name;
    private int damage;

    public Ability(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }
}
