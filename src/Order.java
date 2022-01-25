import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class Order {

	private String status;
	private int orderID;
	private int nrProducts;
	private ProductionLine[] productionLines;
	private RawMaterials[] materials;
	private DBManagement dataBase;
	
	Order(int ID, String status, ProductionLine[] lines, RawMaterials[] materials, int products, DBManagement m){
		this.status = status;
		this.orderID = ID;
		this.productionLines = lines;
		this.materials = materials;
		this.nrProducts = products;
		this.dataBase = m;
		startProcessing();
	}
	
	private void startProcessing() {
		
		//checking the availability of the ingredients in temporary storage and queuing until we have the materials
		System.out.println("Order " + orderID + " received. Checking raw materials...");
		for(RawMaterials e : materials)
			e.checkAmount(nrProducts);
		System.out.println("Order " + orderID + " in queue.");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//handling of the production line and redirecting in case of emergency
		System.out.println("Order " + orderID + " started processing...");
		for(ProductionLine e : productionLines)
			if(e.getlineNumber() != 0)
				if(e.startProcessing())					//if it is true either a machine broke or contaminated food was detected
				{
					if(productionLines[0].getStatus() == Status.Idle)			//if the backup line is idle, the production will move to it
					{
						informMES(", backup line available.");
						System.out.println("Problem detected on the production line of order " + orderID + ", backup line available.");
						updateStatus("Producing on backup line");
						productionLines[0].startProcessing();
					}
					else
					{
						informMES(", backup line not available.");
						System.out.println("Problem detected on the production line of order " + orderID + ", backup line not available.");
						
						//here the call to the MES from above should return another production line to which the products should be moved
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						//creating a dummy production line
						AssemblingMachinery.Procedures[] p = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Bake, AssemblingMachinery.Procedures.Cut};
						AssemblingMachinery.Procedures[] p1 = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Pour};
						Machinery[] m = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 123, "Middle", dataBase), 
									new AssemblingMachinery(new GregorianCalendar(2025, 5, 10), 17, "Side", p, AssemblingMachinery.Machines.BredCutter, dataBase), 
									new AssemblingMachinery(new GregorianCalendar(2025, 6, 8), 208, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, dataBase)};
						ProductionLine line = new ProductionLine(3, m, dataBase);
						line.startProcessing();
						
						
						updateStatus("Producing on a new line");
					}
				}
				else
					updateStatus("Producing");
		
		//after all products are finished they should be packaged
		Packaged pack = new Packaged(nrProducts, orderID, dataBase);
		System.out.println("Starting packaging...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		pack.startPackaging();
		updateStatus("Packaged");
	}
	
	//should inform the MES as well as the WebShop regarding the progress
	private void updateStatus(String newStatus) {
		status = newStatus;
		System.out.println("Status of order " + orderID + " is " + status);
		
		/* should update the status of the order in the orders table
		String s = "UPDATE STATUS FROM ORDERS SET STATUS = ? WHERE ORDER_ID = ?";
		try {
			PreparedStatement stm = dataBase.conn.prepareStatement(s);
			stm.setString(1, status);
			stm.setInt(2, orderID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
	
	//inform the MES when something went wrong
	private String informMES(String message) {
		return "Problem detected on the production line of order " + orderID + message;
	}
	
	public RawMaterials[] getIngredients() {
		return materials;
	}
}
