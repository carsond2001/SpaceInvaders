import java.awt.*;

public class collisionRectangle extends Ship{

    private boolean isAlive = true;

    public collisionRectangle(int x, int y) {
        super(x, y);
    }


    public void killed(){
        isAlive = false;
    }

    public boolean checkAlive(){
        return isAlive;
    }

    public Rectangle getBounds() {
        return new Rectangle(super.getX(), super.getY(), 2000, 20);
    }

    public void draw(Graphics2D graph) {
        //draw invisible rectangle
        graph.setColor(Color.black);
        graph.draw(getBounds());


    }

}
