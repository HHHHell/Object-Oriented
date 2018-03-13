package week_02;

import java.util.Vector;

import week_02.Scheduler.Enumkind;
import week_02.Scheduler.Enumstate;

class Reqlist
{
	private Vector <Request> list;
	private int size;
	
	Reqlist()
	{
		list = new Vector <Request> (10,10);
		size = list.size();
	}
	
	int getsize()
	{
		return size;
	}
	
	Request get(int i)
	{
		return list.get(i);
	}
	
	void addterm(Request re)
	{
		list.add(re);
		size = list.size();
	}
	
	void remove(Request re)
	{
		list.remove(re);
		size = list.size();
	}
	
	
	boolean check(Request re)
	{
		double t = 0;
		if(list.size() != 0)
			t = list.get(list.size()-1).gettime();
		if(re.getkind() == Enumkind.ER)
			return t <= re.gettime();
		else
		{
			if(re.getfloor() == 10)
				return (t <= re.gettime() && re.getdir() != Enumstate.UP); 
			else if(re.getfloor() == 1)
				return (t <= re.gettime() && re.getdir() != Enumstate.DOWN);
			else 
				return t <= re.gettime();
		}
	}
}