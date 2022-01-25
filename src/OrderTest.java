import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

public class OrderTest {

	@Test
	void testOrder() {
		//establish a data management connection
		DBManagement m = new DBManagement();
				
		//dummy list of some ingredients
		RawMaterials[] materials = {new RawMaterials(150, 200, 100, new GregorianCalendar(2021, 3, 9), RawMaterials.Materials.Bred, m), 
									new RawMaterials(100, 200, 100, new GregorianCalendar(2021, 3, 9), RawMaterials.Materials.Ham, m)};
				
		//dummy list of some machinery
		AssemblingMachinery.Procedures[] p = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Bake, AssemblingMachinery.Procedures.Cut};
		AssemblingMachinery.Procedures[] p1 = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Pour};
		Machinery[] machinery = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 133, "Middle", m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 3, 1), 155, "Side", p, AssemblingMachinery.Machines.BredCutter, m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 11, 1), 187, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, m)};
		Machinery[] machinery2 = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 143, "Middle", m), 
				new AssemblingMachinery(new GregorianCalendar(2025, 5, 10), 156, "Side", p, AssemblingMachinery.Machines.BredCutter, m), 
				new AssemblingMachinery(new GregorianCalendar(2025, 6, 8), 188, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, m)};
				
		//two dummy production lines. First line will always be the backup line
		ProductionLine[] lines = {new ProductionLine(0, machinery2, m), new ProductionLine(2, machinery, m)};
			
		//dummy order
		Order ord = new Order(56, "Scheduled", lines, materials, 150, m);
	}
	
	@Test
	void testMachinery() {
		DBManagement m = new DBManagement();
		
		//create a dummy conveyor belt
		Machinery machinery = new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 133, "Middle", m);
		assertEquals(Status.Idle, machinery.getStatus());
		
		//check if the status is updated correctly if it breaks down
		if(machinery.startMachinery() == 1)
			assertEquals(Status.Broken, machinery.getStatus());
		
		//create dummy machinery
		AssemblingMachinery.Procedures[] p = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Bake, AssemblingMachinery.Procedures.Cut};
		AssemblingMachinery machine = new AssemblingMachinery(new GregorianCalendar(2025, 1, 1), 133, "Middle", p, AssemblingMachinery.Machines.BredCutter, m);
		assertEquals(Status.Idle, machinery.getStatus());
		
		//check if statuses are updated correctly
		if(machinery.startMachinery() == 1)
			assertEquals(Status.Broken, machine.getStatus());
		else if(machinery.startMachinery() == 2)
			assertEquals(Status.BeingWashed, machine.getStatus());
		
		//check if the procedures are correct
		assertArrayEquals(p, machine.getProcedures());
	}
	
	@Test
	void testStorage() {
		DBManagement m = new DBManagement();
		
		//create an ingredient
		RawMaterials mat1 = new RawMaterials(150, 200, 100, new GregorianCalendar(2021, 2, 20), RawMaterials.Materials.Bred, m);
		
		//check if the order can be fulfilled
		assertEquals(true ,mat1.checkAmount(100));
		
		//check if new ingredients have been ordered
		assertEquals(200, mat1.getAmount());
		mat1.checkAmount(100);
		
		//check if a new order can be fulfilled
		assertEquals(100 ,mat1.getAmount());
	}
	
	//check the output of a production line
	//can't be deterministic since the breakdowns and contaminated food incidence are random
	@Test
	void testProductionLine() {
		DBManagement m = new DBManagement();
		AssemblingMachinery.Procedures[] p = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Bake, AssemblingMachinery.Procedures.Cut};
		AssemblingMachinery.Procedures[] p1 = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Pour};
		Machinery[] machinery = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 133, "Middle", m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 3, 1), 155, "Side", p, AssemblingMachinery.Machines.BredCutter, m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 11, 1), 187, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, m)};
		ProductionLine line = new ProductionLine(1, machinery, m);		
	}
}
