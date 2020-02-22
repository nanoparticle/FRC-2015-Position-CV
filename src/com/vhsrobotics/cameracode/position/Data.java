package com.vhsrobotics.cameracode.position;
import java.util.*;

import org.opencv.core.Mat;

public class Data {

	static float[] data;
	static int[] pos;
	static int[] neg;
	static int[] analysis;
	
	public static int analyze (Mat input) {
		data = new float[(int) input.size().area()]; //potential range: -160 -> 160
		input.get(0, 0, data);
		
		analysis = new int[320];
//		pos = new int[160];
//		neg = new int[160];
		for (int i = 0; i < data.length; i++) {
			analysis[(int) (data[i]) + 160]++;
		}
		//Arrays.sort(data);
		//System.out.print(data[0] + "  " + data[data.length - 1] + "  ");
		//System.out.print(data[data.length - 1] + "  ");
		int value = 0;
		int key = 0;
		for (int i = 0; i < analysis.length; i++) {
			//System.out.print(analysis[i] + "  ");
			if (analysis[i] > value) {
				value = analysis[i];
				key = i;
			}
		}
		return (key - 160);
	}
	
}
