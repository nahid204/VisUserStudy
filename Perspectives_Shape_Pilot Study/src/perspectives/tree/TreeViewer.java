package perspectives.tree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import perspectives.base.ObjectInteraction;
import perspectives.base.Property;
import perspectives.base.Viewer;
import perspectives.base.ObjectInteraction.RectangleItem;
import perspectives.properties.PBoolean;
import perspectives.properties.PDouble;
import perspectives.properties.PInteger;
import perspectives.properties.PList;
import perspectives.properties.PPercent;
import perspectives.properties.PString;
import perspectives.two_d.JavaAwtRenderer;
import perspectives.util.Label;
import perspectives.util.Oval;

public class TreeViewer extends Viewer implements JavaAwtRenderer
{

	private Tree tree;	
	private TreeDrawer drawer;
	
	private Color nodeSelectedColor = Color.red;
	private Color nodeColor = Color.gray;
	private Color edgeColor = Color.gray;
	
	private int widthFactor = 100;
	private int heightFactor = 100;
	
	private int nodeSize = 10;
	
	protected Oval[] nodeVisuals;
	
	private boolean prevdone = false;
	
	private ObjectInteraction nodeInteraction = new ObjectInteraction()
	{
		@Override
		protected void itemsSelected(int[] objects) {
			
			String[] indexList = new String[objects.length];	
		
			for (int i=0; i<objects.length; i++)
				indexList[i] = ""+objects[i];					
			
			PList list = new PList(indexList);
			getProperty("Selected").setValue(list);
			
			super.itemsSelected(objects);
		}
		
		@Override
		protected void itemsDeselected(int[] objects) {
			
			ArrayList<Integer> l = new ArrayList<Integer>();
			for (int i=0; i<getNumberOfItems(); i++)
				if (getItem(i).selected)
					l.add(i);
			
			String[] indexList = new String[l.size()];
			for (int i=0; i<l.size(); i++)
				indexList[i] = ""+l.get(i);
			
			PList list = new PList(indexList);
			getProperty("Selected").setValue(list);
			
			super.itemsSelected(objects);
		}
		
		@Override
		protected void mouseIn(int object) {
			Color c = new Color((int)(0.75*nodeColor.getRed()), (int)(0.75*nodeColor.getGreen()), (int)(0.75*nodeColor.getBlue()));
			if (getItem(object).selected)
				c = new Color((int)(0.75*nodeSelectedColor.getRed()), (int)(0.75*nodeSelectedColor.getGreen()), (int)(0.75*nodeSelectedColor.getBlue()));
			
			nodeVisuals[object].setColor(c);
			requestRender();
			super.mouseIn(object);
		}

		@Override
		protected void mouseOut(int object) {
			Color c = nodeColor;
			if (getItem(object).selected)
				c = nodeSelectedColor;
			nodeVisuals[object].setColor(c);
			requestRender();
			super.mouseOut(object);
		}
	};
	
	
	
	
	protected TreeViewer(String name)
	{
		super(name);
		
		nodeInteraction.setNoDragging(true);
		
		Property<PInteger> p4 = new Property<PInteger>("Appearance.Width", new PInteger(10))
				{
					@Override
					protected boolean updating(PInteger newvalue) {

						widthFactor = newvalue.intValue();						
						TreeNode[] nodes = tree.getNodes();
						double maxh = -999999;
						double minh = 999999;
						for (int i=0; i<nodes.length; i++)
						{
							double y = getNodeX(nodes[i]);
							if (y >  maxh) maxh = y;
							if (y < minh) minh = y;
						}
						
						for (int i=0; i<nodes.length; i++)
						{
							double x = getNodeX(nodes[i]);							
							x = minh + widthFactor * (x- minh)/(maxh - minh);								
							setNodeX(nodes[i],x);							
						}						
						requestRender();						
						return true;
					}
			
				};
		this.addProperty(p4);
		
		Property<PInteger> p5 = new Property<PInteger>("Appearance.Height", new PInteger(10))
				{
					@Override
					protected boolean updating(PInteger newvalue) {

						heightFactor = newvalue.intValue();						
						TreeNode[] nodes = tree.getNodes();
						double maxh = -999999;
						double minh = 999999;
						for (int i=0; i<nodes.length; i++)
						{
							double y = getNodeY(nodes[i]);
							if (y >  maxh) maxh = y;
							if (y < minh) minh = y;
						}
						
						for (int i=0; i<nodes.length; i++)
						{
							double y = getNodeY(nodes[i]);							
							y = minh + heightFactor * (y- minh)/(maxh - minh);								
							setNodeY(nodes[i],y);							
						}						
						requestRender();						
						return true;
					}
			
				};
		this.addProperty(p5);
		
		
		Property<PList> pselected = new Property<PList>("Selected", new PList())
				{
					@Override
					protected boolean updating(PList newvalue) {				
						
						for (int i=0; i<nodeVisuals.length; i++)
							nodeVisuals[i].setColor(nodeColor);
						
						nodeInteraction.clearSelection();
						
						for (int i=0; i<newvalue.items.length; i++)
						{
							int index = Integer.parseInt(newvalue.items[i]);							
							nodeVisuals[index].setColor(nodeSelectedColor);
							
							nodeInteraction.getItem(index).selected = true;
						}
									
						requestRender();
						return true;
					}
					
				};
		pselected.setVisible(false);
		this.addProperty(pselected);	
	}
	
