package stimulusgen;

import java.awt.Point;
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
			control[i+2]=0;	
			j++;
						
		}
		double[] spline = SplineFactory.createCatmullRom(control, 10);
		Point[] curvePoints = new Point[spline.length/3];
		
		for (int k=0; k<curvePoints.length; k++)
		{
			curvePoints[k] = new Point((int)spline[3*k], (int)spline[3*k+1]);
//			System.out.println("<Point x=\""+curveX[k]+"\" y=\""+curveY[k]+"\" />");
		}
		
		this.drawPoint=curvePoints;
		
	}
	private ArrayList<Double> createApproximation()
	{
		ArrayList<Double> approx = new ArrayList<Double>();
		
		for(int i=0;i<drawPoint.length;i++){
					
					Point p=drawPoint[i];
					approx.add((double) p.x);
					approx.add((double) p.y);
					approx.add((double) 0);
								
		}
		
		return approx;
	}
	ArrayList<Double> approx;
	public ArrayList<Double> getApproximation()
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
}
