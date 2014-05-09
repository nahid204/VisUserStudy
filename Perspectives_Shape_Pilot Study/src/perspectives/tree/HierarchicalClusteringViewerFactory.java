package perspectives.tree;

import java.util.Vector;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.base.ViewerFactory.RequiredData;
import perspectives.util.DistanceMatrixDataSource;
import perspectives.util.TableData;

public class HierarchicalClusteringViewerFactory extends ViewerFactory 
{
	
	@Override
	public RequiredData requiredData() {
		RequiredData rd1 = new RequiredData("TreeData","1");
		RequiredData rd2 = new RequiredData("TableData","1");
		RequiredData rd3 = new RequiredData("DistanceMatrixDataSource","1");
		
		RequiredData[] op = {rd1,rd2,rd3};
		RequiredData rd4 = RequiredData.Or(op);
		
		return rd4;
	}

	@Override
	public String creatorType() {
		return "Hierarchical Clustering";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
		{
			if (this.getData().get(0).getClass() == new TreeData("dummy").getClass())
				return new HierarchicalClusteringViewer(name, (TreeData)this.getData().get(0));
			else if (this.getData().get(0).getClass() == new TableData("dummy").getClass())
				return new HierarchicalClusteringViewer(name, (TableData)this.getData().get(0));
			else
				return new HierarchicalClusteringViewer(name, (DistanceMatrixDataSource)this.getData().get(0));
		}
		return null;
	}

}
