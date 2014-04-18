package perspectives.properties;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.base.PropertyWidget;

public class PTextWidget extends PropertyWidget{
	public PTextWidget(Property p) {
		super(p);
	}


	JTextArea control = null;	
	
	public void widgetLayout()
	{			
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel lp = new JPanel();
		lp.setLayout(new BoxLayout(lp, BoxLayout.X_AXIS));
		lp.add(new JLabel(this.p.getDisplayName()));
		lp.add(Box.createHorizontalGlue());
		this.add(lp);

		final PropertyWidget th = this;
		
		control = new JTextArea();
		control.setLineWrap(true);
		control.setText(((PText)this.p.getValue()).stringValue());
		control.setMaximumSize(new Dimension(500,200));
		Font font = new Font("Verdana", Font.PLAIN, 10);
		control.setFont(font);
	
		
		this.add(Box.createRigidArea(new Dimension(5,1)));
		this.add(control);	
	
		
		p.setReadOnly(p.getReadOnly());
		p.setVisible(p.getVisible());	
	}		


	@Override
	public <T extends PropertyType> void propertyValueUpdated(T newvalue) {
		control.setText(((PText)newvalue).stringValue());		
	}

	@Override
	public void propertyReadonlyUpdated(boolean r) {
		if (control != null)
		{
			if (r)
				control.setEditable(false);
			else
				control.setEditable(true);
		}		
	}

}
