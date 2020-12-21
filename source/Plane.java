public class Plane {
	Vector V1, V2, NV;
	double[] P = new double[3];
	
	public Plane(Vector VE1, Vector VE2, double[] Z)
	{
		P = Z;
		
		V1 = VE1;
		
		V2 = VE2;
		
		NV = V1.CrossProduct(V2);
	}
}
