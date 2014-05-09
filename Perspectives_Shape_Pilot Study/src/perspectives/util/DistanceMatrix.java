package perspectives.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

public class DistanceMatrix implements DistancedPoints
{
	float[][] d;
	
	HashMap<String, Integer> map;
	String[] elements;
	
	int count;
	
	public DistanceMatrix(int nrElem)
	{
		d = new float[nrElem][];
		
		for (int i=0; i<nrElem; i++)
			d[i] = new float[i+1];
		
		for (int i=0; i<d.length; i++)
			for (int j=0; j<d[i].length; j++)
				d[i][j] = Float.MAX_VALUE;
		
		count = nrElem;
	}
	
	public DistanceMatrix(String[] elements)
	{
		this(elements.length);
		
		this.elements = elements;
		
		for (int i=0; i<elements.length; i++)
			map.put(elements[i], new Integer(i));		
	}
	
	public void set(int i, int j, float val)
	{
		if (i == j) return;
		
		if (i < j)
			d[j][i] = val;
		else
			d[i][j] = val;
	}
	
	public void set(String elem1, String elem2, float val)
	{
		Integer i1 = map.get(elem1);
		if (i1 == null) return;
		Integer i2 = map.get(elem2);
		if (i2 == null) return;
		
		set(i1.intValue(), i2.intValue(), val);
	}
	
	public float get(int i, int j)
	{
		if (i<j)
			return d[j][i];
		else
			return d[i][j];	
	}
	
	public float get(String elem1, String elem2)
	{
		Integer i1 = map.get(elem1);
		if (i1 == null) return Float.MAX_VALUE;
		Integer i2 = map.get(elem2);
		if (i2 == null) return Float.MAX_VALUE;
		
		return get(i1.intValue(), i2.intValue());	
	}
	
	public void toFile(String file)
	{
		 FileOutputStream fos = null;
	     BufferedOutputStream bos = null;
	     DataOutputStream dos = null;
	       try {
			fos = new FileOutputStream(file);
		
	       bos = new BufferedOutputStream(fos);
	       dos = new DataOutputStream(bos);
	       
	       dos.writeInt(d.length+1);
	       
	       if (map.size() != 0)
	       {
	    	   dos.writeBoolean(true);
	    	   
	    	   String[] k = (String[]) map.keySet().toArray();
	    	   
	    	   for (int i=0; i<k.length; i++)
	    	   {
	    		   dos.writeInt(k[i].length());
	    		   dos.writeChars(k[i]);
	    	   }
	       }
	       else
	    	   dos.writeBoolean(false);
	       	       
	       for (int i = 0; i < d.length; i ++)
	    	   for (int j=0; j < d[i].length; j++)
	    		   dos.writeFloat(d[i][j]);
	        dos.close();
	       }	        
	        catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	        
	
	public void FromFile(String file)
	{
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		try{
			fis = new FileInputStream(file);
			
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			
			int nrElem = dis.readInt();
			d = new float[nrElem-1][];			
			for (int i=0; i<nrElem; i++)
				d[i] = new float[i+1];
			
			
			boolean b = dis.readBoolean();
			
			if (b)
			{
				map = new HashMap<String, Integer>();
				for (int i=0; i<nrElem; i++)
				{
					int se = dis.readInt();
					String s="";
					for (int j=0; j<se; j++)
						s += dis.readChar();
					map.put(s, new Integer(i));
				}
			}
			else
				map = null;
			
			for (int i=0; i<d.length; i++)
				for (int j=0; j<d[i].length; j++)
					d[i][j] = dis.readFloat();					
		}
		catch (Exception e)
		{
			
		}
		
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public float getDistance(int index1, int index2) {
		return get(index1, index2);
	}

	@Override
	public String getPointId(int index) {
		if (elements != null && index >=0 && index<elements.length) return elements[index];
		else
			return ""+index;
	}

	@Override
	public int getPointIndex(String id) {
		if (map != null)
		{
			Integer o = map.get(id);
			if (o == null) return -1;
			else return o;
		}
		else
		{
			try{
				int i = Integer.parseInt(id);
				return i;
			}
			catch(Exception e)
			{
				return -1;
			}
		}
	}

	@Override
	public void normalize() {
		float max = -999999;
		float min = 999999;
		
		for (int i=0; i<this.getCount()-1; i++)
			for (int j=i+1; j<getCount(); j++)
			{
				float f = get(i,j);
				if (f > max) max = f;
				if (f < min)
					min = f;
			}
		
		for (int i=0; i<this.getCount()-1; i++)
			for (int j=i+1; j<getCount(); j++)
				set(i,j,(float)(get(i,j)-min)/(max-min));
	}

	
	
}
