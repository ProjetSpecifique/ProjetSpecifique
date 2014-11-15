package ImageCompV;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ChainCode {

	static Vector<Integer> checkx = new Vector<Integer>();
	static Vector<Integer> checky = new Vector<Integer>();
	static Vector<Integer> direct = new Vector<Integer>();

	public static void main(String[] args) throws IOException {
		BufferedImage image = null;

		File imagefile = new File("src/bestpic.jpg");
		image = ImageIO.read(imagefile);
		
		ImagePlus imagePlus = new ImagePlus("First Image", image);
		ImageProcessor ip = imagePlus.getProcessor();
		//imagePlus.setRoi(602, 4, 603, 5);
		imagePlus.show();
		

		int w = image.getWidth();
		int h = image.getHeight();

		int x, y;
		x = y = 0;

		boolean flag = false;
		for (int k = 0; k < h; k++) {
			for (int j = 0; j < w; j++) {
				int rgbValue = image.getRGB(j, k) + 0xFFFFFF + 1;

				if (rgbValue == 0) {
					x = j;
					y = k;

					flag = true;

					break;
				}
			}

			if (flag)
				break;
		}

		System.out.println("Success");
		System.out.println( x+" " + y);

		int[] lx = new int[8];
		int[] ly = new int[8];
		lx[0] = 0;
		lx[1] = 1;
		lx[2] = 1;
		lx[3] = 1;
		lx[4] = 0;
		lx[5] = -1;
		lx[6] = -1;
		lx[7] = -1;
		ly[0] = -1;
		ly[1] = -1;
		ly[2] = 0;
		ly[3] = 1;
		ly[4] = 1;
		ly[5] = 1;
		ly[6] = 0;
		ly[7] = -1;
		int l = 0;
		int a = 0;
		int b = 0;
		int beforex = 0;
		int beforey = 0;

		boolean flagz = false;
		for (int j = 0; j < 500; j++) {
			if (flagz)
				break;
			for (int i = 0; i < 8; i++) {
				if (check(x + lx[i], y + ly[i])
						&& !((x + lx[i] == beforex) && (y + ly[i] == beforey)))
				// if(!((x + lx[i] == beforex) && (y + ly[i] == beforey)))
				{
					if ((image.getRGB(x + lx[i], y + ly[i]) == -16777216)) {
						l = i;

						a = x + lx[i];
						b = y + ly[i];

						checkx.add(x);
						checky.add(y);
						ip.drawLine(x, y, x+1, y+1);
						
						beforex = x;
						beforey = y;

						x = a;
						y = b;

						break;
					}
					checkx.add(x);
					checky.add(y);
				}

				if (i == 7)
					flagz = true;
			}

			System.out.println(x + " " + y + " = " + l);
			direct.add(l);

		}

		Integer[] chain = new Integer[direct.size()];
		direct.copyInto(chain);
		for (int i = 0; i < chain.length; i++) {
			System.out.println("chaincode = " + chain[i]);
		}

		int count = 0;

		for (int i = 0; (i + 2) < chain.length; i = i + 2) // direction in array
		{
			for (int j = 0; j < 8; j++) // direction 0 to 7
			{
				if (chain[i] == j) // if 2nd pixel = i
				{
					if (chain[i + 1] == j + 1 || chain[i + 1] == j - 1) // if
																		// (3rd
																		// pixel
																		// =
																		// i+1)
																		// ||
																		// (3rd
																		// pixel
																		// =
																		// i-1)
					{
						count++;
					}
				}
			}
		}
		System.out.println("total input =" + count);
	
		

	}

	public static boolean check(int x, int y) {

		Integer[] xed = new Integer[checkx.size()];
		Integer[] yed = new Integer[checky.size()];
		checkx.copyInto(xed);
		checky.copyInto(yed);
		for (int i = 0; i < xed.length; i++) {
			// System.out.println(x + " . " + xed[i] + " . " + y + " . " +
			 //yed[i]);
			if ((x == xed[i]) && (y == yed[i])) {
				// System.out.println(x + " . " + xed[i] + " . " + y + " . " +
				// yed[i]);
				return false;
			}
		}
		return true;

	}

}
