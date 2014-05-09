package perspectives.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PFileInput;
import perspectives.properties.POptions;


public class TreeData extends DataSource{
	
	Tree tree;
	
	boolean valid;
	
	private ArrayList<TreeParser> treeParsers;

	public TreeData(String name) {
		super(name);
		
		tree = null;
		valid = false;
		
		treeParsers = new ArrayList<TreeParser>();
		treeParsers.add(new MLTreeParser());
		treeParsers.add(new TextTreeParser());
		treeParsers.add(new MatlabLinkageParser());
		
		try {
			PFileInput f = new PFileInput();
			f.dialogTitle = "Open Graph File";		
			Property<PFileInput> p1 = new Property<PFileInput>("Tree File", f)
			{

				@Override
				protected boolean updating(PFileInput newvalue) {
					POptions format = (POptions)getProperty("Format").getValue();
					
					System.out.println("TreeData, tree file: " + format.selectedIndex + " " + newvalue.path);
					
					TreeParser p = treeParsers.get(format.selectedIndex);
					
					File f = new File(newvalue.path);
					
					try {
						tree = p.from(new FileInputStream(f));
						setLoaded(true);
					} catch (FileNotFoundException e) {						
						e.printStackTrace();
					}									
					
					return true;
				}
				
			};
			this.addProperty(p1);
			
			String[] parserNames = new String[treeParsers.size()];
			for (int i=0; i<parserNames.length; i++) parserNames[i] = treeParsers.get(i).getName();			
			POptions o = new POptions(parserNames);	
			
			Property<POptions> p2 = new Property<POptions>("Format", o);			
			this.addProperty(p2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void addTreeParser(TreeParser p)
	{
		if (treeParsers.indexOf(p) < 0)
			treeParsers.add(p);
		
		String[] parserNames = new String[treeParsers.size()];
		for (int i=0; i<parserNames.length; i++) parserNames[i] = treeParsers.get(i).getName();	
		POptions o = new POptions(parserNames);			
		
		getProperty("Format").setValue(o);
	}

}
