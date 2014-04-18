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
import perspectives.parallel_coordinates.ParallelCoordinateViewerFactory;
import perspectives.properties.PFileInput;
import perspectives.util.TableDataFactory;
import stimulusgen.AreaShapeViewerFactory;
import stimulusgen.CurveStructureViewerFactory;
import stimulusgen.CurveViewerFactory;
import stimulusgen.GazeAnalyzer;
import stimulusgen.MultiplicationViewerFactory;
import stimulusgen.StimulusGenViewerFactory;


public class main {

	  public static void main(String[] args) {  
		  
		  Environment e = new Environment(false);
//		 e.setLocalDataPath("c:/work/code/perspectives/data");
		 
		// e.addViewer(new PngTester("f"));
	    
	       
	     e.registerDataSourceFactory(new GraphDataFactory());
	      
	   //  e.registerDataSourceFactory(new TableDataFactory()); 
	      
	      e.registerViewerFactory(new GraphViewerFactory());
	      e.registerViewerFactory(new StimulusGenViewerFactory());
	      e.registerViewerFactory(new CurveViewerFactory());
	      e.registerViewerFactory(new CurveStructureViewerFactory());
	      e.registerViewerFactory(new MultiplicationViewerFactory());
	      e.registerViewerFactory(new AreaShapeViewerFactory());
	    //  e.registerViewerFactory(new BundledGraphFactory());
	      
	    // e.registerViewerFactory(new ParallelCoordinateViewerFactory());
	      
	   //  e.registerDataSourceFactory(new BrainDataFactory());
	     // e.registerViewerFactory(new BrainViewerFactory());

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


