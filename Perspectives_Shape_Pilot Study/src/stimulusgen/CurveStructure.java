package stimulusgen;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import perspectives.util.SplineFactory;

public class CurveStructure {
	public Point origin;
	private  Point controlPoint[];
	public Point drawPoint[];
	public CurveStructure(Point origin, Point controlPoint[]){
		this.origin=origin;
		this.controlPoint=controlPoint;
		int controlLength=(controlPoint.length)*3;
		double[] control=new double[controlLength];
		int j=0;
		for(int i=0;i<controlLength;i=i+3){
			
			Point p=controlPoint[j];
			control[i]=p.x;
			control[i+1]=p.y;
			control[i+2] = 0;
			j++;
						
		}
		double[] spline = SplineFactory.createCatmullRom(control, 10);
		Point[] curvePoints = new Point[spline.length/3];
		
		for (int k=0; k<curvePoints.length; k++)
			curvePoints[k] = new Point((int)spline[3*k], (int)spline[3*k+1]);
		
		this.drawPoint=curvePoints;
		
	}
	private Point2D[] createApproximation()
	{
		Point2D[] approx = new Point2D[drawPoint.length];
		
		for(int i=0;i<drawPoint.length;i++){
					
					Point p=drawPoint[i];
					approx[i] = new Point2D.Double(p.x + origin.x, p.y + origin.y);
		
		}
		
		return approx;
	}
	Point2D[] approx;
	public Point2D[] getApproximation()
	{
		if (approx == null) approx = createApproximation();
		return approx;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str="origin:("+origin.x+","+origin.y+")";
		String str2= "Control Point :["+controlPoint.length+"] :";
		for(int i=0; i<controlPoint.length;i++){
			str2= str2+"("+controlPoint[i].x+","+controlPoint[i].y+"),";
		}
		return str+str2;
	}
	
	public double dist(CurveStructure other, boolean pow2)
	{
		Point2D[] a1 = this.getApproximation();
		Point2D[] a2 = other.getApproximation();
		
		double sumd = 0;
		for (int i=0; i<a1.length; i++)
		{
			double mind = 999999;
			for (int j=0; j<a2.length; j++)
			{
				double d= a1[i].distance(a2[j]);
				if (d < mind)
					mind = d;
			}
			if (!pow2)
			sumd += mind;
			else sumd += (mind*mind);
		}
		
		for (int i=0; i<a2.length; i++)
		{
			double mind = 999999;
			for (int j=0; j<a1.length; j++)
			{
				double d= a2[i].distance(a1[j]);
				if (d < mind)
					mind = d;
			}
			if (!pow2)
			sumd += mind;
			else sumd += (mind*mind);
		}
		
		if (!pow2)
		return sumd/(a1.length + a2.length);
		else
			return Math.pow(sumd/(a1.length + a2.length), 0.5);
		
	}
}
