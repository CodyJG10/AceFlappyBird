package com.codygordon.aceflappybird.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.codygordon.aceflappybird.gameobjects.MidPipe;
import com.codygordon.aceflappybird.gameobjects.Pipe;
import com.codygordon.aceflappybird.gameobjects.Player;
import com.codygordon.aceflappybird.util.EndGameTimer;
import com.codygordon.game.Game;
import com.codygordon.game.assets.AssetLoader;
import com.codygordon.game.gameobjects.GameObject;
import com.codygordon.game.gameobjects.components.Collider;
import com.codygordon.game.gameobjects.components.Rigidbody;
import com.codygordon.game.input.events.KeyDownEvent;
import com.codygordon.game.physics.Vector2;
import com.codygordon.game.settings.Settings;
import com.codygordon.game.ui.GameView;
import com.codygordon.game.util.ScreenBorder;
import com.codygordon.game.util.ScreenBorder.BorderDelegate;

public class FlappyBirdGameView extends GameView {

	private static final long serialVersionUID = 1L;

	public static int pipeMoveSpeed;

	//Pipes
	private int startingPipes;
	private int pipeXMargin;
	private int pipeWidth;
	private int pipeHeight;
	private int pipeYMargin;
	private int minPipeMid;
	private int maxPipeMid;
	private int startingPipeMargin;

	//Player
	private Player player;
	private long deathDelay;
	private static int spawnX;
	public static int score = 0;
	private int playerSize;
	private int playerSpawnX;
	private int playerSpawnY;
	private float currentRotation = 0;
	private float rotationSpeed = -5f;
	private float maxRotationDown = 45;
	private float upRotation = -30;

	//Sprite
	private BufferedImage backgroundImage;
	private BufferedImage pipeBottomImage;
	private BufferedImage pipeTopImage;
	private BufferedImage birdImage;
	
	private ArrayList<Pipe> pipes = new ArrayList<Pipe>();

	public static boolean canJump = true;

	private int getIntSetting(String name) {
		return Integer.parseInt(Settings.getInstance().getSetting(name));
	}

	/*Overrides*/
	
	@Override
	public void onCreate() {
		loadSettings();
		loadSprites();
		super.onCreate();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		screenBorder = new ScreenBorder(5, new BorderDelegate() {
			@Override
			public void BorderHit(int border, GameObject col) {
				onBorderHit(border, col);
			}
		}, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Game.getInstance().getGameLoop().unRegisterUpdateListener(this);
		canJump = true;
	}
	
	@Override
	public void onCreateGameObjects() {
		player = new Player();
		player.location = new Vector2(playerSpawnX, playerSpawnY);
		player.size = new Point(playerSize, playerSize);
		((Collider) player.getComponent(Collider.class)).autoResize();
		createGameObject(player);
		int initialPipeX = pipeXMargin * startingPipeMargin;
		for (int i = 0; i < startingPipes; i++) {
			addNewPipes(initialPipeX);
			initialPipeX += pipeXMargin;
		}
		spawnX = initialPipeX - pipeXMargin * (startingPipes * 2);
	}
	
	@Override
	public void onKeyPressed(KeyDownEvent event) {
		int key = event.getKeyEvent().getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			if (canJump) {
				player.jump();
			}
		}
	}

	@Override
	public synchronized void destroyGameObject(GameObject obj) {
		if (obj instanceof Pipe) {
			pipes.remove(obj);
		}
	}
	
	/*Initialization*/
	
	private void loadSprites() {
		int width = Game.getInstance().getWindow().getWidth();
		int height = Game.getInstance().getWindow().getHeight();
		backgroundImage = AssetLoader.getSprite("background.png", width + 750, height - 25);
		pipeTopImage = AssetLoader.getSprite("pipe_top.png", pipeWidth, pipeHeight);
		pipeBottomImage = AssetLoader.getSprite("pipe_bottom.png", pipeWidth, pipeHeight);
		birdImage = AssetLoader.getSprite("casey.png", playerSize, playerSize);
	}

