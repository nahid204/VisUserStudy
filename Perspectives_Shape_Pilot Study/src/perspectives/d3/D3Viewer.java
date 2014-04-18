package perspectives.d3;
import java.awt.Graphics2D;
import org.json.simple.JSONObject;
import perspectives.two_d.Viewer2D;


 public abstract class D3Viewer extends Viewer2D{
    
     public D3Viewer(String name)
     {
         super(name);
     }
     abstract  public JSONObject updateData(boolean isInitialCall);
     @Override
    public void render(Graphics2D g) {
        
        
    }

    @Override
    public void simulate() {
        
    }
    
    public abstract String d3codefile();
}
