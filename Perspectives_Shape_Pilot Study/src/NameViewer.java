import java.awt.Color;
import java.awt.Graphics2D;

import perspectives.base.Property;
import perspectives.base.PropertyManager;
import perspectives.base.Viewer;
import perspectives.properties.PColor;
import perspectives.properties.POptions;
import perspectives.properties.PString;
import perspectives.two_d.JavaAwtRenderer;


public class NameViewer extends Viewer implements JavaAwtRenderer{

	NameData nameData;
	
	String shape;
	Color color;
	String selected;
	
	public NameViewer(String name, NameData d) {
		super(name);	
		nameData = d;
		
		POptions shapeOptions = new POptions(new String[]{"Oval","Rectangle"});		
		Property<POptions> shapes = new Property<POptions>("Shapes", shapeOptions){
					@Override
					protected boolean updating(POptions newvalue) {
						
						System.out.println("update shape: " + newvalue.selectedIndex);
						
						shape = newvalue.options[newvalue.selectedIndex];	
						
						System.out.println("update shape2: " + shape);
						requestRender();
						return true;
					}					
				};
		addProperty(shapes);
		shape = "Oval";
		
			
		Property<PColor> colorp = new Property<PColor>("Color",  new PColor(Color.LIGHT_GRAY)){
					@Override
					protected boolean updating(PColor newvalue) {						
						color = newvalue.colorValue(); 	
						requestRender();
						return true;
					}					
				};
		addProperty(colorp);
		color = Color.LIGHT_GRAY;
		
		Property<PString> selectedp = new Property<PString>("Selected", new PString("")){
					@Override
					protected boolean updating(PString newvalue) {						
						selected = newvalue.stringValue();
						requestRender();
						return true;
					}

					@Override
					protected void receivedBroadcast(PString newvalue,
							PropertyManager sender) {
					
						setValue(newvalue);
						super.receivedBroadcast(newvalue, sender);
					}
				};
		selectedp.setPublic(true);
		addProperty(selectedp);
	}

	@Override
	public void render(Graphics2D g) {
		
		for (int i=0; i<nameData.names.length; i++)
		{
			if (nameData.names[i].equals(selected))
				g.setColor(Color.red);
			else
				g.setColor(color);
			if (shape.equals("Oval"))
				g.fillOval(i*100, i*100, 80, 40);
			else if (shape.equals("Rectangle"))
				g.fillRect(i*100, i*100, 80, 40);
			
			g.setColor(Color.black);
			g.drawString(nameData.names[i], i*100+40, i*100+20);
		}
		
	}	

	@Override
	public boolean mousepressed(int x, int y, int button) {
		
		for (int i=0; i<nameData.names.length; i++)
			if (x > i*100 && x < i*100+80 && y > i*100 && y < i*100+40)
			{
				getProperty("Selected").setValue(new PString(nameData.names[i]));
				return true;
			}	
		return false;
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mousereleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousemoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void keyPressed(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}
	

}
