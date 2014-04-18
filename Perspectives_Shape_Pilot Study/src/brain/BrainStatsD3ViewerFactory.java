/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import perspectives.base.Viewer;
import perspectives.base.ViewerFactory;

/**
 *
 * @author rajin
 */
public class BrainStatsD3ViewerFactory extends ViewerFactory{

	@Override
	public String creatorType() {
		// TODO Auto-generated method stub
		return "BrainStatsD3Viewer";
	}

	@Override
	public Viewer create(String name) {
		if (this.isAllDataPresent()) {
                return new BrainStatsD3Viewer(name, (BrainData)this.getData().get(0));
            }
		return null;
	}

    @Override
    public ViewerFactory.RequiredData requiredData() {
        ViewerFactory.RequiredData rd = new ViewerFactory.RequiredData("BrainData","1");
        return rd;
    }
}
