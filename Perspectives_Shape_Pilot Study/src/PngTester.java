
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import perspectives.base.*;
import perspectives.properties.PFileInput;
import perspectives.two_d.JavaAwtRenderer;
import perspectives.util.PngEncoder;

public class PngTester extends Viewer implements JavaAwtRenderer{

	public PngTester(String name) {
		super(name);
		
	
						
						try{
						BufferedImage bim = ImageIO.read(new File("C:\\pngtest.jpg"));
						BufferedImage image = new BufferedImage(bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_ARGB);
						image.createGraphics().drawImage(bim, 0,0,null);
				
						PngEncoder p = new PngEncoder(image, true);
						p.setCompressionLevel(8);
						byte[] bs = p.pngEncode(true);
						
						BufferedImage other = ImageIO.read(new ByteArrayInputStream(bs));
						
						FileOutputStream fos = new FileOutputStream("c:\\testpng.png");
						fos.write(bs);
						fos.close();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}						
						
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mousepressed(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousereleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousemoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void keyPressed(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(String key, String modifiers) {
		// TODO Auto-generated method stub
		
	}

}
