import java.awt.*;

public class InvaderBottom extends Invader {
	private Image image = getImage("img_invaderbottomA.gif");

	public InvaderBottom(int x, int y) {
		super(x,y);
	}

	@Override
	public void draw(Graphics2D graph) {
		//return the image
		graph.drawImage(image, this.getX(), this.getY(), null);

	}

}
