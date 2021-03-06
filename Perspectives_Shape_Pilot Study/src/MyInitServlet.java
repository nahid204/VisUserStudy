import brain.BrainData;
import brain.BrainStatsD3Viewer;
import brain.BrainStatsD3ViewerFactory;
import brain.BrainViewer;
import perspectives.base.Environment;
import perspectives.graph.GraphData;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.properties.PFileInput;
import perspectives.properties.POptions;
import perspectives.tree.HierarchicalClusteringViewer;
import perspectives.tree.TreeData;
import perspectives.util.DistanceMatrixDataSource;
import perspectives.web.InitServlet;


public class MyInitServlet extends InitServlet{

	@Override
	public void environmentInit(Environment e) {
		
		/*GraphData d = new GraphData("graphdata");
		PFileInput f = new PFileInput();
		f.path = e.getLocalDataPath() + "/edgelist.txt";
		System.out.println(f.path);
		d.getProperty("Graph File").setValue(f);
		
		while (!d.isLoaded())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
				
		e.addDataSource(d);		
		
		GraphUserStudyViewer v = new GraphUserStudyViewer("User Study", d);
		e.addViewer(v);		
	
		
		String path = e.getLocalDataPath() + "/edgelist_positions.txt";
		PFileInput file = new PFileInput();
		file.path = path;		
		v.getProperty("Load Positions").setValue(file);
		
		System.out.println("done loading positions");
	
		v.showUserStudy();*/
		
		
		///////////////////////////////////////////////////////////////////////
		
		/*BrainData d = new BrainData("braindata");
		PFileInput f = new PFileInput();
		f.path = e.getLocalDataPath() + "/tubes.txt";
		d.getProperty("Load").setValue(f);
		
		while (!d.isLoaded())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
				
		e.addDataSource(d);		
		
		BrainViewer v = new BrainViewer("brainviewer", d);
		e.addViewer(v);	
		
		BrainStatsD3Viewer d3 = new BrainStatsD3Viewer("viewerd3",d);
		e.addViewer(d3);
		
		e.linkViewers(v, d3, true);*/
		
		
		TreeData d = new TreeData("dm");
		String path = e.getLocalDataPath() + "/linkage.txt";
		
		POptions f = (POptions)d.getProperty("Format").getValue();
		f.selectedIndex = 2;
		d.getProperty("Format").setValue(f);
		d.getProperty("Tree File").setValue(new PFileInput(path));
		e.addDataSource(d);
		
		HierarchicalClusteringViewer v = new HierarchicalClusteringViewer("hc",d);
		e.addViewer(v);
		
		BrainExperiments v2 = new BrainExperiments("exp");
		v2.getProperty("ImageDir").setValue(new PFileInput(e.getLocalDataPath() + "/brainproj"));
		e.addViewer(v2);
		
		System.out.println("both viewrs created");
		
		v.setPropertyBarHidden(true);
		v2.setNoVisibleProperties(true);
		
		e.linkViewers(v, v2, true);
		
	
	}
}
