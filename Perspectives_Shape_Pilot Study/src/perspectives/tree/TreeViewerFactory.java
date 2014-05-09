package perspectives.tree;

import java.util.Vector;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.base.ViewerFactory.RequiredData;

public class TreeViewerFactory extends ViewerFactory
{

	@Override
	public RequiredData requiredData() {
		RequiredData rd = new RequiredData("TreeData","1");
		return rd;
	}

	@Override
	public String creatorType() {
		return "treeviewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent())
			return new TreeViewer(name, (TreeData)this.getData().get(0));
		return null;
	}
}
