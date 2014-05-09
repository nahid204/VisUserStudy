package stimulusgen;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
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


public class MultiplicationViewer extends Viewer implements JavaAwtRenderer{
	public static final String PROPERTY_NAME_DIFFICULTY_LEVEL = "Difficulty Level";
	public static final String PROPERTY_NAME_STRESS_LEVEL = "Stress Level";
	
	public String number1=null;
	public String number2=null;
	int rand1,rand2,answer;
	long time;
	long time0=4000;
	long time1=8000;
	long time2= 20000;
	
	public MultiplicationViewer(String name) {
		super(name);
		this.loadProperties();
				
		changeNumbers(0,0);
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
								int difficultyLevel = newvalue.intValue();
								int stressLevel = getPropertyIntValue(PROPERTY_NAME_STRESS_LEVEL);
								changeNumbers(difficultyLevel,stressLevel);
								return true;
							}
					};
			this.addProperty(difficultyLevel);
			
			Property<PInteger> StressLevel = new Property<PInteger>(PROPERTY_NAME_STRESS_LEVEL, new PInteger(0))
					{
							@Override
							protected boolean updating(PInteger newvalue) {
								// TODO Auto-generated method stub
								int difficultyLevel =getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL); 
								int stressLevel = newvalue.intValue();
								changeNumbers(difficultyLevel,stressLevel);
								return true;
							}
					};
			this.addProperty(StressLevel);
			
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
			
			
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
	}
	private void changeNumbers(int difficultyLevel,int stressLevel )
	{

		System.out.println("Difficulty level:"+difficultyLevel);
		Random random = new Random();
		if(difficultyLevel==0){
			while(true){
				rand1 = (Math.abs(random.nextInt()% 4)+1);
				rand2 = (Math.abs(random.nextInt()% 4)+1);
				answer= rand1* rand2;
				if(answer<=10){
					number1=""+rand1;
					number2=""+rand2;
					time=time0;
					break;
				}
			}
			
		}
		else if(difficultyLevel==1){
			while(true){
				rand1 = (Math.abs(random.nextInt()% 5)+4);
				rand2 = (Math.abs(random.nextInt()% 5)+4);
				answer= rand1* rand2;
				if(answer>10){
					number1=""+rand1;
					number2=""+rand2;
					time=time1;
					break;
				}
			}
			
		}
		else if(difficultyLevel==2){
			while(true){
				rand1 = ((Math.abs(random.nextInt()% 9)+1)*10)+(Math.abs(random.nextInt()% 9)+1);
				rand2 = (Math.abs(random.nextInt()% 6)+3);
				answer= rand1* rand2;
				if(answer>10){
					number1=""+rand1;
					number2=""+rand2;
					time=time2;
//					if(stressLevel==0)time=time*10;
//					if(stressLevel==1)time=time*10;
//					if(stressLevel==2)time=time*8;
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
	
	public void saveStimulusP1()
	{		
		BufferedImage bim = new BufferedImage(1400,1400, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bim.createGraphics();
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1400, 1400);
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK MULTI"+"-" + this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"-"+c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\Multiplication Task\\Part1\\" + filename + ".PNG"));
			String imageFilePath = "Multiplication Task\\Part1\\" + filename + ".PNG";
			String data = imageFilePath+"\tMULTI\t"+this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"\t"+answer+"\t"+this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"\t1\t"+time+"\r\n";
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
		
		this.render(g);
		
		Calendar c = Calendar.getInstance();
		String filename ="TASK MULTI"+"-" + this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"-"+c.get(c.DAY_OF_YEAR) + c.get(c.HOUR) + c.get(c.MINUTE) + c.get(c.SECOND);
		
		try {
			ImageIO.write(bim, "PNG", new File("c:\\work\\Multiplication Task\\Part2\\" + filename + ".PNG"));
			String imageFilePath = "Multiplication Task\\Part2\\" + filename + ".PNG";
			String data = imageFilePath+"\tMULTI\t"+this.getPropertyIntValue(PROPERTY_NAME_STRESS_LEVEL)+"\t"+answer+"\t"+this.getPropertyIntValue(PROPERTY_NAME_DIFFICULTY_LEVEL)+"\t2\t"+time+"\r\n";
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
		Font font = new Font("Verdana", Font.BOLD, 76);
		if(number1 != null && number2!=null)
		{
			
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString(number1, 500, 600);
			g.drawString("x", 610, 600);
			g.drawString(number2, 670, 600);
			
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
