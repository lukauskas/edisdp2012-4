package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.ConfusedException;
import balle.strategy.executor.dribbling.DribbleStraight;
import balle.world.Coord;
import balle.world.Snapshot;

public class DribbleMilestone2 extends AbstractPlanner {

    private static final Logger LOG                = Logger.getLogger(DribbleMilestone2.class);

    DribbleStraight             executor;
    Coord                       startingCoordinate = null;
    private static final double DISTANCE_TO_TRAVEL = 0.6;                                      // in
                                                                                                // metres

    public DribbleMilestone2() {
        executor = new DribbleStraight();
    }

    @Override
    public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {

        if (startingCoordinate == null) {
            if (executor.isPossible(snapshot)) {
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
				executor.step(controller, snapshot);
            } else {
                LOG.info("Distance travelled. Finishing");
                executor.stop(controller);
            }
        }

    }
}
