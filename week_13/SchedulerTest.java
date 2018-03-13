package week_13;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SchedulerTest {
	public Scheduler scheduler;
	
	@Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
	}

	@Test
	public void testScheduler() {
		if(scheduler.timer == null || scheduler.reqs == null)
			fail("Not yet implemented");
	}

	@Test
	public void testErrhandler() {
		scheduler.errhandler(0, "aaaaa");
		scheduler.errhandler(1, "bbbbb");
	}

	@Test
	public void testGetreqs() {
		Reqlist aReqlist = new Reqlist();
		scheduler.reqs = aReqlist;
		
		Reqlist rReqlist = scheduler.getreqs();
		if(rReqlist != aReqlist)
			fail("Not yet implemented");
	}

}
