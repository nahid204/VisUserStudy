package brain;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Date;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.util.glu.GLU;

import perspectives.three_d.Vector3D;
import perspectives.three_d.ViewerContainer3D;

public class BrainModel {
	
	BrainData data;
	
	private Tube[] tubes;
	private Color[] colors;
	private Color[] selectedColors;
	private int[] faces;
	private double[] widths;
	private boolean[] hidden;
	private boolean[] selected;
	
	int[] totalNumVerts;
	int[] vbo;
	int[] selectedVbo;

	int vertexStride;
	int colorPointer;
	int vertexPointer;
	int normalPointer;
	
	BrainViewer v;
	
	Pbuffer currentPbuffer;
	
	public BrainModel(BrainData data, BrainViewer v, double width)
	{
		this.v = v;
		
		   int bytesPerFloat = Float.SIZE / Byte.SIZE;
		      vertexStride = 9 * bytesPerFloat;
		      
		      colorPointer = 0;
		      vertexPointer = 3 * bytesPerFloat;
		      normalPointer = 6* bytesPerFloat;
		      
		this.data = data;
		
		tubes = new Tube[data.segments.length];
		colors = new Color[data.segments.length];
		selectedColors = new Color[data.segments.length];
		faces = new int[data.segments.length];
		widths = new double[data.segments.length];
		selected = new boolean[data.segments.length];
		hidden = new boolean[data.segments.length];
		vbo = new int[data.segments.length];
		selectedVbo = new int[data.segments.length];
		totalNumVerts = new int[data.segments.length];
		
		for (int i=0; i<data.segments.length; i++)
		{
			tubes[i] = new Tube(data.segments[i], 6, width);
			faces[i] = 6;
			widths[i] = width;
			
			//this will also create the vbos
			setTubeColor(i, Color.LIGHT_GRAY);
			setSelectedTubeColor(i, Color.red);
		}
	}
	
	public int getTubeCount()
	{
		return tubes.length;
	}
	
	public Tube getTube(int i)
	{
		return tubes[i];
	}
	
	public int[] getSelectedTubes()
	{
		int cnt = 0;
		for (int i=0; i<selected.length; i++)
			if (selected[i]) cnt++;
		
		int[] ret = new int[cnt];
		cnt=0;
		for (int i=0; i<selected.length; i++)
			if (selected[i]) ret[cnt++] = i;
		return ret;
	}	
	
	public boolean getSelected(int tube)
	{
		return selected[tube];
	}
	
	public void selectTubes(int[] tubes)
	{
		for (int i=0; i<tubes.length; i++)
			selected[tubes[i]] = true;
	}
	
	public void deselectTubes(int[] tubes)
	{
		for (int i=0; i<tubes.length; i++)
			selected[tubes[i]] = false;
	}
	
	public void selectTube(int tube)
	{
		selected[tube] = true;
	}
	public void deselectTube(int tube)
	{
		selected[tube] = false;
	}
	
	public void clearSelection()
	{
		for (int i=0; i<selected.length; i++)
			selected[i] = false;
	}
	
	public void setSelectedTubeColor(int tube, Color color)
	{
		selectedColors[tube] = color;
		selectedVbo[tube] = -1;
	}
	
	public void setTubeColor(int tube, Color color)
	{
		colors[tube] = color;		
		vbo[tube] = -1;
	}
	
	public Color getTubeColor(int tube)
	{
		return colors[tube];
	}
	
	public Color getSelectedTubeColor(int tube)
	{
		return selectedColors[tube];
	}
	
	public void setTubeWidth(int tube, double width)
	{
		tubes[tube] = new Tube(data.segments[tube], faces[tube], width);
		setTubeColor(tube, colors[tube]);
		setSelectedTubeColor(tube, selectedColors[tube]);
	}

	public void setTubeFaces(int tube, int faces)
	{
		tubes[tube] = new Tube(data.segments[tube], faces, widths[tube]);
		setTubeColor(tube, colors[tube]);
		setSelectedTubeColor(tube, selectedColors[tube]);
	}
	
	public void setHidden(int tube, boolean hidden)
	{
		this.hidden[tube] = hidden;
	}
	
	public boolean getHidden(int tube)
	{
		return this.hidden[tube];
	}
	
