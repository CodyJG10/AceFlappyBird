package com.codygordon.aceflappybird.gameobjects;

import java.awt.Color;
import java.awt.Graphics;

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
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)location.x,(int)location.y,
				(int)size.x,(int)size.y);
		
		g.setColor(Color.BLUE);
		g.drawRect(col.rect.x, col.rect.y, col.rect.width, col.rect.height);
	}
}
