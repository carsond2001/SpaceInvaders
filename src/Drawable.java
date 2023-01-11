import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class Drawable {
	
	private int x;
	private int y;
	

	public Drawable(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public abstract void draw(Graphics2D graph);

	protected Clip getSound(String filename) {
		Clip clip = null;
		try {
			InputStream in     = getClass().getResourceAsStream( filename );
			InputStream      buf    = new BufferedInputStream( in );
			AudioInputStream stream = AudioSystem.getAudioInputStream( buf );
			clip = AudioSystem.getClip();
			clip.open( stream );
		} catch (UnsupportedAudioFileException |
				 IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
	protected Image getImage(String filename) {
		URL url  = getClass().getResource( filename );
		ImageIcon icon = new ImageIcon( url );
		return    icon.getImage();
	}


}
