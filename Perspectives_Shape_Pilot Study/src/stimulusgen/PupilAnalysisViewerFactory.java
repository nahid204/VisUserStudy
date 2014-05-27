package stimulusgen;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;

public class PupilAnalysisViewerFactory extends ViewerFactory{

	@Override
	public RequiredData requiredData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Pupil Analysis Viewer";
	}

	@Override
	public Viewer create(String name) {
		// TODO Auto-generated method stub
		if(this.isAllDataPresent())
		{
			return new PupilAnalysisViewer(name);
		}
		else
		{
			return null;
		}
	}

}
