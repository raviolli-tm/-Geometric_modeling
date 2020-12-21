import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

public class PrimitiveObject {
    Point[] P;
    Color c;
    boolean draw = true, visible = true, seeThrough;

    public PrimitiveObject(double[] x, double[] y, Color c, int n, boolean seeThrough)
    {
        P = new Point[x.length];
        for(int i = 0; i<x.length; i++)
            P[i] = new Point((int) x[i], (int) y[i]);

        this.c = c;
        this.seeThrough = seeThrough;
    }

    void updatePrimitive(double[] x, double[] y)
    {

        for(int i = 0; i<x.length; i++)
        {
            P[i].x = (int) x[i];
            P[i].y = (int) y[i];

        }
    }

    void drawPrimitive(Graphics g)
    {
        if(draw && visible)
            for(int i = 1; i<P.length; i++)
            {
            g.setColor(c);
                g.drawLine(P[i-1].x,P[i-1].y,P[i].x,P[i].y);
        }
    }

}
