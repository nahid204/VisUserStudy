package stimulusgen;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.text.MaskFormatter;

import perspectives.base.Property;
import perspectives.base.Viewer;
import perspectives.properties.PColor;
import perspectives.properties.PDouble;
import perspectives.properties.PInteger;
import perspectives.properties.PSignal;
import perspectives.two_d.JavaAwtRenderer;
import perspectives.util.SplineFactory;


public class CurveStructureViewer extends Viewer implements JavaAwtRenderer{

	public static final String PROPERTY_NAME_MIN_DISTANCE = "Distance.Minimum Distance";
	public static final String PROPERTY_NAME_MAX_DISTANCE = "Distance.Maximum Distance";
	public static final String PROPERTY_NAME_CONTROL_POINT_COUNT = "Control Point Count";
	public static final String PROPERTY_NAME_CURVE_COUNT = "Curve Count";
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	public static final String PROPERTY_NAME_STRESS_LEVEL = "Stress Level";
	
	CurveStructure []curveSt=null;
	int taskCurve;
	long time=1000;
	
	public CurveStructureViewer(String name) {
		super(name);
		this.loadProperties();
		
		
		positionLayout(0,200,10,3,0,0.5,0);

	}

	private void loadProperties()
	{
		try {
			Property<PInteger> minDistance = new Property<PInteger>(PROPERTY_NAME_MIN_DISTANCE, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								int minDist = newvalue.intValue();
								int maxDist = getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
								int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
								int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
								int difficultyLevel = ((PInteger)getProperty("Difficulty Level").getValue()).intValue();	
								double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
								int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
								positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
								return true;
							}
					};
			this.addProperty(minDistance);
			
			Property<PInteger> maxDistance = new Property<PInteger>(PROPERTY_NAME_MAX_DISTANCE, new PInteger(200))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
							int maxDist = newvalue.intValue();
							int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
							int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
							int difficultyLevel = ((PInteger)getProperty("Difficulty Level").getValue()).intValue();	
							double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
							int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
							positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
							return true;
					
						}
					};
			this.addProperty(maxDistance);
			
					
			Property<PInteger> controlPointCount = new Property<PInteger>(PROPERTY_NAME_CONTROL_POINT_COUNT, new PInteger(10))
					{
						@Override
						protected boolean updating(PInteger newvalue) {
							// TODO Auto-generated method stub
							int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
							int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
							int controlPointCount = newvalue.intValue();
							int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
							int difficultyLevel = ((PInteger)getProperty("Difficulty Level").getValue()).intValue();	
							double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
							int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
							positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
							return true;
						}
					};
			this.addProperty(controlPointCount);
			
			Property<PInteger> curveCount = new Property<PInteger>(PROPERTY_NAME_CURVE_COUNT, new PInteger(3))
				{
					@Override
					protected boolean updating(PInteger newvalue) {
						// TODO Auto-generated method stub
						int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
						int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
						int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
						int curveCount = newvalue.intValue();
						int difficultyLevel = ((PInteger)getProperty("Difficulty Level").getValue()).intValue();	
						double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
						int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
						positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
						return true;
					}
				};
			this.addProperty(curveCount);
			
						
			Property<PInteger> difficultyLevel = new Property<PInteger>(PROPERTY_NAME_DIFFICULTY_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
								int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
								int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
								int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
								int difficultyLevel =newvalue.intValue(); 	
								double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
								int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
								positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
								return true;
							}
					};
			difficultyLevel.setVisible(true);
					this.addProperty(difficultyLevel);
			
			
			Property<PDouble> jagged  = new Property<PDouble>("Jagged", new PDouble(0.5))
					{
						@Override
						protected boolean updating(PDouble newvalue) {
							int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
							int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
							int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
							int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
							int difficultyLevel =((PInteger)getProperty("Difficulty Level").getValue()).intValue(); 	
							double jagged = newvalue.doubleValue();
							int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
							positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
							return true;
						}
					};
			this.addProperty(jagged);
			Property<PInteger> stressLevel = new Property<PInteger>(PROPERTY_NAME_STRESS_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
								int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
								int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
								int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
								int difficultyLevel =((PInteger)getProperty("Difficulty Level").getValue()).intValue(); 	
								double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
								int stressLevel =newvalue.intValue();
								positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
								return true;
							}
					};
					stressLevel.setVisible(true);
					this.addProperty(stressLevel);
			
			Property<PSignal> savePart1 = new Property<PSignal>("SavePart1", new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							// TODO Auto-generated method stub
							saveStimulusP1();
							return true;
						}
					};
			this.addProperty(savePart1);
			
			Property<PSignal> savePart2 = new Property<PSignal>("SavePart2", new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							// TODO Auto-generated method stub
							saveStimulusP2();
							return true;
						}
					};
			this.addProperty(savePart2);
			
			
			
			Property<PSignal> gen = new Property<PSignal>("Generate", new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
							int minDist = getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
							int maxDist =  getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
							int controlPointCount = getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
							int curveCount = getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
							int difficultyLevel =((PInteger)getProperty("Difficulty Level").getValue()).intValue(); 	
							double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
							int stressLevel =((PInteger)getProperty(PROPERTY_NAME_STRESS_LEVEL).getValue()).intValue(); 
							positionLayout(minDist,maxDist, controlPointCount, curveCount, difficultyLevel, jagged,stressLevel);
							return true;
						}
					};
			this.addProperty(gen);
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
	}
	
	private void positionLayout(
									int minDist,
									int maxDist,
									int controlPointCount,
									int curveCount,
									int difficultyLevel,
									double jagged,
									int stressLevel
								)
	{
//		int minDist = this.getPropertyIntValue(PROPERTY_NAME_MIN_DISTANCE);
//		int maxDist = this.getPropertyIntValue(PROPERTY_NAME_MAX_DISTANCE);
//		int controlPointCount = this.getPropertyIntValue(PROPERTY_NAME_CONTROL_POINT_COUNT);
//		int curveCount = this.getPropertyIntValue(PROPERTY_NAME_CURVE_COUNT);
//		int difficultyLevel = ((PInteger)getProperty("Difficulty Level").getValue()).intValue();	
//		double jagged = ((PDouble)getProperty("Jagged").getValue()).doubleValue();
		
		ArrayList<CurveStructure> curves = new ArrayList<CurveStructure>();

		int stoppingCondition = 0;
		while (curves.size() < curveCount && stoppingCondition < curveCount * 1000)
		{
			stoppingCondition++;
				//create random curve
				
				int minX =0;
				int maxX = 1200;
				int x=minX;
				int y=0;
				int step = (maxX - minX )/ controlPointCount;
				int difference=maxDist-minDist;
				Random random = new Random();
				Point startPoint=new Point(0,0);
				Point endPoint= new Point(0,0);
				//Start and end points have fixed x value and random y value.
				//First Curve is drawn in this if block and added to curves
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
					for(j=1;j<controlPointCount;j++){
						
						Point prev = randomPoints[j-1];
						Point prevprev = null;
						if (j>1)
							prevprev = randomPoints[j-2];
						
						double mindifm = 99999;
						Point minP = null;
						for (int z = 0; z< 100; z++)
						{
							x=j*step ;
							y = Math.abs(random.nextInt()% (maxDist - minDist))+minDist;
							
							if (prevprev == null) { minP = new Point(x,y); break;}
							
							double mprev = (double)(prev.y - prevprev.y) / (maxDist-minDist); // / (prev.x - prevprev.x);
							double m = (double)(y - prev.y) / (maxDist-minDist); // / (x - prev.x);
							double difm = Math.abs(m-mprev);
							System.out.println(difm);
							if (Math.abs(difm - jagged) < 0.1) {minP = new Point(x,y); break;}
							else if (Math.abs(difm - jagged) < mindifm)
							{
								mindifm = Math.abs(difm-jagged);
								minP = new Point(x,y);
							}
						}
						Point rPoint= minP;
						randomPoints[j]=rPoint;
					}
					
					CurveStructure curve = new CurveStructure(randomOrigin, randomPoints);
					curves.add(curve);
				}
				else
				{
					//all other curves are drawn here.If the curve qualify then it is added to curves 
					// new control point count and new max and new min are calculated here.	
					int up =(int)(controlPointCount*1.5);
					int down = (int)(controlPointCount/1.5);
					int diff = up-down;
					int rand = (int)Math.abs( random.nextInt());
					int newControlPointCount = (rand%diff)+down+1;
						
					int newmax=maxDist; 
					int newmin=minDist;
					step =( maxX - minX )/ newControlPointCount;
					int originx = 100;
					int originy= Math.abs(random.nextInt())% 100; 
					Point randomOrigin= new Point(originx,originy);
					Point[]randomPoints  = new Point[newControlPointCount];
					x=minX;
					y=(Math.abs(random.nextInt())% (newmax- newmin))+newmin;
					startPoint= new Point(x,y);
					int j=0;
					randomPoints[j]=startPoint;
					for(j=1;j<newControlPointCount;j++){
						
						Point prev = randomPoints[j-1];
						Point prevprev = null;
						if (j>1)
							prevprev = randomPoints[j-2];
						
						double mindifm = 9999;
						Point minP = null;
						for (int z = 0; z< 100; z++)
						{
							x=j*step ;
							y = Math.abs(random.nextInt()% (newmax - newmin))+newmin;
							
							if (prevprev == null) {minP = new Point(x,y); break;}
							
							double mprev = (double)(prev.y - prevprev.y) / (newmax-newmin); // / (prev.x - prevprev.x);
							double m = (double)(y - prev.y) / (newmax - newmin); // / (x - prev.x);
							double difm = Math.abs(m-mprev);
							System.out.println("-- " + z + " -- " + difm);
							if (Math.abs(difm-jagged) < 0.1) {minP =new Point(x,y); break;}
							else if (Math.abs(difm - jagged) < mindifm)
							{
								mindifm = Math.abs(difm - jagged);
								minP = new Point(x,y);
							}
						}
						
						Point rPoint= minP;
						randomPoints[j]=rPoint;
					}
					
					
					CurveStructure curve = new CurveStructure(randomOrigin, randomPoints);
					
					startPoint= new Point(startPoint.x+randomOrigin.x, startPoint.y+randomOrigin.y);
					endPoint= new Point(endPoint.x+randomOrigin.x, endPoint.y+randomOrigin.y);
					
					//make sure start and endpoints are not very close to any other start and endpoints
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
						
						
						if (d1 < 20 || d2 < 20)
						{
							tooClose = true;
							break;
						}
						
						
					}
					
					double d = curves.get(0).dist(curve,true); 
					// difficulty level 0 means curve could be from 10 to 200 distance from other curves. As the level increases the distance increases.
					//If the new curve qualify in the distance range then it is added to curves.
					//int minThreshold =(5 - difficultyLevel)*15;
					//int maxThreshold =(5 - difficultyLevel)*35;
					if (tooClose /*|| !(d>=minThreshold && d<=maxThreshold)*/) continue;
					
				
					curves.add(curve);
				
				}
		
			}
		
