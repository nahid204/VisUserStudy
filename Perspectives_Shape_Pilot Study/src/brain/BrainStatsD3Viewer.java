/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import perspectives.base.PropertyManager;
import perspectives.d3.D3Renderer;
import perspectives.properties.PString;
import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.base.Viewer;
import perspectives.three_d.Vector3D;

/**
 *
 * @author rajin
 */
public class BrainStatsD3Viewer extends Viewer implements D3Renderer{
    
    private final int TOTAL_CATEGORIES = 10;    
    private BrainData brainData;
    private String lastTubeSelection;
    
    boolean isInitialCall = true;
    
    public BrainStatsD3Viewer(String name, BrainData d) {
        super(name);

        final BrainStatsD3Viewer thisf = this;
        this.brainData = d;
        this.lastTubeSelection = "";
        
        String alldata = "";
        for (int i=0; i<brainData.segments.length; i++)
        	if (alldata.length() == 0)
        		alldata += i;
        	else
        		alldata += "," + i;
        	

        Property<PString> p = new Property<PString>("Selected", new PString(alldata)) {
        	
        	@Override
                protected void receivedBroadcast(PString newvalue, PropertyManager origin) {        			
                    Property<PString> propertySelectedTube = getProperty("Selected");
                    propertySelectedTube.setValue((PString) newvalue);
                    requestRender();
                }
         };
         p.setPublic(true);
         p.setVisible(false);
         addProperty(p); 
    }
    
   
    @Override
    public JSONObject renderToData() {
    	
        JSONObject data = new JSONObject();
        
        //InitialCall
        data.put("IsInitialCall", isInitialCall);
        
        // Property Data
        JSONObject propertyData= new JSONObject();
        
        Property<PString> propertySelectedTube = getProperty("Selected");
        String selectedTubes = propertySelectedTube.getValue().stringValue();
        propertyData.put("Selected", selectedTubes);
        
        boolean dataUpdated = false;
        if(!selectedTubes.equals(this.lastTubeSelection) || isInitialCall)
        {
            dataUpdated = true;
            this.lastTubeSelection = selectedTubes;
        }
        
        data.put("PropertyData", propertyData);
        JSONArray dataArray =null;
        if(dataUpdated)
        {
            dataArray = this.getDataArray(selectedTubes);
            dataUpdated = false;
        }
        data.put("DataArray", dataArray);
        
        isInitialCall = false;
        
        return data;
    }
    
    private JSONArray getDataArray(String selectedTubes)
    {
        JSONArray dataArray = new JSONArray();
     
        String[] tubeIds = selectedTubes.split(",");
        if(tubeIds.length ==0)
            return dataArray;
     
        HashMap<String, Double> tubeLengthList = new HashMap<String, Double>();
        double minLength = Integer.MAX_VALUE;
        double maxLength = Integer.MIN_VALUE;
        
        for(String tubeIdString : tubeIds)
        {
            int tubeId = Integer.parseInt(tubeIdString);
            
            double tubeLength = brainData.getTubeLength(tubeId);
            tubeLengthList.put(tubeIdString, tubeLength);
            
            if(tubeLength < minLength)
                minLength = tubeLength;            
            if(tubeLength > maxLength)
                maxLength = tubeLength;
        }
        
        double range = maxLength - minLength+.01;
        double categoryStep = range / TOTAL_CATEGORIES;
        
        String[] cateoryTubeIndex = new String[TOTAL_CATEGORIES];
        
        int[] categorySegmentCount = new int[TOTAL_CATEGORIES];
       
        for(Entry<String,Double> e : tubeLengthList.entrySet())
        {
            String tubeId = e.getKey();
            Double tubeLength = e.getValue();
            int categoryIndex = (int) Math.floor((tubeLength-minLength)/categoryStep);
            categorySegmentCount[categoryIndex]++;
            
            if(cateoryTubeIndex[categoryIndex]== null || cateoryTubeIndex[categoryIndex].isEmpty())
            {
               cateoryTubeIndex[categoryIndex] = tubeId;
            }
            else
            {
                String categoryTubeId = cateoryTubeIndex[categoryIndex];
                categoryTubeId+= ","+tubeId;
                cateoryTubeIndex[categoryIndex] = categoryTubeId;
            }
        }       
        
        for(int i=0;i< TOTAL_CATEGORIES;i++)
        {
            double min = minLength+i*categoryStep;
            double max = minLength+(i+1)*categoryStep -0.01;
            String key = String.format("%.2f-%.2f", min, max);
            int value = categorySegmentCount[i];
            JSONObject obj = new JSONObject();
            obj.put("Index",i);
            obj.put("Key", key);
            obj.put("Value", value);
            obj.put("TubeIds", cateoryTubeIndex[i]);
            
            dataArray.add(obj);
        }
        return dataArray;
    }

}
