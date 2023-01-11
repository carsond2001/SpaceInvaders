import java.awt.*;

public abstract class Invader extends Ship{
	private Image image = getImage("img_invaderhit.gif");


	private boolean isAlive = true;

	public Invader(int x, int y) {
		super(x, y);
	}

	public void killed(){
		isAlive = false;
	}

	public boolean checkAlive(){
		return isAlive;
	}

	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), 20, 20);
	}



}
