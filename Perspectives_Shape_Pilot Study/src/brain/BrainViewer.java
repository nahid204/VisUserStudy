package brain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import perspectives.base.Property;
import perspectives.base.PropertyManager;
import perspectives.base.Viewer;
import perspectives.three_d.LWJGL3DRenderer;
import perspectives.three_d.Vector3D;
import perspectives.properties.PBoolean;
import perspectives.properties.PColor;
import perspectives.properties.PInteger;
import perspectives.properties.POptions;
import perspectives.properties.PSignal;
import perspectives.properties.PString;


public class BrainViewer extends Viewer implements LWJGL3DRenderer{

	protected BrainData data;
	protected BrainModel brainModel;
	
	protected Vector3D[][] projectedSegments;	
	protected float[] modelMatrix = new float[16];
	protected float[] projectionMatrix = new float[16];
	protected int[] viewport = new int[4];
	
	protected Point2D[][] tubeProjections;
	
	private boolean onlySelected = false;


	public BrainViewer(String name, BrainData data) {
		
		super(name);
		
		this.data = data;
		brainModel = new BrainModel(data, this, 0.01);
		
		tubeProjections = new Point2D[brainModel.getTubeCount()][];
       
	
		Property<PInteger> ptubewidth = new Property<PInteger>("Tube Width", new PInteger(1))
		{
			@Override
			public boolean updating(PInteger newvalue)
			{
				double width = newvalue.intValue();
				if (width <= 0) return true;
				width *= 0.01;
				
				for (int i=0; i<brainModel.getTubeCount(); i++)
					brainModel.setTubeWidth(i, width);
									
				requestRender();
				return true;
			}
		};
		addProperty(ptubewidth);
		
				
		Property<PColor> ptubecolor = new Property<PColor>("Tube Color", new PColor(Color.LIGHT_GRAY))
		{
								@Override
								public boolean updating(PColor newvalue)
								{
									Color c = newvalue.colorValue();
									for (int i=0; i<brainModel.getTubeCount(); i++)
										brainModel.setTubeColor(i, c);									
									
									requestRender();
									return true;
								}
		};
        addProperty(ptubecolor);


        Property<PString> pselection = new Property<PString>("Selected", new PString("")) 
        {
 	
                               @Override
                                protected void receivedBroadcast(PString newvalue, PropertyManager sender) {

                                    String[] split = newvalue.stringValue().split(",");
                                  
                                    int[] select = new int[split.length];
                                    
                                    for (int i=0; i<split.length; i++)
                                    	select[i] = Integer.parseInt(split[i]);
                                    
                                    int mode = ((POptions)getProperty("Selection Mode").getValue()).selectedIndex;
                               
                                    brainModel.deselectTubes(select);
                                    if (onlySelected) 
                                    	for (int i=0; i<select.length; i++)
                                    		brainModel.setHidden(select[i], true);                                                         		
                            		getProperty("Selected").setValue(new PString(computeSelectionString()));	
                                    

                                    requestRender();
                                }
         };
         pselection.setPublic(true);
         pselection.setVisible(false);
         addProperty(pselection);
                            
                            
         Property<POptions> pSelectionMode = new Property<POptions>("Selection Mode", new POptions(new String[]{"Add", "Remove"}));
         addProperty(pSelectionMode);
         
         Property<PSignal> pClear = new Property<PSignal>("Clear Selection", new PSignal())
        		 {
					@Override
					protected boolean updating(PSignal newvalue) {
						brainModel.clearSelection();
						requestRender();
						return true;
					}
        	 
        		 };
        addProperty(pClear);
                            
         Property<PBoolean> pOnlySelected = new Property<PBoolean>("Only Selected", new PBoolean(false))
         {
                            	protected boolean updating(PBoolean newvalue) {
                            		
                            		onlySelected = newvalue.boolValue();
                            		if (onlySelected)
                            		{
                            			for (int i=0; i<brainModel.getTubeCount(); i++)
                            				if (brainModel.getSelected(i))
                            					brainModel.setHidden(i, false);
                            				else
                            					brainModel.setHidden(i, true);
                            		}
                            		else
                            			for (int i=0; i<brainModel.getTubeCount(); i++)                            				
                            				brainModel.setHidden(i, false);
                            		
                            		requestRender();
                            		return true;
                            	};
                    		};
          addProperty(pOnlySelected);
	}	
	

