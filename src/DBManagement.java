import java.sql.*;

public class DBManagement {
	
	Connection conn = null;
	
	//try and establish a connection to the data base
	public void dataConnection() {
	    	try {
	    		conn = DriverManager.getConnection("jdbc:derby:demoDB;create=true");

	    		System.out.println("Connection established!");

	    	} catch (SQLException e) {
	    		throw new Error("Problem", e);
	    	}
	    initializeTables();
	 }
	
	//make sure tables are reset when the program is launched
	private void initializeTables() {
		deleteTables();
		createTables();
	}
	
	//delete previous tables
	private void deleteTables() {
		try {
			Statement stmt1 = conn.createStatement();
			String table1 = "DROP TABLE PRODUCTION_LINE";
			String table2 = "DROP TABLE SHOPFLOOR_STORAGE";
			String table3 = "DROP TABLE MACHINES";
			String table4 = "DROP TABLE MACHINE_PROCEDURE";
			String table5 = "DROP TABLE INSTRUCTIONS";
			String table6 = "DROP TABLE ORDERS";
			stmt1.execute(table1);
			stmt1.execute(table2);
			stmt1.execute(table3);
			stmt1.execute(table4);
			stmt1.execute(table5);
			stmt1.execute(table6);
			stmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//crate the tables needed for the Shopfloor
	private void createTables() {
		try {
			Statement stmt1 = conn.createStatement();
			String table1 = "CREATE TABLE ORDERS" +
					"(order_id INTEGER NOT NULL," +
					"status VARCHAR(255)," +
					"PRIMARY KEY (order_id))";
			String table2 = "CREATE TABLE MACHINES" +
					"(machine_id INTEGER NOT NULL," +
					"machine_name VARCHAR(255)," +
					"occupied_until TIME," +
					"status VARCHAR(255)," +
					"access_path VARCHAR(255)," +
					"warranty DATE," +
					"PRIMARY KEY (machine_id))";
			String table3 = "CREATE TABLE MACHINE_PROCEDURE" +
					"(machine_id INTEGER NOT NULL," +
					"machine_procedure VARCHAR(255)," +
					"PRIMARY KEY (machine_id))";
			String table4 = "CREATE TABLE INSTRUCTIONS" +
					"(instruction_id INTEGER NOT NULL," +
					"order_id INTEGER," +
					"product_id INTEGER," +
					"machine_id INTEGER," +
					"machine_procedure VARCHAR(255)," +
					"ingredientes VARCHAR(255)," +
					"duration INTEGER," +
					"PRIMARY KEY (instruction_id))";
			String table5 = "CREATE TABLE SHOPFLOOR_STORAGE" +
					"(ingredientes VARCHAR(255)," +
					"min_amount INTEGER," +
					"max_amount INTEGER," +
					"amount INTEGER," +
					"expiration_date time," +
					"PRIMARY KEY (ingredientes))";
			String table6 = "CREATE TABLE PRODUCTION_LINE" +
					"(production_line_id INTEGER NOT NULL," +
					"machine_id INTEGER," +
					"status VARCHAR(255)," +
					"PRIMARY KEY (production_line_id))";
			stmt1.execute(table1);
			stmt1.execute(table2);
			stmt1.execute(table3);
			stmt1.execute(table4);
			stmt1.execute(table5);
			stmt1.execute(table6);
			stmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//initialize the two columns needed for the shopfloor from the ORDERS table
	public void addDummyOrder(int orderID) {
		String query = "insert into ORDERS (ORDER_ID, STATUS)" + "values(?, ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, orderID);
			stmt.setString(2, "Starting...");
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//create the table for the machinery
	public void addDummyMachinery(Machinery m) {
		String query = "insert into MACHINES (MACHINE_ID, MACHINE_NAME, OCCUPIED_UNTIL, STATUS, ACCESS_PATH, WARRANTY)" + " values (?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, m.getID());
			
			if(m.getClass() == AssemblingMachinery.class)
				stmt.setString(2, ((AssemblingMachinery) m).getType().toString());
			else
				stmt.setString(2, "ConveyorBelt");
			stmt.setTime(3, new java.sql.Time(0));
			stmt.setString(4, m.getStatus().toString());
			stmt.setString(5, m.getRepairPath());
			stmt.setDate(6, new java.sql.Date(m.getWarranty().getTimeInMillis()));
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//create the table for machinery procedures
	public void addDummyProcedures(Machinery m, String pro) {
		String query = "insert into MACHINE_PROCEDURE (MACHINE_ID, MACHINE_PROCEDURE)" + " values (?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, m.getID());
			stmt.setString(3, pro);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//create the table for production lines and their machinery
	public void addDummyProductionLine(ProductionLine p, Machinery m) {
		String query = "insert into PRODUCTION_LINE (PRODUCTION_LINE_ID, MACHINE_ID, STATUS)" + " values (?, ?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, p.getlineNumber());
			stmt.setInt(2, m.getID());
			stmt.setString(3, p.getStatus().toString());
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//create the table for the shopfloor storage to keep track of ingredients
	public void addDummyShopfloorStorage(RawMaterials rm) {
		String query = "insert into SHOPFLOOR_STORAGE (INGREDIENTS, MIN_AMOUNT, MAX_AMOUNT, AMOUNT, EXPIRATION_DATE)" + " values (?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rm.getIngredient().toString());
			stmt.setInt(2, rm.getMinAmount());
			stmt.setInt(3, rm.getMaxAmount());
			stmt.setInt(4, rm.getAmount());
			stmt.setDate(5, new java.sql.Date(rm.getExpirationDate().getTimeInMillis()));
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//create the table for instructions to be executed
	public void addDummyInstructions() {
		String query = "insert into INSTRUCTIONS (INSTRUCTION_ID, ORDER_ID, PRODUCT_ID, MACHINE_ID, MACHINE_PROCEDURE, INGREDIENTS, DURATION)" + " values (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, 1);
			stmt.setInt(2, 14);
			stmt.setInt(3, 14);
			stmt.setInt(4, 14);
			stmt.setString(5, "Cut,Bake");
			stmt.setString(6, "Ham,Tomatoes");
			stmt.setInt(7, 10);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//fetch a new order from the data base and start its production
	public void fetchOrder(ProductionLine[] lines, RawMaterials[] mat) {
		String query1 = "SELECT ORDER_ID, STATUS FROM ORDERS";
		String query2 = "SELECT COUNT(PRODUCT_NAME) AS PRODUCTS FROM ORDER_PRODUCTS WHERE ORDER_ID = ";
		String query3 = "SELECT INGREDIENTES, MACHINE_PROCEDURE, MACHINE_ID FROM INSTRUCTIONS WHERE ORDER_ID = ";
		int nrProducts = 0;
		RawMaterials[] mat1 = new RawMaterials[mat.length];
		
		try {
			//get orderID and status 
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query1);
			
			//for every order present
			while(rs.next())
			{
				int id = rs.getInt("ORDER_ID");
				String status = rs.getString("STATUS");
				
				//if the order is scheduled it means it can begin production
				if(status == "SCHEDULED")
				{
					//get how many products need to be made
					try {
						query2 = query2 + id;
						Statement s1 = conn.createStatement();
						ResultSet rs1 = s1.executeQuery(query2);
						nrProducts = rs1.getInt("PRODUCTS");
						s1.close();
					}catch (SQLException e) {
						e.printStackTrace();
					}
					
					//get the ingredients needed for the order
					try {
						query3 = query3 + id;
						Statement s2 = conn.createStatement();
						ResultSet rs2 = s2.executeQuery(query3);
						String[] ingredientes = rs2.getString("INGREDIENTES").split(",");
						int j = 0;
						for(int i = 0; i < ingredientes.length; i++)
							for(RawMaterials r : mat)
								if(r.getIngredient().toString().equals(ingredientes[i]))
								{
									mat1[j] = r;
									j++;
								}
						s.close();
						s2.close();
					}catch(SQLException e) {
						e.printStackTrace();
					}
					
					//create the new order
					new Order(id, status, lines, mat1, nrProducts, this);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
