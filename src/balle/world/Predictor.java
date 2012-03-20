package balle.world;

public class Predictor {
    /*
     * This code is from here:
     * http://www.particle.kth.se/~lindsey/JavaCourse/Book
     * /Part1/Physics/Chapter08/lsqFits.html
     */
    public void fitLine(double[] parameters, double[] x, double[] y,
            double[] sigma_x,
            double[] sigma_y, int num_points) {

        double s = 0.0, sx = 0.0, sy = 0.0, sxx = 0.0, sxy = 0.0, del;

        // Null sigma_y implies a constant error which drops
        // out of the divisions of the sums.
        if (sigma_y != null) {
            for (int i = 0; i < num_points; i++) {

                s += 1.0 / (sigma_y[i] * sigma_y[i]);
                sx += x[i] / (sigma_y[i] * sigma_y[i]);
                sy += y[i] / (sigma_y[i] * sigma_y[i]);
                sxx += (x[i] * x[i]) / (sigma_y[i] * sigma_y[i]);
                sxy += (x[i] * y[i]) / (sigma_y[i] * sigma_y[i]);
            }
        } else {
            s = x.length;
            for (int i = 0; i < num_points; i++) {
                sx += x[i];
                sy += y[i];
                sxx += x[i] * x[i];
                sxy += x[i] * y[i];
            }
        }

        del = s * sxx - sx * sx;

        // Intercept
        parameters[0] = (sxx * sy - sx * sxy) / del;
        // Slope
        parameters[1] = (s * sxy - sx * sy) / del;

        // Errors (sd**2) on the:
        // intercept
        parameters[2] = sxx / del;
        // and slope
        parameters[3] = s / del;
    }
}
