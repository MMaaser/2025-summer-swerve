package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

class SwerveModule { // make everything private so it can't be messed with / is only exported in an approved format
    private Talon driveMotor, steerMotor; // falcon 500
    private SwerveModuleState state; //the current state
    public SwerveModule(int driveMotorPort, int steerMotorPort){ //immediately instantiate the vars for each module
        driveMotor = new Talon(driveMotorPort);
        steerMotor = new Talon(steerMotorPort);
        state =  new SwerveModuleState(); // no params, has 0s for angle and speed
    }

    public SwerveModuleState getSwerveState(){ // getter for security, best practice not to give other classes the ability to mess with state directly, and I can centralize all changes to state here
        return state;
    }

    public void setSwerveState(SwerveModuleState newState){ //updates the state
        state = newState; 
    }
}

public class SwerveSubsystem extends SubsystemBase{ //SubsystemBase gets the subsystem's periodic called by the command scheduler


    SwerveModule frontLeft = new SwerveModule(1, 2);
    SwerveModule frontRight = new SwerveModule(3, 4);
    SwerveModule backLeft = new SwerveModule(5, 6);
    SwerveModule backRight = new SwerveModule(7, 8);

    double chassisWidth = Units.inchesToMeters(20);
    double chassisLength = Units.inchesToMeters(20);

    Translation2d frontLeftPos = new Translation2d(chassisLength/2, chassisWidth/2); // Loc for location
    Translation2d frontRightPos = new Translation2d(chassisLength/2, -chassisWidth/2);
    Translation2d backLeftPos = new Translation2d(-chassisLength/2, chassisWidth/2);
    Translation2d backRightPos = new Translation2d(-chassisLength/2, -chassisWidth/2);


    // takes in ChassisSpeeds and returns SwerveModuleStates in the same order wheel positions were given
    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeftPos, frontRightPos, backLeftPos, backRightPos);

    CommandXboxController controller;

    //TODO: integrate commands instead of passing in the driver controller as a parameter
    public SwerveSubsystem(CommandXboxController io){ //when this subsystem is created as an object, this is called once
        new ChassisSpeeds(0,0,0);
        controller = io;
    }

    public void setChassisSpeed(ChassisSpeeds goalSpeed){
        SwerveModuleState[] goalStates = kinematics.toSwerveModuleStates(goalSpeed);

        //TODO: turn the SwerveModules into an array so this can be wrapped up in a for loop
        //set states (speed/direction of modules)
        frontLeft.setSwerveState(goalStates[0]);
        frontRight.setSwerveState(goalStates[1]);
        backLeft.setSwerveState(goalStates[2]);
        backRight.setSwerveState(goalStates[3]);
    }


    @Override
    public void periodic(){

        ChassisSpeeds newGoalSpeeds = new ChassisSpeeds(
            controller. getLeftY(), // makes bot move forward/backward in the y direction
            controller.getLeftX(), // makes bot move left/right in the x direction
            controller.getRightX() // makes bot rotate in the x direction
        );

        setChassisSpeed(newGoalSpeeds);
        double swerveStateLogs[] = { //rotation , velocity
                frontLeft.getSwerveState().angle.getDegrees(), frontLeft.getSwerveState().speedMetersPerSecond, 
                frontRight.getSwerveState().angle.getDegrees(), frontRight.getSwerveState().speedMetersPerSecond, 
                backLeft.getSwerveState().angle.getDegrees(), backLeft.getSwerveState().speedMetersPerSecond, 
                backRight.getSwerveState().angle.getDegrees(), backRight.getSwerveState().speedMetersPerSecond 
        };

        SmartDashboard.putNumberArray("Swerve Module States", swerveStateLogs);
    }
    
}
