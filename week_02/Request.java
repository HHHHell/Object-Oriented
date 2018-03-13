package week_02;

import week_02.Scheduler.Enumkind;
import week_02.Scheduler.Enumstate;

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
	
	double gettime()
	{
		return time;
	}
	
	int getfloor()
	{
		return floor;
	}
}
