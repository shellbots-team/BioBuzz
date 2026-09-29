package org.firstinspires.ftc.teamcode.Core;

import com.bylazar.configurables.annotations.Configurable;

/**
 * This utility is intended to make creating a standard legacy teleop easier. Call the
 * findNextDistance() function at a point in the auto to stop the program and construct the next
 * movement using Panels. To access Panels, go to 192.168.43.1:8001 on the robot Wi-Fi.
 * <p>
 * The values you can change in Panels are:
 * </p>
 * - direction: The direction to move.
 * <p>
 * - distance: The distance to move, measured in motor ticks.
 * </p>
 * - done: When set to true, will cause the function to return. It will automatically set
 * itself back to false.
 * </p>
 *
 */
@Configurable
public class LegacyAutoTuner {

    /**
     * The direction to move in.
     */
    private static Robot.MoveDirection direction;

    /**
     * The distance, in ticks, to move.
     */
    public static int distance = 0;

    /**
     * Breaks the loop and returns the function. Will automatically set itself back to false.
     */
    public static boolean done = false;

    /**
     * Call this function at a point in the auto to stop the program and construct the next
     * movement using Panels. To access Panels, go to 192.168.43.1:8001 on the robot Wi-Fi.
     * <p>
     * The values you can change in Panels are:
     * </p>
     * - direction: The direction to move.
     * <p>
     * - distance: The distance to move, measured in motor ticks.
     * </p>
     * - done: When set to true, will cause the loop to end once the current movement is done. It will automatically set
     * itself back to false.
     * </p>
     * Changing these values will not hardcode them in
     * @param auto The AutonomousPLUS object the auto is using. Used to import things like the
     *             hardware map and the move() function.
     */
    public static void findNextDistance(AutonomousPLUS auto) {
        while (true) {
            if (direction != null) {
                auto.tentativeMove(direction, distance);
            }
            if (done && auto.checkMovement()) {
                done = false;
                return;
            }
        }
    }
}