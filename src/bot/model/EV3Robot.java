package bot.model;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * 
 * @author Luke Barrowes
 *@version 0.x Dec 10, 2015
 */
public class EV3Robot 
{
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	
	private MovePilot botPilot;
	
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor backTouch;
	private double []roomDistance;
	private float [] ultrasonicSamples;
	private float [] touchSamples;
	
	public void EV3Bot()
	{
		this.botMessage = "Luke's Bot";
		this.xPosition = 50;
		this.yPosition = 50;
		this.waitTime = 4000;	
	}
	
	private void setupPilot()
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A,  55.0).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B,  55.0).offset(72);
		WheeledChassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}
	
	public void driveRoom()
	{
		ultrasonicSamples= new float [distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		if(ultrasonicSamples[0] < 2.5) //2.5 is not a real number!  figure out a better number!
		{
			displayMessage("Short Drive");
			driveShort();
		}
		else
		{
			displayMessage("LongDrive");
			driveShort();
		}
		displayMessage("I am at the other door!!!");
	}
	
	private void displayMessage(String message)
	{
		LCD.drawString(botMessage, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	
	private void driveShort()
	{
		
	}
	
	public void driveAround()
	{
		while(LocalEV3.get().getKeys().waitForAnyPress() != LocalEV3.get().getKeys().ID_ESCAPE)
		{
			double distance = (Math.random() * 100) % 23;
			double angle = (Math.random() * 360);
			boolean isPositive = ((int) (Math.random() * 2) %2 == 0);
			distanceSensor.fetchSample(ultrasonicSamples, 0);
			if(ultrasonicSamples[0] < 17)
			{
				botPilot.travel(-distance);;
				botPilot.rotate(angle);;
			}
			else
			{
				botPilot.rotate(-angle);
				botPilot.travel(distance);
			}
		}
	}
}
