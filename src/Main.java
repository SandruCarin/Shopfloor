
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] args) {
		//establish a data management connection
		DBManagement m = new DBManagement();
		//m.dataConnection();
		
		//dummy list of some ingredients
		RawMaterials[] materials = {new RawMaterials(150, 200, 100, new GregorianCalendar(2021, 3, 9), RawMaterials.Materials.Bred, m), 
									new RawMaterials(100, 200, 100, new GregorianCalendar(2021, 3, 9), RawMaterials.Materials.Ham, m)};
		/*add the ingredients to the table
		  for(RawMaterials e : materials)
			m.addDummyShopfloorStorage(e);
		*/
		
		//dummy list of some machinery
		AssemblingMachinery.Procedures[] p = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Bake, AssemblingMachinery.Procedures.Cut};
		AssemblingMachinery.Procedures[] p1 = new AssemblingMachinery.Procedures[] {AssemblingMachinery.Procedures.Pour};
		Machinery[] machinery = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 133, "Middle", m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 3, 1), 155, "Side", p, AssemblingMachinery.Machines.BredCutter, m), 
									new AssemblingMachinery(new GregorianCalendar(2025, 11, 1), 187, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, m)};
		Machinery[] machinery2 = {new ConveyorBelt(new GregorianCalendar(2025, 1, 1), 143, "Middle", m), 
				new AssemblingMachinery(new GregorianCalendar(2025, 5, 10), 156, "Side", p, AssemblingMachinery.Machines.BredCutter, m), 
				new AssemblingMachinery(new GregorianCalendar(2025, 6, 8), 188, "Rear", p1, AssemblingMachinery.Machines.SauceRobot, m)};
		
		/* add the machinery to the table
		for(Machinery e : machinery)
		{
			m.addDummyMachinery(e);
			if(e.getClass() == AssemblingMachinery.class)
				for(AssemblingMachinery.Procedures f : ((AssemblingMachinery)e).getProcedures())
					m.addDummyProcedures(e, f.toString());
		}
		for(Machinery e : machinery2)
		{
			m.addDummyMachinery(e);
			if(e.getClass() == AssemblingMachinery.class)
				for(AssemblingMachinery.Procedures f : ((AssemblingMachinery)e).getProcedures())
					m.addDummyProcedures(e, f.toString());
		}
		*/
		
		//two dummy production lines. First line will always be the backup line
		ProductionLine[] lines = {new ProductionLine(0, machinery2, m), new ProductionLine(2, machinery, m)};
		
		/* att the line to the table
		for(ProductionLine e : lines)
			for(Machinery f : e.getMachinery())
				m.addDummyProductionLine(e, f);
		*/
		
		//dummy order
		Order ord = new Order(56, "Scheduled", lines, materials, 150, m);
		
		//should fetch the order from the data table
		//m.fetchOrder(lines, materials);
	}

}
