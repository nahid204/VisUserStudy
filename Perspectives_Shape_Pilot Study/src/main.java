import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import brain.BrainDataFactory;
import brain.BrainDataModifier;
import brain.BrainDataModifierFactory;
import brain.BrainViewerFactory;

import perspectives.*;
import perspectives.base.Environment;
import perspectives.graph.BundledGraphFactory;
import perspectives.graph.GraphData;
import perspectives.graph.GraphDataFactory;
import perspectives.graph.GraphViewerFactory;
import perspectives.multidimensional.PlanarProjectionViewerFactory;
import perspectives.parallel_coordinates.ParallelCoordinateViewerFactory;
import perspectives.properties.PFileInput;
import perspectives.tree.HierarchicalClusteringViewerFactory;
import perspectives.tree.TreeDataFactory;
import perspectives.tree.TreeViewerFactory;
import perspectives.util.DistanceMatrixDataSourceFactory;
import perspectives.util.TableDataFactory;
import stimulusgen.AreaShapeViewer;
import stimulusgen.AreaShapeViewerFactory;
import stimulusgen.CurveStructureViewer;
import stimulusgen.CurveStructureViewerFactory;
import stimulusgen.DifficultyExperiment;
import stimulusgen.GazeAnalyzer;
import stimulusgen.MultiplicationViewerFactory;
import stimulusgen.PupilAnalysisViewerFactory;
import stimulusgen.PupilSizeViewer;


public class main {

	  public static void main(String[] args) {  
		  
		  
		/*  for (int exp=0; exp<379; exp++)
		  {
		  BufferedImage[] im = BrainExperiment.projectExperimentAll("2", "c:/users/radu/documents/facedata.txt");
		  for (int i=0; i<im.length; i++)
			try {
				ImageIO.write(im[i], "PNG", new File("c:/exp_"+ exp + "_"+i+".png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }*/
		  
		 /* for (int exp=1; exp<=378; exp++)
		  {
		  BufferedImage im = BrainExperiment.projectExperimentOne(""+exp, "c:/users/radu/documents/FaceCoordVal.txt");
		 
			try {
				ImageIO.write(im, "PNG", new File("c:/exp_"+ (exp+379) + ".png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		  }*/
		  
		  Environment e = new Environment(false);
		 e.setLocalDataPath("c:/work/code/perspectives/data");
		 
		// e.addViewer(new BrainExperiments("blka"));
		 
		// e.addViewer(new PivotPathLayout("Gg"));
		 
		// e.addViewer(new AreaShapeViewer("nnn"));
		// e.addViewer(new CurveStructureViewer("bbb"));
		 
		// e.addViewer(new DifficultyExperiment("f"));
		 
		// e.addViewer(new PngTester("f"));
	    
	       
	     e.registerDataSourceFactory(new GraphDataFactory());
	      
	   //  e.registerDataSourceFactory(new TableDataFactory()); 
	      
	      e.registerViewerFactory(new GraphViewerFactory());
	      
	      e.registerDataSourceFactory(new DistanceMatrixDataSourceFactory());
	      e.registerViewerFactory(new HierarchicalClusteringViewerFactory());
	      
	      e.registerViewerFactory(new PlanarProjectionViewerFactory());
	      e.addViewer(new PupilSizeViewer("f"));
	      e.registerViewerFactory(new PupilAnalysisViewerFactory());
	      
	    //  e.registerViewerFactory(new BundledGraphFactory());
	      
	    // e.registerViewerFactory(new ParallelCoordinateViewerFactory());
	      
	     e.registerDataSourceFactory(new BrainDataFactory());
	      e.registerViewerFactory(new BrainViewerFactory());
	      e.registerViewerFactory(new BrainViewerETFactory());

	      e.registerDataSourceFactory(new TreeDataFactory());
	      e.registerViewerFactory(new TreeViewerFactory());
	      e.registerViewerFactory(new MultiplicationViewerFactory());
	      e.registerViewerFactory(new CurveStructureViewerFactory());
	      e.registerViewerFactory(new AreaShapeViewerFactory());
	      
	      //e.registerDataSourceFactory(new BrainDataModifierFactory());
	     // e.registerViewerFactory(new PerformanceViewerFactory());
	      
	     // e.addViewer(new PerformanceTester("g"));
	      
	    //  e.addViewer(new GazeAnalyzer("g"));
	     // e.registerDataSourceFactory(new NameDataFactory());
	     // e.registerViewerFactory(new NameViewerFactory());   
		  
		
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
		
		GraphUserStudyViewer v = new GraphUserStudyViewer("v", d);
		e.addViewer(v);
		String path = e.getLocalDataPath() + "/edgelist_positions.txt";
		PFileInput file = new PFileInput();
		file.path = path;		
		v.getProperty("Load Positions").setValue(file);
		v.showUserStudy();*/
	  }
	      
}


