package stimulusgen;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.text.MaskFormatter;

import perspectives.base.Property;
import perspectives.properties.PColor;
import perspectives.properties.PDouble;
import perspectives.properties.PInteger;
import perspectives.properties.PSignal;
import perspectives.two_d.Viewer2D;
import perspectives.util.SplineFactory;


public class CurveStructureViewer extends Viewer2D{
	public static final String PROPERTY_NAME_MIN_DISTANCE = "Distance.Minimum Distance";
	public static final String PROPERTY_NAME_MAX_DISTANCE = "Distance.Maximum Distance";
	public static final String PROPERTY_NAME_CONTROL_POINT_COUNT = "Control Point Count";
	public static final String PROPERTY_NAME_CURVE_COUNT = "Curve Count";
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	
	CurveStructure []curveSt=null;
	int taskCurve;
	
	public CurveStructureViewer(String name) {
		super(name);
		this.loadProperties();
		
		
		positionLayout();
		// TODO Auto-generated constructor stub
	}


	private void loadProperties()
	{
		try {
			Property<PInteger> minDistance = new Property<PInteger>(PROPERTY_NAME_MIN_DISTANCE, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								positionLayout();
								return true;
							}
					};
			this.addProperty(minDistance);
			
			Property<PInteger> maxDistance = new Property<PInteger>(PROPERTY_NAME_MAX_DISTANCE, new PInteger(500))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							positionLayout();
					
							return true;
						}
					};
			this.addProperty(maxDistance);
			
					
			Property<PInteger> controlPointCount = new Property<PInteger>(PROPERTY_NAME_CONTROL_POINT_COUNT, new PInteger(10))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							positionLayout();
						
							return true;
						}
					};
			this.addProperty(controlPointCount);
			
			Property<PInteger> curveCount = new Property<PInteger>(PROPERTY_NAME_CURVE_COUNT, new PInteger(3))
				{
					@Override
					protected boolean updating(PInteger newvalue) {
						// TODO Auto-generated method stub
						positionLayout();
					
						return true;
					}
				};
			this.addProperty(curveCount);
			
						
			Property<PInteger> similarityLevel = new Property<PInteger>(PROPERTY_NAME_DIFFICULTY_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								positionLayout();
								return true;
							}
					};
			this.addProperty(similarityLevel);	
			Property<PSignal> save = new Property<PSignal>("SaveStimulus", new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							// TODO Auto-generated method stub
							saveStimulus();
							return true;
						}
					};
			this.addProperty(save);
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
	}
	
	private void positionLayout()
	{
		System.out.println("Position Layout");
		int minDist = this.getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
		int maxDist = this.getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
		int controlPointCount = this.getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
		int curveCount = this.getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
		int difficultyLevel=this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL);
		
		ArrayList<CurveStructure> curves = new ArrayList<CurveStructure>();
				
		while (true)
		{
			while (curves.size() < curveCount)
			{
				//Random curve
				
				int minX =0;
				int maxX = 1200;
				int x=minX;
				int y=0;
				int step =( maxX - minX )/ controlPointCount;
				int difference=maxDist-minDist;
				Random random = new Random();
				Point startPoint=new Point(0,0);
				Point endPoint= new Point(0,0);
				if (curves.size() == 0)
				{
					int originx = 100;
					int originy = Math.abs(random.nextInt())% 100;
					Point randomOrigin= new Point(originx,originy);
					Point[]randomPoints  = new Point[controlPointCount];
					x=minX;
					y=(Math.abs(random.nextInt())% (maxDist - minDist))+minDist;
					startPoint= new Point(x,y);
					int j=0;
					randomPoints[j]=startPoint;
					for(j=1;j<controlPointCount-1;j++){
						x=j*step ;
						y = Math.abs(random.nextInt()% (maxDist - minDist))+minDist;
						Point rPoint= new Point(x,y);
						randomPoints[j]=rPoint;
					}
					x=maxX;
					y=Math.abs(random.nextInt()% (maxDist - minDist))+minDist;
					endPoint=new Point(x,y);
					randomPoints[controlPointCount-1]=endPoint;
					
					CurveStructure curve = new CurveStructure(randomOrigin, randomPoints);
					curves.add(curve);
				}
				else
				{
					//make sure endpoints are not very close to any other endpoints
					int up =controlPointCount*2;
					int down = controlPointCount/2;
					int diff = up-down;
					int rand = (int)Math.abs( random.nextInt());
					int newControlPointCount = (rand%diff)+up+1;
							

					int newmax=difference*2;
					int newmin=difference/2;
					step =( maxX - minX )/ newControlPointCount;
					int originx = 100;
					int originy= Math.abs(random.nextInt())% 100; 
					Point randomOrigin= new Point(originx,originy);
					Point[]randomPoints  = new Point[newControlPointCount];
					x=minX;
					y=(Math.abs(random.nextInt())% (maxDist - minDist))+minDist;
					startPoint= new Point(x,y);
					int j=0;
					randomPoints[j]=startPoint;
					for(j=1;j<newControlPointCount-1;j++){
						if(x<maxX){
							x=j*step ;						
						}
						y =(Math.abs(random.nextInt())% (newmax - newmin))+newmin;
						Point rPoint= new Point(x,y);
						randomPoints[j]=rPoint;
					}
					
					x=maxX;
					y=Math.abs(random.nextInt()% (newmax - newmin))+newmin;
					endPoint=new Point(x,y);
					randomPoints[newControlPointCount-1]=endPoint;
					
					CurveStructure curve = new CurveStructure(randomOrigin, randomPoints);
					
					startPoint= new Point(startPoint.x+randomOrigin.x, startPoint.y+randomOrigin.y);
					endPoint= new Point(endPoint.x+randomOrigin.x, endPoint.y+randomOrigin.y);
					
					boolean tooClose = false;
					for (int j1=0; j1<curves.size(); j1++)
					{
						Point otherOrigin=curves.get(j1).origin;
						Point firstPoint = curves.get(j1).drawPoint[0];
						Point lastPoint = curves.get(j1).drawPoint[curves.get(j1).drawPoint.length-1];
						firstPoint=new Point((firstPoint.x+otherOrigin.x),(firstPoint.y+otherOrigin.y));
						lastPoint=new Point((lastPoint.x+otherOrigin.x),(lastPoint.y+otherOrigin.y));
						
						double d1 = Math.sqrt(((startPoint.x-firstPoint.x)*(startPoint.x-firstPoint.x))+((startPoint.y-firstPoint.y)*(startPoint.y-firstPoint.y)));
						double d2 = Math.sqrt(((endPoint.x-lastPoint.x)*(endPoint.x-lastPoint.x))+((endPoint.y-lastPoint.y)*(endPoint.y-lastPoint.y)));
						
						if (d1 < 40 || d2 < 40)
						{
							tooClose = true;
							break;
						}
					}
					if (tooClose) continue;
					
				
					curves.add(curve);
				}
			}
			
			ArrayList<Double> allOthers = new ArrayList<Double>();
			for (int i=1; i<curves.size(); i++)
			{
				ArrayList<Double> a = curves.get(i).getApproximation();
				for (int j=0; j<a.size(); j++)
					allOthers.add(a.get(j));
			}
			
			double d = CurveDist.curveToCurveDistance(curves.get(0).getApproximation(), allOthers, 0);
			System.out.println("all curves dist = " + d);
			
			int minThreshold =(difficultyLevel+1)*10;
			int maxThreshold =minThreshold+10;
			if(d>=minThreshold && d<=maxThreshold)
				break;
			else
				curves.clear();
			
			
		}

		//-- Curves => curveSt
		curveSt = new CurveStructure[curveCount];
		for(int i=0;i<curves.size();i++){
			curveSt[i]=curves.get(i);
		}
		taskCurve=(int)(Math.random()*(curveSt.length));
		
		this.requestRender();
		
	}
	private int getPropertyIntValue(String name)
	{
		Property<PInteger> prop = this.getProperty(name);
		return prop.getValue().intValue();
	}
	
	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
		if(curveSt != null)
		{
			for(int k=0;k<curveSt.length;k++){
				Point drawPoints[]=curveSt[k].drawPoint;
				Point origin=curveSt[k].origin;
				
				g.setColor(Color.black);
				
				for(int i=0; i<drawPoints.length-1;i++){
					Point p1=drawPoints[i];
					Point p2=drawPoints[i+1];
					
					g.drawLine(p1.x+origin.x,p1.y+origin.y,p2.x+origin.x,p2.y+origin.y);
									
				}
				Point p3=drawPoints[drawPoints.length-1];
				g.setColor(Color.LIGHT_GRAY);
				g.fillOval((p3.x+origin.x)-10, (p3.y+origin.y)-10, 20, 20);
				g.setColor(Color.black);
				g.drawString(""+k, (p3.x+origin.x)-5, (p3.y+origin.y)+5);
				
			}
			
			Point p4=curveSt[taskCurve].drawPoint[0];
			Point origin=curveSt[taskCurve].origin;
			g.setColor(Color.LIGHT_GRAY);
			g.fillOval((p4.x+origin.x)-10, (p4.y+origin.y)-10, 20, 20);
			g.setColor(Color.black);
			g.drawString("A", (p4.x+origin.x)-5, (p4.y+origin.y)+5);
		}
		
	}

	public void saveStimulus()
	{		
		BufferedImage bim = new BufferedImage(1400,1400, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
		
	//	g.translate(620, 470);
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK 2  " + c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\" + filename + ".PNG"));
			//saveData("c:\\work\\curve_stim_" + filename + ".tex");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}
}
