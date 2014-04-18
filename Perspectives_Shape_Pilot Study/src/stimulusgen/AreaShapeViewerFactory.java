package stimulusgen;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.base.ViewerFactory.RequiredData;

public class AreaShapeViewerFactory extends ViewerFactory{
	@Override
	public RequiredData requiredData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Area Shape Viewer";
	}

	@Override
	public Viewer create(String name) {
		// TODO Auto-generated method stub
		if(this.isAllDataPresent())
		{
			return new AreaShapeViewer(name);
		}
		else
		{
			return null;
		}
	}


}
