package stimulusgen;

import java.util.ArrayList;

public class UserData {
	
	String user;
	
	ArrayList<Stimulus> stimuli;
	
	ArrayList< ArrayList<Fixation>> fixations;	
	ArrayList< ArrayList<Saccade>> saccades;
	
	public UserData(String userId)
	{
		user = userId;
		stimuli = new ArrayList<Stimulus>();
		fixations = new ArrayList< ArrayList<Fixation>>();		
		saccades = new ArrayList<ArrayList<Saccade>>();
	}

	
	public void addStimulus(Stimulus s)
	{
		if (stimuli.indexOf(s) >= 0)
			return;
		
		stimuli.add(s);
		fixations.add(new ArrayList<Fixation>());
		saccades.add(new ArrayList<Saccade>());
	}
	
	public void addFixation(Stimulus s, Fixation f)
	{
		int si = stimuli.indexOf(s);
		fixations.get(si).add(f);
		f.rank = fixations.get(si).size()-1;
	}
	
	public void addSaccade(Stimulus s, Saccade f)
	{
		saccades.get(stimuli.indexOf(s)).add(f);
	}
	
	public void sortFixations()
	{
		for (int i=0; i<stimuli.size(); i++)
		{
			ArrayList<Fixation> f = fixations.get(i);
			Fixation[] fa = new Fixation[f.size()];
			for (int j=0; j<fa.length; j++)
				fa[j] = f.get(j);
			
			
			while (true)
			{
				boolean sw = false;
				for (int j=0; j<f.size()-1; j++)
				{
					if (fa[j].startTime > fa[j+1].startTime)
					{
						sw = true;
						Fixation a = fa[j];
						fa[j] = fa[j+1];
						fa[j+1]  = a;
					}
				}
				if (!sw) break;
			}
			
			f.clear();
			for (int j=0; j<fa.length; j++)
			{
				fa[j].rank = j;
				f.add(fa[j]);
			}
		}
	}
	
	public void cleanTimes()
	{
		long minTime = Long.MAX_VALUE;
		for (int i=0; i<this.stimuli.size(); i++)
			for (int j=0; j<this.fixations.get(i).size(); j++)
			{
				Fixation f = this.fixations.get(i).get(j);
				if (f.startTime < minTime) minTime = f.startTime;
				if (f.endTime < minTime) minTime = f.endTime;
			}
		
		for (int i=0; i<this.stimuli.size(); i++)
			for (int j=0; j<this.fixations.get(i).size(); j++)
			{
				Fixation f = this.fixations.get(i).get(j);
				f.startTime -= minTime;
				f.endTime -= minTime;
			}
	}
	
}
