package balle.world;

/**
 * Immutable 
 */
public class Snapshot {

	private FieldObject opponent;
	private FieldObject bot;
	private FieldObject ball;
	
	public Snapshot(FieldObject opponent, FieldObject balle, FieldObject ball) {
		super();
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
	}

	public FieldObject getOpponent() {
		return opponent;
	}

	public FieldObject getBalle() {
		return bot;
	}

	public FieldObject getBall() {
		return ball;
	}
	
}
