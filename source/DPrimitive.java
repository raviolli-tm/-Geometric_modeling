import java.awt.*;

public class DPrimitive {
    Color c;
    double[] x, y, z;
    boolean draw = true, seeThrough = false;
    double[] CalcPos, newX, newY;
    PrimitiveObject DrawablePrimitive;

    public DPrimitive(double[] x, double[] y,  double[] z, Color c, boolean seeThrough)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.c = c;
        this.seeThrough = seeThrough;
        createPrimitive();
    }

    void createPrimitive()
    {
        DrawablePrimitive = new PrimitiveObject(new double[x.length], new double[x.length], c, Screen.DPrimitives.size(), seeThrough);
    }

    void updatePrimitive()
    {
        newX = new double[x.length];
        newY = new double[x.length];
        draw = true;
        for(int i=0; i<x.length; i++)
        {
            if(Screen.isPerspectiveMode) {
                CalcPos = Calculator.CalculatePositionP(Screen.ViewFrom, Screen.ViewTo, x[i], y[i], z[i]);
                newX[i] = (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
                newY[i] = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;
                if (Calculator.t < 0)
                    draw = false;
            }else
            {
                CalcPos = Calculator.CalculatePositionP(Screen.ViewFrom, Screen.ViewTo, x[i], y[i], z[i]);
                newX[i]=CalcPos[0];
                newY[i]=CalcPos[1];
            }
        }


        DrawablePrimitive.draw = draw;
        DrawablePrimitive.updatePrimitive(newX, newY);
    }

}
