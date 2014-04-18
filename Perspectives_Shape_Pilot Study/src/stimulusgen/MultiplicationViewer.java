package stimulusgen;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
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

public class MultiplicationViewer extends Viewer2D{
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	public String number1=null;
	public String number2=null;
	
	public MultiplicationViewer(String name) {
		super(name);
		this.loadProperties();
		
		
		changeNumbers();
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
								changeNumbers();
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
	private void changeNumbers()
	{
		this.setBackgroundColor(Color.lightGray);
		int difficultyLevel = this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL);
		int rand1,rand2,answer;
		Random random = new Random();
		if(difficultyLevel==0){
			while(true){
				rand1 = (Math.abs(random.nextInt()% 9)+1);
				rand2 = (Math.abs(random.nextInt()% 9)+1);
				answer= rand1* rand2;
				if(answer<=10){
					number1=""+rand1;
					number2=""+rand2;
					break;
				}
			}
			
		}
		else if(difficultyLevel==1){
			while(true){
				rand1 = (Math.abs(random.nextInt()% 9)+2);
				rand2 = (Math.abs(random.nextInt()% 9)+2);
				answer= rand1* rand2;
				if(answer>10){
					number1=""+rand1;
					number2=""+rand2;
					break;
				}
			}
			
		}
		else if(difficultyLevel==2){
			while(true){
				rand1 = ((Math.abs(random.nextInt()% 9)+1)*10)+(Math.abs(random.nextInt()% 9)+1);
				rand2 = (Math.abs(random.nextInt()% 9)+2);
				answer= rand1* rand2;
				if(answer>10){
					number1=""+rand1;
					number2=""+rand2;
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
		String filename ="" + c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
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
		Font font = new Font("Verdana", Font.BOLD, 76);
		if(number1 != null && number2!=null)
		{
			
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString(number1, 500, 470);
			g.drawString("x", 610, 470);
			g.drawString(number2, 670, 470);
			
		}
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

}
