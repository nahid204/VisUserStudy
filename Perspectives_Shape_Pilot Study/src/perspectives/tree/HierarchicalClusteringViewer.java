package perspectives.tree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import perspectives.base.ObjectInteraction;
import perspectives.base.Property;
import perspectives.d3.D3Renderer;
import perspectives.multidimensional.SpringEmbedder;
import perspectives.properties.PDouble;
import perspectives.properties.PList;
import perspectives.properties.POptions;
import perspectives.properties.PPercent;
import perspectives.properties.PString;
import perspectives.util.DistanceMatrixDataSource;
import perspectives.util.HierarchicalClustering;
import perspectives.util.DistancedPoints;
import perspectives.util.Label;
import perspectives.util.Oval;
import perspectives.util.TableData;



public class HierarchicalClusteringViewer extends TreeViewer implements D3Renderer
{
	private double[] percToScale;
	private int[] indexToScale;
	private int scaleIntervals = 10;
	
	
	Label[] scaleVisuals;
	double[] scaleValues;
	ObjectInteraction scaleInteraction = new ObjectInteraction()
	{
		@Override
		protected void itemDragged(int item, Point2D delta) {			
			scaleVisuals[item].x -= delta.getX();
			
			super.itemDragged(item, delta);	
			
			if (item == 0 || 
					(item > 0 && scaleVisuals[item].y + delta.getY() > scaleVisuals[item-1].y)  ||
					(item <scaleVisuals.length-1 && scaleVisuals[item].y + delta.getY() < scaleVisuals[item+1].y))
			{
				scaleVisuals[item].y -= delta.getY();
				return;
			}
			
			for (int i=item+1; i<scaleVisuals.length; i++)
					scaleVisuals[i].y += delta.getY();

		}

		@Override
		protected void mouseIn(int object) {			
			scaleVisuals[object].setColor(new Color(200,120,120,150));
			requestRender();
			super.mouseIn(object);
		}

		@Override
		protected void mouseOut(int object) {
			scaleVisuals[object].setColor(new Color(200,180,180,100));
			requestRender();
			super.mouseOut(object);
		}

	};

	public HierarchicalClusteringViewer(String name, TreeData t) {
		super(name, t);	
		setupProperties();
	}
	
	public HierarchicalClusteringViewer(String name, DistanceMatrixDataSource d) {
		super(name);		
		fromDistancedPoints(d.dm);
		setupProperties();
	}	

	public HierarchicalClusteringViewer(String name, TableData d) {		
		super(name);		
		fromDistancedPoints(d.getTable());
		setupProperties();
	}
	
	private void setupProperties()
	{
		//overloading the selected property
		final Property<PList> oldSelected = getProperty("Selected");
		this.removeProperty("Selected");	    
	    Property<PList> pselected = new Property<PList>("Selected", new PList())
	    		{
			    	@Override
					protected boolean updating(PList newvalue) {
			    		
			    		
			    		
			    		TreeNode[] allNodes = getTree().getNodes();
			    		
			    		String[] ids = new String[newvalue.items.length];
			    		for (int i=0; i<newvalue.items.length; i++)
			    		{
			    			int index = Integer.parseInt(newvalue.items[i]);
			    			ids[i] = ((PString)allNodes[index].getProperty("id").getValue()).stringValue();
			    		}
			    		getProperty("Selected Node").setValue(new PList(ids));
			    		
			    		
			    		String[] sel = newvalue.items;			    		
			    	
			    		ArrayList<String> newselect = new ArrayList<String>();
			    		for (int i=0; i<sel.length; i++)
			    		{
			    			int index = Integer.parseInt(sel[i]);
			    			
			    			newselect.add(sel[i]);
			    			
			    			if (!allNodes[index].isLeaf())
			    			{
			    				
			    				ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
			    				nodesInSubtree(allNodes[index], nodes);
			    				for (int j=1; j<nodes.size(); j++)
			    					newselect.add(""+indexOf(nodes.get(j), allNodes));			    							    				
			    			}
			    		}			    		
			    		oldSelected.setValue(new PList(newselect.toArray(new String[]{})));
						return true;
			    	}
	    		};
	    pselected.setPublic(true);
	    pselected.setVisible(false);
	    addProperty(pselected);
	    
	    Property<PList> selNode = new Property<PList>("Selected Node", new PList());
	    selNode.setVisible(false);
	    selNode.setPublic(true);
	    addProperty(selNode);
	}
	
	private void fromDistancedPoints(DistancedPoints d)
	{
		d.normalize();		
		this.setTree(HierarchicalClustering.compute(d));	
	}
	
