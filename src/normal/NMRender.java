package normal;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class NMRender {

	ArrayList<Light> lights;
	
	public NMRender() {
		lights = new ArrayList<Light>();
	}
	
	
	public void addLight(Light light) {
		lights.add(light);
	}


	public void drawImage(BufferedImage main, BufferedImage img, BufferedImage normal, int px, int py, int width, int height) {
		WritableRaster src = img.getRaster();
		WritableRaster nrm = normal.getRaster();
		WritableRaster dstOut = main.getRaster();
		
    	float alpha = 1;

    	int[] result = new int[4];
    	int[] srcPixel = new int[4];
    	int[] srcPixels = new int[width];
    	int[] nrmPixel = new int[4];
    	int[] nrmPixels = new int[width];

    	for (int y = 0; y < height; y++) {
//    		System.out.println(src);
//    		System.out.println(dstOut);
    		src.getDataElements(0, y, width, 1, srcPixels);
    		nrm.getDataElements(0, y, width, 1, nrmPixels);
    		for (int x = 0; x < width; x++) {
    			int pixel = srcPixels[x];
    			srcPixel[0] = (pixel >> 16) & 0xFF;
    			srcPixel[1] = (pixel >>  8) & 0xFF;
    			srcPixel[2] = (pixel      ) & 0xFF;
    			srcPixel[3] = (pixel >> 24) & 0xFF;
    			
    			pixel = nrmPixels[x];
    			nrmPixel[0] = (pixel >> 16) & 0xFF;
    			nrmPixel[1] = (pixel >>  8) & 0xFF;
    			nrmPixel[2] = (pixel      ) & 0xFF;
    			nrmPixel[3] = (pixel >> 24) & 0xFF;
    			
    			blend(x, y, srcPixel, nrmPixel, result);

    			srcPixels[x] = ((int) (result[3] * alpha) & 0xFF) << 24 |
    					((int) (result[0] * alpha) & 0xFF) << 16 |
    					((int) (result[1] * alpha) & 0xFF) <<  8 |
    					(int) (result[2] * alpha) & 0xFF;
    		}
    		dstOut.setDataElements(0, y, width, 1, srcPixels);
    	}
	}
	
	public static final int CENTER = 125;
	
	private double toDouble(int i) {
		return (i-CENTER)/(CENTER*2d);
//		return (i)/(CENTER*2d);
	}
	
	private double getDistance(double angleK) {
		return Math.sqrt(Math.cos((angleK-0.5)*Math.PI));
	}
	
	 public void blend(int x, int y, int[] src, int[] nrm, int[] result) {
		 double r = toDouble(nrm[0]), g = toDouble(nrm[1]), b = nrm[2]/255d -0.5;

		 
		 for (int i = 0; i < lights.size(); i++) {
			 float darkR = 1;
			 float darkG = 1;
			 float darkB = 1;
			 
			 Light l = lights.get(i);
			 double k = 1;
			 
			 double dir = Math.atan2((y-l.getY()), -(x-l.getX()));

			 double distance = l.getPower()/Math.hypot(x-l.getX(), y-l.getY());
			 
//			 k *= Math.abs(r+Math.cos(dir))/2d + Math.abs(g+Math.sin(dir))/2d;
			 k *= (b+0.5)/2d + Math.sqrt(Math.pow((r/1d+Math.cos(dir))/2d, 2) + Math.pow((g/1d+Math.sin(dir))/2d, 2));

			 /*
			  * 
			 Light l = lights.get(i);
			 double k = 1;
			 

			 double dir = Math.atan2((y-l.getY()), (x-l.getX()));
			 
			 
//			 k *= blueDistance/(distance);

			 k *= Math.abs(r+Math.cos(dir))/2d + Math.abs(g+Math.sin(dir))/2d;
			  */
			 
			 //getDistance(r)/(distance);
//			 k *= getDistance(g)/(distance);
//			 k *= getDistance(b)/(distance);
//			 double dir1 = Math.atan2((y-l.getY()), (x-l.getX()));
//			 double dir2 = Math.atan2(g, r);
			 
			 /*
			 
			 double dir1 = Math.atan2(y-l.getY(), x-l.getX());
			 double dir2 = Math.atan2(g, r)+Math.PI/2d;

			 double dirZ = Math.atan(b+0.5);
			 
			 if(x == 0 && y == 0) {
				 System.out.println(
						 "RGB: " + src[0] + " " + src[1] + " " + src[2]
						 + " Double: " + r + " " + g 
						 + " Degrees: " + Math.toDegrees(dir2) + " " + Math.toDegrees(dir1)
						 + " Radians: " + dir2 + " " + dir1
						 );
			 }
			 k*= dirZ/(2d*Math.PI) + 0.5;
//			 dark *= (1-(b+0.5))/2d + 0.5;
			 
			 k*= (Math.max(dir1, dir2)-Math.min(dir1, dir2))/(1d*Math.PI);
			 */

			 if(darkR > 1) darkR = 1;
			 if(darkR < 0) darkR = 0;
			 
			 if(darkG > 1) darkG = 1;
			 if(darkG < 0) darkG = 0;
			 
			 if(darkB > 1) darkB = 1;
			 if(darkB < 0) darkB = 0;

			 darkR *= k * l.getR()/255d*distance;// -1d;
			 darkG *= k * l.getG()/255d*distance;// -1d;
			 darkB *= k * l.getB()/255d*distance;// -1d;

			 src[0] *= 0.5d + darkR/2d;
			 src[1] *= 0.5d + darkG/2d;
			 src[2] *= 0.5d + darkB/2d;
//			 dark *= (Math.PI - 2d*(Math.max(dir1, dir2)-Math.min(dir1, dir2)))/Math.PI;
//			 dark *= Math.atan2(Math.sin(dir1)*Math.sin(dir2),Math.cos(dir1)*Math.cos(dir2));//2d;//(int) (r/255d*Math.cos(dir));
		 }
		 
//		 int dark = src[0]

//		 darkR -= 0.5d;
//		 darkG -= 0.5d;
//		 darkB -= 0.5d;
		 
         result[0] = (int) Math.min(255, src[0]);//*darkR);
         result[1] = (int) Math.min(255, src[1]);//*darkG);
         result[2] = (int) Math.min(255, src[2]);//*darkB);
         result[3] = src[3];
//         
//         result[0] = src[0];
//         result[1] = src[1];
//         result[2] = src[2];
//         result[3] = src[3];
     }
}
