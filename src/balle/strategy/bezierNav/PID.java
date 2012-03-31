package balle.strategy.bezierNav;

import java.util.LinkedList;


/**
 * P. I. D. controller, smoothes controls to reduce severity of overshoots.
 * 
 * Links: http://www.societyofrobots.com/programming_PID.shtml
 */
public class PID {

	/**
	 * Length of history to use.
	 * 
	 */
	protected int HISTORY = 10;

	/**
	 * P - Proportional, I - Integral, D - Derivative,
	 */
	protected double kp, ki, kd;

	/**
	 * History of the PID, for modifying values.
	 */
	protected LinkedList<Tuple> history;

	/**
	 * Current number of examples.
	 */
	protected long historyLength = 0;

	/**
	 * Constructor.
	 * 
	 * @param p
	 *            Proportional weighting,
	 * @param i
	 *            Integral weighting,
	 * @param d
	 *            Derivative weighting,
	 */
	public PID(int history, double kp, double ki, double kd) {
		this.HISTORY = history;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;

		this.history = new LinkedList<Tuple>();
		for (int i = 0; i < HISTORY; i++) {
			this.history.add(new Tuple(Double.NaN, Double.NaN));
		}
	}

	class Tuple {
		private double a, b;

		Tuple(double a, double y) {
			this.a = a;
			this.b = y;
		}

		public double a() {
			return a;
		}

		public double b() {
			return b;
		}

		public boolean isNaN() {
			return Double.isNaN(a) || Double.isNaN(b);
		}
	}

	public double convert(double SP, double PV) {
		double p, i, d;

		// Calculate Potential
		p = SP - PV;
		
		// Calculate Integral
		i = 0;
		for (Tuple each : history) {
			double x = each.a() - each.b();
			if (!Double.isNaN(x))
				i += x;
		}
		
		// Calculate Differential
		d = SP - history.getLast().b();
		System.out.println(d);


		double output = PV;
		if (kp != 0)
			output += kp * p;
		if (ki != 0)
			output += ki * i;
		if (kd != 0 && !Double.isNaN(kd))
			output += kd * d;

		history.add(new Tuple(SP, PV));
		history.pollFirst();

		// String x = "";
		// for (Tuple t : history)
		// if (t.isNaN()) {
		// x += "X";
		// } else {
		// x += "?";
		// }

		return output;
	}

}