	@Override
	protected void doneComputingPositions()
	{
		percToScale = new double[getTree().getNodeCount()];
		indexToScale = new int[getTree().getNodeCount()];
		
		scaleVisuals = new Label[scaleIntervals+1];
		
		TreeNode[] nodes = getTree().getNodes();
		
		double maxh = getMaxHeight();
		for (int i=0; i<percToScale.length; i++)
		{
			double h = getHeight(nodes[i]);			
			
			int scaleIndex = (int)(Math.floor(scaleIntervals * (h/maxh)));
			if (scaleIndex == scaleVisuals.length-1)
				scaleIndex--;
			
			indexToScale[i] = scaleIndex;
			
			double lowScale = scaleIndex * (maxh/scaleIntervals);
			double highScale = (scaleIndex+1) * (maxh/scaleIntervals);
			
			double perc = (h - lowScale) / (highScale - lowScale);
			
			percToScale[i] = perc;
		}
		
		int minX = this.getMinX();
		int maxY = this.getMaxY();
		double maxHeight = this.getMaxHeight();
		scaleVisuals = new Label[11];
		scaleValues = new double[11];
		for (int i=0; i<scaleVisuals.length; i++)
		{
			Label l = new Label(minX-200, maxY - (int)(i*200.), String.format("%1$,.2f", i* maxHeight/scaleIntervals));
			l.setColor(new Color(200,180,180,100));
			l.setFont(l.getFont().deriveFont((float)30.));
			
			scaleValues[i] = i* maxHeight/10.;
		
			scaleVisuals[i] = l;
			scaleInteraction.addItem(scaleInteraction.new RectangleItem(l));
		}
		
		super.doneComputingPositions();
		
		for (int i=0; i<nodeVisuals.length; i++)
		{
			int n = this.numberOfNodes(nodes[i]);
			nodeVisuals[i].w *= Math.pow(n, 0.25);
			nodeVisuals[i].h *= Math.pow(n, 0.25);
		}
		
		this.generateIds();
	}
	
	@Override
	public void render(Graphics2D g) {
		
		int maxX = this.getMaxX();
		for (int i=0; i<scaleVisuals.length; i++)
		{
			int lineThick = 2;
			if (scaleInteraction.getItem(i).hovered)
			{
				g.setColor(new Color(200,100,100,250));
				lineThick = 4;
			}
			else
				g.setColor(Color.gray);
			
			g.setStroke(new BasicStroke(lineThick, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {5,5}, 0));
			g.drawLine((int)scaleVisuals[i].x, (int)scaleVisuals[i].y, maxX, (int)scaleVisuals[i].y);
			if (i > 0)
				g.drawLine((int)scaleVisuals[i].x, (int)scaleVisuals[i].y, (int)scaleVisuals[i-1].x, (int)scaleVisuals[i-1].y);
			
			scaleVisuals[i].render(g);
		}
	
		int[] e1 = getTree().getEdgeSources();
		int[] e2 = getTree().getEdgeTargets();
		TreeNode[] nodes = getTree().getNodes();
		
		for (int i=0; i<e1.length; i++)
			this.renderEdge(e1[i], e2[i], nodes[e1[i]], nodes[e2[i]], i, getNodeSelected(e1[i]) || getNodeHovered(e2[i]), g);
		
		for (int i=0; i<nodes.length; i++)
				this.renderNode(nodes[i], i, this.getNodeSelected(i), this.getNodeHovered(i), g);
	}
	
	public void renderNode(TreeNode n, int nodeIndex, boolean selected, boolean hovered, Graphics2D g)
	{		
		double y = scaleVisuals[indexToScale[nodeIndex]].y + percToScale[nodeIndex] * (scaleVisuals[indexToScale[nodeIndex]+1].y -scaleVisuals[indexToScale[nodeIndex]].y);		
		setNodeY(n, y);	
		double x = getNodeX(n);		
		nodeVisuals[nodeIndex].render(g);
	}
	

	public void renderEdge(int p1, int p2, TreeNode n1, TreeNode n2, int edgeIndex, boolean selected,
			Graphics2D g) {		
		
			double y1 = scaleVisuals[indexToScale[p1]].y + percToScale[p1] * (scaleVisuals[indexToScale[p1]+1].y -scaleVisuals[indexToScale[p1]].y);
			double y2 = scaleVisuals[indexToScale[p2]].y + percToScale[p2] * (scaleVisuals[indexToScale[p2]+1].y -scaleVisuals[indexToScale[p2]].y);
			int x1 = (int)getNodeX(n1);
			int x2 = (int)getNodeX(n2);
			
			g.setStroke(new BasicStroke(1));
			if (selected)
				g.setColor(Color.red);
			else
				g.setColor(Color.black);
			
			g.drawLine(x1, (int)y1, x2, (int)y1);
			g.drawLine(x2, (int)y1, x2, (int)y2);
		
	}
	
	
	private int getMaxX()
	{
		double maxX = -9999999;
		TreeNode[] nodes = getTree().getNodes();
		for (int i=0; i<nodes.length; i++)
		{
			double x = getNodeX(nodes[i]);
			if (x > maxX) maxX = x;		
		}
		return (int)(maxX);		
	}
	
	private int getMinX()
	{
		double minX = 9999999;
		TreeNode[] nodes = getTree().getNodes();
		for (int i=0; i<nodes.length; i++)
		{
			double x = getNodeX(nodes[i]);
			if (x < minX) minX = x;		
		}
		return (int)(minX);		
	}
	
