public class Simple_bspline {

    int n;
    int m;

    Simple_bspline(int a0, int a1){
         this.n = a0; this.m = a1;
    }

    double result(double a, double[] u){
        double b;//SQF[m,n]

        if(!(m==0)) {
            if ((!(u[n + m] == u[n])) && (!(u[n + m + 1] == u[n + 1]))) {
                b = new Simple_bspline(this.n, this.m - 1).result(a, u) * (a - u[n]) / (u[n + m] - u[n]) + new Simple_bspline(this.n + 1, this.m - 1).result(a, u) * (u[n + m + 1] - a) / (u[n + m + 1] - u[n + 1]);
                return b;
            }
            else {
                if(!(u[n + m] == u[n])){
                    b = new Simple_bspline(this.n, this.m - 1).result(a, u) * (a - u[n]) / (u[n + m] - u[n]) ;
                    return b;
                }
                else {
                    if(!(u[n + m + 1] == u[n + 1])){

                        b = new Simple_bspline(this.n + 1, this.m - 1).result(a, u) * (u[n + m + 1] - a) / (u[n + m + 1] - u[n + 1]);
                        return b;
                    }
                    else {
                        return 0;
                    }
                }

            }

        }
        else{
            if((a>=u[n]) && (a<=u[n+1])) {
                return 1;
            }
            else{
                return 0;
            }
        }

    }


}
