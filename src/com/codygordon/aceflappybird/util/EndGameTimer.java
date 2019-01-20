package com.codygordon.aceflappybird.util;

import java.util.Timer;
import java.util.TimerTask;

import com.codygordon.aceflappybird.views.EndView;
import com.codygordon.game.Game;

public class EndGameTimer {

	public EndGameTimer(long delay) {
		startTimer(delay);
	}

	private void startTimer(long delay) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Game.getInstance().switchScreen(new EndView());
			}
		}, delay);
	}
}