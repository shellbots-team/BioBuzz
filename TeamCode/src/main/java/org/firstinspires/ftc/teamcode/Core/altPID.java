package org.firstinspires.ftc.teamcode.Core;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@Configurable
public class altPID {
    /// A PID controller maintains the speed or position of a motor, allowing for consistent movements
    /// EzPID simplifies custom PID controllers and cleans up usage
    /// To use an ezPID, just make an instance in the script using it and pass in the PIDF values
    /// kneecap values, encoder resolution, and a speed or position mode into the constructor.
    /// Then, just call run inside of another script's loop, using the instance you made there
    /// If you want to use Panels or another dashboard, be sure to call changeBehaviorValues to get live values
    ///
    /// This script was written by Clay Kramer, FTC 13206 🌊🤖, 2025-2026

    private Robot robot;
    private DcMotorEx motor;

    /// SET THESE IN INSTANCES, NOT HERE
    public double p;

    public double i;
    public double d;
    public double f;

    public PIDFController motorConroller;
    public SimpleMotorFeedforward ffcontrol;

    public double ticksPerRotation;
    public double tolerance;
    public boolean withinTolerance;
    ///
    /**
     * This is the constructor for a regular motor PID.
     * Pass in your values and stuff in your script as so
     * private ezPID singleMotorPID(motor, encoderResolutionValue, p, i, d, f, kneecap, toleranceInTicks, motorMode)
     *
     * @param ticksPerRotationIN The number of ticks in a full rotation
     * @param inP                The P value
     * @param inI                The I value
     * @param inD                The D value
     * @param inF                The F valueSPEED mode or POSITION mode.
     */
    public altPID(DcMotorEx motorIN, double ticksPerRotationIN, double inP, double inI, double inD, double inF, double toleranceIN) {
        motor = motorIN;
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorConroller = new PIDFController(inP, inI, inD, inF);
        motorConroller.setTolerance(toleranceIN);
        ffcontrol = new SimpleMotorFeedforward(0, 0);
        ticksPerRotation = ticksPerRotationIN;
        tolerance = toleranceIN;
        withinTolerance = false;
    }

    public double recalculateFF(int ticksToMove, double ticksPerSecond)
    {
       return  ffcontrol.calculate(ticksToMove, ticksPerSecond);
    }

    public void runPID(double targetPosition, boolean breaks)
    {
        if(breaks)
        {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        else
        {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        double output;
        motorConroller.setSetPoint(targetPosition);

        if (!motorConroller.atSetPoint())
        {
            output = motorConroller.calculate(motor.getCurrentPosition());
            motor.setVelocity(output / 0.00390625); //Hardcoded value translates encoder resolutions

        }
        motor.setVelocity(0); // stop the motor

    }

    public void changeBehaviorValues(double inP, double inI, double inD, double target)
    {
        p = inP;
        i = inI;
        d = inD;
        //f = inF;
        motorConroller.setPIDF(p, i, d, recalculateFF(491520, 0.5*491520));
        this.tolerance = tolerance;
        motorConroller.setTolerance(tolerance);
    }

}











