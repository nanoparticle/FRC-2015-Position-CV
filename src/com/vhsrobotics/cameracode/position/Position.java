package com.vhsrobotics.cameracode.position;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Position {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2085.local");
	}
	
	static NetworkTable table = NetworkTable.getTable("cameracode");

	static VideoCapture cap = new VideoCapture();
	static Mat mat = new Mat(new Size(160, 120), CvType.CV_8UC3);
	static Mat gray = new Mat(new Size(160, 120), CvType.CV_8UC1);
	static Mat old = new Mat(new Size(160, 120), CvType.CV_8UC1);
	static Mat flow = new Mat(new Size(160, 120), CvType.CV_8UC2);
	static ArrayList<Mat> channels = new ArrayList<Mat>();
	static Mat magnitude = new Mat(new Size(160, 120), CvType.CV_8UC1);
	static Mat angle = new Mat(new Size(160, 120), CvType.CV_8UC1);
	static ArrayList<Mat> channels2 = new ArrayList<Mat>();
	static Mat viz = new Mat(new Size(160, 120), CvType.CV_8UC3);
	static Mat viz2 = new Mat(new Size(160, 120), CvType.CV_8UC3);
	static Imshow window = new Imshow("camera");
	static double x = 0;
	static double y = 0;

	public static void main(String[] args) {
		cap.open("http://10.20.85.12/mjpg/video.mjpg");
		if (cap.isOpened()) {
			System.out.println("Connected to Camera");
			flushCamera();
			
			cap.read(mat);
			Imgproc.cvtColor(mat, old, Imgproc.COLOR_BGR2GRAY);
			while (true) {
				channels.clear();
				channels2.clear();
				
				cap.read(mat);
				
				Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
				
				//Video.calcOpticalFlowFarneback(old, gray, flow, 0.5, 1, 20, 1, 7, 3, Video.OPTFLOW_FARNEBACK_GAUSSIAN);
				Video.calcOpticalFlowFarneback(old, gray, flow, 0.5, 2, 10, 2, 7, 3, Video.OPTFLOW_FARNEBACK_GAUSSIAN);

				Core.split(flow, channels);
				Core.cartToPolar(channels.get(0), channels.get(1), magnitude, angle, true);
								
				x += Data.analyze(channels.get(0));
				y += Data.analyze(channels.get(1));
				
				System.out.println(" x: " + x + "   y: " + y);
				
				Core.multiply(magnitude.clone(), new Scalar(50), magnitude);
				Core.multiply(angle.clone(), new Scalar(0.5), angle);
				
				angle.convertTo(angle, CvType.CV_8UC1);
				magnitude.convertTo(magnitude, CvType.CV_8UC1);
				
				channels2.add(angle);
				channels2.add(new Mat(new Size(160, 120), CvType.CV_8UC1, new Scalar(255)));
				channels2.add(magnitude);
				
				Core.merge(channels2, viz);
				
				Imgproc.cvtColor(viz, viz, Imgproc.COLOR_HSV2BGR);
				
				window.showImage(viz);
				
				old = gray.clone();
			}
		} else {
			System.out.println("Connection Failed.");
		}
		
	}
	
	public static void flushCamera () {
		for (int i = 0; i < 150; i++) {
			cap.grab();
		}
	}
}