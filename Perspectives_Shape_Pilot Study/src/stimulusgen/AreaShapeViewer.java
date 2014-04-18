package stimulusgen;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

import perspectives.base.Property;
import perspectives.properties.PInteger;
import perspectives.properties.PSignal;
import perspectives.two_d.Viewer2D;

public class AreaShapeViewer extends Viewer2D {
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	public double radius=0;
	public double width=0;
	public double height=0;
	int difficultyLevel;
	

	public AreaShapeViewer(String name) {
		super(name);
		this.loadProperties();
		// TODO Auto-generated constructor stub
	}
	
	private void loadProperties()
	{
		try {
			Property<PInteger> difficultyLevel = new Property<PInteger>(PROPERTY_NAME_DIFFICULTY_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								changeShape();
								return true;
							}
					};
			this.addProperty(difficultyLevel);
			
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
	
	private void changeShape()
	{
		this.setBackgroundColor(Color.lightGray);
		difficultyLevel = this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL);
		double rand1,rand2,rand3, areaCircle,areaEllipse,ratio,max,min,ratio_ellipse;
		Random random = new Random();
		if(difficultyLevel==0){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% ((rand2*1.3)-(rand2*.7))+(rand2*.7))+1;
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3)/4;
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);				
				ratio= max/min;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				System.out.println("ratio"+ratio+"\t ratio ellipse"+ratio_ellipse);
				if(ratio>=1.61 && ratio<=2 && ratio_ellipse<=1.3 && ratio_ellipse>=.7 ){
					radius=rand1*50;
					width=rand2*50;
					height=rand3*50;
					break;
				}
			}
			
		}
		else if(difficultyLevel==1){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% ((rand2*1.6)-(rand2*.4))+(rand2*.4))+1;
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3)/4;
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);
				ratio= max/min;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				System.out.println("ratio"+ratio+" ratio ellipse"+ratio_ellipse);
				if(ratio>=1.31 && ratio<=1.6 && ratio_ellipse<=1.6 && ratio_ellipse>=.6){
					radius=rand1*50;
					width=rand2*50;
					height=rand3*50;
					break;
				}
			}
		}
		else if(difficultyLevel==2){
			while(true){
				rand1 = (Math.abs(random.nextDouble())% 9)+1;
				rand2= (Math.abs(random.nextDouble())% 9)+1;
				rand3= (Math.abs(random.nextDouble())% ((rand2*1.9)-(rand2*.1))+(rand2*.1))+1;
				areaCircle= Math.PI* rand1*rand1;
				areaEllipse=(Math.PI*rand2*rand3)/4;
				max=Math.max(areaCircle, areaEllipse);
				min=Math.min(areaCircle, areaEllipse);
				ratio= max/min;
				ratio_ellipse=(Math.max(rand2, rand3)/Math.min(rand2, rand3));
				System.out.println("ratio"+ratio+" ratio ellipse"+ratio_ellipse);
				if(ratio>=1 && ratio<=1.3 && ratio_ellipse<=1.9 && ratio_ellipse>=.1){
					radius=rand1*50;
					width=rand2*50;
					height=rand3*50;
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
		BufferedImage bim = new BufferedImage(1240,940, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
		
//		g.translate(620, 470);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1240, 940);
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		//String filename ="" + c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		String filename ="TASK 3" +"-" +"Difficulty Level" +difficultyLevel;
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\" + filename + ".PNG"));
			//saveData("c:\\work\\curve_stim_" + filename + ".tex");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		Font font = new Font("Verdana", Font.BOLD, 26);
		if(radius != 0 &&  width!=0 && height !=0)
		{
			
			g.setColor(Color.black);
			//g.setFont(font);
			g.fillOval(400, 400,(int) (2*radius),(int) (2*radius));
			g.setColor(Color.red);
			g.fillOval(800, 400, (int)width, (int)height);
			
		}
		// TODO Auto-generated method stub
		
	}
		
	
	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

}
