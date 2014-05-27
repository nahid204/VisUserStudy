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
import java.util.HashMap;

import javax.imageio.ImageIO;

import perspectives.base.Property;
import perspectives.base.Viewer;
import perspectives.properties.PFileInput;
import perspectives.two_d.JavaAwtRenderer;

public class PupilSizeViewer extends Viewer implements JavaAwtRenderer{
	
	
	public class Stimulus
	{
		double[] pupil;
		double[] pupilsize;
		Point[] gaze;
		String image;
		int time;
		int difficulty;
		String accuracy;
		String folder;
		int type;
		
		public void smooth(int wind)
		{
			double[] pupil2 = new double[pupil.length];
			for (int i=0; i<pupil.length; i++)
			{
				double avv = 0;
				int cnt = 0;				
				for (int j=-wind; j<=+wind; j++)
				{
					if (i+j<0 || i+j>=pupil.length)
						continue;
					avv += (wind - Math.abs(j))*pupil[i+j];
					cnt+= (wind - Math.abs(j));
				}
				pupil2[i] = avv/cnt;
			}
			pupil = pupil2;
		}
		
		public double average()
		{
			double sum = 0;
			for (int i=0; i<pupil.length; i++)
				sum += pupil[i];
			return sum/pupil.length;
		}
		
		public double averageType()
		{
			double sum = 0;
			int count=0;
			for (int i=20; i<pupilsize.length; i++)
				if(pupilsize[i]>0)
				{
					sum += pupilsize[i];
					count++;
				}
			return sum/count;
		}

	};
	
	public class User
	{
		Stimulus[] stimuli;
		
		public User(){
			
		}
		
		public double getMax()
		{
			double max = 0;
			for (int j=0; j<stimuli.length; j++)
			for (int i=0; i<stimuli[j].pupil.length; i++)
				if (stimuli[j].pupil[i] > max)
					max = stimuli[j].pupil[i];
			return max;
		}
		
		public double getMin()
		{
			double min = 99999;
			for (int j=0; j<stimuli.length; j++)
			for (int i=0; i<stimuli[j].pupil.length; i++)
				if (stimuli[j].pupil[i] < min)
					min = stimuli[j].pupil[i];
			return min;
		}
		
		public double getAvg()
		{
			double sum = 0;
			int cnt = 0;
			for (int j=0; j<stimuli.length; j++)
			for (int i=0; i<stimuli[j].pupil.length; i++)
			{				
					sum+= stimuli[j].pupil[i]; cnt++;
			}
					
			return sum/cnt;
		}
		public void normalize()
		{
			double m = getAvg();
			for (int j=0; j<stimuli.length; j++)
			for (int i=0; i<stimuli[j].pupil.length; i++)
				stimuli[j].pupil[i] /= m;

		}
		
		public double[] averageByType()
		{
			int maxt = 0;
			for (int i=0; i<stimuli.length; i++)
				if (stimuli[i].type > maxt)
					maxt = stimuli[i].type;
			
			double[] r =new double[maxt+1];
			double[] avgType =new double[maxt+1];
			for (int i=0; i<r.length; i++)
			{
				double s = 0;
				int c = 0;

				for (int j=0; j<stimuli.length; j++)
					if (stimuli[j].type == i)
					{
						s+= stimuli[j].average();
						c++;
						double av = s/c;
						//System.out.println("Avg by Type"+i + " " + av);
					}
				r[i] = s/c;
				//System.out.println("Avg by Type"+i + " " + r[i]);
			}
			for (int k=0; k<avgType.length; k++)
			{
				double s = 0;
				int c = 0;
				double av=0;

				for (int j=0; j<stimuli.length; j++)
					if (stimuli[j].type == k)
					{
						
						double avg= stimuli[j].averageType();
						if(avg>=0)
						{
							s+= avg;
							c++;
							av = s/c;
						}
						
					//	System.out.println("innerAvg by Type"+j + " " + av);
					}
				avgType[k] = av;
				System.out.println(avgType[k]);
			}
			return r;
		}
		
