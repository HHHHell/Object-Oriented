package week_01;
import java.util.Scanner;

public class ComPoly
{
	final char ADD = '+';
	final char SUB = '-';
	
	private Poly polylist[];
	private char oplist[];
	private int count;
	
	public ComPoly()
	{	
		int i = 0;
		polylist = new Poly[20];
		oplist = new char[20];
		count = 0;
		
		for(i = 0;i < 20;i++)
			polylist[i] = new Poly(0,0);
	}
	
	public String  getString()
	{
		Scanner s = new Scanner(System.in);
		String str = s.nextLine();
		str = str.replaceAll("[^0-9, {}()+-]", " ");
		str = str.replaceAll(" ", "");
		s.close();
		if(str.isEmpty())
		{
			System.out.println("Wrong input: input cannot be empty!");
			System.exit(0);
		}
		return str;
	}
	
	public void getPoly(String s)
	{
		int i = 0;
		int op_num = 0;
		int poly_num = 0;
		int flag = 0;
		int coef = 0;
		int index = 0;
		int Nochange = 1;
		int fs = 1;
		
//		re = "([+-]?\\{\\([+-]?\\d{1,6},[+-]?\\d{1,6}\\)(,\\([+-]?\\d{1,6},[+-]?\\d{1,6}\\))*})"
//		+ "([+-]\\{\\([+-]?\\d{1,6},[+-]?\\d{1,6}\\)(,\\([+-]?\\d{1,6},[+-]?\\d{1,6}\\))*})"
//		{(1,2),(3,4)}
		
		Poly temp = new Poly(0,0);
		String[] str = s.split("[{}()]");
		
		if(str.length <= 0)
		{
			System.out.println("Wrong input: input cannot be empty!");
			System.exit(0);
		}
		else if(!str[0].equals("+") && !str[0].equals("-"))
		{
			oplist[0] = '+';
			op_num = 1;
			fs = 0;
		}
		
		for(i = 0;i < str.length;i++)
		{
			if(str[i].matches("[+-]?\\d{1,},[+-]?\\d{1,}\\b"))
			{
				String[] num = str[i].split(",");
				for(String x : num)
				{
					if(!x.isEmpty())
					{
						if(flag == 0)
						{
							try
							{
								coef = Integer.valueOf(x);
								flag = 1;
								if(coef > 999999)
								{	
									System.out.println("Wrong input: too big numbers!");
									System.exit(0);
								}
							}
							catch(NumberFormatException e)
							{
								System.out.println(e);
								System.exit(0);
							}
						}
						else
						{
							try
							{
								index = Integer.valueOf(x);
								flag = 0;
								if(index > 999999)
								{	
									System.out.println("Wrong input: too big numbers!");
									System.exit(0);
								}
							}
							catch(NumberFormatException e)
							{
								System.out.println(e);
								System.exit(0);
							}
						}
					}
				}
				if(index >= 0)
				{	
					temp = new Poly(coef,index);
					polylist[poly_num].add(temp);
					Nochange = 0;
				}
				else 
				{
					System.out.println("Wrong input!");
					System.exit(0);
				}
			} 
			else if(str[i].equals("+") || str[i].equals("-"))
			{
				if(str[i].equals("+"))
					oplist[op_num++] = '+';
				else 
					oplist[op_num++] = '-';
				
				if(fs == 0)
				{
					temp = new Poly(0,0);
					poly_num++;
					flag = 0;
				}
				else 
					fs = 0;
			}
			else if(!str[i].isEmpty() && !str[i].equals(","))
			{
				System.out.println("Wrong input!");
				System.exit(0);
			}
		}
		
		count = poly_num + 1;

		if(Nochange == 1)
		{
			System.out.println("Wrong input: input cannot be empty!");
			System.exit(0);
		}
	}
	
	public Poly compute()
	{
		Poly result = new Poly(0,0);
		int i = 0;
		for(i = 0;i < count;i++)
		{
			if(oplist[i] == ADD)
				result.add(polylist[i]);
			else if(oplist[i] == SUB)
				result.sub(polylist[i]);
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		String str;
		try
		{
			ComPoly cp = new ComPoly();
			str = cp.getString();
			cp.getPoly(str);
			Poly result = cp.compute();
		
			result.print();
		}
		catch (Exception e)
		{
			System.out.println("Wrong input!");
			System.exit(0);
		}
	}
}

class Poly
{
	private int term[];
	private int deg;

	public Poly(int c, int n)
	{
		int i = 0;
		term = new int[1000000];
		for(i = 0;i < 1000000; i++)
			term[i] = 0;
		term[n] = c;
		deg = n;
	}
	
	public int getDeg()
	{
		return deg;
	}
	
	public int getCoef(int n)
	{
		return term[n];
	}
	
	public void add(Poly a)
	{
		int i = 0;
		int newdeg = 0;
		int max = this.deg > a.deg ? this.deg : a.deg;
		for(i = 0; i <= max; i++)
		{
			this.term[i] += a.term[i];
			if(this.term[i] != 0 && i > newdeg)
				newdeg = i;
		}
		this.deg = newdeg;		
		return;
	}
	
	public void sub(Poly a)
	{
		int i = 0;
		int newdeg = 0;
		int max = this.deg > a.deg ? this.deg : a.deg;
		for(i = 0; i <= max; i++)
		{
			this.term[i] -= a.term[i];
			if(this.term[i] != 0 && i > newdeg)
				newdeg = i;
		}
		this.deg = newdeg;
		
		return;

	}
	
	public void print()
	{
		int i = 0;
		int isfirst = 1;
		for(i = 0; i <= deg; i++)
		{
			if(term[i] != 0 && deg != 0)
			{
				if(isfirst == 1 && i != deg)
				{
					System.out.printf("{(%d,%d),", term[i],i);
					isfirst = 0;
				}
				else if(isfirst == 1 && i == deg)
				{
					System.out.printf("(%d,%d)\n", term[i],i);
					isfirst = 0;
				}
				else if(isfirst != 1 && i != deg)
					System.out.printf("(%d,%d),", term[i],i);
				else 
					System.out.printf("(%d,%d)}\n", term[i],i);
			}
			else if(deg == 0 && term[0] == 0)
				System.out.println("{(0,0)}");
		}
	}
}