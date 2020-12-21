import java.awt.Color;

public class spline {
    double[] x, y, z;
    Color c;

    public spline(double[] x, double[] y, double[] z, Color c)
    {
        Screen.DPrimitives.add(new DPrimitive(x, y, z, c, false));


        this.c = c;
        this.x = x;
        this.y = y;
        this.z = z;

    }


}
