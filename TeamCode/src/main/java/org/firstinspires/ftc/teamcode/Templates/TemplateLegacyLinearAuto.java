package org.firstinspires.ftc.teamcode.Templates;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.AutonomousPLUS;
import org.firstinspires.ftc.teamcode.Core.Robot;

@Disabled // TODO remove this if you ever want to run the program
@Autonomous(group = "Templates", name = "Legacy Linear Autonomous")
public class TemplateLegacyLinearAuto extends LinearOpMode {
    private Robot robot;
    private AutonomousPLUS auto;

    public static final String ALLIANCE_KEY = "Alliance";
    public static final String PATTERN_KEY = "Pattern";

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

        //The code to run once play has been pressed goes here.


    }
}

