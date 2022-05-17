package com.connectfourgui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class ConnectFourFactory implements EntityFactory {
    public final int OFFSET = 5;
    @Spawns("Player")
    public Entity spawnsPlayer(SpawnData data){
        var TileSize = data.<Integer>get("Tile Size");
        var color = data.<Color>get("color");
        var circle = new Circle(TileSize - (TileSize/2),TileSize - (TileSize/2),(TileSize/2) - OFFSET, color);
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .view(circle)
                .build();

    }
    @Spawns("Tile")
    public  Entity spawnsTile(SpawnData data){

        var color = data.<Color>get("color");
        var tileSize = data.<Integer>get("Tile Size");
        var rectangle = new Rectangle(tileSize,tileSize,Color.BLUE);
        var circle = new Circle(tileSize - (tileSize/2), tileSize - (tileSize/2),(tileSize / 2) - OFFSET, color);
        return FXGL.entityBuilder(data)
                .type(EntityType.TILE)
                .view(rectangle)
                .view(circle)
                .build();
    }
}
