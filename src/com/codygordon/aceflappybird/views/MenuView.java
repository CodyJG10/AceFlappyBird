package com.codygordon.aceflappybird.views;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import com.codygordon.game.Game;
import com.codygordon.game.assets.AssetLoader;
import com.codygordon.game.ui.GameView;

public class MenuView extends GameView {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage backgroundImage;
	
	@Override
	public void onEnable() {
		setLayout(null);
		int width = Game.getInstance().getWindow().getWidth();
		int height = Game.getInstance().getWindow().getHeight();
		backgroundImage = AssetLoader.getSprite("background.png", width + 750, height - 25);
		JButton btn = new JButton("Play");
		btn.setSize(150, 50);
		btn.setLocation((width / 2) - (btn.getWidth() / 2), height / 2 + (height / 2) / 2);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});
		add(btn);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
	private void play() {
		FlappyBirdGameView.score = 0;
		Game.getInstance().switchScreen(new FlappyBirdGameView());
	}
	
}