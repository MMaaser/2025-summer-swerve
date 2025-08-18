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

import org.littletonrobotics.junction.Logger;

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

    SwerveModule[] modules = {
        new SwerveModule(1, 2), //front left
        new SwerveModule(3, 4), // front right
        new SwerveModule(5, 6), // back left
        new SwerveModule(7, 8) // back right
    };


    double chassisWidth = Units.inchesToMeters(20);
    double chassisLength = Units.inchesToMeters(20);

    Translation2d[] positions = {
        new Translation2d(chassisLength/2, chassisWidth/2), //front left
        new Translation2d(chassisLength/2, - chassisWidth/2), // front right
        new Translation2d(- chassisLength/2, chassisWidth/2), // back left
        new Translation2d(- chassisLength/2, - chassisWidth/2) // back right
    };


    // takes in ChassisSpeeds and returns SwerveModuleStates in the same order wheel positions were given
    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(positions[0], positions[1], positions[2], positions[3]);

    CommandXboxController controller;

    //TODO: integrate commands instead of passing in the driver controller as a parameter
    public SwerveSubsystem(CommandXboxController io){ //when this subsystem is created as an object, this is called once
        new ChassisSpeeds(0,0,0);
        controller = io;
    }

    public void setChassisSpeed(ChassisSpeeds goalSpeed){
        SwerveModuleState[] goalStates = kinematics.toSwerveModuleStates(goalSpeed);

        //set states (speed/direction of modules) and log it to the SmartDashboard
        for (int i = 0; i < 4; i++){
            modules[i].setSwerveState(goalStates[i]);
            //Logger.recordOutput("modules/" + i + " angle", modules[i].getSwerveState().angle);
            //Logger.recordOutput("modules/" + i + " speed", modules[i].getSwerveState().speedMetersPerSecond);
        }
  
        Logger.recordOutput("module speed/direction", goalStates);

    }


    @Override
    public void periodic(){

        ChassisSpeeds newGoalSpeeds = new ChassisSpeeds(
            controller.getLeftY(), // makes bot move forward/backward in the y direction
            controller.getLeftX(), // makes bot move left/right in the x direction
            controller.getRightX() // makes bot rotate in the x direction 
        );
        setChassisSpeed(newGoalSpeeds);

        //setChassisSpeed(new ChassisSpeeds(-1, 0, 0)); // all modules ~180°
        //setChassisSpeed(new ChassisSpeeds(0, 1, 0));  // left strafe
       // setChassisSpeed(new ChassisSpeeds(0, -1, 0)); // right strafe
        double swerveStateLogs[] = { //rotation , velocity
                modules[0].getSwerveState().angle.getDegrees(), modules[0].getSwerveState().speedMetersPerSecond, 
                modules[1].getSwerveState().angle.getDegrees(), modules[1].getSwerveState().speedMetersPerSecond, 
                modules[2].getSwerveState().angle.getDegrees(), modules[2].getSwerveState().speedMetersPerSecond, 
                modules[3].getSwerveState().angle.getDegrees(), modules[3].getSwerveState().speedMetersPerSecond 
        };

      //  SmartDashboard.putNumberArray("Swerve Module States", swerveStateLogs);
        
    }
    
}
