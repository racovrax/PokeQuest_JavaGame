package battle;

public enum AttackType {
    BASIC_ATTACK("Basic Attack", 10),
    SPECIAL_ATTACK("Special Attack", 20),
    ULTIMATE_ATTACK("Ultimate Attack", 30),
    KICK("Kick", 5);

    private final String name;
    private final int damage;

    AttackType(String name, int damage) {
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
