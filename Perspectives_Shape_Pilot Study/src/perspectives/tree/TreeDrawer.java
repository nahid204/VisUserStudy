package perspectives.tree;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import perspectives.base.Property;
import perspectives.properties.PInteger;
import perspectives.two_d.Vector2D;


public abstract class TreeDrawer
{	
	public TreeDrawer(Tree t){};
	
	public abstract boolean iteration();	
	
	public abstract double getX(TreeNode n);	
	public abstract double getY(TreeNode n);	
	public abstract void setX(TreeNode n, double x);	
	public abstract void setY(TreeNode n, double y);
}
	

