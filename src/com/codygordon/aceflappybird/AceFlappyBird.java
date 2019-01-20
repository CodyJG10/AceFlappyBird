package com.codygordon.aceflappybird;

import com.codygordon.aceflappybird.views.*;
import com.codygordon.game.Game;

public class AceFlappyBird extends Game {

	public static void main(String[] args) {
		new AceFlappyBird();
	}

	@Override
	public void initGameView() {
		gameView = new FlappyBirdGameView();
	}
}