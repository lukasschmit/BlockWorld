package stranded.util;

public class Interpolator {

    public static final int STEP = 0;
    public static final int LINEAR = 1;
    public static final int COSINE = 2;
    public static final int DECELERATION = 3;

    public int interpolationMode;

    public Interpolator() {
        interpolationMode = LINEAR;
    }

    public Interpolator(int interpolationMode) {
        this.interpolationMode = interpolationMode;
    }

    public double interpolate(double p1, double p2, double offs) {
        if (interpolationMode == STEP) {
            return stepInterpolate(p1, p2, offs);
        } else if (interpolationMode == LINEAR) {
            return linearInterpolate(p1, p2, offs);
        } else if (interpolationMode == COSINE) {
            return cosineInterpolate(p1, p2, offs);
        }
        return decelerateInterpolate(p1, p2, offs);
    }

    private double stepInterpolate(double p1, double p2, double offs) {
        if (offs < 0.5) {
            return p1;
        }
        return p2;
    }

    private double linearInterpolate(double p1, double p2, double offs) {
        return p1 + offs * (p2 - p1);
    }

    private double decelerateInterpolate(double p1, double p2, double offs) {
        double decelOffs = 1.0 - Math.pow((1.0 - offs), 2);

        return linearInterpolate(p1, p2, decelOffs);
    }

    private double cosineInterpolate(double p1, double p2, double offs) {
        double cosOffs = -Math.cos(offs * Math.PI) / 2.0 + 0.5;

        return linearInterpolate(p1, p2, cosOffs);
    }
}
