import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RawMaterials {

	enum Materials{
		Bred, Ham, Tomatoes, Ketchup;
	}

	//always have at least 1 and at most 2 batches of the same ingredient. That means 2 amounts and 2 expiration dates
	private int amount1;
	private int amount2;
	private int maxAmount;
	private int minAmount;
	private Calendar expirationDate1;
	private Calendar expirationDate2;
	private Materials material;
	private DBManagement dataBase;
	
	RawMaterials(int amount, int max, int min, Calendar date, Materials m, DBManagement db){
		this.amount1 = amount;
		this.amount2 = 0;
		this.maxAmount = max;
		this.minAmount = min;
		this.expirationDate1 = date;
		this.material = m;
		this.dataBase = db;
	}
	
	public boolean checkAmount(int amountNeeded) {
		if(amount1 + amount2 >= amountNeeded && expirationDate1.compareTo(new GregorianCalendar(2021, 1, 9)) == 1)		//check if we have enough and if it is not expired
		{
			System.out.println(material.toString() + " available");
			if(amount1 < amountNeeded)					//get ingredients from both batches if needed
			{
				amountNeeded = amountNeeded - amount1;
				amount1 = 0;
				amount2 = amount2 - amountNeeded;
			}
			else
				amount1 = amount1 - amountNeeded;
			if(amount1 + amount2 < minAmount)
				orderMaterials();
			return true;
		}
		else
		{
			System.out.println(material.toString() + " not available. Informing warehouse!");
			
			//simulate waiting for ingredients
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			orderMaterials();
			System.out.println(material.toString() + " now available.");
			return false;
		}
	}

	//move the second batch into the first one so to always use what is left and call the warehouse for a delivery
	private void orderMaterials() {
		amount1 = amount1 + amount2;
		amount2 = maxAmount - amount1;
		expirationDate1 = new GregorianCalendar(2021, 2, 9); //should be expirationsDate2, but it is not initialized 
		
		/*
		String s = "UPDATE AMOUNT FROM SHOPFLOOR_STORAGE SET AMOUNT = ? WHERE INGREDIENTS = ?";
		try {
			PreparedStatement stm = dataBase.conn.prepareStatement(s);
			stm.setInt(1, amount1);
			stm.setString(2, material.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public Materials getIngredient() {
		return material;
	}
	
	public int getMinAmount() {
		return minAmount;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
	
	public int getAmount() {
		return amount1+amount2;
	}
	
	public Calendar getExpirationDate() {
		return expirationDate1;
	}
}
