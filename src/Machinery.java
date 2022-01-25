import java.util.Calendar;

public interface Machinery {
	
	//start the machinery to work on some orders
	public int startMachinery();
	
	//get machinery id
	public int getID();
	
	//get machinery warranty
	public Calendar getWarranty();
	
	//get the last time a check up was done
	public Calendar getLastCheckup();
	
	//get the status of the machinery
	public Status getStatus();
	
	//get the path to the machinery
	public String getRepairPath();
	
	//get the machinery to know which one has problems
	public Machinery problems();
}