		public BufferedImage createImage()
		{
			//smooth filter
			for (int i=0; i<stimuli.length; i++)
				stimuli[i].smooth(5);
		
			int w = 0;
			for (int i=0; i<stimuli.length; i++)
				for (int j=0; j<stimuli[i].pupil.length; j++)
					w++;
			
			BufferedImage image = new BufferedImage(w, 1000, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			
			int cnt = 0;
			for (int i=0; i<stimuli.length; i++)
			{
				int prevcnt = cnt;
				for (int j=0; j<stimuli[i].pupil.length; j++)
				{		
					g.setColor(Color.gray);			
					g.drawLine(cnt, 10, cnt, (int)(stimuli[i].pupil[j]*800)+30);
					cnt++;
				}		
	
				if (i %2 == 0){
					g.setColor(Color.red);			
					g.fillRect(prevcnt, 0, cnt - prevcnt, 15);
				}							
				else{
					g.setColor(Color.blue);
					g.fillRect(prevcnt, 15, cnt - prevcnt, 30);
				}		
			
				g.drawLine(cnt, 10, cnt, 1000);
			}
			
			int x = 0;
			for (int i=0; i<stimuli.length; i++)
			{
				double av = stimuli[i].average();
				g.setColor(new Color(200,200,0,70));
				//g.fillRect(x, 30, stimuli[i].pupil.length, (int)(av*800));
				x += stimuli[i].pupil.length;
			}
			
			x =0;
			double[] avgByType = this.averageByType(); 
			for (int i=0; i<stimuli.length; i++)
			{
				double av = avgByType[stimuli[i].type];
				g.setColor(new Color(200,200,0,70));
				g.fillRect(x, 30, stimuli[i].pupil.length, (int)(av*800));
				x += stimuli[i].pupil.length;
			}
			
		
			
			return image;
		}
	};
	
	
	
	
	BufferedImage image;	
	BufferedImage stim;
	Stimulus currentStimulus;
	int sx;
	
	ArrayList<User> users = new ArrayList<User>();
	User currentUser = null;


	public PupilSizeViewer(String name) {
		super(name);
		Property<PFileInput> loadp = new Property<PFileInput>("Load", new PFileInput())
				{
					@Override
					protected boolean updating(PFileInput newvalue) {
						
						User u = createUserFromFile(newvalue.path);
						users.add(u);
						
						User[] us = new User[users.size()];
						for (int i=0; i<us.length; i++)
							us[i] = users.get(i);
						
						currentUser = averageUsers(us);
						double min = currentUser.getMin();
						for (int i=0; i<currentUser.stimuli.length; i++)
							for (int j=0; j<currentUser.stimuli[i].pupil.length; j++)
								currentUser.stimuli[i].pupil[j] -= min;
						image = currentUser.createImage();
						return true;
					}
			
				};
		addProperty(loadp);
		}

