import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Packaged {

	private String status;
	private int nrProducts;
	private int orderID;
	private DBManagement dataBase;
	
	Packaged(int products, int ID, DBManagement m){
		this.status = "";
		this.nrProducts = products;
		this.orderID = ID;
		this.dataBase = m;
	}
	
	public void startPackaging() {
		System.out.println("Order " + orderID + " is done!");
		System.out.println("Informing Logostics!");
		status = "Packaged";
		informLogistics();
	}
	
	//update the status of the order so the logistics team can pick it up
	public void informLogistics() {
		/*
		String s = "UPDATE STATUS FROM ORDERS SET STATUS = ? WHERE ORDER_ID = ?";
		try {
			PreparedStatement stm = dataBase.conn.prepareStatement(s);
			stm.setString(1, status.toString());
			stm.setInt(2, orderID);
		} catch (SQLException f) {
			f.printStackTrace();
		}
		*/
	}
}
