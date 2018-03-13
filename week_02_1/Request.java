package week_02_1;

import week_02_1.Main.Enumkind;
import week_02_1.Main.Enumstate;

class Request
{
	private Enumstate dir;
	private Enumkind kind;
	private int floor;
	private double time;
	
	Request(Enumkind k ,int f, Enumstate d, double t)
	{
		kind = k;
		time = t;
		floor = f;
		dir = d;
	}
	
	Request()
	{
		kind = Enumkind.FR;
		time = 0;
		floor = 1;
		dir = Enumstate.NULL;
	}
	
	Enumstate getdir()
	{
		return dir;
	}
	Enumkind getkind()
	{
		return kind;
	}
	int getfloor()
	{
		return floor;
	}
	double gettime()
	{
		return time;
	}	
	double getcost(Elevator ele)
	{
		double cost = Math.abs(floor - ele.getpos())*0.5 + 1;
		return cost;
	}
	
	public String toString()
	{
		String s;
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false); 
		if(kind == Enumkind.FR)
			s = new String("[" + kind + "," + floor + "," + dir.toString() + "," + nf.format(time) + "]");
		else 
			s = new String("[" + kind + "," + floor + "," + nf.format(time) + "]");
		
		return s;
	}
	
	public boolean validitycheck(Boolean isfirst, Request re)
	{
		boolean result = true;
		
		if(isfirst)
		{
//			if(!this.toString().equals("[FR,1,UP,0]"))
//				result = false;
			if(this.time != 0)
				result = false;
		}
		
		if(this.time < re.time)
			result = false;
		
		if(this.kind == Enumkind.FR && this.dir == Enumstate.UP && this.floor == 10)
			result = false;
		
		if(this.kind == Enumkind.FR && this.dir == Enumstate.DOWN && this.floor == 1)
			result = false;
		
//		System.out.println(result);
		return result;
	}
	
	public boolean pickcheck(Elevator ele)
	{
		boolean result = false;
		if(this.kind == Enumkind.FR)
		{
			if(this.dir == ele.getstate() || ele.getstate() == Enumstate.STILL)
			{
				if(this.dir == Enumstate.UP && this.floor <= ele.getmainreq().getfloor() && this.floor > ele.getpos())
					result = true;
				else if(this.dir == Enumstate.DOWN && this.floor >= ele.getmainreq().getfloor() && this.floor < ele.getpos())
					result = true;
			}
		}
		else
		{
			if(this.dir == ele.getstate() || ele.getstate() ==Enumstate.STILL)
			{
				if(ele.getstate() == Enumstate.UP && this.floor > ele.getpos())
					result = true;
				else if(ele.getstate() == Enumstate.DOWN && this.floor < ele.getpos())
					result = true;
			}
		}
		return result;
	}
}
