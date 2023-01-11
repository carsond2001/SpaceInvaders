import java.awt.*;

public class InvaderMiddle extends Invader{
	private Image image = getImage("img_invadermiddleA.gif");

	public InvaderMiddle(int x, int y) {
		super(x,y);
	}

	@Override
	public void draw(Graphics2D graph) {
		//return the image
		graph.drawImage(image, this.getX(), this.getY(), null);

	}

}
