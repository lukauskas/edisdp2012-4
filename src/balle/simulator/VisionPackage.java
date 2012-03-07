package balle.simulator;

public class VisionPackage {

	float yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX, ballPosY;
	long timestamp;

	public VisionPackage(float yPosX, float yPosY, float yRad, float bPosX,
			float bPosY, float bRad, float ballPosX, float ballPosY,
			long timestamp) {
		super();
		this.yPosX = yPosX;
		this.yPosY = yPosY;
		this.yRad = yRad;
		this.bPosX = bPosX;
		this.bPosY = bPosY;
		this.bRad = bRad;
		this.ballPosX = ballPosX;
		this.ballPosY = ballPosY;
		this.timestamp = timestamp;
	}

	public float getYPosX() {
		return yPosX;
	}

	public float getYPosY() {
		return yPosY;
	}

	public float getYRad() {
		return yRad;
	}

	public float getBPosX() {
		return bPosX;
	}

	public float getBPosY() {
		return bPosY;
	}

	public float getBRad() {
		return bRad;
	}

	public float getBallPosX() {
		return ballPosX;
	}

	public float getBallPosY() {
		return ballPosY;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
