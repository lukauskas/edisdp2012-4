package balle.misc;

import java.util.Arrays;
import java.util.Comparator;

public class VtoPData {

	public static Powers[] getData() {
		Powers[] powers = new Powers[] {
				

				// these are for the crazy fast gear ratios {71302349 start
				// new Powers(0, 0), new Powers(75, 2.88E-003f),
				// new Powers(100, 3.61E-002f), new Powers(500, 7.92E-001f),
				// new Powers(900, 8.25E-001f),
		// //end 71302349}
				
		// for 1:1 gear ratio {893457 start
//				new Powers(0, 0),
//				new Powers(600, 4.39E-001f),
//				new Powers(900, 4.81E-001f),
		// end 893457}

				// the medium gears
				new Powers(0, 0f), new Powers(25, 2.382833804979511E-2f),
				new Powers(50, 5.0044476132977016E-2f),
				new Powers(75, 9.554520727123252E-2f),
				new Powers(100, 1.278530767154936E-1f),
				new Powers(125, 1.586674905380479E-1f),
				new Powers(150, 1.9634270609614763E-1f),
				new Powers(175, 2.2990605276723892E-1f),
				new Powers(200, 2.66043469458304E-1f),
				new Powers(225, 2.950133890016405E-1f),
				new Powers(250, 3.2607728065454217E-1f),
				new Powers(275, 3.551003915921365E-1f),
				new Powers(300, 3.9227521023990283E-1f),
				new Powers(350, 4.4983811556482934E-1f),
				new Powers(375, 4.8291953177803865E-1f),
				new Powers(400, 5.157067980296969E-1f),
				new Powers(425, 5.423887868439868E-1f),
				new Powers(450, 5.699713737107192E-1f),
				new Powers(475, 5.960800691707655E-1f),
				new Powers(500, 6.207346751710824E-1f),
				new Powers(525, 6.367033745526317E-1f),
				new Powers(550, 6.512724462555654E-1f),
				new Powers(575, 6.649185545637489E-1f),
				new Powers(600, 6.725554461847763E-1f),
				new Powers(625, 6.825919368093553E-1f),
				new Powers(650, 6.87133308015843E-1f),
				new Powers(675, 6.949623218007021E-1f),
				new Powers(700, 7.005919249978846E-1f),

				// new Powers(725, 7.045040363665344E-1f),
				// new Powers(750, 7.031940619511138E-1f),
				// new Powers(775, 7.080022415823777E-1f),

				new Powers(800, 7.030352575295477E-1f),

				// new Powers(825, 7.071186789416736E-1f),
				// new Powers(850, 7.069356054893757E-1f),
				// new Powers(875, 7.027000719049154E-1f),

				new Powers(900, 7.035865910478905E-1f)
		// end

		// new Powers(25, 1.75E-002f), new Powers(50, 4.05E-002f),
		// new Powers(75, 5.83E-002f), new Powers(100, 8.14E-002f),
		// new Powers(125, 9.79E-002f), new Powers(150, 1.19E-001f),
		// new Powers(175, 1.37E-001f), new Powers(200, 1.56E-001f),
		// new Powers(225, 1.71E-001f), new Powers(250, 1.90E-001f),
		// new Powers(275, 2.07E-001f), new Powers(300, 2.29E-001f),
		// new Powers(325, 2.43E-001f), new Powers(350, 2.66E-001f),
		// new Powers(375, 2.81E-001f), new Powers(400, 3.00E-001f),
		// new Powers(425, 3.23E-001f), new Powers(450, 3.40E-001f),
		// new Powers(475, 3.59E-001f), new Powers(500, 3.77E-001f),
		// new Powers(525, 3.90E-001f), new Powers(550, 4.13E-001f),
		// new Powers(575, 4.33E-001f), new Powers(600, 4.39E-001f),
		// new Powers(625, 4.45E-001f), new Powers(650, 4.54E-001f),
		// new Powers(675, 4.70E-001f), new Powers(700, 4.68E-001f),
		// new Powers(725, 4.79E-001f), new Powers(750, 4.82E-001f),
		// new Powers(775, 4.83E-001f), new Powers(800, 4.88E-001f),
		// new Powers(825, 4.81E-001f), new Powers(850, 4.77E-001f),
		// new Powers(875, 4.78E-001f), new Powers(900, 4.81E-001f),
		};
		Arrays.sort(powers, new Comparator<Powers>() {

			@Override
			public int compare(Powers o1, Powers o2) {
				return o1.getPower() - o2.getPower();
			}

		});

		return powers;
	}

}
