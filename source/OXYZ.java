import java.awt.*;
import java.lang.reflect.Array;

public class OXYZ {

Color c = Color.black;
    double step;

    public OXYZ(double minx, double miny, double minz, double maxx, double maxy, double maxz){
        double max = maxx, min = minx;
        if(max< maxy) max = maxy;
        if(max< maxz) max = maxz;
        if(min< maxy) min = miny;
        if(min< maxz) min = minz;
        step =1.0;
        Boolean flag =true;
        do{
            if(!((int)(max-min)/10==0)){
                if((((int)(max-min)/10==0)&&((int)(max-min)%10==0))){
                    step=step/10;
                    max = max*10;
                    min = min*10;
                }
                else
                if((int)(max-min)/10>1){
                    step=step*10;
                    max = max/10;
                    min = min/10;
                }
            }

        }while(!flag);


       Screen.DPrimitives.add(new DPrimitive(new double[]{0,0}, new double[]{step,-step}, new double[]{0,0}, c, false));
        Screen.DPrimitives.add(new DPrimitive(new double[]{0,0}, new double[]{0,0}, new double[]{step,-step}, c, false));
        Screen.DPrimitives.add(new DPrimitive(new double[]{step, -step}, new double[]{0,0}, new double[]{0,0}, c, false));
        Arrow();
    }
public void Arrow(){

    Screen.DPrimitives.add(new DPrimitive(new double[]{step/40,0,-step/40}, new double[]{step-(step/20),step, step-(step/20)}, new double[]{0,0,0}, c, false));
    Screen.DPrimitives.add(new DPrimitive(new double[]{0,0,0}, new double[]{step-(step/20),step, step-(step/20)}, new double[]{step/40,0,-step/40}, c, false));
    Screen.DPrimitives.add(new DPrimitive(new double[]{0,0,0}, new double[]{step/40,0,-step/40}, new double[]{step-(step/20),step, step-(step/20)}, c, false));
    Screen.DPrimitives.add(new DPrimitive(new double[]{step/40,0,-step/40}, new double[]{0,0,0}, new double[]{step-(step/20),step, step-(step/20)}, c, false));
    Screen.DPrimitives.add(new DPrimitive(new double[]{step-(step/20),step, step-(step/20)}, new double[]{0,0,0}, new double[]{step/40,0,-step/40}, c, false));
    Screen.DPrimitives.add(new DPrimitive(new double[]{step-(step/20),step, step-(step/20)}, new double[]{step/40,0,-step/40}, new double[]{0,0,0}, c, false));
}

}
