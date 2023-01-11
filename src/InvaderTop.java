import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Image;

public class InvaderTop extends Invader{
	private Image image = getImage("img_invadertopA.gif");


	public InvaderTop(int x, int y) {
		super(x,y);
	}


	//getImage
	private String getImage() {
		return "InvaderTop.png";
	}

	@Override
	public void draw(Graphics2D graph) {
		//return the image
		graph.drawImage(image, this.getX(), this.getY(), null);

	}


}
