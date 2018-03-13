package week_03;

import java.util.Vector;

public class Reqlist
{
	private Vector <Request> list;
	private int size;
	
	Reqlist()
	{
		list = new Vector <Request> (10,1);
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
	
	void remove(int i)
	{
		list.remove(i);
		size = list.size();
	}
}