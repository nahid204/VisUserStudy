
package perspectives.tree;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import perspectives.base.Property;
import perspectives.properties.PDouble;

public class DefaultTreeDrawer extends TreeDrawer 
{
	private boolean done;
	
	Tree tree;

	public DefaultTreeDrawer(Tree t) {
		super(t);
		
		tree = t;
		done = false;
		
		TreeNode[] nodes = t.getNodes();
		for (int i=0; i<nodes.length; i++)
		{
			nodes[i].addProperty(new Property<PDouble>("x", new PDouble(0.)));
			nodes[i].addProperty(new Property<PDouble>("y", new PDouble(0.)));
		}
	}

	@Override
	public boolean iteration() {
		
		if (done)
			return true;	
		
		TreeNode root = tree.getRoot();
		drawSubTree(root);
		
		setX(root, 0);
		setY(root, 0);
		
		drawSubTree2(tree.getRoot());		

		done = true;
		
		return true;
	}
	
	private double[] drawSubTree(TreeNode tn) //returns two doubles, width and height
	{	
		if (tn.isLeaf())
			return new double[]{1,1};
		
		TreeNode[] children = tn.getChildren();
		
		int width = 0;
		int height = 0;
		
		double[][] bounds = new double[children.length][];
		
		for (int i=0; i<children.length; i++)
		{
			double[] size = drawSubTree(children[i]);
			
			width = width + (int)size[0];
			if (i != children.length-1) width++;
			
			height = Math.max(height, (int)size[1]);
			
			bounds[i] = size;
		}
		height++;
		
		int cx = -width/2;
		for (int i=0; i<children.length; i++)
		{
			cx = cx + (int)Math.floor(bounds[i][0]/2.);
			
			setX(children[i], cx);
			
			setY(children[i], (int)bounds[i][1] - height);
			
			if (i != children.length)
				cx = cx + (int)Math.ceil(bounds[i][0]/2.) + 1;
		}
		
		return new double[]{width, height};		
	}
	
	private void drawSubTree2(TreeNode tn)
	{
		if (tn.isLeaf()) return;
		
		TreeNode[] children = tn.getChildren();
		for (int i=0; i<children.length; i++)
		{
			setX(children[i], getX(tn) + getX(children[i]));
			setY(children[i], getY(tn) - getY(children[i]));
			drawSubTree2(children[i]);
		}
	}

	@Override
	public double getX(TreeNode n) {
		return ((PDouble)n.getProperty("x").getValue()).doubleValue();	
	}

	@Override
	public double getY(TreeNode n) {
		return ((PDouble)n.getProperty("y").getValue()).doubleValue();	
	}

	@Override
	public void setX(TreeNode n, double x) {
		n.getProperty("x").setValue(new PDouble(x));
		
	}

	@Override
	public void setY(TreeNode n, double y) {
		n.getProperty("y").setValue(new PDouble(y));	
	}
	
}
