package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import normal.Light;
import normal.NMRender;

public class JNormalPanel extends JPanel implements MouseMotionListener {
	
	NMRender render;
	Light light;
	
	BufferedImage img, normal;
	
	int scale = 1;
	
	public JNormalPanel() {
		render = new NMRender();
		light = new Light();
		light.setColor(new Color(255, 100, 100));
		render.addLight(light);

		Light light2 = new Light();
		light2.setColor(new Color(100, 255, 100));
		render.addLight(light2);
		
		try {
			BufferedImage loaded = ImageIO.read(new File("normals.png"));
			
			normal = new BufferedImage(500*scale, 500*scale, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) normal.getGraphics();
			g.drawImage(loaded, 0, 0, loaded.getWidth()*scale, loaded.getHeight()*scale, null);
			g.dispose();
			
			loaded = ImageIO.read(new File("img.png"));
			
			img = new BufferedImage(500*scale, 500*scale, BufferedImage.TYPE_INT_RGB);
			g = (Graphics2D) img.getGraphics();
			g.drawImage(loaded, 0, 0, loaded.getWidth()*scale, loaded.getHeight()*scale, null);
			g.dispose();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setFocusable(true);
		addMouseMotionListener(this);
	}
	
	BufferedImage main;
	
	public void start() {
		main = new BufferedImage(500*scale, 500*scale, BufferedImage.TYPE_INT_RGB);
		new Thread(() -> {
			while (true) {
				draw((Graphics2D) getGraphics());
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}).start();
	}

	private void draw(Graphics2D g) {
		render.drawImage(main, img, normal, 0, 0, img.getWidth(), img.getHeight());
		
		g.drawImage(main, 0, 0, null);
		
		g.setColor(Color.WHITE);
		g.drawRect(light.getX(), light.getY(), 3, 3);
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		updateLight(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateLight(e);
	}
	
	public void updateLight(MouseEvent e) {
		light.setX(e.getX());
		light.setY(e.getY());
	}
}
