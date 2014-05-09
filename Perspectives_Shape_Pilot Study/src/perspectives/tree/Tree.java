package perspectives.tree;

import perspectives.graph.Graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import perspectives.base.Property;
import perspectives.properties.PBoolean;
import perspectives.properties.PDouble;

public class Tree implements TreeNodeChangeListener
{
	private TreeNode root;
	
	private TreeNode[] nodes;
	private int[] edges1, edges2;
	
	private ArrayList<TreeChangeListener> listeners;
		
	public Tree(Graph g)
	{
		listeners = new ArrayList<TreeChangeListener>();
		this.fromGraph(g);
		createFlattedArrays();
	}

	
	public Tree(TreeNode root)
	{
		listeners = new ArrayList<TreeChangeListener>();
		this.root = root;	
		createFlattedArrays();
	}
	
	private void createFlattedArrays(TreeNode node, ArrayList<TreeNode> nList, ArrayList<Integer> e1List, ArrayList<Integer> e2List)
	{
		nList.add(node);
		int nodeIndex = nList.size()-1;
		
		TreeNode[] c = node.getChildren();
		for (int i=0; i<c.length; i++)
		{
			e1List.add(nodeIndex); 
			e2List.add(nList.size());
			createFlattedArrays(c[i],nList, e1List, e2List);
		}
	}
	
	private void createFlattedArrays()
	{
		ArrayList<TreeNode> l = new ArrayList<TreeNode>();
		ArrayList<Integer> e1= new ArrayList<Integer>();
		ArrayList<Integer> e2 = new ArrayList<Integer>();
		
		createFlattedArrays(root,l,e1,e2);
		
		nodes = l.toArray(new TreeNode[]{});
		edges1 = new int[e1.size()];
		edges2 = new int[e2.size()];
		for (int i=0; i<edges1.length; i++)
		{
			edges1[i] = e1.get(i);
			edges2[i] = e2.get(i);
		}
	}
		
	
	public TreeNode getRoot()
	{
		return root;
	}	
	
	public int getNodeCount()
	{
		return nodes.length;
	}
	
	public TreeNode[] getNodes()
	{
		return nodes.clone();
	}
	
	public int[] getEdgeSources()
	{
		return edges1.clone();
	}
	
	public int[] getEdgeTargets()
	{
		return edges2.clone();
	}
	
	
	
	public Graph getAsGraph() 
	{
		Graph g = new Graph(false);
		asGraph(g, root);
		return g;
	}
	
	private void asGraph(Graph g, TreeNode n)
	{
		for (int i=0; i<edges1.length; i++)
		{
			g.addEdge(""+edges1[i], ""+edges2[i]);
		}
		g.addNodeProperty("0", new Property<PBoolean>("root",new PBoolean(true)));
		
	}
	
	private void fromGraph(Graph g)
	{
		ArrayList<String> nodes = g.getNodes();
		
		TreeNode root = null;
		int rootIndex = -1;
		for (int i=0; i<nodes.size(); i++)
		{
			Property p = g.nodeProperty(nodes.get(i), "root");
			if (p != null && ((PBoolean)p.getValue()).boolValue())
			{
				root = new TreeNode();
				//copy graph node properties
				ArrayList<Property> ps = g.getNodeProperties(nodes.get(i));
				for (int j=0; j<ps.size(); j++)
					root.addProperty(new Property(ps.get(j).getName(), ps.get(j).getValue().copy()));
				rootIndex = i;
			}
		}
		
		if (root == null) return;
		
		
		ArrayList<TreeNode> nodeStack = new ArrayList<TreeNode>();
		ArrayList<String> indexStack = new ArrayList<String>();
		
		indexStack.add(nodes.get(rootIndex));
		nodeStack.add(root);
		
		while (indexStack.size() > 0)
		{
			String index = indexStack.get(0);
			TreeNode node = nodeStack.get(0);
			indexStack.remove(0);
			nodeStack.remove(0);
			
			ArrayList<String> c = g.neighbors(index);
			for (int i=0; i<c.size(); i++)
			{
				TreeNode n2 = new TreeNode();	
				//copy graph node properties
				ArrayList<Property> ps = g.getNodeProperties(c.get(i));
				for (int j=0; j<ps.size(); j++)
					root.addProperty(new Property(ps.get(j).getName(), ps.get(j).getValue().copy()));
				
				node.addChild(n2);
								
				indexStack.add(c.get(i));
				nodeStack.add(n2);
			}
		}
	}


	@Override
	public void childAdded(TreeNode parentNode, TreeNode childNode) {
		createFlattedArrays();	
		for (int i=0; i<listeners.size(); i++)
			listeners.get(i).treeStructureChanged();
	}


	@Override
	public void childRemoved(TreeNode parentNode, TreeNode childNode) {
		createFlattedArrays();	
		for (int i=0; i<listeners.size(); i++)
			listeners.get(i).treeStructureChanged();
	}
}
