package week_02_1;

public class Main
{
	enum Enumstate{UP, DOWN, STILL,NULL};
	enum Enumkind{FR, ER};
	
	public static void main(String[] args)
	{
		try 
		{
			Elevator ele = new Elevator();
			Scheduler sch = new Scheduler();
			sch.getreqs();
			sch.command(ele);
		}
		catch(Exception e) 
		{
			Scheduler sch = new Scheduler();
			sch.errhandler(1, "");
		}
	}
}
