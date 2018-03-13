package week_02;
import java.util.Scanner;

public class Scheduler
{
	enum Enumstate{UP, DOWN, STILL,NULL};
	enum Enumkind{FR, ER};
	static String ERR0 = new String("Wrong format!");
	static String ERR1 = new String("Invalid input!");
	
	private double timer;
	private Reqlist reqs;
	
	Scheduler()
	{
		timer = 0;
		reqs = new Reqlist();
	}
	
	void getreqs()
	{
		int count = 0;
		int f = 0;
		double t = 0;
		Request req;
		boolean isfirst = true;
		Scanner s = new Scanner(System.in);
		String str = s.nextLine().replaceAll(" ", "");
		String re = new String("\\(FR,([1-9]|10),(UP|DOWN),\\d{1,16}\\)|\\(ER,([1-9]|10),\\d{1,16}\\)");
		
		while(!str.matches("run"))
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
						System.out.println(str + ":" + ERR1);
						str = s.nextLine().replaceAll(" ", "");
						continue;
					}
				}
				catch(NumberFormatException e)
				{
					System.out.println(str + ":" + ERR1);
					str = s.nextLine().replaceAll(" ", "");
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
				
				if(reqs.check(req))
				{
					if(isfirst && req.gettime() != 0)
					{
						System.out.println(str + ":" + ERR1);
					}
					else 
					{
						reqs.addterm(req);
						isfirst = false;
						count ++;
					}
				}
				else 
					System.out.println(str + ":" + ERR1);
			}
			else
			{
				System.out.println(str + ":" + ERR0);
			}
			
			str = s.nextLine().replaceAll(" ", "");
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
		double endt = (timer + cost) > (re.gettime() + cost) ? (timer + cost) : (re.gettime() + cost);
		
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
			double cost = getcost(re, ele);
			ele.move(re);
			if(timer > re.gettime())
				timer += cost;
			else 
				timer = cost + re.gettime();
			ele.printstate(timer);
				
			reqs.remove(re);
		}
	}
	
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
			System.out.println(ERR0);
		}
	}
}
