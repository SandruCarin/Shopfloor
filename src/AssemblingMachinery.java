import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class AssemblingMachinery implements Machinery{

	enum Machines{
		BredCutter, SauceRobot, Oven;
	}
	
	enum Procedures{
		Cut, Bake, Pour, Freeze;
	}

	private Status status;
	private final Calendar warranty;
	private Calendar lastCheckup;
	private int machineryID;
	private String repairPath;
	private Machines machine;
	private Procedures[] procedure;
	private DBManagement dataBase;
	
	AssemblingMachinery(Calendar warranty, int ID, String path, Procedures[] p, Machines m, DBManagement dataBase){
		this.status = Status.Idle;
		this.warranty = warranty;
		this.lastCheckup = new GregorianCalendar(2021, 1, 1);
		this.machineryID = ID;
		this.repairPath = path;
		this.procedure = p;
		this.machine = m;
		this.dataBase = dataBase;
	}
	
	@Override
	public int startMachinery(){
		status = Status.Working;
		
		//do the procedures mantioned in the order instructions
		for(Procedures e : procedure)
		{
			System.out.println(machine.toString() + " " + e.toString() + "...");
			//simulate working
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		//10% chance that the machinery breaks and 10% chance that contaminated food is found
		Random r = new Random();
		int low = 1;
		int high = 10;
		int result = r.nextInt(high-low) + low;
		
		switch(result) {
			case 1:
				status = Status.Broken;
				System.out.println(machine.toString() + " broke down. Checking backup line!");
				updateStatus();
				problems();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return 1;
			case 2:
				status = Status.BeingWashed;
				System.out.println("Contaminated food has been detected. Checking backup line!");
				updateStatus();
				problems();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return 2;
			default:
				status = Status.Idle;
				updateStatus();
				return 0;
		}
	}
	
	private void updateStatus() {
		/*
		String s = "UPDATE STATUS FROM MACHINES SET STATUS = ? WHERE MACHINE_ID = ?";
		try {
			PreparedStatement stm = dataBase.conn.prepareStatement(s);
			stm.setString(1, status.toString());
			stm.setInt(2, machineryID);
		} catch (SQLException f) {
			f.printStackTrace();
		}
		*/
	}

	@Override
	public int getID() {
		return machineryID;
	}
	
	public Machines getType() {
		return machine;
	}
	
	public Procedures[] getProcedures() {
		return procedure;
	}
	
	@Override
	public Calendar getWarranty() {
		return warranty;
	}

	@Override
	public Calendar getLastCheckup() {
		return lastCheckup;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public String getRepairPath() {
		return repairPath;
	}

	@Override
	public Machinery problems() {
		return this;
	}
}
