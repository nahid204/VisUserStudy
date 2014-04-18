package stimulusgen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import perspectives.base.Property;
import perspectives.properties.PColor;
import perspectives.properties.PDouble;
import perspectives.properties.PInteger;
import perspectives.two_d.Viewer2D;
import perspectives.util.SplineFactory;

public class CurveViewer extends Viewer2D{
	public static final String PROPERTY_NAME_MIN_DISTANCE = "Distance.Minimum Distance";
	public static final String PROPERTY_NAME_MAX_DISTANCE = "Distance.Maximum Distance";
	public static final String PROPERTY_NAME_CONTROL_POINT_COUNT = "Control Point Count";
	public static final String PROPERTY_NAME_CURVE_COUNT = "Curve Count";
	
	public static final String PROPERTY_NAME_COLOR1 = "Color1";
	public static final String PROPERTY_NAME_COLOR2 = "Color2";
	public static final String PROPERTY_NAME_SEGMENT1 = "Segment1";
	public static final String PROPERTY_NAME_SEGMENT2 = "Segment2";
	public static final String PROPERTY_NAME_SIMILARITY_LEVEL = "Similarity Level";
	
	
	private CurveShape[] curves;
	public CurveViewer(String name) {
		super(name);
		this.loadProperties();
		positionLayout();
		// TODO Auto-generated constructor stub
	}
	
	private void loadProperties()
	{
		try {
			Property<PInteger> minDistance = new Property<PInteger>(PROPERTY_NAME_MIN_DISTANCE, new PInteger(50))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								positionLayout();
								return true;
							}
					};
			this.addProperty(minDistance);
			
