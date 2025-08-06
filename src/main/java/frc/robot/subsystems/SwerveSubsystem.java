package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class SwerveModule { // make everything private so it can't be messed with / is only exported in an approved format
    private Talon driveMotor, steerMotor; // falcon 500
    private SwerveModuleState state; //the current state
    public SwerveModule(int driveMotorPort, int steerMotorPort){ //immediately instantiate the vars for each module
        driveMotor = new Talon(driveMotorPort);
        steerMotor = new Talon(steerMotorPort);
        state =  new SwerveModuleState(); // no params, has 0s for angle and speed
    }
}

public class SwerveSubsystem extends SubsystemBase{ //SubsystemBase gets the subsystem's periodic called by the command scheduler


    SwerveModule frontLeft = new SwerveModule(0, 1);
    SwerveModule frontRight = new SwerveModule(2, 3);
    SwerveModule backLeft = new SwerveModule(4, 5);
    SwerveModule backRight = new SwerveModule(6, 7);


    public SwerveSubsystem(){ //when this subsystem is created as an object, this is called once
        System.out.println("Swerve Subsystem's constructor has been called");
    }


    @Override
    public void periodic(){
        double loggingState[] = { //rotation , velocity
                frontLeft.state(),1, //fl
                45, 2, //fr
                180, 3, //bl
                33, 4 //br
        };

        SmartDashboard.putNumberArray("Swerve Module States", loggingState);
    }
    
}