	@Override
	public void render(Graphics2D g) {
		int diameter=1;
		if (image != null)
			g.drawImage(image,  0, 0, null);
		if (stim != null)
		{
			g.drawImage(stim, sx, 0-stim.getHeight()/2, sx+stim.getWidth()/2, 0, 0, 0, stim.getWidth(), stim.getHeight(),null);
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
						g.fillOval(currentStimulus.gaze[i].x/2-(diameter/2)+sx, currentStimulus.gaze[i].y/2-(diameter/2)+0-stim.getHeight()/2, diameter, diameter);
					}
					
					//g.drawOval(currentStimulus.gaze[i].x/2-radius+sx, currentStimulus.gaze[i].y/2-radius+0-stim.getHeight()/2, 2*radius, 2*radius);
				}
			}
			
		}
			
		
	}

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mousepressed(int x, int y, int button) {
		if (y > 0 && y < 30 && currentUser!= null)
		{
			int cnt = 0;
			for (int i=0; i<currentUser.stimuli.length; i++)
			{
				int prevcnt = cnt;
				for (int j=0; j<currentUser.stimuli[i].pupil.length; j++)
					cnt++;
				
				if (x > prevcnt && x < cnt)
				{
					currentStimulus = currentUser.stimuli[i];
					String path = currentUser.stimuli[i].folder + "/" + currentUser.stimuli[i].image;
					File f = new File(path);
					System.out.println(path);
					try {
						this.stim = ImageIO.read(f);
						sx = x;
					} catch (IOException e) {
						e.printStackTrace();
						this.stim = null;
					}
				}
				
			}
			
		}
		return false;
	}

	@Override
	public boolean mousereleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousemoved(int x, int y) {
		if (y > 0 && y < 30 && currentUser!=null)
		{
			int cnt = 0;
			for (int i=0; i<currentUser.stimuli.length; i++)
			{
				int prevcnt = cnt;
				for (int j=0; j<currentUser.stimuli[i].pupil.length; j++)
					cnt++;
				
				if (x > prevcnt && x < cnt)
					this.setToolTipText(currentUser.stimuli[i].image + " , " + currentUser.stimuli[i].difficulty + " , " + currentUser.stimuli[i].time);
				
			}
				
		}
		else
			this.setToolTipText("" + ((y-30)/100.));
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
	
	public User createUserFromFile(String path)
	{
		User u = new User();
		
	
		File f = new File(path);		
		
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		
			String line;
			ArrayList<String> lines = new ArrayList<String>();
			while ((line = br.readLine()) != null)
				lines.add(line);
		
			boolean[] in = new boolean[lines.size()];
			for (int i=0; i<lines.size(); i++)
				in[i] = true;
	
			for (int i=0; i<lines.size(); i++)
			{
				line = lines.get(i);
				if (line.startsWith("Gaze"))
				{
					double s = Double.parseDouble(line.split("\t")[3]);
				
					if (s == 0)
						for (int j=i-2; j<=i+2; j++)
							if (j >=0 && j<lines.size())
								in[j] = false;
				}
				else
					in[i] = false;
			}
		
		
			ArrayList<Stimulus> stimuli = new ArrayList<Stimulus>();		
			Stimulus cs = null;
			int gazeCnt = 0;
			double av = 0;
		
			line = null;
			
			ArrayList<Double> p = new ArrayList<Double>();
			ArrayList<Point> gazepoint=new ArrayList<Point>();
			ArrayList<Double> pupilDiameter=new ArrayList<Double>();
			for (int i=0; i<=lines.size(); i++)
			{
				if (i < lines.size() && lines.get(i).startsWith("Gaze"))
				{
					if (!in[i])
					continue;
					String[] split = lines.get(i).split("\t");
					int x = Integer.parseInt(split[1]);
					int y = Integer.parseInt(split[2]);
					Point p1=new Point(x,y);
					gazepoint.add(p1);
					
					
					double s = Double.parseDouble(split[3]);	
					av += s;
					gazeCnt++;
					pupilDiameter.add(s);
				
					if (gazeCnt == 5)
					{
						p.add(av / gazeCnt);
						av = 0;
						gazeCnt = 0;
					}
				}
				else if (i < lines.size() && lines.get(i).startsWith("Time"))
				{					
					if (cs != null)
					cs.time = Integer.parseInt(lines.get(i).split("\t")[1]) ;
				}
				else if (i < lines.size() && lines.get(i).startsWith("Accu"))
				{
					if (cs != null)
						cs.accuracy = lines.get(i).split("\t")[1];
				}
				else
				{
					if (gazeCnt != 0)
						p.add(av/gazeCnt);
					av = 0;
					gazeCnt=0;
					if (cs != null)
					{
						cs.pupil = new double[p.size()];
						for (int j=0; j<p.size(); j++)
							cs.pupil[j] = p.get(j);
						//stimuli.add(cs);
						
						cs.gaze=new Point[gazepoint.size()];
						for (int k=0; k<gazepoint.size(); k++)
							cs.gaze[k] = gazepoint.get(k);
						//stimuli.add(cs);
						if(pupilDiameter.size()==0)
						{
							System.out.println("Got it");
						}
						cs.pupilsize = new double[pupilDiameter.size()];
						for (int l=0; l<pupilDiameter.size(); l++)
							cs.pupilsize[l] = pupilDiameter.get(l);
						
						stimuli.add(cs);
						
					}
					
					if (i < lines.size())
					{
						
						String l = lines.get(i);
					
					cs = new Stimulus();
					cs.folder = new File(path).getParent();
					cs.image = new File(lines.get(i).split("\t")[0]).getName();
					cs.difficulty = Integer.parseInt(lines.get(i).split("\t")[1]);
					cs.type = Integer.parseInt(lines.get(i).split("\t")[2]);
					p.clear();
					gazepoint.clear();
					pupilDiameter.clear();
					}
				
				}
		}
			
		u.stimuli = new Stimulus[stimuli.size()];
		for (int i=0; i<stimuli.size(); i++)
			u.stimuli[i] = stimuli.get(i);
		u.normalize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		return u;
		
	}
	
	public User averageUsers(User[] users)
	{
		User u = new User();
		
		//find user with most stimuli
		int mxs = 0;
		User mxu = null;
		for (int i=0; i<users.length; i++)
			if (users[i].stimuli.length > mxs)
			{
				mxs = users[i].stimuli.length;
				mxu = users[i];
				
			}
		
		ArrayList<Stimulus> combS = new ArrayList<Stimulus>();
		
		for (int i=0; i<mxu.stimuli.length; i++)
		{
			Stimulus s = new Stimulus();
			s.difficulty = mxu.stimuli[i].difficulty;
			s.folder = mxu.stimuli[i].folder;
			s.image = mxu.stimuli[i].image;
			s.type = mxu.stimuli[i].type;
			s.gaze =  mxu.stimuli[i].gaze;
			s.pupilsize=mxu.stimuli[i].pupilsize;
			ArrayList<Stimulus> ss = new ArrayList<Stimulus>();
			for (int j=0; j<users.length; j++)
				for (int k=0; k<users[j].stimuli.length; k++)
				{
					String im2 = users[j].stimuli[k].image;
					if (im2.equals(s.image))
						ss.add(users[j].stimuli[k]);
				}

			
	
			int mxpl = 0;
			for (int j=0; j<ss.size(); j++)				
					mxpl += ss.get(j).pupil.length;
			mxpl /= ss.size();
			
			for (int j=0; j<ss.size(); j++)
			{
				int l = ss.get(j).pupil.length;
				if (l<0.5*mxpl || l>2*mxpl)
				{
					ss.remove(j);
					j--;
				}
			}
			
			mxpl = 0;
			for (int j=0; j<ss.size(); j++)
				if (ss.get(j).pupil.length > mxpl)
					mxpl = ss.get(j).pupil.length;
			
			s.pupil = new double[mxpl];
			for (int j=0; j<ss.size(); j++)
			{
				Stimulus s2 = ss.get(j);
				
				if (s2.pupil.length == 0)
				{
					ss.remove(j);
					j--;
					continue;
				}
				for (int k=0; k<s.pupil.length; k++)
				{
					double perc = (double)k/(mxpl-1);
					double l2 = s2.pupil.length-1;
					int index = (int)(l2*perc);
					
					s.pupil[k] += s2.pupil[index];
				}
			}
			for (int k=0; k<s.pupil.length; k++)
				s.pupil[k] /= ss.size();	
			
			combS.add(s);
			
		}
		
		u.stimuli = new Stimulus[combS.size()];
		for (int i=0; i<u.stimuli.length; i++)
			u.stimuli[i] = combS.get(i);
		
		return u;
		
	}
	
	

}
