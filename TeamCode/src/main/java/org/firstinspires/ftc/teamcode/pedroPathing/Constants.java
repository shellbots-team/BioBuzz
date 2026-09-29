package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(11.34)
            .forwardZeroPowerAcceleration(-46.7473767728444)
            .lateralZeroPowerAcceleration(-74.7156762508709)

            // MORE PEDRO ???
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(false)

            .translationalPIDFCoefficients(new PIDFCoefficients(0.015, 0.1, 0.009, 0.02))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.075,0.0001,0.001, 0.02))

            .headingPIDFCoefficients(new PIDFCoefficients(0.9, 0, 0, 0.02))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(0.9, 0.075, 0, 0.02))

            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.003, 0, 0, 0.5, 0.02))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.0, 0, 0, 0, 0.02))

            .centripetalScaling(0.5)
            .automaticHoldEnd(true)
            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 0);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRightDrive")
            .rightRearMotorName("backRightDrive")
            .leftRearMotorName("backLeftDrive")
            .leftFrontMotorName("frontLeftDrive")
            .leftFrontMotorDirection(DcMotorEx.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorEx.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorEx.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorEx.Direction.REVERSE)

            .xVelocity(41.66101458692175)
            .yVelocity(51.29496416707677)

            .useBrakeModeInTeleOp(true)
            .useVoltageCompensation(true);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(7.25)
            .strafePodX(-7.375)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            ;

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
