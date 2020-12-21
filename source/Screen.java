import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Screen extends JPanel implements KeyListener, MouseMotionListener, MouseWheelListener{


	static ArrayList<DPrimitive> DPrimitives = new ArrayList<DPrimitive>();

	static ArrayList<OXYZ> oxyzs = new ArrayList<OXYZ>();
	static ArrayList<spline> splines = new ArrayList<spline>();
	ArrayList<Color> colors = new ArrayList<Color>();

	static Boolean isFrontView = false, isTopView = false, isSideView = false;
	
	static Boolean isPerspectiveMode = true;
	static int OX, OY;

	//limits
	static double x1,x2,y1,y2,z1,z2;

	//grid step
	static double step_x,step_y, step_z;

	//Used for keeping mouse in center
	Robot r;

	static double	[] ViewFrom = new double[] {10, 10, 10},
					ViewTo = new double[] {1, 1, 1};


	double unPerspectivezoom =20000,  unPerspectiveMinZoom = 2001,  unPerspectiveMaxZoom = 250000000;
	static double startingzoom=500;
	static double zoom = 500, MinZoom =201, MaxZoom = 250000000, MouseX = 0, MouseY = 0, MovementSpeed = 0.05;//old 0.5

	int WIDTH;
	int HEIGHT;
	double drawFPS = 0, MaxFPS = 1000,  LastRefresh = 0, LastFPSCheck = 0, Checks = 0;


	double VertLook = -1/Math.sqrt(3)+0.000001, HorLook = -3*Math.PI/4, aimSight = 4, HorRotSpeed = 900, VertRotSpeed = 2200, SunPos = 0;


	static boolean OutLines = true;
	boolean[] Keys = new boolean[4];

	  double Min_x = 0, Min_y=0, Min_z=0, Max_x=1,Max_y=1,Max_z=1;


	public Screen()
	{
		unPerspectivezoom =20000;
		zoom = 500;
		isPerspectiveMode = true;
		isFrontView = false; isTopView = false; isSideView = false;
		ViewFrom = new double[] {10, 10, 10};
		ViewTo = new double[] {1, 1, 1};

		 VertLook = -1/Math.sqrt(3)+0.000001; HorLook = -3*Math.PI/4;
		this.addKeyListener(this);
		setFocusable(true);


		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);


		invisibleMouse();
		colors.add(Color.red);
		colors.add(Color.blue);
		colors.add(Color.green);
		colors.add(Color.magenta);
		colors.add(Color.yellow);
		colors.add(Color.black);
		colors.add(Color.orange);


		oxyzs.add(new OXYZ(-20,-20,-20,10,10,10));
		int color_i=0;
		for(curve_calculation o : My_in.curves) {
			splines.add(new spline(o.result_x, o.result_y, o.result_z, colors.get(color_i)));
			color_i++;
		}
		double maxx, minx, maxy, miny, maxz, minz;

		for (spline b:
				splines) {
			maxx = b.x[0];
			minx = b.x[0];
			maxy = b.y[0];
			miny = b.y[0];
			maxz = b.z[0];
			minz = b.z[0];
			for (int i = 0; i < b.x.length; i++) {
				if (b.x[i] > maxx) maxx = b.x[i];
				if (b.y[i] > maxy) maxy = b.y[i];
				if (b.z[i] > maxz) maxz = b.z[i];
				if (b.x[i] < minx) minx = b.x[i];
				if (b.y[i] < miny) miny = b.y[i];
				if (b.z[i] < minz) minz = b.z[i];
			}
			if (maxx > Max_x) Max_x = maxx;
			if (maxy > Max_y) Max_y = maxy;
			if (maxz > Max_z) Max_z = maxz;
			if (minx < Min_x) Min_x = minx;
			if (miny < Min_y) Min_y = miny;
			if (minz < Min_z) Min_z = minz;

		}
		this.x1 = (Min_x - (Max_x - Min_x) * 0.1);
		this.x2 = (Max_x + (Max_x - Min_x) * 0.1);
		this.y1 = (Min_y - (Max_y - Min_y) * 0.1);
		this.y2 = (Max_y + (Max_y - Min_y) * 0.1);
		this.z1 = (Min_z - (Max_z - Min_z) * 0.1);
		this.z2 = (Max_z + (Max_z - Min_z) * 0.1);

		
		step_x = step_estimation(x1,x2);
		step_y = step_estimation(y1,y2);
		step_z = step_estimation(z1,z2);
	}	
	
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(140, 180, 180));
		g.fillRect(0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		CameraMovement();

		Calculator.SetPrederterminedInfo();


		paintD(g);

			for (int i = 0; i < DPrimitives.size(); i++)
				DPrimitives.get(i).updatePrimitive();

			for (int i = 0; i < DPrimitives.size(); i++) {
				DPrimitives.get(i).DrawablePrimitive.drawPrimitive(g);
			}


		drawMouseAim(g);
		setlabels(g);
		//FPS display
		g.drawString("FPS: " + (int)drawFPS + " (Benchmark)", 40, 40);
		g.drawString("HorLook : " + HorLook + " (Benchmark)", 40, 60);
		g.drawString("VertLook : " + VertLook + " (Benchmark)", 40, 80);
		g.drawString("zoom : " + zoom + " (Benchmark)", 40, 120);

		SleepAndRefresh();
	}


	void setlabels(Graphics g){
		if(!(isFrontView||isTopView||isSideView)){
			if((Math.pow(DPrimitives.get(0).DrawablePrimitive.P[0].x-DPrimitives.get(0).DrawablePrimitive.P[1].x, 2)+Math.pow(DPrimitives.get(0).DrawablePrimitive.P[0].y-DPrimitives.get(0).DrawablePrimitive.P[1].y, 2))>(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())
			g.drawString("y",DPrimitives.get(0).DrawablePrimitive.P[0].x+((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100),DPrimitives.get(0).DrawablePrimitive.P[0].y);
			if((Math.pow(DPrimitives.get(1).DrawablePrimitive.P[0].x-DPrimitives.get(1).DrawablePrimitive.P[1].x, 2)+Math.pow(DPrimitives.get(1).DrawablePrimitive.P[0].y-DPrimitives.get(1).DrawablePrimitive.P[1].y, 2))>(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())
			g.drawString("z",DPrimitives.get(1).DrawablePrimitive.P[0].x+((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100),DPrimitives.get(1).DrawablePrimitive.P[0].y);
			if((Math.pow(DPrimitives.get(2).DrawablePrimitive.P[0].x-DPrimitives.get(2).DrawablePrimitive.P[1].x, 2)+Math.pow(DPrimitives.get(2).DrawablePrimitive.P[0].y-DPrimitives.get(2).DrawablePrimitive.P[1].y, 2))>(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth())
			g.drawString("x",DPrimitives.get(2).DrawablePrimitive.P[0].x+((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100),DPrimitives.get(2).DrawablePrimitive.P[0].y);
		}
		else{
			if(isFrontView){
				VertLook =0;
				HorLook =Math.PI;
			}
			if(isTopView){
				VertLook = -1+0.000000000001;
				HorLook = 0;
			}
			if(isSideView){
				VertLook = 0;
				HorLook = -(Math.PI/2)+0.000000001;
			}

		}



	}
		
	void invisibleMouse()
	{
		 Toolkit toolkit = Toolkit.getDefaultToolkit();
		 BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
		 Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");        
		 setCursor(invisibleCursor);
	}
	
	void drawMouseAim(Graphics g)
	{
		g.setColor(Color.black);
		g.drawLine((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - aimSight), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 + aimSight), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2));
		g.drawLine((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - aimSight), (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 + aimSight));
	}

	void SleepAndRefresh()
	{
		long timeSLU = (long) (System.currentTimeMillis() - LastRefresh); 

		Checks ++;			
		if(Checks >= 15)
		{
			drawFPS = Checks/((System.currentTimeMillis() - LastFPSCheck)/1000.0);
			LastFPSCheck = System.currentTimeMillis();
			Checks = 0;
		}
		
		if(timeSLU < 1000.0/MaxFPS)
		{
			try {
				Thread.sleep((long) (1000.0/MaxFPS - timeSLU));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
				
		LastRefresh = System.currentTimeMillis();
		repaint();
	}

	
	void CameraMovement() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
        double xMove = 0, yMove = 0, zMove = 0;
        Vector VerticalVector = new Vector(0, 0, 1);
        Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);
        if (isPerspectiveMode){
            if (Keys[0]) {
                xMove += ViewVector.x;
                yMove += ViewVector.y;
                zMove += ViewVector.z;
            }

        if (Keys[2]) {
            xMove -= ViewVector.x;
            yMove -= ViewVector.y;
            zMove -= ViewVector.z;
        }

        if (Keys[1]) {
            xMove += SideViewVector.x;
            yMove += SideViewVector.y;
            zMove += SideViewVector.z;
        }

        if (Keys[3]) {
            xMove -= SideViewVector.x;
            yMove -= SideViewVector.y;
            zMove -= SideViewVector.z;
        }
    }
		else{

            if(Keys[0]){
                if(isFrontView){
                    z1+=0.0562*step_z;
                    z2+=0.0562*step_z;
                }
                if(isTopView||isSideView){
                    y1+=0.0562*step_y;
                    y2+=0.0562*step_y;
                }

            }
            if(Keys[2]){
                if(isFrontView){
                    z1-=0.0562*step_z;
                    z2-=0.0562*step_z;
                }
                if(isTopView||isSideView){
                    y1-=0.0562*step_y;
                    y2-=0.0562*step_y;
                }
            }
            if(Keys[3])
            {
                if(isFrontView||isTopView) {
                    x1 += 0.0562*step_x;
                    x2 += 0.0562*step_x;
                }
                if(isSideView){
                    z1+=0.0562*step_z;
                    z2+=0.0562*step_z;
                }
            }

            if(Keys[1]) {
                if (isFrontView||isTopView) {
                    x1 -= 0.0562*step_x;
                    x2 -= 0.0562*step_x;
                }
                if(isSideView){
                    z1-=0.0562*step_z;
                    z2-=0.0562*step_z;
                }
            }
		}
		Vector MoveVector = new Vector(xMove, yMove, zMove);
		MoveTo(ViewFrom[0] + MoveVector.x * MovementSpeed, ViewFrom[1] + MoveVector.y * MovementSpeed, ViewFrom[2] + MoveVector.z * MovementSpeed);
	}

	void MoveTo(double x, double y, double z)
	{
		ViewFrom[0] = x;
		ViewFrom[1] = y;
		ViewFrom[2] = z;
		updateView();
	}

	void MouseMovement(double NewMouseX, double NewMouseY)
	{		
			double difX = (NewMouseX - Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
			double difY = (NewMouseY - Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
			difY *= 6 - Math.abs(VertLook) * 5;
			VertLook -= difY  / VertRotSpeed;
			HorLook += difX / HorRotSpeed;
	
			if(VertLook>0.99999999)
				VertLook = 0.999999999;
	
			if(VertLook<-0.99999999)
				VertLook = -0.99999999;
			
			updateView();
	}
	
	void updateView()
	{
		double r = Math.sqrt(1 - (VertLook * VertLook));
		ViewTo[0] = ViewFrom[0] + r * Math.cos(HorLook);
		ViewTo[1] = ViewFrom[1] + r * Math.sin(HorLook);
		ViewTo[2] = ViewFrom[2] + VertLook;
	}
	
	void CenterMouse() 
	{
			try {
				r = new Robot();
				r.mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
			} catch (AWTException e) {
				e.printStackTrace();
			}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			My_in.F.setVisible(false);
			My_in.F.removeAll();
			My_in.F.dispose();


		}
		if (e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = true;
		if (e.getKeyCode() == KeyEvent.VK_O)
			OutLines = !OutLines;
		if (e.getKeyCode() == KeyEvent.VK_F) {

			zoom=unPerspectivezoom;
			isPerspectiveMode = false;
			isTopView = false;
			isSideView = false;
			isFrontView = true;
			ViewFrom = new double[]{500, 0, 0};

		}
		if (e.getKeyCode() == KeyEvent.VK_T) {
			zoom=unPerspectivezoom;
            isPerspectiveMode = false;
			isFrontView = false;
			isSideView = false;
			isTopView = true;
			ViewFrom = new double[]{0, 0, 500};
		}
		if (e.getKeyCode() == KeyEvent.VK_K) {
			zoom=unPerspectivezoom;
			isFrontView = false;
			isTopView = false;
			isSideView = true;
            isPerspectiveMode = false;
			ViewFrom = new double[]{0, 500, 0};

		}


		if (e.getKeyCode() == KeyEvent.VK_0) {

			VertLook = -1/Math.sqrt(3)+0.000001; HorLook = -3*Math.PI/4;
			isFrontView = false;
			isTopView = false;
			isSideView = false;
			isPerspectiveMode=true;
			zoom= startingzoom;
            ViewFrom = new double[]{10,10,10};
		}


	}
		
		


	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = false;
		if(e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = false;
		if(e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = false;
		if(e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseDragged(MouseEvent arg0) {
		MouseMovement(arg0.getX(), arg0.getY());
		MouseX = arg0.getX();
		MouseY = arg0.getY();
		CenterMouse();
	}
	
	public void mouseMoved(MouseEvent arg0) {
		MouseMovement(arg0.getX(), arg0.getY());
		MouseX = arg0.getX();
		MouseY = arg0.getY();
		CenterMouse();
	}



	public void mouseWheelMoved(MouseWheelEvent arg0) {
		int rot = arg0.getWheelRotation();

		if(!isPerspectiveMode) {
			double delta = (zoom -250*rot)/zoom;
			if(rot>0){
				if (zoom > unPerspectiveMinZoom) {
					zoom -= 250 * rot;
				}}
			else {
				if (zoom < unPerspectiveMaxZoom) {

					zoom -= 250 * rot;
				}
			}
			if (((zoom-250*rot) > unPerspectiveMinZoom)&&((zoom-250*rot) < unPerspectiveMaxZoom)) {
				x1 = (Max_x - Min_x) / 2 - ((Max_x - Min_x) / 2 - x1) / delta;
				x2 = (Max_x - Min_x) / 2 + (-(Max_x - Min_x) / 2 + x2) / delta;
				y1 = (Max_y - Min_y) / 2 - ((Max_y - Min_y) / 2 - y1) / delta;
				y2 = (Max_y - Min_y) / 2 + (-(Max_y - Min_y) / 2 + y2) / delta;
				z1 = (Max_z - Min_z) / 2 - ((Max_z - Min_z) / 2 - z1) / delta;
				z2 = (Max_z - Min_z) / 2 + (-(Max_z - Min_z) / 2 + z2) / delta;
			}
			unPerspectivezoom = zoom;
		}
		else{

			if(rot>0){
				if (zoom > MinZoom) {
					zoom -= 25 * rot;
				}}
			else {
				if (zoom < MaxZoom) {

					zoom -= 25 * rot;
				}
			}

		}
			repaint();
		}


	public void paintD(Graphics g){


		HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Font temp  = g.getFont();
		g.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
		int tempOX = OX, tempOY = OY;
		if(OX<0){
			tempOX = 0;
		}
		else if(OX>WIDTH)
		{
			tempOX = WIDTH-45;
		}
		if(OY<0){
			tempOY = 45;
		}
		else if(OY>HEIGHT)
		{
			tempOY = HEIGHT;
		}
		if(isTopView) {
			paintD(g, step_x, step_y, x1, x2, y1, y2);

			g.drawString("y" , tempOX+15, 15);
			g.drawString("x" , WIDTH-15, tempOY-15);
		}
		if(isSideView) {
			paintD(g, step_z, step_y, z1, z2, y1, y2);
			g.drawString("y" , tempOX+15, 15);
			g.drawString("z" , WIDTH-15, tempOY-15);
		}
		if(isFrontView){
				paintD(g,step_x,  step_z,  x1,  x2,  z1,  z2);
			g.drawString("z" , tempOX+15, 15);
			g.drawString("x" , WIDTH-15, tempOY-15);
		}

		g.setFont(temp);
	}
	public void paintD(Graphics g,double step_hor, double step_ver, double point_1_hor, double point_2_hor, double point_1_ver, double point_2_ver) {
		g.setColor(Color.GRAY);

		OX = (int) (-point_1_hor / (point_2_hor - point_1_hor) * WIDTH);
		OY = (int) (HEIGHT + point_1_ver / (point_2_ver - point_1_ver) * HEIGHT);

		for (int i = (int) Math.floor(point_1_hor / step_hor); i <= Math.floor(point_2_hor / step_hor); i++) {
			int positionX = (int) (-(point_1_hor - step_hor * i) / (point_2_hor - point_1_hor) * WIDTH);

			g.drawLine(positionX, 0, positionX, HEIGHT);

			int positionY;

			if (OY < 12) {
				positionY = 10;
			} else if (OY > HEIGHT - 2) {
				positionY = HEIGHT - 2;
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

			int len = (int) new TextLayout(formated_value, g.getFont(), new FontRenderContext(null, true, true)).getBounds().getWidth();

			if (OX < 1) {
				positionX = 2;
			} else if (OX > WIDTH - 7 - len) {
				positionX = WIDTH - 5 - len;
			} else {
				positionX = OX + 2;
			}

			g.drawString(formated_value, positionX, positionY - 1);
		}

		g.setColor(Color.BLACK);
		g.fillRect(OX - 1, 0, 3, HEIGHT);
		g.fillRect(0, OY - 1, WIDTH, 3);

	}


	public double step_estimation(double x, double y) {
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
