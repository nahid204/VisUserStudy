package stimulusgen;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

import perspectives.base.Property;
import perspectives.base.Viewer;
import perspectives.properties.PInteger;
import perspectives.properties.PSignal;
import perspectives.two_d.JavaAwtRenderer;

public class AreaShapeViewer extends Viewer implements JavaAwtRenderer {
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	public double radius=0;
	public double width=0;
	public double height=0;
	int difficultyLevel;
	
	double area1;
	double area2;
	String answer=null;
	long time=1000;
	

	public AreaShapeViewer(String name) {
		super(name);
		this.loadProperties();
		// TODO Auto-generated constructor stub
		changeShape(0);
	}
	
	private void loadProperties()
	{
		try {
			Property<PInteger> difficultyLevel = new Property<PInteger>(PROPERTY_NAME_DIFFICULTY_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								changeShape(newvalue.intValue());
								return true;
							}
					};
			this.addProperty(difficultyLevel);
			
					
			Property<PSignal> regen = new Property<PSignal>("Regen", new PSignal())
					{
							@Override
							protected boolean updating(PSignal newvalue) {
								changeShape(((PInteger)getProperty(PROPERTY_NAME_DIFFICULTY_LEVEL).getValue()).intValue());
								return true;
							}
					};
			this.addProperty(regen);
			
			
			Property<PSignal> save = new Property<PSignal>("SaveStimulus", new PSignal())
					{
						@Override
						protected boolean updating(PSignal newvalue) {
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
	
	private void changeShape(int difficultyLevel)
	{
		double rand1,rand2,rand3, areaCircle,areaEllipse,ratio,max,min,ratio_ellipse;
		Random random = new Random();
		
		if(difficultyLevel==0){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% 9)+1;
				// rand2 is the width of ellipse and rand3 is the height. rand3 is taken as 30% increase or decrease of width because not to stretch out the ellipse.
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3);
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);				
				ratio= min/max;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				double score = ratio * ratio_ellipse;
				System.out.println("score " + score);
				time=6000;
				
				
				if(ratio < 0.65 && score > 0.6 && score < 0.8){
					radius=rand1;
					width=rand2;
					height=rand3;
					
					area1 = areaCircle;
					area2 = areaEllipse;
					if(area1>=area2){
						answer="left";
					}
					if(area1<area2){
						answer="right";
					}
					break;
				}
			}
			
		}
		else if(difficultyLevel==1){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% 9)+1;
				// rand2 is the width of ellipse and rand3 is the height. rand3 is taken as 60% increase or decrease of width
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3);
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);
				ratio= min/max;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				double score = ratio * ratio_ellipse;
				System.out.println("score " + score);
				time=10000;
				
				if(ratio > 0.7 && ratio < 0.85 && score >= 1.2 && score < 1.4){
					radius=rand1;
					width=rand2;
					height=rand3;
					area1 = areaCircle;
					area2 = areaEllipse;
					if(area1>=area2){
						answer="left";
					}
					if(area1<area2){
						answer="right";
					}
					break;
				}
			}
		}
		else if(difficultyLevel==2){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% 9)+1;
				// rand2 is the width of ellipse and rand3 is the height. rand3 is taken as 90% increase or decrease of width
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3);
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);
				ratio= min/max;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				double score = ratio * ratio_ellipse;
				System.out.println("score " + score);
				time=20000;
				if(ratio > 0.85 && ratio < 0.95 && score >= 1.8 && score < 2){
					radius=rand1;
					width=rand2;
					height=rand3;
					area1 = areaCircle;
					area2 = areaEllipse;
					if(area1>=area2){
						answer="left";
					}
					if(area1<area2){
						answer="right";
					}
					break;
				}
			}
			
		}
		this.requestRender();
	}
	
	private int getPropertyIntValue(String name)
	{
		Property<PInteger> prop = this.getProperty(name);
		return prop.getValue().intValue();
	}
	
	public void saveStimulus()
	{		
		BufferedImage bim = new BufferedImage(1400,1400, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1400, 1400);
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK AREA"+"-" + this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"-"+c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\Area Task\\" + filename + ".PNG"));
			String imageFilePath = "Area Task\\" + filename + ".PNG";
			String data = imageFilePath+"\tAREA\t"+this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"\t"+answer+"\t"+(this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+3)+"\t1\t"+time+"\r\n";
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
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		//Font font = new Font("Verdana", Font.BOLD, 26);
		if(radius != 0 &&  width!=0 && height !=0)
		{
			
			g.setColor(Color.black);
			//g.setFont(font);
			g.fillOval(400, 500,(int) (100*radius),(int) (100*radius));
			g.setColor(Color.red);
			g.fillOval(800, 500, (int)(100*width), (int)(100*height));
			
		}
		
		// TODO Auto-generated method stub
		
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
