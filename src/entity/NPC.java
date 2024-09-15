package entity;

import Main.GamePanel;

public class NPC extends Entity {
    private final String npcId;

    public NPC(GamePanel gp, String npcId) {
        super(gp);
        this.npcId = npcId;
        speed = 2;
        direction = "down";
        getPlayerImage();
        setDialogue();
    }

    public void getPlayerImage() {
        up1 = setup("/npc/up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        String[] dialoguri = {
                "Hei, tu! Vrei să te lupți?",
                "Nu ai nicio șansă!",
                "Acesta este sfârșitul pentru tine!"
        };
        setDialogue(dialoguri);
    }
    @Override
    public String getDialog(){
        return dialoguri[dialoguriNum];
    }

    public void setAction() {
        // Golim metoda pentru a ne asigura că NPC-ul nu își schimbă direcția
    }

    public void speak() {
      super.speak();
        }
    }

