package com.codygordon.aceflappybird.views;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.codygordon.game.Game;
import com.codygordon.game.assets.AssetLoader;
import com.codygordon.game.ui.GameView;

public class EndView extends GameView {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage backgroundImage;
	
	@Override
	public void onEnable() {
		setLayout(null);
		int width = Game.getInstance().getWindow().getWidth();
		int height = Game.getInstance().getWindow().getHeight();
		backgroundImage = AssetLoader.getSprite("background.png", width + 750, height - 25);
		JLabel lbl = new JLabel("Your score: " + FlappyBirdGameView.score);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setSize(300, 100);
		lbl.setFont(new Font("Ariel", Font.BOLD, 25));
		lbl.setLocation((width / 2) - (lbl.getWidth() / 2), height / 2 - (height / 2) / 2);
		JButton btn = new JButton("Play Again");
		btn.setSize(150, 50);
		btn.setLocation((width / 2) - (btn.getWidth() / 2), height / 2 + (height / 2) / 2);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playAgain();
			}
		});
		add(btn);
		add(lbl);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
	private void playAgain() {
		FlappyBirdGameView.score = 0;
		Game.getInstance().switchScreen(new FlappyBirdGameView());
	}
}