	private int getMaxY()
	{
		double maxY = 0;
		TreeNode[] nodes = getTree().getNodes();
		for (int i=0; i<nodes.length; i++)
		{
			double y = getNodeY(nodes[i]);
			if (y > maxY) maxY = y;		
		}
		return (int)(maxY);		
	}
	
	private double getHeight(TreeNode n)
	{
		Property<PDouble> p = n.getProperty("height");
		if (p != null)
			return (float)p.getValue().doubleValue();
		
		return Double.NaN;
	}
	
	private double getMaxHeight()
	{
		TreeNode[] nodes = getTree().getNodes();
		double maxHeight = 0;
		for (int i=0; i<nodes.length; i++)
		{
			double h = getHeight(nodes[i]);
			if (!Double.isNaN(h) && h > maxHeight) maxHeight = h;		
		}
		return maxHeight;
	}
		
	
    @Override
    public boolean mousepressed(int x, int y, int button) {   
    	if (super.mousepressed(x, y, button))
    		return true;
    	scaleInteraction.mousePress(x, y);
        return false;
    }

    @Override
    public boolean mousereleased(int x, int y, int button) {
    	if (super.mousereleased(x, y, button))
    		return true;
    	scaleInteraction.mouseRelease(x, y);     
        return false;
    }

    @Override
    public boolean mousemoved(int x, int y) {    	
        boolean ret = super.mousemoved(x, y) || scaleInteraction.mouseMove(x, y);
        return ret;   
    }

    @Override
    public boolean mousedragged(int x, int y, int px, int py) {	
    	  	
        boolean ret = super.mousemoved(x, y) ||  scaleInteraction.mouseMove(x, y);
        return ret;       
    }

	@Override
	public JSONObject renderToData() {
		
        JSONObject data = new JSONObject();   
               
        
        JSONArray nodesArray = new JSONArray();
       /* for (int i=0; i<nodeVisuals.length; i++)
        {
        	JSONObject n = new JSONObject();
        	n.put("x", nodeVisuals[i].x);
        	n.put("y", nodeVisuals[i].y);
        	n.put("w", nodeVisuals[i].w);
        	n.put("h", nodeVisuals[i].h);
        	n.put("color", nodeVisuals[i].getColor().getRed());
        	nodesArray.add(n);
        }*/
        
        JSONArray edgesArray = new JSONArray();  
        /*for (int i=0; i<edgeTargets.length; i++)
        {
        	JSONObject n = new JSONObject();
        	n.put("x1", nodeVisuals[edgeTargets[i]].x);
        	n.put("y1", nodeVisuals[edgeTargets[i]].y);
        	n.put("x2", nodeVisuals[edgeSources[i]].x);
        	n.put("y2", nodeVisuals[edgeSources[i]].y);
        	edgesArray.add(n);
        }*/
        
        data.put("nodes", nodesArray);
        data.put("edges", edgesArray);   
        
        return data;
	
	}

	@Override
	public BufferedImage getImage(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private int numberOfNodes(TreeNode n)
	{
		int ret = 1;
		TreeNode[] c = n.getChildren();
		for (int i=0; i<c.length; i++)
			ret += numberOfNodes(c[i]);
		return ret;
	}
	
	private void nodesInSubtree(TreeNode n, ArrayList<TreeNode> l)
	{
		l.add(n);
		TreeNode[] c = n.getChildren();
		for (int i=0; i<c.length; i++)
			nodesInSubtree(c[i], l);
	}
	
	private int indexOf(TreeNode n)
	{
		TreeNode[] nodes = getTree().getNodes();
		for (int i=0; i<nodes.length; i++)
			if (nodes[i] == n)
				return i;
		return -1;
	}
	
	private int indexOf(TreeNode n, TreeNode[] nodes)
	{		
		for (int i=0; i<nodes.length; i++)
			if (nodes[i] == n)
				return i;
		return -1;
	}
	
	
	private void generateIds()
	{
		ArrayList<TreeNode> leafs = new ArrayList<TreeNode>();
		ArrayList<TreeNode> inners = new ArrayList<TreeNode>();
		
		TreeNode[] nodes = getTree().getNodes();
		
		for (int i=0; i<nodes.length; i++)
		{
			double h = getHeight(nodes[i]);
			double x = getNodeX(nodes[i]);
			
			if (nodes[i].isLeaf()) //leaf
			{
				int index = 0;
				for (; index < leafs.size(); index++)
					if (x < getNodeX(leafs.get(index)))
						break;
				leafs.add(index, nodes[i]);
			}
			else
			{
				int index = 0;
				for (; index < inners.size(); index++)
					if (h < getHeight(inners.get(index)))
						break;
				inners.add(index, nodes[i]);
			}
		}
		
		for (int i=0; i<inners.size(); i++)
			inners.get(i).addProperty(new Property<PString>("id", new PString(""+(leafs.size() + i + 1))));
		for (int i=0; i<leafs.size(); i++)
			leafs.get(i).addProperty(new Property<PString>("id", new PString(""+(i+1))));
	}



}
