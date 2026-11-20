package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.BACKWARD;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.FORWARD;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.LEFT;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.RIGHT;
import static org.firstinspires.ftc.teamcode.Core.Robot.MoveDirection.TURN_LEFT;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(group = "Core", name = "ShellbotFlowerAuto")
public class ShellbotsFlowerAuto extends LinearOpMode {
    private Robot robot;
    private AutonomousPLUS auto;

    public static final String ALLIANCE_KEY = "Alliance";
    public static final String PATTERN_KEY = "Pattern";

    private boolean launching = false;
    private double launchStartTime = 0;


    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);
        auto = new AutonomousPLUS(robot);

        while (opModeInInit()) {
            // This is the equivalent of init_loop(). It will repeat until the play button is
            // pressed.
            sleep(1);
        }


        robot.readyHardware();
        blackboard.put(ALLIANCE_KEY, "RED");

        waitForStart();

        auto.speed = 0.5;

        //The code to run once play has been pressed goes here.
        auto.moveAndStall(FORWARD, 150, 0);
        launching = true;
        robot.launcher.switchStateSwitcher();
        waitWhileLaunching();
        robot.launcher.stopLaunch();
        auto.moveAndStall(FORWARD, 750, 0);
        auto.moveAndStall(LEFT, 1000, 0);
        auto.moveAndStall(FORWARD, 1800, 0);
        auto.moveAndStall(RIGHT, 1200, 0);
        robot.intake.flowerBarDown();
        auto.moveAndStall(FORWARD, 100, 0);
        auto.moveAndStall(BACKWARD, 100, 0);
        auto.moveAndStall(TURN_LEFT, 100, 0);
        launching = true;
        robot.launcher.switchStateSwitcher();
        waitWhileLaunching();
        robot.launcher.stopLaunch();
        auto.moveAndStall(LEFT, 900, 0);
    }

    private void waitWhileLaunching() {
        launchStartTime = getRuntime();
        while (launching) {
            robot.launcher.update();
            if (getRuntime() - launchStartTime >= 7) {
                launching = false;
            }
            sleep(1);
        }
    }
}

