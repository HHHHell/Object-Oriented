package week_02_1;

class Timer
{
	private double t;
	
	Timer()
	{
		t = 0;
	}
	
	double gettime()
	{
		return t;
	}
	
	void goes(double x)
	{
		t = t + x;
	}
}