	public TreeViewer(String name, TreeData t) {
		this(name);		
		tree = t.tree;		
		setTree(tree);
	}
	
	public void setTree(Tree t)
	{
		tree = t;	
		TreeNode[] nodes = t.getNodes();
		for (int i=0; i<nodes.length; i++)
			nodes[i].addProperty(new Property<PInteger>("index",new PInteger(i)));
		
		drawer = new DefaultTreeDrawer(tree);		
		this.startSimulation(50);
	}
	
	public Tree getTree()
	{
		return tree;
	}

	

	public void simulate() {
		boolean done = drawer.iteration();
		if (prevdone == false && done)	
		{
			doneComputingPositions();
			this.stopSimulation();
		}
		prevdone = done;
	}
	

	
	protected void doneComputingPositions()
	{		
		nodeVisuals = new Oval[tree.getNodeCount()];		
		
		TreeNode[] nodes = tree.getNodes();
		
		double minX = 9999999;
		double minY = 9999999;
		double maxX = -9999999;
		double maxY = -9999999;
		for (int i=0; i<nodes.length; i++)
		{
			double x = getNodeX(nodes[i]);
			double y = getNodeY(nodes[i]);
			
			if (x < minX) minX = x; if (x > maxX) maxX = x;
			if (y < minY) minY = y; if (y > maxY) maxY = y;
		}
		widthFactor = (int)(maxX - minX) * 10;
		heightFactor = (int)(maxY - minY) * 20;
		
		for (int i=0; i<nodes.length; i++)
		{
			double x = getNodeX(nodes[i]);
			double y = getNodeY(nodes[i]);
			
			int s = nodeSize;
			
			Oval o = new Oval(x*10-s,y*20-s, 2*s,2*s);
			o.setColor(nodeColor);
			
			nodeVisuals[i] = o;
			nodeInteraction.addItem(nodeInteraction.new RectangleItem(o));			
		}
		
		Property p1 = getProperty("Appearance.Height");
		Property p2 = getProperty("Appearance.Width");
	
		if (p1 != null)
			p1.setValue(new PInteger(heightFactor));
		if (p2 != null)
			p2.setValue(new PInteger(widthFactor));
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if (tree == null) return;
		
		int[] e1 = tree.getEdgeSources();
		int[] e2 = tree.getEdgeTargets();
		TreeNode[] nodes = tree.getNodes();
		
		for (int i=0; i<e1.length; i++)
		{
			int x1 = (int)getNodeX(nodes[e1[i]]);
			int y1 = (int)getNodeY(nodes[e1[i]]);
			int x2 = (int)getNodeX(nodes[e2[i]]);
			int y2 = (int)getNodeY(nodes[e2[i]]);
			
			g.setColor(edgeColor);
			
			g.drawLine(x1, y1, x2, y1);
			g.drawLine(x2, y1, x2, y2);
		}
		
		for (int i=0; i<nodes.length; i++)
			nodeVisuals[i].render(g);
	}

	protected double getNodeX(TreeNode n) {
		return drawer.getX(n);
	}

	protected double getNodeY(TreeNode n) {
		return drawer.getY(n);
	}

	protected void setNodeX(TreeNode n, double x) {
		drawer.setX(n, x);	
		int index = ((PInteger)n.getProperty("index").getValue()).intValue();
		nodeVisuals[index].x = x;
	}

	protected void setNodeY(TreeNode n, double y) {
		drawer.setY(n, y);
		int index = ((PInteger)n.getProperty("index").getValue()).intValue();
		nodeVisuals[index].y = y;
	}
	
	protected boolean getNodeSelected(int index)
	{
		return this.nodeInteraction.getItem(index).selected;
	}
	protected boolean getNodeHovered(int index)
	{
		return this.nodeInteraction.getItem(index).hovered;
	}
	
	

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

    @Override
    public boolean mousepressed(int x, int y, int button) {
    	nodeInteraction.mousePress(x, y);    
        return false;
    }

    @Override
    public boolean mousereleased(int x, int y, int button) {
    	nodeInteraction.mouseRelease(x, y);  	
        return false;
    }

    @Override
    public boolean mousemoved(int x, int y) {    	
        boolean ret = nodeInteraction.mouseMove(x, y);
        return ret;  
    }

    @Override
    public boolean mousedragged(int x, int y, int px, int py) {	 
    	nodeInteraction.mouseMove(px, py);
    	return false;
    }

	@Override
	public void keyPressed(String key, String modifiers) {		
	}

	@Override
	public void keyReleased(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}
}
