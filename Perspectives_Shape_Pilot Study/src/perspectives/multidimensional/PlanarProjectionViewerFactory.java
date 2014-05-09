package perspectives.multidimensional;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;
import perspectives.tree.HierarchicalClusteringViewer;
import perspectives.tree.TreeData;
import perspectives.util.DistanceMatrixDataSource;
import perspectives.util.TableData;



public class PlanarProjectionViewerFactory extends ViewerFactory 
{	

		public RequiredData requiredData() {
			
			RequiredData rd1 = new RequiredData("TableData","1");
			RequiredData rd2 = new RequiredData("DistanceMatrixDataSource", "1");
						
			return RequiredData.Or(new RequiredData[]{rd1,rd2});
		}

		@Override
		public String creatorType() {
			// TODO Auto-generated method stub
			return "Planar Projection";
		}

		@Override
		public Viewer create(String name) {
			if (this.isAllDataPresent())
			{
				if (this.getData().get(0).getClass() == new TableData("dummy").getClass())
					return new PlanarProjectionViewer(name, (TableData)this.getData().get(0));
				else
					return new PlanarProjectionViewer(name, (DistanceMatrixDataSource)this.getData().get(0));
			}
			return null;
		}
}
