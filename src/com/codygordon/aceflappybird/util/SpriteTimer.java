package com.codygordon.aceflappybird.util;

import java.util.Timer;
import java.util.TimerTask;

public class SpriteTimer  {
	
	private Timer timer;
	private long delay;
	private INextSprite callback;
	
	public SpriteTimer(long delay, INextSprite iNextSprite) {
		this.delay = delay;
		this.callback = iNextSprite;
	}
	
	public void start() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				callback.nextSprite();
			}
		}, delay, delay);
	}
	
	public void stop() {
		timer.cancel();
	}
}