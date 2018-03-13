package week_03;

import week_03.Main.Enumkind;
import week_03.Main.Enumstate;

public class ALS_Scheduler extends Scheduler
{
	private Reqlist sdreqs; 				 //list of requests been picked
	
	public ALS_Scheduler()
	{
		super();
		sdreqs = new Reqlist();
	}

	public boolean pickcheck(Elevator ele, Request re)
	{
		boolean result = false;
		if(re.getkind() == Enumkind.FR)
		{
			if(re.getdir() == ele.getstate())
			{
				if(re.getdir() == Enumstate.UP && re.getfloor() <= ele.getmainreq().getfloor() && re.getfloor() > ele.getpos())
					result = true;
				else if(re.getdir() == Enumstate.DOWN && re.getfloor() >= ele.getmainreq().getfloor() && re.getfloor() < ele.getpos())
					result = true;
			}
		}
		else
		{			
			if(ele.getstate() == Enumstate.UP && re.getfloor() > ele.getpos())
				result = true;
			else if(ele.getstate() == Enumstate.DOWN && re.getfloor() < ele.getpos())
				result = true;
		}
		return result;
	}
	
	void setmain(Elevator ele)						//set main request
	{
		Request main;
		if(ele.getmainreq() == null)								
		{
			if(sdreqs.getsize() != 0)
			{
				main = sdreqs.get(0);
				for(int i = 0; i < sdreqs.getsize(); i++)
				{
					if(sdreqs.get(i).gettime() < main.gettime())
						main = sdreqs.get(i);
				}
				sdreqs.remove(main);
			}
			else 
			{
				main = reqs.get(0);
				reqs.remove(main);
			}
			ele.changemain(main);
			if(main.gettime() > timer.gettime())
				timer.goes(main.gettime() - timer.gettime());
		}
	}
	
	void rmsame(Elevator ele)			// remove same requests
	{
		Request temp;
		Request main = ele.getmainreq();
		double cost = main.getcost(ele);
		
		for(int i = 0;i < reqs.getsize() && reqs.get(i).gettime() <= cost + timer.gettime(); i++)
		{
			temp = reqs.get(i);
			if(main.getkind() == Enumkind.ER)
			{
				if(temp.getkind() == Enumkind.ER && temp.getfloor() == main.getfloor())
					{
						reqs.remove(temp);
						i--;
						errhandler(1, temp.toString());
					}
			}
			else 
			{
				if(temp.getkind() == Enumkind.FR && temp.getfloor() == main.getfloor() && temp.getdir() == main.getdir())
				{
					reqs.remove(temp);
					i--;
					errhandler(1, temp.toString());
				}
			}
		}
	}
	
	void merge(Elevator ele, Request request, double timer)		// merge the same requests picked
	{
		for(int i = 0; i < sdreqs.getsize(); i++)
		{
			if(!sdreqs.get(i).equales(request) && sdreqs.get(i).getfloor() == request.getfloor())
			{
				System.out.println(ele.toString(sdreqs.get(i),timer));
				sdreqs.remove(sdreqs.get(i));
				i--;
			}
			
			if(!sdreqs.get(i).equales(request) && sdreqs.get(i).getfloor() == ele.getmainreq().getfloor())
			{
				System.out.println(ele.toString(sdreqs.get(i),timer));
				sdreqs.remove(sdreqs.get(i));
				i--;
			}
		}
	}
	
	Request schedule(Elevator ele)					//find the requests to be picked up and return request to be done
	{
		Request r;
		rmsame(ele);
		for(int i = 0;i < reqs.getsize() && reqs.get(i).gettime() < ele.getmainreq().getcost(ele) + timer.gettime() - 1; i++)
		{
			if(reqs.get(i).getkind() == Enumkind.FR && 
					reqs.get(i).gettime() >= ele.getmainreq().getcost(ele,reqs.get(i).getfloor()) + timer.gettime() - 1)
				continue;
			r = reqs.get(i);
			if(pickcheck(ele,r))
			{
				sdreqs.addterm(r);
				reqs.remove(r);
				i--;
			}
		}
		
		if(sdreqs.getsize() == 0)
			return null;
		else 
		{
			int min = 0;
			for(int i = 0; i < sdreqs.getsize();i++)
			{
				if(ele.getstate() == Enumstate.UP && sdreqs.get(i).getfloor() < sdreqs.get(min).getfloor())
					min = i;
				else if(ele.getstate() == Enumstate.DOWN && sdreqs.get(i).getfloor() > sdreqs.get(min).getfloor())
					min = i;
			}
			return sdreqs.get(min);
		}
	}
	
	void command(Elevator ele)
	{
		boolean isopen = false;
		Request request;	
		setmain(ele);
		request = schedule(ele);
		while(reqs.getsize() >= 0)
		{
			if(isopen)
			{
				isopen = false;
				timer.goes(0.5);
				if(ele.getpos() == ele.getmainreq().getfloor())
				{
					ele.resetmain();
					ele.resetstate();
				}

				if((reqs.getsize() > 0 || sdreqs.getsize() > 0) && ele.getmainreq() == null)
				{
					setmain(ele);
					request = schedule(ele);
				}
				
				if(ele.getmainreq() == null)
					break;
				continue;
			}
			
			if(ele.getmainreq().getfloor() > ele.getpos())
			{
				ele.moveup();
				timer.goes(0.5);
			}
			else if(ele.getmainreq().getfloor() < ele.getpos())
			{
				ele.movedown();
				timer.goes(0.5);
			}
			
			if(ele.getpos() == ele.getmainreq().getfloor())
			{

				System.out.println(ele.toString(ele.getmainreq(), timer.gettime()));
				merge(ele, request, timer.gettime());
				timer.goes(0.5);
				isopen = true;
			}
			
			if( request != null && ele.getpos() == request.getfloor())
			{
				if(ele.getpos() != ele.getmainreq().getfloor())
				{
					System.out.println(ele.toString(request, timer.gettime()));
					merge(ele, request, timer.gettime());
					timer.goes(0.5);
				}
				else
				{
					System.out.println(ele.toString(request, timer.gettime() - 0.5));
					merge(ele, request, timer.gettime() - 0.5);
				}
				sdreqs.remove(request);
				isopen = true;
				
				if(reqs.getsize() > 0 || sdreqs.getsize() > 0)
					request = schedule(ele);
			}
			
			if(reqs.getsize() == 0 && sdreqs.getsize() == 0 && ele.getmainreq() == null)
				break;
		}
	}
}
