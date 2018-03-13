package week_02;
import week_02.Scheduler.Enumstate;

class Elevator
{
	private int pos;
	private Enumstate state;
	
	Elevator()
	{
		pos = 1;
		state = Enumstate.STILL;
	}
	
	int getpos()
	{
		return pos;
	}
	
	Enumstate getstate()
	{
		return state;
	}
	
	void move(Request re)
	{
		if(re.getfloor() > pos)
		{
			state = Enumstate.UP;
		}
		else if(re.getfloor() < pos)
		{
			state = Enumstate.DOWN;
		}
		else
			state = Enumstate.STILL;
		
		pos = re.getfloor();
	}
	
	void printstate(double time)
	{
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		if(state == Enumstate.STILL)
			System.out.println("("+ pos + "," + state + "," + nf.format(time) + ")");
		else 
			System.out.println("("+ pos + "," + state + "," + nf.format(time - 1) + ")");
		
		state = Enumstate.STILL;
	}
}
