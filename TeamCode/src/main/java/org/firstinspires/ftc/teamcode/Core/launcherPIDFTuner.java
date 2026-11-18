package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */
@TeleOp(name = "launcherPIDFTuner", group = "Core")
public class launcherPIDFTuner extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;

    public double highVelocity = 2300;
    public double lowVelocity = 1000;
    private double currentTargetVelocity = highVelocity;

    private double F = 0;
    private double P = 0;

    private double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    private int stepIndex = 1;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {
        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);
        robot.launchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
        telemetry.update();
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 10);
        gamepad2.setLedColor(0, 0, 255, 10);

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        if (gamepad2.crossWasPressed()) {
            if (currentTargetVelocity == highVelocity) {
                currentTargetVelocity = lowVelocity;
            } else {
                currentTargetVelocity = highVelocity;
            }
        }

        if (gamepad2.circleWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad2.dpadLeftWasPressed()) {
            F-= stepSizes[stepIndex];
        }
        if (gamepad2.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad2.dpadDownWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad2.dpadUpWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        robot.launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        robot.launchMotor.setVelocity(currentTargetVelocity);

        double currentVelocity = robot.launchMotor.getVelocity();
        double error = currentTargetVelocity - currentVelocity;

        telemetry.addData("Target Velocity", currentTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f", currentTargetVelocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addLine("------------------------------");
        telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
        telemetry.addData("Tuning F", "%.4f (D-Pad L/R)", F);
        telemetry.addData("Step Size", "%.4f (Cross Button)", stepSizes[stepIndex]);
        }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


