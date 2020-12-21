import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class curve_calculation {
    int n;//степень кривой
    double[] x,y,z;
    double[] result_x, result_y, result_z;
    double[] weight;
    double[] parametrize;
    double beta1 , beta2;

    public static int getFactorial(int f) {
        if(f==0) {return 1;}
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result*i;
        }
        return result;
    }


    public static ArrayList<curve_calculation> construct_all(ArrayList<double []> x, ArrayList<double []>  y, ArrayList<double []>  z, ArrayList<double []>  w, ArrayList<double []> p, ArrayList<Integer> power)
    {
        ArrayList<curve_calculation> temp = new ArrayList<curve_calculation>();
        Iterator<double[]> ix= x.iterator();
        Iterator<double[]> iy = y.iterator();
        Iterator<double[]> iz = z.iterator();
        Iterator<double[]> ip = p.iterator();
        Iterator<Integer> ipower = power.iterator();
        Iterator<double[]> iw = w.iterator();
        while(ix.hasNext() && iy.hasNext()&& iz.hasNext()&& ip.hasNext()&& iw.hasNext()&& ipower.hasNext())
        {
            temp.add(new curve_calculation(ipower.next(),ix.next(), iy.next(),iz.next(),iw.next(),ip.next()));

        }
        return temp;
    }
    curve_calculation(int n,double[] x,double[] y,double[] z,double[] w,double[] p){
        this.n = n;
        this.x = x;
        this.y = y;
        this.z = z;
        this.weight = w;
        this.parametrize = p;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public void setZ(double[] z) {
        this.z = z;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public void setParametrize(double[] parametrize) {
        this.parametrize = parametrize;
    }

    public void setAll(int m,int n,double[] x,double[] y,double[] z,double[] w,double[] p){
        this.n = n;
        this.x = x;
        this.y = y;
        this.z = z;
        this.weight = w;
        this.parametrize = p;
        
    }
    public void setAll(double[] x,double[] y,double[] z){
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public double B_spline(double u, double[] up) {
        double temp_result = 0;

        for (int i = 0; i < up.length; i++) {

            Simple_bspline temp_func = new Simple_bspline(i, n);
            temp_result = temp_result + (temp_func.result(u, parametrize) * up[i]);
        }
        return temp_result;


    }

    public void closed_or_periodic_B_spline() {

        int segment  = (int) (((parametrize[parametrize.length - 1-n] - parametrize[n])/0.01));
        result_x = new double[segment];
        result_y = new double[segment];
        result_z = new double[segment];
        int j =0;
        for (double i = parametrize[n]+0.01; i < parametrize[parametrize.length-1 -n]+0.001; i = i + 0.01,j++) {
            result_x[j] = B_spline(i, x);
            result_y[j] = B_spline(i, y);
            result_z[j] = B_spline(i, z);

        }
    }

    public void opened_B_spline() {
        int segment  = (int) (((parametrize[parametrize.length - n -  1] - parametrize[n])/0.01));
        result_x = new double[segment+1];
        result_y = new double[segment+1];
        result_z = new double[segment+1];
        int j =0;
        for (double i = parametrize[n]; i < parametrize[parametrize.length - 1 - n]+0.001; i = i + 0.01,j++) {
            result_x[j] = B_spline(i, x);
            result_y[j] = B_spline(i, y);
            result_z[j] = B_spline(i, z);

        }
    }

    void Bezier(){
        int j=0;
        result_x=new double[201];
        result_y=new double[201];
        result_z = new double[201];
        for(double t=0; t<1.005; t=t+0.005) {
            double _bx = 0, _by = 0, _bz = 0;

            for (int i = 0; i <= n; i++) {
                _bx = _bx + ((double) getFactorial(n) / (getFactorial(i) * getFactorial(n - i))) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * x[i] ;
                _by = _by + ((double) getFactorial(n) / (getFactorial(i) * getFactorial(n - i))) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * y[i] ;
                _bz = _bz + ((double) getFactorial(n) / (getFactorial(i) * getFactorial(n - i))) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * z[i] ;

            }

            result_x[j] = _bx;
            result_y[j] = _by;
            result_z[j] = _bz;
            j++;


        }

    }



    void rational_Bezier(){
        int j=0;
        int k=1;
        int kol = (int)Math.ceil(((double)x.length-1)/n);
        result_x=new double[201*kol];
        result_y=new double[201*kol];
        result_z=new double[201*kol];
        do {
            for(double t=0; t<1.005; t=t+0.005) {
                double _bx = 0, _by = 0, _bz = 0;
                double _b = 0;

                for (int i = (k - 1) * n; i <= k * n; i++) {
                    int temp = i;
                    if(i>=x.length-1){
                        temp=x.length-1;
                    }
                    _bx = _bx + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * x[temp]*weight[temp];
                    _by = _by + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * y[temp]*weight[temp];
                    _bz = _bz + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * z[temp]*weight[temp];
                    _b = _b + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) *weight[temp];

                }
                result_x[j] = _bx / _b;
                result_y[j] = _by / _b;
                result_z[j] = _bz / _b;
                j++;
            }
            k++;
        }while(x.length>=((k)*n));

    }


    void component_Bezier() {
        int j=0;
        int k=1;
        int kol = (int)Math.ceil(((double)x.length-1)/n);
        result_x=new double[201*kol];
        result_y=new double[201*kol];
        result_z = new double[201*kol];
        do {

            for(double t=0; t<1.005; t=t+0.005) {
                double _bx = 0, _by = 0, _bz = 0;

                for (int i = (k-1)*n; i <= k*n; i++) {

                    int temp = i;
                    if(i>=x.length-1){
                        temp=x.length-1;
                    }
                    _bx = _bx + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * x[temp];
                    _by = _by + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * y[temp];
                    _bz = _bz + ((double) getFactorial(n) / (getFactorial((i-(k-1)*n)) * getFactorial(n - (i-(k-1)*n)))) * Math.pow(t, (i-(k-1)*n)) * Math.pow((1 - t), (n - (i-(k-1)*n))) * z[temp];
                }
                result_x[j] = _bx;
                result_y[j] = _by;
                result_z[j] = _bz;
                j++;

            }
            k++;
        }while(x.length>=((k)*n));
    }


    public double component_Hermite(double P0, double P1, double Q0, double Q1, double t) {
        return(1-3*t*t+2*t*t*t)*P0+t*t*(3-2*t)*P1+t*(1-2*t+t*t)*Q0-t*t*(1-t)*Q1;
    }


    public void component_Hermite() {
        int kol = (x.length / 2);
        int segment = (int) (((1-0) / 0.005));
        result_x = new double[segment * (kol - 1) + 1];
        result_y = new double[segment * (kol - 1) + 1];
        result_z = new double[segment * (kol - 1) + 1];

        int j = 0;
        for (int J = 0; J < kol - 1; J++) {

            for (double i = 0; i < 1; i = i + 0.005) {
                result_x[j] = component_Hermite(x[J], x[J + 1], x[J + kol], x[J + kol + 1], i);
                result_y[j] = component_Hermite(y[J], y[J + 1], y[J + kol], y[J + kol + 1], i);
                result_z[j] = component_Hermite(z[J], z[J + 1], z[J + kol], z[J + kol + 1], i);
                j++;
            }

        }
        result_x[j] = x[kol - 1];
        result_y[j] = y[kol - 1];
        result_z[j] = z[kol - 1];

    }

    public double Beta_spline(double beta1,double beta2,double P0,double P1,double P2,double P3, double t) {
        double delta=2*beta1*beta1*beta1+4*beta1*beta1+4*beta1+beta2+2;
        double b0,b1,b2,b3;
        b0=2*beta1*beta1*beta1*(1-t)*(1-t)*(1-t)/delta;
        b1=(2*beta1*beta1*beta1*t*(t*t-3*t+3)+2*beta1*beta1*(t*t*t-3*t*t+2)+2*beta1*(t*t*t-3*t+2)+beta2*(2*t*t*t-3*t*t+1))/delta;
        b2=(2*beta1*beta1*t*t*(3-t)+2*beta1*t*(3-t*t)+2*beta2*t*t*(3-2*t)+2*(1-t*t*t))/delta;
        b3=2*t*t*t/delta;
        return b0*P0+b1*P1+b2*P2+b3*P3;
    }

    public void Beta_spline() {
        int kol = (x.length - 2);
        int segment = (int) (((1-0) / 0.005));
        result_x = new double[segment * (kol - 1) + 1];
        result_y = new double[segment * (kol - 1) + 1];
        result_z = new double[segment * (kol - 1) + 1];

        int j=0;
        for (int J =0; J < kol-1; J++) {
            for (double i = 0; i < 1; i = i + 0.005) {
                result_x[j]= Beta_spline(beta1,beta2,x[J],x[J+1],x[J+2],x[J+3],i);
                result_y[j] = Beta_spline(beta1,beta2,y[J],y[J+1],y[J+2],y[J+3],i);
                result_z[j] = Beta_spline(beta1,beta2,z[J],z[J+1],z[J+2],z[J+3],i);
                j++;
            }
        }
        result_x[j]=  Beta_spline(beta1,beta2,x[x.length-4],x[x.length-3],x[x.length-2],x[x.length-1],1);
        result_y[j] = Beta_spline(beta1,beta2,y[x.length-4],y[x.length-3],y[x.length-2],y[x.length-1],1);
        result_z[j] = Beta_spline(beta1,beta2,z[x.length-4],z[x.length-3],z[x.length-2],z[x.length-1],1);
    }

    public double Catmull_Rom(double P0,double P1,double P2,double P3, double t) {
        return ((-t)*(1-t)*(1-t)*P0+(2-5*t*t+3*t*t*t)*P1+t*(1+4*t-3*t*t)*P2-t*t*(1-t)*P3)/2;
    }

    public void Catmull_Rom() {
        int kol = (x.length);
        int segment = (int) (((1-0) / 0.005));

        result_x = new double[segment * (kol - 3) + 1];
        result_y = new double[segment * (kol - 3) + 1];
        result_z = new double[segment * (kol - 3) + 1];

        int j=0;
        for (int J =0; J < kol-3; J++) {
            for (double i = 0; i < 1; i = i + 0.005) {
                result_x[j]= Catmull_Rom(x[J],x[J+1],x[J+2],x[J+3],i);
                result_y[j] = Catmull_Rom(y[J],y[J+1],y[J+2],y[J+3],i);
                result_z[j] = Catmull_Rom(z[J],z[J+1],z[J+2],z[J+3],i);
                j++;
            }
        }
        result_x[j]=  Catmull_Rom(x[x.length-4],x[x.length-3],x[x.length-2],x[x.length-1],1);
        result_y[j] = Catmull_Rom(y[x.length-4],y[x.length-3],y[x.length-2],y[x.length-1],1);
        result_z[j] = Catmull_Rom(z[x.length-4],z[x.length-3],z[x.length-2],z[x.length-1],1);
    }

    public void NURBS() {
        int segment  = (int) (((parametrize[parametrize.length - 1] - parametrize[0])/0.01) + 1);
        result_x = new double[segment];
        result_y = new double[segment];
        result_z = new double[segment];
        int j =0;
        for (double i = parametrize[0]+0.01; i < parametrize[parametrize.length-1]; i=i+0.01, j++) {
            result_x[j] = NURBS(i, x);
            result_y[j] = NURBS(i, y);
            result_z[j] = NURBS(i, z);

        }
    }

    public double NURBS(double u,double[] up){
        double temp_result1 =0;
        double temp_result2 =0;
        for(int i=0;i<up.length;i++){
            Simple_bspline temp_func =  new Simple_bspline(i,n);
            temp_result1 = temp_result1+(temp_func.result(u, parametrize) * up[i]*weight[i]);
            temp_result2 = temp_result2+(temp_func.result(u, parametrize)*weight[i]);
        }

        return (temp_result1/temp_result2) ;
    }

}