			Property<PInteger> maxDistance = new Property<PInteger>(PROPERTY_NAME_MAX_DISTANCE, new PInteger(100))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							positionLayout();
							return true;
						}
					};
			this.addProperty(maxDistance);
			
					
			Property<PInteger> controlPointCount = new Property<PInteger>(PROPERTY_NAME_CONTROL_POINT_COUNT, new PInteger(5))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							positionLayout();
							return true;
						}
					};
			this.addProperty(controlPointCount);
			
			Property<PInteger> curveCount = new Property<PInteger>(PROPERTY_NAME_CURVE_COUNT, new PInteger(2))
				{
					@Override
					protected boolean updating(PInteger newvalue) {
						// TODO Auto-generated method stub
						positionLayout();
						return true;
					}
				};
			this.addProperty(curveCount);
			
			Property<PColor> color1 = new Property<PColor>(PROPERTY_NAME_COLOR1 ,new PColor(Color.blue))
				{
					@Override
					protected boolean updating(PColor newvalue) {
						// TODO Auto-generated method stub
						positionLayout();
						return true;
					}
				};
			this.addProperty(color1);
			
			Property<PColor> color2 = new Property<PColor>(PROPERTY_NAME_COLOR2 ,new PColor(Color.gray))
				{
					@Override
					protected boolean updating(PColor newvalue) {
						// TODO Auto-generated method stub
						positionLayout();
						return true;
					}
				};
			this.addProperty(color2);
			
			Property<PDouble> segment1 = new Property<PDouble>(PROPERTY_NAME_SEGMENT1,new PDouble(0.5))
					{
						@Override
						protected boolean updating(PDouble newvalue) {
							positionLayout();
							double newValue = ((PDouble) newvalue).doubleValue();
							double segment2Val = getPropertyDoubleValue(PROPERTY_NAME_SEGMENT2);
							if(newValue < 0)
							{
								PDouble val = new PDouble(0.0);
								
								this.setValue(val);
							}
							else if(newValue > 1)
							{
								PDouble val = new PDouble(1.0);
								this.setValue(val);
							}
							else if(newValue >segment2Val)
							{
								PDouble val = new PDouble(0.0);
								this.setValue(val);
							}
							return true;
						}
				
					};
			this.addProperty(segment1);
			
			Property<PDouble> segment2 = new Property<PDouble>(PROPERTY_NAME_SEGMENT2,new PDouble(0.7))
				{
					protected boolean updating(PDouble newvalue) {
						positionLayout();
						double newValue = ((PDouble) newvalue).doubleValue();
						double segment1Val = getPropertyDoubleValue(PROPERTY_NAME_SEGMENT1);
						if(newValue < 0)
						{
							PDouble val = new PDouble(0.0);
							this.setValue(val);
						}
						else if(newValue > 1)
						{
							PDouble val = new PDouble(1.0);
							this.setValue(val);
						}
						else if(newValue < segment1Val)
						{
							PDouble val = new PDouble(1.0);
							this.setValue(val);
						}
						return true;
					}	
				};
			this.addProperty(segment2);
			Property<PInteger> similarityLevel = new Property<PInteger>(PROPERTY_NAME_SIMILARITY_LEVEL, new PInteger(50))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								positionLayout();
								return true;
							}
					};
			this.addProperty(similarityLevel);		
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
	}
	private int getPropertyIntValue(String name)
	{
		Property<PInteger> prop = this.getProperty(name);
		return prop.getValue().intValue();
	}
	
	private double getPropertyDoubleValue(String name)
	{
		Property<PDouble> prop = this.getProperty(name);
		return prop.getValue().doubleValue();
	}
	
	private Color getPropertyColorValue(String name)
	{
		Property<PColor> prop = this.getProperty(name);
		return prop.getValue().colorValue();
	}
	
	private void positionLayout()
	{
		
		int minDist = this.getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
		int maxDist = this.getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
		int controlPointCount = this.getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
		int curveCount = this.getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
		Color color1=this.getPropertyColorValue(PROPERTY_NAME_COLOR1);
		Color color2=this.getPropertyColorValue(PROPERTY_NAME_COLOR2);
		double segment1=this.getPropertyDoubleValue(PROPERTY_NAME_SEGMENT1);
		double segment2=this.getPropertyDoubleValue(PROPERTY_NAME_SEGMENT2);
		
		this.curves = new CurveShape[curveCount];
		Random random = new Random();
		int selectionIndex = (int) Math.abs(random.nextInt()) % curveCount;
		for(int i=0;i<this.curves.length;i++)
		{
			StimulusGenPlotter plotter = this.getPlotter(controlPointCount, minDist, maxDist);
			Point[] curvePoints = this.createCurveXY(plotter);
			boolean selection = false;
			if(i == selectionIndex)
			{
				selection = true;
			}
			CurveShape curve = new CurveShape(curvePoints, color1, color2, segment1, segment2, selection);
			this.curves[i] = curve;
		}
		
	}
	
	private Point[] createCurveXY(StimulusGenPlotter plotter)
	{
		
		int controlLength=(plotter.getObjectCount())*3;
		double[] control=new double[controlLength];
		int minX =0;
		int maxX = 1200;
		int x=minX;
		int step =( maxX - minX )/ plotter.getObjectCount();
		int j=1;
		Point p=null;
		//Randomize the first point
		Random random= new Random();
		x+= step;
		control[0]=x;
		control[1]=random.nextInt()%300;
		control[2]=0;
		for(int i=3;i<controlLength;i=i+3){
			
			if(j<plotter.getObjectCount()){
				p =plotter.getPosition(j);
			}
			x+= step;
			control[i]=x;
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
		
		return curvePoints;
	}
			
	private StimulusGenPlotter getPlotter(int objectCount, int minDistance, int maxDistance)
	{
		StimulusGenPlotter plotter = new RadialDistancePlotter(objectCount, minDistance, maxDistance);
		
		return plotter;
	}		
	
	
	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		if(this.curves != null)
		{
			for(int i=0;i< this.curves.length;i++)
			{	
				if(this.curves[i] != null)
				{
					this.curves[i].render(g);
				}
				
			}
		}
		
		
		
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

}