	private void loadSettings() {
		playerSize = getIntSetting("PLAYER_SIZE");
		playerSpawnX = getIntSetting("PLAYER_SPAWN_X");
		playerSpawnY = getIntSetting("PLAYER_SPAWN_Y");
		startingPipes = getIntSetting("STARTING_PIPES");
		pipeXMargin = getIntSetting("PIPE_X_MARGIN");
		pipeWidth = getIntSetting("PIPE_WIDTH");
		pipeHeight = getIntSetting("PIPE_HEIGHT");
		pipeYMargin = getIntSetting("PIPE_Y_MARGIN");
		minPipeMid = getIntSetting("PIPE_MIN_MID");
		maxPipeMid = getIntSetting("PIPE_MAX_MID");
		startingPipeMargin = getIntSetting("STARTING_PIPE_MARGIN");
		deathDelay = Long.parseLong(Settings.getInstance().getSetting("DEATH_DELAY"));
		pipeMoveSpeed = Integer.parseInt(Settings.getInstance().getSetting("PIPE_MOVE_SPEED"));
	}
	
	/**/

	public void addNewPipes(int x) {
		Pipe pipeTop = new Pipe();
		Pipe pipeBottom = new Pipe();
		MidPipe mid = new MidPipe();

		pipeTop.size = new Point(pipeWidth, pipeHeight);
		pipeBottom.size = new Point(pipeWidth, pipeHeight);

		pipeTop.name = "Pipe Top";

		int midY = (int) (Math.random() * (maxPipeMid - minPipeMid)) + minPipeMid;
		int topY = midY - (pipeYMargin + (pipeHeight / 2));
		int bottomY = midY + (pipeYMargin + (pipeHeight / 2));
		mid.size = new Point(pipeYMargin, pipeWidth * 2);

		pipeTop.location = new Vector2(x, topY);
		pipeBottom.location = new Vector2(x, bottomY);
		mid.location = new Vector2(pipeTop.location.x, bottomY - pipeYMargin * 2);

		createGameObject(pipeTop);
		createGameObject(pipeBottom);
		createGameObject(mid);

		((Collider) pipeTop.getComponent(Collider.class)).autoResize();
		((Collider) pipeBottom.getComponent(Collider.class)).autoResize();
		((Collider) mid.getComponent(Collider.class)).autoResize();

		gameObjects.add(pipeTop);
		gameObjects.add(pipeBottom);
		gameObjects.add(mid);

		pipes.add(pipeTop);
		pipes.add(pipeBottom);
	}

	private void onBorderHit(int border, GameObject col) {
		if (border == ScreenBorder.LEFT) {
			if (col instanceof Pipe) {
				if (col.name == "Pipe Top") {
					Pipe pipe = (Pipe) col;
					if (!pipe.offScreen) {
						addNewPipes(spawnX);
						pipe.offScreen = true;
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		if(!isInitialized()) return;
		super.paint(g);
		g.drawImage(backgroundImage, 0, 0, null);
		drawPipes(g);
		g.setColor(Color.WHITE);
		drawBird(g);
		drawCenteredString(g, score + "",
				new Rectangle(Game.getInstance().getWindow().getWidth(), Game.getInstance().getWindow().getHeight()),
				new Font("Ariel", Font.PLAIN, 45), 50);
		g.setColor(Color.BLACK);
	}        
	
	private void drawBird(Graphics g) {
		float velY = ((Rigidbody)player.getComponent(Rigidbody.class)).velocity.y;
		//Going up
		if(velY < 0) {
			currentRotation = upRotation;
		}
		//Going down
		else {
			if(currentRotation <= maxRotationDown) {
				currentRotation -= rotationSpeed;	
			}
		}
		BufferedImage rotatedImg = rotateImageByDegrees(birdImage, currentRotation);
		g.drawImage(rotatedImg,
				(int) player.location.x,
				(int) player.location.y,
				null);
	}
	
	public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();

        return rotated;
    }

	private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font, int y) {
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		g.setFont(font);
		g.drawString(text, x, y);
	}

	private synchronized void drawPipes(Graphics g) {
		for (Pipe pipe : pipes) {
			if (pipe.name != null) {
				g.drawImage(pipeTopImage, (int) pipe.location.x, (int) pipe.location.y, null);
			} else {
				g.drawImage(pipeBottomImage, (int) pipe.location.x, (int) pipe.location.y, null);
			}
		}
	}

	public void die() {
		if (!canJump)
			return;
		canJump = false;
		pipeMoveSpeed = 0;
		maxRotationDown = 75;
		new EndGameTimer(deathDelay);
	}
}