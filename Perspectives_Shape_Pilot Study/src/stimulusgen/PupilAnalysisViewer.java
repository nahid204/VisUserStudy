package stimulusgen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import perspectives.base.Property;
import perspectives.base.Viewer;
import perspectives.properties.PFileInput;
import perspectives.two_d.JavaAwtRenderer;
import stimulusgen.PupilSizeViewer.Stimulus;
import stimulusgen.PupilSizeViewer.User;

public class PupilAnalysisViewer extends PupilSizeViewer{

	int index=0;
	int total=0;
	int yes=0;
	public PupilAnalysisViewer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void nextImage(){
		if ( currentUser!= null)
		{
			
			currentStimulus = currentUser.stimuli[index];
			String path = currentUser.stimuli[index].folder + "/" + currentUser.stimuli[index].image;
			File f = new File(path);
			System.out.println(path);
			try {
				this.stim = ImageIO.read(f);
			} catch (IOException e) {
				e.printStackTrace();
				this.stim = null;
			}
			if(index<(currentUser.stimuli.length)-1){
				index++;
			}
			requestRender();
		}
	}
	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		int diameter=3;
//		if (image != null)
//			g.drawImage(image,  0, 0, null);
		g.setColor(Color.red);
		g.fillOval(0, 0, 10, 10);
		if (stim != null)
		{
			g.drawImage(stim, 0, 0,stim.getWidth(),stim.getHeight(), 0, 0, stim.getWidth(), stim.getHeight(),null);
			if(currentStimulus != null && currentStimulus.gaze != null)
			{
				
				int j=0;
				for(int i=0;i<currentStimulus.gaze.length;i++)
				{
					if(j<currentStimulus.pupilsize.length)
					{
						double d=currentStimulus.pupilsize[j]-2.5;
						diameter=(int)Math.ceil(d*15);
						if(diameter>1 && diameter<=15){
							Color c = new Color(0, 160, 155, 40);
							g.setColor(c);
						}
						if(diameter>=16 && diameter<=22){
							Color c = new Color(0, 255, 0, 40);
							g.setColor(c);
						}
						if(diameter>=23 && diameter<=30){
							Color c = new Color(255, 0, 255, 40);
							g.setColor(c);
						}
						if(diameter>=31 && diameter<=45){
							Color c = new Color(0, 255, 255, 40);
							g.setColor(c);
						}
						j++;
					}
					
					if(currentStimulus.gaze[i].x>0 && currentStimulus.gaze[i].y>0 && currentStimulus.pupilsize[i]>0)
					{
						g.fillOval(currentStimulus.gaze[i].x-(diameter/2), currentStimulus.gaze[i].y-(diameter/2), diameter, diameter);
					}
					
					//g.drawOval(currentStimulus.gaze[i].x/2-radius+sx, currentStimulus.gaze[i].y/2-radius+0-stim.getHeight()/2, 2*radius, 2*radius);
				}
			}
			
		}
	}
	
	@Override
	public void keyReleased(String key, String modifiers) {
		// TODO Auto-generated method stub
		
		//System.out.println("key:"+key+", "+modifiers);
		if(key.equals("Y")){
			yes++;
			total++;
		}
		if(key.equals("N")){
			total++;
		}
		if(yes!=0 && total!=0){
			double accuracy=(double)yes/(double)total;
			System.out.println("Accuracy:"+String.format("%.2f",(accuracy*100))+"%");
		}
		nextImage();
		
	}
	
}
