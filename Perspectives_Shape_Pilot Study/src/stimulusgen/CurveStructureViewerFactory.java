package stimulusgen;
import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import stimulusgen.CurveStructureViewer;


public class CurveStructureViewerFactory extends ViewerFactory {

	@Override
	public RequiredData requiredData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Curve Structure Viewer";
	}

	@Override
	public Viewer create(String name) {
		// TODO Auto-generated method stub
		if(this.isAllDataPresent())
		{
			return new CurveStructureViewer(name);
		}
		else
		{
			return null;
		}
	}


}