	public void render()
	{
		if (((ViewerContainer3D)v.getContainer()).getPbuffer() != currentPbuffer)
		{
			for (int i=0; i<vbo.length; i++)
			{
				vbo[i] = -1;
				selectedVbo[i] = -1;
			}
			currentPbuffer = ((ViewerContainer3D)v.getContainer()).getPbuffer();
		}
		
		for (int i=0; i<tubes.length; i++)
		{
			if (hidden[i]) continue;
			
			int vbo;
			if (!selected[i])
			{
				if (this.vbo[i] == -1)
				{
					Color c = colors[i];
					GL15.glDeleteBuffers(this.vbo[i]);
					this.vbo[i] = createTubeVbo(i, colors[i]);
				}
				vbo = this.vbo[i];
			}
			else
			{
				if (this.selectedVbo[i] == -1)
				{
					Color c = selectedColors[i];
					GL15.glDeleteBuffers(this.selectedVbo[i]);
					this.selectedVbo[i] = createTubeVbo(i, selectedColors[i]);
				}
				vbo = this.selectedVbo[i];
			}
			
			
		     	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

	        	GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	        	GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
	        	GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		      
	        	GL11.glColorMaterial( GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE );
	        	GL11.glEnable(GL11.GL_COLOR_MATERIAL);

	        	GL11.glColorPointer(3,GL11.GL_FLOAT, vertexStride, colorPointer);
	        	GL11.glVertexPointer(3,GL11.GL_FLOAT, vertexStride, vertexPointer);
	        	GL11.glNormalPointer(GL11.GL_FLOAT, vertexStride, normalPointer);

	        	GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, totalNumVerts[i]);

	        	GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	        	GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	        	GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	        	GL11.glDisable(GL11.GL_COLOR_MATERIAL);

	        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}
	

	private int createTubeVbo(int tube, Color color)
	{
		
		float[] c = new float[]{color.getBlue()/255f, color.getGreen()/255f, color.getRed()/255f};

		totalNumVerts[tube] = tubes[tube].indeces.length;

		IntBuffer buf = BufferUtils.createIntBuffer(1);
		GL15.glGenBuffers(buf);
		int vbo = buf.get();

		
		FloatBuffer data = BufferUtils.createFloatBuffer(tubes[tube].indeces.length * 9);
		      
		for (int i = 0; i < tubes[tube].indeces.length; i++) {		        	
		    data.put(c);
		            
		    Vector3D vertex = tubes[tube].vertices[tubes[tube].indeces[i]];
		    Vector3D normal = tubes[tube].normals[tubes[tube].indeces[i]];
		            
		    float[] vertexf = new float[]{vertex.x, vertex.y, vertex.z};
		    float[] normalf = new float[]{normal.x, normal.y, normal.z};
		            	            
		     data.put(vertexf);
		     data.put(normalf);     
		}		
		data.rewind();

		int bytesPerFloat = Float.SIZE / Byte.SIZE;	  
		int numBytes = data.capacity() * bytesPerFloat;
		GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData( GL15.GL_ARRAY_BUFFER, data,  GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, 0);
		
		return vbo;
	}
	
	public Point2D[] project(int tube, float[] modelMatrix, float[] projectionMatrix, int[] viewport)
	{
		return project(tube, modelMatrix, projectionMatrix, viewport, 1);
	}
	
	public Point2D[] project(int tube, float[] modelMatrix, float[] projectionMatrix, int[] viewport, int step)
	{
		GLU glu = new GLU();
		
	   	 FloatBuffer modelbuf = BufferUtils.createFloatBuffer(16);
    	 modelbuf.put(modelMatrix); modelbuf.rewind();
    	 FloatBuffer projbuf = BufferUtils.createFloatBuffer(16);
    	 projbuf.put(projectionMatrix); projbuf.rewind();
    	 IntBuffer viewportbuf = BufferUtils.createIntBuffer(4);
    	 viewportbuf.put(viewport); viewportbuf.rewind();
		
		Point2D[] projectedSegments = new Point2D[(int)Math.ceil(tubes[tube].segments.length/(double)step)];
		
		int cnt = 0;
		for (int j=0; j<tubes[tube].segments.length; j=j+step)
		{
				FloatBuffer result = BufferUtils.createFloatBuffer(3);
				
				glu.gluProject(tubes[tube].segments[j].x, tubes[tube].segments[j].y, tubes[tube].segments[j].z,
						modelbuf,  projbuf,  viewportbuf, 
						result);
			
				Point2D p = new Point2D.Double(result.get(0), viewport[3] - result.get(1) -1);		
				
				projectedSegments[cnt++] = p;
		}	
	
		return projectedSegments;
	}
		

}
