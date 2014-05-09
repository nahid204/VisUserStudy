package perspectives.tree;

import java.util.ArrayList;

import perspectives.base.Property;

public class TreeNode
{
	private ArrayList<TreeNode> childs;	
	private TreeNode parent;	
	private ArrayList<Property> properties;
	
	private ArrayList<TreeNodeChangeListener> listeners;
	
	public TreeNode()
	{
		childs = new ArrayList<TreeNode>();
		parent = null;
		properties = new ArrayList<Property>();
		listeners = new ArrayList<TreeNodeChangeListener>();
	}
	
	public boolean isLeaf()
	{
		if (childs.size() == 0) return true;
		return false;
	}
	
	public boolean isRoot()
	{
		if (parent == null)
			return true;
		return false;
	}
	
	public TreeNode[] getChildren()
	{
		TreeNode[] r = new TreeNode[childs.size()];
		childs.toArray(r);
		return r;
	}
	
	public void addChild(TreeNode child)
	{
		childs.add(child);
		for (int i=0; i<listeners.size(); i++)
			listeners.get(i).childAdded(this,child);
	}
	
	public void removeChild(TreeNode child)
	{
		int index = childs.indexOf(child);
		if (index < 0)
			return;
		
		childs.remove(index);
		for (int i=0; i<listeners.size(); i++)
			listeners.get(i).childRemoved(this,child);
	}
	
	public Property getProperty(String name)
	{
		for (int i=0; i<properties.size(); i++)
			if (properties.get(i).getName() == name)
				return properties.get(i);
		return null;
	}
	
	public ArrayList<Property> getProperties()
	{
		return properties;
	}
	
	public void addProperty(Property p)
	{
		properties.add(p);
	}
	
	public void removeProperty(Property p)
	{
		int index = properties.indexOf(p);
		if (index < 0)
			return;
		
		properties.remove(index);
	}
	
	public void removeProperty(String name)
	{
		removeProperty(getProperty(name));
	}
	
}
