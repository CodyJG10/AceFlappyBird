package com.codygordon.aceflappybird.gameobjects;

import com.codygordon.aceflappybird.views.FlappyBirdGameView;
import com.codygordon.game.Game;
import com.codygordon.game.gameobjects.GameObject;
import com.codygordon.game.gameobjects.components.Collider;

public class MidPipe extends GameObject {

	private boolean playerPassedThrough = false;
	
	@Override
	public void onAddComponents() {
		addComponent(new Collider(this));
	}	
	
	@Override
	public void onCollision(GameObject obj) {
		if(obj instanceof Player) {
			if(!playerPassedThrough) {
				FlappyBirdGameView.score++;
				Game.getInstance().getGameView().destroyGameObject(this);
				playerPassedThrough = true;
			}
		}
	}
	
	@Override
	public void update() {
		location.x -= FlappyBirdGameView.pipeMoveSpeed;
	}
}