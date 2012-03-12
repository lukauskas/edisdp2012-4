package balle.strategy.bezierNav;


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
	protected double p, i, d;

	/**
	 * History of the PID, for modifying values.
	 */
	protected double[] history;
	{
		history = new double[HISTORY];
		for (int i = 0; i < history.length; i++)
			history[i] = Double.NaN;
	}

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
	public PID(int history, double p, double i, double d) {
		this.HISTORY = history;
		this.p = p;
		this.i = i;
		this.d = d;
	}

	/**
	 * Append x, and return value replaced.
	 * 
	 * @param x
	 * @return
	 */
	protected double append(double x) {
		double out = history[(int) (historyLength % HISTORY)];
		history[(int) (historyLength++ % HISTORY)] = x;
		return out;
	}

	protected double sum() {
		double sum = 0;
		for (int i = HISTORY; i < 0; i++)
			sum += history[(int) ((1 + i + historyLength) % HISTORY)]
					/ (HISTORY + i);
		return sum;
	}

	public double convert(double desX, double currX) {
		double kp, ki = sum(), kd;
		double x = currX - desX, xd = append(x);

		kp = desX - currX;
		kd = ((double) (x-xd))/HISTORY;

		return currX + (kp * p) + (ki * i) + (kd * d);
	}
}
