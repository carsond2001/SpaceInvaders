import java.awt.*;

public class Base extends Ship{

	public Base(int x, int y) {
		super(x,y);
	}

	@Override
	public void draw(Graphics2D graph) {
		// TODO Auto-generated method stub
	}
	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), 20, 20);
	}


}
