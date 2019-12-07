/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.Network;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class PullBall extends Subsystem {

  private static Float angleBall;

  Ultrasonic ult;

  private static PullBall instance;

  public static NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("tb");

  private PullBall(){
    ult = new Ultrasonic(RobotMap.TRIGGER, RobotMap.ECHO);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Network());
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public double GetDist(){
    return ult.getRangeInches() / 2.54;
  }

  static public void network(){
    networkTable.addEntryListener("angle", (table, key, entry, value, flags) -> {
      angleBall = (Float) value.getValue();
      SmartDashboard.putNumber("ang:", angleBall);
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }

  public static PullBall getInstance() {
    if(instance == null) instance = new PullBall();
    return instance;
  }
}
