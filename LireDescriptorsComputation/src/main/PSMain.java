package main;

import java.io.IOException;

import descripteurs.AutoColorCorrelogramDescriptor;
import descripteurs.CEDD;

/**
 * Super Main
 * @author AdeN
 *
 */
public class PSMain {
	public static void main(String[] args) throws IOException {
		
//		AutoColorCorrelogramDescriptor.applyIt();
		AutoColorCorrelogramDescriptor.go2();
		
		
//		String workingDir = System.getProperty("user.dir");
//	    System.out.println("Current working directory : " + workingDir);
//		String[] fakeArgs = {workingDir + "/data/ferrari/black"};
//		indexElements(fakeArgs);
//		String[] colors = {
//				"black.jpg",
//				"red.jpg",
//				"white.jpg",
//				"yellow.jpg",
//				"notferrari.jpg"
//		};
//		for (String s : colors) {
//			System.out.println("=========" + s + "==========");
//			fakeArgs[0] = workingDir + "/data/ferrari/tests/" + s;
//	        CEDD.searchImage(fakeArgs);
//		}
    }
}
