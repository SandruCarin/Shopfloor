import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class ConveyorBelt implements Machinery{

	private Status status;
	private final Calendar warranty;
	private Calendar lastCheckup;
	private int machineryID;
	private String repairPath;
	private DBManagement dataBase;
	
	ConveyorBelt(Calendar warranty, int ID, String path, DBManagement m){
		this.status = Status.Idle;
		this.warranty = warranty;
		this.lastCheckup = new GregorianCalendar(2021, 1, 1);
		this.machineryID = ID;
		this.repairPath = path;
		this.dataBase = m;
	}
	
	@Override
	public int startMachinery(){
		status = Status.Working;
		
		//10% of this to fail to simulate reality
		Random r = new Random();
		int low = 1;
		int high = 10;
		int result = r.nextInt(high-low) + low;
		
		switch(result) {
			case 1:
				status = Status.Broken;
				System.out.println("Conveyor belt broke down. Checking backup line!");
				updateStatus();
				problems();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return 1;
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

	@Override
	public int getID() {
		return machineryID;
	}

}
