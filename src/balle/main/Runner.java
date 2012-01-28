package balle.main;

/**
 * This is where the main executable code for BALL-E lies. It is responsible of
 * initialising various subsystems of the code and make sure they interact.
 * 
 * 
 * @author s0909773
 * 
 */
public class Runner {

    private static void print_usage() {
        System.out.println("Usage: java balle.main.Runner <balle_colour>");
        System.out
                .println("Where <balle_colour> is either \"blue\" or \"yellow\"");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Check the usage
        if (args.length != 1) {
            print_usage();
            System.exit(-1);
        }

        // Get the colour
        boolean balleIsBlue;
        if (args[0].equals("blue"))
            balleIsBlue = true;
        else if (args[0].equals("yellow"))
            balleIsBlue = false;
        else {
            System.out.println("Invalid colour provided");
            print_usage();
            System.exit(-1);
        }

    }
}
