import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sample_Paint extends JPanel {
    curve_calculation container;
    int type;
    double x1, x2, y1, y2, z1, z2, step_x, step_y, step_z;
    int WIDTH;
    int HEIGHT;
    int lastX;
    int lastY;
    double beta1;
    double beta2;
    JPanel P = null;
    int current_view = 0;

    Sample_Paint() {




    }
    Sample_Paint(double[] ux, double[] uy,double[] uz,double[] uw, double[] up, int power, int type, JPanel jp, int view) {
        this(new curve_calculation(power,ux,uy,uz,uw,up), type,view,  jp);

    }
    Sample_Paint(curve_calculation curve, int type,int view, JPanel jp) {
        container = curve;
        current_view = view;
        this.P = jp;
        double maxx, minx, maxy, miny, maxz, minz;

        maxx = container.x[0];
        minx = container.x[0];
        maxy = container.y[0];
        miny = container.y[0];
        maxz = container.z[0];
        minz = container.z[0];
        for (int i = 0; i < container.x.length; i++) {
            if (container.x[i] > maxx) maxx = container.x[i];
            if (container.y[i] > maxy) maxy = container.y[i];
            if (container.z[i] > maxz) maxz = container.z[i];
            if (container.x[i] < minx) minx = container.x[i];
            if (container.y[i] < miny) miny = container.y[i];
            if (container.z[i] < minz) minz = container.z[i];
        }
        this.type = type;


        HEIGHT = jp.getHeight();
        WIDTH = jp.getWidth();



        
        step_x = step_estimation(minx,maxx);
        step_y = step_estimation(miny,maxz);
        step_z = step_estimation(minz,maxz);
if((this.type==6)||(this.type==9)){
    this.x1 = (minx -2*step_x);
    this.x2 = (maxx + 2*step_x);
    this.y1 = (miny - 2*step_y);
    this.y2 = (maxy + 2*step_y);
    this.z1 = (minz - 2*step_z);
    this.z2 = (maxz + 2*step_z);


}
else {
    this.x1 = (minx - step_x);
    this.x2 = (maxx + step_x);
    this.y1 = (miny - step_y);
    this.y2 = (maxy + step_y);
    this.z1 = (minz - step_z);
    this.z2 = (maxz + step_z);
}
        lastX = 0;
        lastY = 0;

        frame_without_scaling(P);
    }


 

    public void frame_without_scaling(JPanel jp) {

        this.setSize(WIDTH, HEIGHT);
        jp.add(this);
        this.setBackground(Color.WHITE);


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintD(g);
        if (!(this.P == null)) {

            g.setColor(Color.RED);
            paint_simple(g,current_view);
            g.setColor(Color.BLUE);
            switch (type) {
                case 0:
                case 3:
                    container.closed_or_periodic_B_spline();
                    break;
                case 1:
                    container.Bezier();
                    break;
                case 2:
                    container.opened_B_spline();
                    break;
                case 4:
                    container.component_Bezier();
                    break;
                case 5:
                    container.rational_Bezier();
                    break;
                case 6:
                    container.component_Hermite();
                    break;
                case 7:
                    container.Beta_spline();
                    break;
                case 8:
                    container.NURBS();
                    break;
                case 9:
                    container.Catmull_Rom();
                    break;
                default:
                    break;
            }

            paint_curve(g,current_view);


        }

    }

    public void paintD(Graphics g){

        switch (current_view){
            case 0: //xy
                paintD(g,step_x,  step_y,  x1,  x2,  y1,  y2);
                break;
            case 1://zy
                paintD(g,step_z, step_y,  z1,  z2,  y1,  y2);
                break;
            case 2://xz
                paintD(g,step_x,  step_z,  x1,  x2,  z1,  z2);
                break;

        }


    }
    public void paintD(Graphics g,double step_hor, double step_ver, double point_1_hor, double point_2_hor, double point_1_ver, double point_2_ver) {
        g.setColor(Color.GRAY);

        int OX = (int) (-point_1_hor / (point_2_hor - point_1_hor) * WIDTH);
        int OY = (int) (HEIGHT + point_1_ver / (point_2_ver - point_1_ver) * HEIGHT);

        for (int i = (int) Math.floor(point_1_hor / step_hor); i <= Math.floor(point_2_hor / step_hor); i++) {
            int positionX = (int) (-(point_1_hor - step_hor * i) / (point_2_hor - point_1_hor) * WIDTH);

            g.drawLine(positionX, 0, positionX, HEIGHT);

            int positionY;

            if (OY < 12) {
                positionY = 10;
            } else if (OY > HEIGHT - 2) {
                positionY = HEIGHT - 42;
            } else {
                positionY = OY - 2;
            }

            int format = (int) (HEIGHT / 5 / (point_2_hor - point_1_hor) / step_hor);

            if (step_hor * i == (int) (step_hor * i)) {
                format = 0;
            } else if (format > 10) {
                format = 10;
            }

            g.drawString(String.format("%." + String.valueOf(format) + "f", i * step_hor), positionX + 2, positionY);
        }

        for (int i = (int) Math.floor(point_1_ver / step_ver); i <= Math.floor(point_2_ver / step_ver); i++) {
            int positionY = (int) (HEIGHT + (point_1_ver - step_ver * i) / (point_2_ver - point_1_ver) * HEIGHT);

            g.drawLine(0, positionY, WIDTH, positionY);

            int positionX;

            int format = (int) (HEIGHT / 5 / (point_2_hor - point_1_hor) / step_ver);

            if (step_ver * i == (int) (step_ver * i)) {
                format = 0;
            } else if (format > 10) {
                format = 10;
            }

            String formated_value = String.format("%." + String.valueOf(format) + "f", i * step_ver);


            if (OX < 1) {
                positionX = 2;
            } else if (OX > WIDTH -7) {
                positionX = WIDTH - 35;
            } else {
                positionX = OX + 2;
            }

            g.drawString(formated_value, positionX, positionY - 1);
        }

        g.setColor(Color.BLACK);
        g.fillRect(OX - 1, 0, 3, HEIGHT);
        g.fillRect(0, OY - 1, WIDTH, 3);

    }


    public void paint_simple(Graphics g, int type) {

        int t_len = container.x.length;
        if(this.type==6){
            t_len =container.x.length/2;
           g.setColor(Color.magenta);
           Graphics2D g2 = (Graphics2D) g;

            switch (type) {
                case 0://(vertical-y ,horizontal -x)
                    for (int i = 0; i < t_len; i++) {
                        int yy1 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[i]+ container.y[t_len+i]/Math.sqrt(container.x[t_len+i]*container.x[t_len+i]+ container.y[t_len+i]*container.y[t_len+i]) - y1) / (y2 - y1));
                        int xx1 = (int) Math.floor(WIDTH * (container.x[i] + container.x[t_len+i]/Math.sqrt(container.x[t_len+i]*container.x[t_len+i]+container.y[t_len+i]*container.y[t_len+i]) - x1) / (x2 - x1));
                        int xx2 = (int) Math.floor(WIDTH * (container.x[i] - x1) / (x2 - x1));
                        int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[i] - y1) / (y2 - y1));
                        g.drawLine(xx1, yy1, xx2, yy2);
                        g2.setStroke(new BasicStroke(4));
                        g.drawLine(xx1, yy1, xx1, yy1);
                        g2.setStroke(new BasicStroke(1));

                    }
                    break;
                case 1://(vertical-y ,horizontal -z)
                    for (int i =0; i < t_len; i++) {
                        int zz1_2 = (int) Math.floor(WIDTH * (container.z[t_len]/Math.sqrt(container.z[t_len+i]*container.z[t_len+i]+ container.y[t_len+i]*container.y[t_len+i]) +container.z[i] - z1) / (z2 - z1));
                        int yy1 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[t_len+i]/Math.sqrt(container.z[t_len+i]*container.z[t_len+i]+ container.y[t_len+i]*container.y[t_len+i]) + container.y[i]- y1) / (y2 - y1));
                        int zz2_2 = (int) Math.floor(WIDTH * (container.z[i] - z1) / (z2 - z1));
                        int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[i] - y1) / (y2 - y1));
                        g.drawLine(zz1_2, yy1, zz2_2, yy2);
                        g2.setStroke(new BasicStroke(4));
                        g.drawLine(zz1_2, yy1, zz1_2, yy1);
                        g2.setStroke(new BasicStroke(1));

                    }
                    break;
                case 2://(vertical-z ,horizontal -x)
                    for (int i = 0; i < t_len; i++) {
                        int xx1 = (int) Math.floor(WIDTH * (container.x[t_len+i]/Math.sqrt(container.x[t_len+i]*container.x[t_len+i]+ container.z[t_len+i]*container.z[t_len+i])+ container.x[i] - x1) / (x2 - x1));
                        int zz1_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.z[t_len]/Math.sqrt(container.x[t_len+i]*container.x[t_len+i]+ container.z[t_len+i]*container.z[t_len+i]) +container.z[i] - z1) / (z2 - z1));
                        int xx2 = (int) Math.floor(WIDTH * (container.x[i] - x1) / (x2 - x1));
                        int zz2_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.z[i] - z1) / (z2 - z1));
                        g.drawLine(xx1, zz1_1, xx2, zz2_1);
                        g2.setStroke(new BasicStroke(4));
                        g.drawLine(xx1, zz1_1, xx1, zz1_1);
                        g2.setStroke(new BasicStroke(1));
                    }
                    break;
            }
            g.setColor(Color.red);

        }
        int yy1 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[0] - y1) / (y2 - y1));
        int xx1 = (int) Math.floor(WIDTH * (container.x[0] - x1) / (x2 - x1));
        int zz1_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.z[0] - z1) / (z2 - z1));
        int zz1_2 = (int) Math.floor(WIDTH * (container.z[0] - z1) / (z2 - z1));
        switch (type) {
            case 0://(vertical-y ,horizontal -x)
                for (int i = 1; i < t_len; i++) {
                    int xx2 = (int) Math.floor(WIDTH * (container.x[i] - x1) / (x2 - x1));
                    int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[i] - y1) / (y2 - y1));
                    g.drawLine(xx1, yy1, xx2, yy2);
                    xx1 = xx2;
                    yy1 = yy2;
                }
                break;
            case 1://(vertical-y ,horizontal -z)
                for (int i = 1; i < t_len; i++) {
                    int zz2_2 = (int) Math.floor(WIDTH * (container.z[i] - z1) / (z2 - z1));
                    int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.y[i] - y1) / (y2 - y1));
                    g.drawLine(zz1_2, yy1, zz2_2, yy2);
                    zz1_2 = zz2_2;
                    yy1 = yy2;
                }
                break;
            case 2://(vertical-z ,horizontal -x)
                for (int i = 1; i < t_len; i++) {
                    int xx2 = (int) Math.floor(WIDTH * (container.x[i] - x1) / (x2 - x1));
                    int zz2_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.z[i] - z1) / (z2 - z1));
                    g.drawLine(xx1, zz1_1, xx2, zz2_1);
                    xx1 = xx2;
                    zz1_1 = zz2_1;
                }
                break;
        }
    }
    public void paint_curve(Graphics g, int type) {
        
        int yy1 = HEIGHT - (int) Math.floor(HEIGHT * (container.result_y[0] - y1) / (y2 - y1));
        int xx1 = (int) Math.floor(WIDTH * (container.result_x[0] - x1) / (x2 - x1));
        int zz1_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.result_z[0] - z1) / (z2 - z1));
        int zz1_2 = (int) Math.floor(WIDTH * (container.result_z[0] - z1) / (z2 - z1));
        switch (type) {
            case 0://(vertical-y ,horizontal -x)
                for (int i = 1; i < container.result_x.length; i++) {
                    int xx2 = (int) Math.floor(WIDTH * (container.result_x[i] - x1) / (x2 - x1));
                    int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.result_y[i] - y1) / (y2 - y1));
                    g.drawLine(xx1, yy1, xx2, yy2);
                    xx1 = xx2;
                    yy1 = yy2;
                }
                break;
            case 1://(vertical-y ,horizontal -z)
                for (int i = 1; i < container.result_x.length; i++) {
                    int zz2_2 = (int) Math.floor(WIDTH * (container.result_z[i] - z1) / (z2 - z1));
                    int yy2 = HEIGHT - (int) Math.floor(HEIGHT * (container.result_y[i] - y1) / (y2 - y1));
                    g.drawLine(zz1_2, yy1, zz2_2, yy2);
                    zz1_2 = zz2_2;
                    yy1 = yy2;
                }
                break;
            case 2://(vertical-z ,horizontal -x)
                for (int i = 1; i < container.result_x.length; i++) {
                    int xx2 = (int) Math.floor(WIDTH * (container.result_x[i] - x1) / (x2 - x1));
                    int zz2_1 = HEIGHT - (int) Math.floor(HEIGHT * (container.result_z[i] - z1) / (z2 - z1));
                    g.drawLine(xx1, zz1_1, xx2, zz2_1);
                    xx1 = xx2;
                    zz1_1 = zz2_1;
                }
                break;
        }

    }
    


    public double step_estimation(double x, double y) {
        if((x==y)){
            return 1;
        }
        double temp_delta = y - x;
        double k = 1;
        Boolean flag = false;
        do {
            if (((int) (temp_delta) / 10) == 1) {
                flag = true;

            } else {
                if ((((int) (temp_delta) / 10) == 0) && ((((int) (temp_delta) % 10) == 0))) {

                    k = k / 10;
                    temp_delta = temp_delta * 10;
                } else {
                    if ((((int) (temp_delta) / 10) == 0) && (!(((int) (temp_delta) % 10) == 0))) {
                        flag = true;

                    } else {
                        temp_delta = (double) temp_delta / 10;
                        k = k * 10;
                    }
                }

            }
        } while (!flag);
        return k;
    }
}



