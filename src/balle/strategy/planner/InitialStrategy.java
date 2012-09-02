package balle.strategy.planner;

import java.awt.Color;

import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.FieldObject;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class InitialStrategy extends GoToBall {

    public static final double SAFE_DISTANCE = Globals.PITCH_WIDTH / 4;

    public InitialStrategy() {
        super(new GoToObjectPFN(0));
    }
    
    @Override
    public FieldObject getTarget(Snapshot snapshot) {
        // If we cannot see ourselves, we're screwed
        Robot ourRobot = snapshot.getBalle();
        if (ourRobot.getPosition() == null)
            return null;
        
        Ball ball = snapshot.getBall();
        FieldObject target;
        
        
        
        if (ball.getPosition() != null)
            target = ball;
        else {
            // If we cannot see the target for some reason, assume it is in the
            // centre
            target = new Point(new Coord(Globals.PITCH_WIDTH / 2,
                    Globals.PITCH_HEIGHT / 2));
        }
        
        // If we're still far away, go slightly off
        boolean goFromRight = (target.getPosition()
                .dist(ourRobot.getPosition()) > SAFE_DISTANCE);

        if (ourRobot.isFacingLeft())
            goFromRight = !goFromRight;

        if (goFromRight)
			target = new Point(new Coord(Globals.PITCH_WIDTH / 2,
					0 - Globals.PITCH_HEIGHT));
        else
            target = new Point(new Coord(Globals.PITCH_WIDTH / 2,
					Globals.PITCH_HEIGHT * 2));
        
        // Extend the line so we're lightning fast
        Line lineToTarget = new Line(ourRobot.getPosition(), target.getPosition());
        lineToTarget = lineToTarget.extend(1);
        addDrawable(new DrawableLine(lineToTarget, Color.PINK));
        return new Point(lineToTarget.getB());
    }
    

}
