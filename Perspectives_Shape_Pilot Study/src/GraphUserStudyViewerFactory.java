

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.graph.GraphData;

public class GraphUserStudyViewerFactory extends ViewerFactory{

/**
	 * 
	 */
	private static final long serialVersionUID = -7821492217114445216L;

public RequiredData requiredData() {
		
		RequiredData rd = new RequiredData("GraphData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "Graph User Study Viewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new GraphUserStudyViewer(name, (GraphData)this.getData().get(0));
		return null;
	}

}