//		if(stressLevel==0)time=time*12;
//		if(stressLevel==1)time=time*9;
//		if(stressLevel==2)time=time*6;
		if(difficultyLevel==0)time=6000;
		if(difficultyLevel==1)time=9000;
		if(difficultyLevel==2)time=20000;
			
//			ArrayList<Double> allOthers = new ArrayList<Double>();
//			for (int i=1; i<curves.size(); i++)
//			{
//				ArrayList<Double> a = curves.get(i).getApproximation();
//				for (int j=0; j<a.size(); j++)
//					allOthers.add(a.get(j));
//			}
//			
//			double d = CurveDist.curveToCurveDistance(curves.get(0).getApproximation(), allOthers, 0);
//			System.out.println("all curves dist = " + d);
//			
//			int minThreshold =(difficultyLevel+1)*10;
//			int maxThreshold =minThreshold+10;
//		
//			if(d>=minThreshold && d<=maxThreshold)
//				break;
//			else
//				curves.clear();
//		
//
//		}

		//Curves to curveSt Conversion
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
				if (curveSt[k] == null)
					continue;
				
				Point2D[] a = curveSt[k].getApproximation();
				
				g.setColor(Color.black);
				
				for(int i=0; i<a.length-1;i++)
					g.drawLine((int)a[i].getX(),(int)a[i].getY(),(int)a[i+1].getX(),(int)a[i+1].getY());
									
				Point2D p3=a[a.length-1];
				g.setColor(Color.LIGHT_GRAY);
				g.fillOval((int)p3.getX()-10, (int)p3.getY()-10, 20, 20);
				g.setColor(Color.black);
				g.drawString(""+k, (int)p3.getX()-5, (int)p3.getY()+5);
				
			}
			
			Point p4=curveSt[taskCurve].drawPoint[0];
			Point origin=curveSt[taskCurve].origin;
			g.setColor(Color.LIGHT_GRAY);
			g.fillOval((p4.x+origin.x)-10, (p4.y+origin.y)-10, 20, 20);
			g.setColor(Color.black);
			g.drawString("A", (p4.x+origin.x)-5, (p4.y+origin.y)+5);
		}
		
	}

	public void saveStimulusP1()
	{		
		BufferedImage bim = new BufferedImage(1400,1400, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
	
		
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1400, 1400);
		
		g.translate(100, 400);
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK CURVE"+"-" + this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"-"+c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\Curve Task\\Part1\\" + filename + ".PNG"));
			String imageFilePath = "Curve Task\\Part1\\" + filename + ".PNG";
			String data = imageFilePath+"\tCurve\t"+this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"\t"+taskCurve+"\t"+(this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+6)+"\t1\t"+time+"\r\n";
			String dataFileName="Result.tex";
			saveResult("c:\\work\\"+dataFileName,data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveStimulusP2()
	{		
		BufferedImage bim = new BufferedImage(1400,1400, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
	
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1400, 1400);
		
		g.translate(100, 400);
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK CURVE"+"-" + this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"-"+c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\Curve Task\\Part2\\" + filename + ".PNG"));
			String imageFilePath = "Curve Task\\Part2\\" + filename + ".PNG";
			String data = imageFilePath+"\tCurve\t"+this.getPropertyIntValue(PROPERTY_NAME_STRESS_LEVEL)+"\t"+taskCurve+"\t"+(this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+6)+"\t2\t"+time+"\r\n";
			String dataFileName="Result.tex";
			saveResult("c:\\work\\"+dataFileName,data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveResult(String filePath, String data)
	{
        try {
            FileWriter fstream;
            File f = new File(filePath);
            if(!f.exists())
            {
            	f.createNewFile();
            }
            
            fstream = new FileWriter(f, true);

            BufferedWriter br = new BufferedWriter(fstream);
            
            br.append(data);
            
            
            br.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mousepressed(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousereleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousemoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void keyPressed(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}
}
