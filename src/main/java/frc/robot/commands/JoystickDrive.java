// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.SwerveSubsystem;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** An example command that uses an example subsystem. */
public class JoystickDrive extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private SwerveSubsystem m_swerveSubsystem;
  private CommandXboxController m_controller;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public JoystickDrive(CommandXboxController controller, SwerveSubsystem swerveSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_swerveSubsystem = swerveSubsystem;
    m_controller = controller;
    addRequirements(m_swerveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ChassisSpeeds newGoalSpeeds = new ChassisSpeeds(
            MathUtil.applyDeadband(m_controller.getLeftY(), 0.02), // makes bot move forward/backward in the y direction
            MathUtil.applyDeadband(m_controller.getLeftX(), 0.02), // makes bot move left/right in the x direction
            MathUtil.applyDeadband(m_controller.getRightX(), 0.02) // makes bot rotate in the x direction 
        );

        m_swerveSubsystem.setChassisSpeed(newGoalSpeeds);        
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
