package balle.strategy.purePF;

import balle.world.Coord;
import balle.world.Orientation;

public abstract class PFObject {

	private Coord myP;
	private Orientation myO;

	public PFObject(Coord pos, Orientation orient) {
		myP = pos;
		myO = orient;
	}

	public PFObject(Coord pos) {
		myP = pos;
		myO = new Orientation(0, true);
	}

	public PFObject(Orientation orient) {
		myP = new Coord(0, 0);
		myO = orient;
	}

	public PFObject() {
		myP = new Coord(0, 0);
		myO = new Orientation(0, true);
	}

	protected Coord getPosition() {
		return myP;
	}

	protected void setPosition(Coord myP) {
		this.myP = myP;
	}

	protected Orientation getOrientation() {
		return myO;
	}

	protected void setOrientation(Orientation myO) {
		this.myO = myO;
	}

	/**
	 * given the absolute position of an object, find the force vector that this
	 * field would apply to it
	 * 
	 * @param pos
	 * @return
	 */
	public Coord getForce(Coord pos) {
		Coord posR = pos.sub(myP);
		posR = posR.rotate(myO.getOpposite());
		Coord relForce = relativePosToForce(posR);
		relForce = relForce.rotate(myO);
		return relForce;
	}

	/**
	 * get relative force for an object at pos and with orientation orient
	 * 
	 * @param pos
	 * @return
	 */
	public Coord getRelativeForce(Coord pos, Orientation orient) {
		Coord absForce = getForce(pos);
		return absForce.rotate(orient.getOpposite());
	}

	/**
	 * get the force that should be applied (assuming attraction) at a given
	 * position (assume that the orientation of this object is 0 (facing right))
	 * and the position given is relative this object
	 * 
	 * @param pos
	 *            position of the objet the force will be applied to relative to
	 *            this object (with orientation facing right) Hence if pos =
	 *            (1,1) then the object is to the right and upwards to this
	 *            object
	 */
	protected abstract Coord relativePosToForce(Coord pos);

}
