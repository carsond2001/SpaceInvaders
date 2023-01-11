import java.awt.*;

public class Mystery extends Invader{
	private Image image = getImage("img_mystery.gif");

	public Mystery(int x, int y) {
		super(x,y);
	}

	@Override
	public void draw(Graphics2D graph) {
		graph.drawImage(image, this.getX(), this.getY(), null);
	}

}
