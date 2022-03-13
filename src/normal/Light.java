package normal;

import java.awt.Color;

public class Light {
	
	private int x, y;
	private float power = 100f;
	private int r = 255, g = 255, b = 255;
	
	public void setPower(float power) {
		this.power = power;
	}
	
	public float getPower() {
		return power;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setColor(Color c) {
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
	}
	
	public int getR() {
		return r;
	}
	
	public int getG() {
		return g;
	}
	
	public int getB() {
		return b;
	}
}
