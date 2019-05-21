package com.codygordon.aceflappybird.views;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.codygordon.game.Game;
import com.codygordon.game.settings.Settings;
import com.codygordon.game.ui.GameView;
import com.github.sarxos.webcam.Webcam;

public class FaceSelection extends GameView {

	private Webcam webcam;
	private JLabel liveFeedHolder;
	private JLabel currentCaptureHolder;
	private BufferedImage currentImage;
	
	public FaceSelection() {
		super();
		
		webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(new Dimension(500, 500));
		webcam.open();
		
		setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelLiveFeed = new JPanel();
		add(panelLiveFeed);
		panelLiveFeed.setLayout(null);
		
		liveFeedHolder = new JLabel("");
		liveFeedHolder.setBounds(25, 25, 200, 150);
		panelLiveFeed.add(liveFeedHolder);
		
		JButton btnCapture = new JButton("Capture");
		btnCapture.setBounds(75, 370, 89, 23);
		btnCapture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capture();
			}
		});
		panelLiveFeed.add(btnCapture);
		
		JPanel panelCurrentImage = new JPanel();
		add(panelCurrentImage);
		panelCurrentImage.setLayout(null);
		
		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(75, 370, 89, 23);
		btnOk.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImage();
			}
		});
		panelCurrentImage.add(btnOk);
		
		currentCaptureHolder = new JLabel("");
		currentCaptureHolder.setBounds(25, 25, 200, 150);
		panelCurrentImage.add(currentCaptureHolder);

		initLiveFeed();
	}
	
	private void initLiveFeed() {
		new VideoFeed().start();
	}
	
	private void capture() {
		BufferedImage img = webcam.getImage();
		
		int diameter = Integer.parseInt(Settings.getInstance().getSetting("PLAYER_IMAGE_MASK_DIAMETER"));   
		BufferedImage mask = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = mask.createGraphics();
	    applyQualityRenderingHints(g2d);
	    g2d.fillOval(0, 0, diameter - 1, diameter - 1);
	    g2d.dispose();

	    BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
	    g2d = masked.createGraphics();
	    applyQualityRenderingHints(g2d);
	    int x = (diameter - img.getWidth()) / 2;
	    int y = (diameter - img.getHeight()) / 2;
	    g2d.drawImage(img, x, y, null);
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
	    g2d.drawImage(mask, 0, 0, null);
	    g2d.dispose();
	
	    currentImage = masked;
		currentCaptureHolder.setIcon(new ImageIcon(currentImage));
	}
	
	private void applyQualityRenderingHints(Graphics2D g2d) {
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
	private void selectImage() {
		try {
			ImageIO.write(currentImage, "PNG", new File("assets/yellowbird-downflap.png"));
		} catch(Exception e) { }
		webcam.close();
		Game.getInstance().switchScreen(new MenuView());
	}
	
	class VideoFeed extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					Image img = webcam.getImage();
					liveFeedHolder.setIcon(new ImageIcon(img));
					Thread.sleep(20);
				} catch (Exception e) { }
			}
		}
	}
}