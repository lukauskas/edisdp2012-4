package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.dribbling.DribbleStraight;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.Snapshot;

public class DribbleMilestone2 extends AbstractPlanner {

    private static final Logger LOG                = Logger.getLogger(DribbleMilestone2.class);

    DribbleStraight             executor;
    Coord                       startingCoordinate = null;
    private static final double DISTANCE_TO_TRAVEL = 0.6;                                      // in
                                                                                                // metres

    public DribbleMilestone2(Controller controller, AbstractWorld world) {
        super(controller, world);
        executor = new DribbleStraight();
    }

    @Override
    protected void aiStep() {
        // do nothing

    }

    @Override
    protected void aiMove(Controller controller) {
        Snapshot snapshot = getSnapshot();
        executor.updateState(snapshot);

        if (startingCoordinate == null) {
            if (executor.isPossible()) {
                startingCoordinate = snapshot.getBalle().getPosition(); // Set
                                                                        // the
                                                                        // starting
                                                                        // position
                LOG.info("Started dribbling");
            } else {
                LOG.warn("Cannot dribble from here. Move ball closer");
                return;
            }
        } else {
            Coord currentCoordinate = snapshot.getBalle().getPosition();
            if (currentCoordinate == null)
                return;

            if (currentCoordinate.dist(startingCoordinate) < DISTANCE_TO_TRAVEL) {
                executor.step(controller);
            } else {
                LOG.info("Distance travelled. Finishing");
                executor.stop(controller);
            }
        }

    }
}
