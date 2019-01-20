package com.codygordon.aceflappybird.gameobjects;

import com.codygordon.aceflappybird.views.FlappyBirdGameView;
import com.codygordon.game.Game;
import com.codygordon.game.gameobjects.GameObject;
import com.codygordon.game.gameobjects.components.Collider;

public class Pipe extends GameObject {
	
	public boolean offScreen = false;
	
	@Override
	public void onAddComponents() {
		addComponent(new Collider(this));
	}
	
	@Override
	public void update() {
		location.x -= FlappyBirdGameView.pipeMoveSpeed;
		if(location.x <= -size.x) {
			Game.getInstance().getGameView().destroyGameObject(this);
		}
	}
	
	@Override
	public void onCollision(GameObject obj) {
		if(obj instanceof Player) {
			((FlappyBirdGameView)Game.getInstance().getGameView()).die();
		}
	}
}