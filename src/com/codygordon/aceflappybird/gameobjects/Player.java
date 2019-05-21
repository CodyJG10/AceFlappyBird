package com.codygordon.aceflappybird.gameobjects;

import java.awt.Point;

import com.codygordon.aceflappybird.views.FlappyBirdGameView;
import com.codygordon.game.Game;
import com.codygordon.game.gameobjects.GameObject;
import com.codygordon.game.gameobjects.components.Collider;
import com.codygordon.game.gameobjects.components.Rigidbody;
import com.codygordon.game.settings.Settings;

public class Player extends GameObject {
	
	private int jumpPower = Integer.parseInt(Settings.getInstance().getSetting("PLAYER_JUMP_STRENGTH"));
	private Rigidbody rb;
	private Collider col;
	
	@Override
	public void onAddComponents() {
//		int size = Integer.parseInt(Settings.getInstance().getSetting("PLAYER_SIZE"));
//		this.size = new Point(size, size);
		rb = new Rigidbody(this);
		rb.useGravity = true;
		addComponent(rb);
		col = new Collider(this);
		col.autoResize();
		addComponent(col);
	}
	
	public void jump() {
		rb.velocity.y = -jumpPower;
	}
	
	@Override
	public void update() {
		super.update();
		if(location.y > Game.getInstance().getWindow().getHeight() + size.y) {
			((FlappyBirdGameView)Game.getInstance().getGameView()).die();
		}
	}
}