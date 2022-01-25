import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductionLine {

	private Status status;
	private int lineNumber;
	private Machinery[] machinery;
	private DBManagement dataBase;
	
	ProductionLine(int lineNumber, Machinery[] machinery, DBManagement m){
		this.status = Status.Idle;
		this.lineNumber = lineNumber;
		this.machinery = machinery;
		this.dataBase = m;
	}
	
	public boolean startProcessing() {
		status = Status.Working;
		System.out.println("Production line " + lineNumber + " is working.");
		boolean state = false;
		for(Machinery e : machinery)		//every machinery on the line should do its job
		{
			switch(e.startMachinery()) {	//set the production line in the appropriate status
				case 1:
					status = Status.Broken;
					updateStatus();
					state = true;
					break;
				case 2:
					status = Status.BeingWashed;
					updateStatus();
					state = true;
					break;
				default:
					status = Status.Idle;
					updateStatus();
					state = false;
					break;
			}
			if(state)
				break;
		}
		return state;
	}
	
	private void updateStatus() {
		/*
		String s = "UPDATE STATUS FROM PRODUCTION_LINE SET STATUS = ? WHERE PRODUCTION_LINE_ID = ?";
		try {
			PreparedStatement stm = dataBase.conn.prepareStatement(s);
			stm.setString(1, status.toString());
			stm.setInt(2, lineNumber);
		} catch (SQLException f) {
			f.printStackTrace();
		}
		*/
	}
	
	public Status getStatus() {
		return status;
	}
	
	public int getlineNumber() {
		return lineNumber;
	}
	
	public Machinery[] getMachinery() {
		return machinery;
	}
}
