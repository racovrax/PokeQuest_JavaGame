package Main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp; // Referință către panoul de joc

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // Metoda pentru verificarea coliziunii cu tile-urile hărții
    public void checkTile(Entity entity) {
        // Calcularea poziției și dimensiunilor entității în lumea jocului
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Determinarea coloanelor și rândurilor tile-urilor pe care se află entitatea
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // Verificarea coliziunii în funcție de direcția entității
        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tm.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
                tileNum2 = gp.tm.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];
                if (gp.tm.tile[tileNum1].collision || gp.tm.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tm.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
                tileNum2 = gp.tm.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
                if (gp.tm.tile[tileNum1].collision || gp.tm.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tm.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
                tileNum2 = gp.tm.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
                if (gp.tm.tile[tileNum1].collision || gp.tm.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tm.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];
                tileNum2 = gp.tm.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
                if (gp.tm.tile[tileNum1].collision || gp.tm.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    // Metoda pentru verificarea coliziunii cu obiectele din joc
    public int checkObject(Entity entity, boolean player) {
        int index = 999; // Indexul obiectului cu care se produce coliziunea (default 999 = niciun obiect)

        // Verifică dacă `gp.object` și `gp.currentMap` sunt valide
        if (gp.object != null && gp.currentMap >= 0 && gp.currentMap < gp.object.length && gp.object[gp.currentMap] != null) {
            // Parcurgerea listei de obiecte
            for (int i = 0; i < gp.object[gp.currentMap].length; ++i) {
                if (gp.object[gp.currentMap][i] != null) {
                    // Actualizarea poziției și dimensiunilor entității și ale obiectului
                    entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
                    gp.object[gp.currentMap][i].solidArea.x = gp.object[gp.currentMap][i].worldX + gp.object[gp.currentMap][i].solidAreaDefaultX;
                    gp.object[gp.currentMap][i].solidArea.y = gp.object[gp.currentMap][i].worldY + gp.object[gp.currentMap][i].solidAreaDefaultY;

                    // Verificarea coliziunii în funcție de direcția entității
                    switch (entity.direction) {
                        case "up":
                            entity.solidArea.y -= entity.speed;
                            if (entity.solidArea.intersects(gp.object[gp.currentMap][i].solidArea)) {
                                if (gp.object[gp.currentMap][i].collision) {
                                    entity.collisionOn = true;
                                }
                                if (player) {
                                    index = i;
                                }
                            }
                            break;
                        case "down":
                            entity.solidArea.y += entity.speed;
                            if (entity.solidArea.intersects(gp.object[gp.currentMap][i].solidArea)) {
                                if (gp.object[gp.currentMap][i].collision) {
                                    entity.collisionOn = true;
                                }
                                if (player) {
                                    index = i;
                                }
                            }
                            break;
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            if (entity.solidArea.intersects(gp.object[gp.currentMap][i].solidArea)) {
                                if (gp.object[gp.currentMap][i].collision) {
                                    entity.collisionOn = true;
                                }
                                if (player) {
                                    index = i;
                                }
                            }
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            if (entity.solidArea.intersects(gp.object[gp.currentMap][i].solidArea)) {
                                if (gp.object[gp.currentMap][i].collision) {
                                    entity.collisionOn = true;
                                }
                                if (player) {
                                    index = i;
                                }
                            }
                            break;
                    }

                    // Resetarea pozițiilor și dimensiunilor entității și ale obiectului
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    gp.object[gp.currentMap][i].solidArea.x = gp.object[gp.currentMap][i].solidAreaDefaultX;
                    gp.object[gp.currentMap][i].solidArea.y = gp.object[gp.currentMap][i].solidAreaDefaultY;
                }
            }
        }
        return index; // Returnarea indexului obiectului cu care se produce coliziunea
    }


    public int checkEntity(Entity entity, Entity[][] target){
        int index = 999; // Indexul obiectului cu care se produce coliziunea (default 999 = niciun obiect)

        // Parcurgerea listei de obiecte
        for (int i = 0; i < target[1].length; ++i) {
            if (target[gp.currentMap][i] != null) {
                // Actualizarea poziției și dimensiunilor entității și ale obiectului
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX +target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                // Verificarea coliziunii în funcție de direcția entității
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                                entity.collisionOn = true;
                                index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                                entity.collisionOn = true;
                                index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                                entity.collisionOn = true;
                                index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                                entity.collisionOn = true;
                                index = i;
                        }
                        break;
                }

                // Resetarea pozițiilor și dimensiunilor entității și ale obiectului
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index; // Returnarea indexului obiectului cu care se produce coliziunea
    }
    public void checkPlayer(Entity entity){
                // Actualizarea poziției și dimensiunilor entității și ale obiectului
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.player.solidArea.x = gp.player.worldX +gp.player.solidArea.x;
                gp.player.solidArea.y =gp.player.worldY + gp.player.solidArea.y;

                // Verificarea coliziunii în funcție de direcția entității
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                        break;
                }

                // Resetarea pozițiilor și dimensiunilor entității și ale obiectului
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.player.solidArea.x = gp.player.solidAreaDefaultX;
                gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }
}