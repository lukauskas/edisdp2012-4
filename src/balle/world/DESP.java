package balle.world;

/**
 * Double exponential smoothing-based prediction
 *
 * Joseph J. LaViola. 2003. Double exponential smoothing: an
 * alternative to Kalman filter-based predictive tracking. In
 * Proceedings of the workshop on Virtual environments 2003 (EGVE
 * '03). ACM, New York, NY, USA, 199-206. DOI=10.1145/769953.769976
 * http://doi.acm.org/10.1145/769953.769976#
 * 
 * @author s0913664
 *
 */
public class DESP {
	
	private final double alpha;
	private Coord s1;
	private Coord s2;

	public DESP(double alpha) {
		this.alpha = alpha;
		s1 = new Coord(0, 0);
		s2 = new Coord(0, 0);
	}

	public void update(Coord vector) {
		s1 = vector.mult(alpha).add(s1.mult(1 - alpha));
		s2 = s1.mult(alpha).add(s2.mult(1 - alpha));
	}

	public Coord predict(int time) {
		double a = 2 + alpha * time / (1 - alpha);
		double b = 1 + alpha * time / (1 - alpha);
		return s1.mult(a).sub(s2.mult(b));
	}

	public void reset() {
		s1 = new Coord(0, 0);
		s2 = new Coord(0, 0);
	}
}
