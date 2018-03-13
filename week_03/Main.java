package week_03;

public class Main
{
	static public enum Enumstate{UP, DOWN, STILL,NULL};
	static public enum Enumkind{FR, ER};
	
	public static void main(String[] args)
	{
		try 
		{
			Elevator ele = new Elevator();
			ALS_Scheduler als_Scheduler = new ALS_Scheduler();
			
			als_Scheduler.getreqs();
			als_Scheduler.command(ele);
		}
		catch(Exception e) 
		{
			ALS_Scheduler sch = new ALS_Scheduler();
			sch.errhandler(0, "**");
		}
	}
}
