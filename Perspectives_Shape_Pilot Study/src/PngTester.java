
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
import perspectives.two_d.Viewer2D;
import perspectives.util.PngEncoder;

public class PngTester extends Viewer2D {

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

}