	@Override
	public void render3D() {
		
		long ttt = new Date().getTime();	
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable( GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc( GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,  GL11.GL_NICEST);
        
		GL11.glPushMatrix();
		
		FloatBuffer pmb = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, pmb);
		pmb.get(projectionMatrix);
		
		FloatBuffer mvb = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, mvb);
		mvb.get(modelMatrix);
		
		IntBuffer vpb = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, vpb);
		vpb.get(viewport);	 		
		 
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();		
		GL11.glLoadIdentity();		
		GL11.glTranslated(0, 0, -10);	
		 
        float SHINE_ALL_DIRECTIONS = 1;
        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(new float[]{0, 0, -10, SHINE_ALL_DIRECTIONS}); lightPos.rewind();
        FloatBuffer lightColorAmbient = BufferUtils.createFloatBuffer(4);
        lightColorAmbient.put(new float[]{0.01f, 0.01f, 0.01f, 1f}); lightColorAmbient.rewind();
        FloatBuffer lightColorSpecular = BufferUtils.createFloatBuffer(4);
        lightColorSpecular.put(new float[]{0.99f, 0.99f, 0.99f, 1f});lightColorSpecular.rewind();
                
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPos);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, lightColorAmbient);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, lightColorSpecular);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightColorSpecular);

        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Set material properties.
        FloatBuffer rgba = BufferUtils.createFloatBuffer(4);
        rgba.put(new float[]{.1f, .1f, .1f, 1f});rgba.rewind();    
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, rgba);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 1f);
        
        GL11.glPopMatrix();     

        brainModel.render();	
		
	    GL11.glPopMatrix();
	      
	    System.out.println("brain viewer render time: " + (new Date().getTime()-ttt));
		
	}

	
	boolean drag = false;
	int startX, startY;
	int endX, endY;
	
	
	public boolean mousepressed(int x, int y, int button)
	{
		if (button == 1)
		{			
			this.requestRender();

			drag = true;
			startX = x;
			startY = y;
			endX = x;
			endY = y;
		}
		return false;
	};
	
	public boolean mousereleased(int x, int y, int button)
	{
		if (button == 1)
		{
			drag = false;
			
			long t = new Date().getTime();
			for (int i=0; i<brainModel.getTubeCount(); i++)
				tubeProjections[i] = brainModel.project(i, modelMatrix, projectionMatrix, viewport);
			t = new Date().getTime()-t;
			System.out.println("brain projected in " + t + " ms");
			
			Line2D.Double l = new Line2D.Double(startX, startY, x, y);
			
			int mode = ((POptions)this.getProperty("Selection Mode").getValue()).selectedIndex;
			
			for (int i=0; i<brainModel.getTubeCount(); i++)
			{
				if (brainModel.getHidden(i))
					continue;
				
				for (int j=0; tubeProjections[i] != null && j<tubeProjections[i].length-1; j++)
				{				
					Line2D.Double l2 = new Line2D.Double(tubeProjections[i][j].getX(), tubeProjections[i][j].getY(), 
							tubeProjections[i][j+1].getX(), tubeProjections[i][j+1].getY());
					
					if (l.intersectsLine(l2))
					{
						if (mode == 0)
							brainModel.selectTube(i);
						else
						{
							brainModel.deselectTube(i);
							if (onlySelected)
								brainModel.setHidden(i, true);
						}
						break;
					}
				}
			}
			
			//set the selected property so that the selection gets propagated to linked viewers
			getProperty("Selected").setValue(new PString(computeSelectionString()));	
		
			this.requestRender();
		}
		return false;
	};
	
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy)
	{		
		if (drag)
		{	
			endX = currentx;
			endY = currenty;
			this.requestRender();
			return true;
		}
		return false;
	};

	public void render2DOverlay(Graphics2D g)
	{
		if (drag)
		{
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(10));
			g.drawLine(startX,  startY, endX, endY);
		}
	}

	private String computeSelectionString()
	{
		String s = "";
		for (int i=0; i<brainModel.getTubeCount(); i++)
			if (brainModel.getSelected(i))
			{
				if (s.length() == 0)
					s += i;
				else
					s += "," + i;
			}
		return s;
	}


	@Override
	public Color getBackgroundColor() {
		return Color.white;
	}


	@Override
	public boolean mousemoved(int x, int y) {
		return false;
	}

	@Override
	public void keyPressed(String key, String modifiers) {
	}


	@Override
	public void keyReleased(String key, String modifiers) {	
	}
}
