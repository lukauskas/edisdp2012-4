package balle.brick;

public class PenaltyKick {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller control = new BrickController();
		
		control.penaltyKick();
		try {
			Thread.sleep(1000);
		} catch (Exception e){
			// 
		}
	}
}
