package week_03;

import java.util.Scanner;

import week_03.Main.Enumkind;
import week_03.Main.Enumstate;

public class Scheduler
{
	static String ERR0 = new String("INVALID ");
	static String ERR1 = new String("SAME ");
	
	protected Timer timer;
	protected Reqlist reqs;
	
	Scheduler()
	{
		timer = new Timer();
		reqs = new Reqlist();
	}
	
	void errhandler(int code, String str)
	{
		if(code == 0)
			System.out.println(ERR0 + "[" + str + "]");
		else if (code == 1)
			System.out.println(ERR1 + str);
	}
	void getreqs()
	{
		int count = 0;
		int f = 0;
		double t = 0;
		Request req, lastreq = null;
		boolean isfirst = true;
		Scanner s = new Scanner(System.in);
		String tem = s.nextLine();
		String str = tem.replaceAll(" ", "");
		String re = new String("\\(FR,\\+?0{0,14}([1-9]|10),(UP|DOWN),\\+?\\d{1,16}\\)|\\(ER,\\+?0{0,14}([1-9]|10),\\+?\\d{1,16}\\)");
		
		while(!str.matches("run") && count <= 100000)
		{
			if(str.matches(re))
			{
				String[] strs = str.split("[,\\(\\)]");
				try
				{
					f = Integer.valueOf(strs[2]);
					t = Double.valueOf(strs[strs.length-1]);
					
					if(t > 4294967295L)
					{
						errhandler(0, tem);
						tem = s.nextLine();
						str = tem.replaceAll(" ", "");
						continue;
					}
				}
				catch(NumberFormatException e)
				{
					errhandler(0, tem);
					tem = s.nextLine();
					str = tem.replaceAll(" ", "");
					continue;
				}
				
				if(strs[1].equals("FR"))
				{
					Enumstate e;
					if(strs[3].equals("UP"))
						e = Enumstate.UP;
					else 
						e = Enumstate.DOWN;
					req = new Request(Enumkind.FR,f,e,t);
				}
				else
				{
					Enumstate e = Enumstate.NULL;
					req = new Request(Enumkind.ER,f,e,t);
				}
				
				if(isfirst)
					lastreq = req;
				
				if(req.validitycheck(isfirst, lastreq))
				{
					reqs.addterm(req);
					lastreq = req;
					isfirst = false;
					count ++;
				}
				else 
				{
					errhandler(0, tem);
					tem = s.nextLine();
					str = tem.replaceAll(" ", "");
					continue;
				}
			}
			else
			{
				errhandler(0, tem);
				tem = s.nextLine();
				str = tem.replaceAll(" ", "");
				continue;
			}
			tem = s.nextLine();
			str = tem.replaceAll(" ", "");
		}
		s.close();
		if(count == 0)
			System.exit(0);
	}
	
	double getcost(Request re, Elevator ele)
	{
		int pos = ele.getpos();
		int floor = re.getfloor();
		
		double cost = Math.abs(floor - pos) * 0.5 + 1;
		
		return cost;
	}
	
	Request schedule(Elevator ele)
	{
		Request re = reqs.get(0);
		Request temp = reqs.get(0);
		double cost = getcost(re, ele);
		double endt = (timer.gettime() + cost) > (re.gettime() + cost) ? (timer.gettime() + cost) : (re.gettime() + cost);
		
		for(int i = 1; i < reqs.getsize() && reqs.get(i).gettime() <= endt; i++)
		{
			temp = reqs.get(i);
			if(re.getkind() == Enumkind.ER)
			{
				if(temp.getkind() == Enumkind.ER && temp.getfloor() == re.getfloor())
					{
						reqs.remove(temp);
					}
			}
			else 
			{
				if(temp.getkind() == Enumkind.FR && temp.getfloor() == re.getfloor() && temp.getdir() == re.getdir())
				{
					reqs.remove(temp);
				}
			}
		}
		return re;
	}

	void command(Elevator ele)
	{
		while(reqs.getsize() > 0)
		{			
			Request re = schedule(ele);
			ele.changemain(re);
			double cost = getcost(re, ele);
			ele.move(re);
			if(timer.gettime() > re.gettime())
				timer.goes(cost);
			else 
				timer.goes(cost + re.gettime() - timer.gettime());
			
			System.out.println(ele.toString(re,timer.gettime()));;
				
			reqs.remove(re);
			ele.resetstate();
		}
	}
}
