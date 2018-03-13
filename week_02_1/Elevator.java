package week_02_1;

import week_02_1.Main.Enumstate;

class Elevator
{
	private int pos;
	private Enumstate state;
	private Request mainreq;
	
	Elevator()
	{
		pos = 1;
		state = Enumstate.STILL;
		mainreq = new Request();
	}
		
	Enumstate getstate()
	{
		return state;
	}
	
	Request getmainreq()
	{
		return mainreq;
	}
	
	int getpos()
	{
		return pos;
	}
	void changemain(Request re)
	{
		mainreq = re;
	}
	void resetstate()
	{
		state = Enumstate.STILL;
	}
	
	public void move(Request re)
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
//		state = Enumstate.STILL;
	}
	
	public String toString(Timer timer)
	{
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false); 
		String str;
		if(state == Enumstate.STILL)
			str = new String(mainreq.toString() + "/" + "("+ pos + "," + state + "," + nf.format(timer.gettime()) + ")");
		else
			str = new String(mainreq.toString() + "/" + "("+ pos + "," + state + "," + nf.format(timer.gettime() - 1) + ")");
		
		return str;
	}
	
/*	void printstate(double time)
	{
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		if(state == Enumstate.STILL)
			System.out.println("("+ pos + "," + state + "," + nf.format(time) + ")");
		else 
			System.out.println("("+ pos + "," + state + "," + nf.format(time - 1) + ")");
		
		state = Enumstate.STILL;
	}
*/
}
