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
import frc.robot.commands.JoystickDrive;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

class SwerveModule { // make everything private so it can't be messed with / is only exported in an approved format
    private Talon driveMotor, steerMotor; // falcon 500
    private SwerveModuleState currentState; 
    private SwerveModuleState goalState;

    // PID controllers
    PIDController drivePID;
    PIDController steerPID;

    public SwerveModule(int driveMotorPort, int steerMotorPort){ //immediately instantiate the vars for each module
        driveMotor = new Talon(driveMotorPort);
        steerMotor = new Talon(steerMotorPort);
        currentState =  new SwerveModuleState(); // no params, has 0s for angle and speed
        goalState = new SwerveModuleState();

        drivePID = new PIDController(0.1, 0,0); //TODO: integrate derivative and integral usage
        steerPID = new PIDController(0.1, 0,0);
    }

    public SwerveModuleState getSwerveState(){ // getter for security, best practice not to give other classes the ability to mess with state directly, and I can centralize all changes to state here
        return currentState;
    }

    public void setGoalSwerveState(SwerveModuleState newState){ //updates the state
        goalState = newState; 
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

    public SwerveSubsystem(){ //when this subsystem is created as an object, this is called once
        new ChassisSpeeds(0,0,0);
    }


    public void setChassisSpeed(ChassisSpeeds goalSpeed){
        SwerveModuleState[] goalStates = kinematics.toSwerveModuleStates(goalSpeed);

        //set states (speed/direction of modules) and log it to the SmartDashboard
        for (int i = 0; i < 4; i++){
            modules[i].setGoalSwerveState(goalStates[i]);
        }
  
        Logger.recordOutput("module speed,direction", goalStates);

    }


    @Override
    public void periodic(){

       /* ChassisSpeeds newGoalSpeeds = new ChassisSpeeds(
            controller.getLeftY(), // makes bot move forward/backward in the y direction
            controller.getLeftX(), // makes bot move left/right in the x direction
            controller.getRightX() // makes bot rotate in the x direction 
        );

        setChassisSpeed(newGoalSpeeds);
        */
    }
    
}
