package week_13;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import week_13.Main.Enumkind;
import week_13.Main.Enumstate;

public class ALS_SchedulerTest {
	public ALS_Scheduler als_scheduler;
	public Elevator elevator;

	@Before
	public void setUp() throws Exception {
		als_scheduler = new ALS_Scheduler();
		elevator = new Elevator();
	}

	@Test
	public void testALS_Scheduler() {
		if (als_scheduler.reqs == null || als_scheduler.sdreqs == null || als_scheduler.timer == null)
			fail("Not yet implemented");
	}

	@Test
	public void testRepOK() {
		if (!als_scheduler.repOK())
			fail("Not yet implemented");

		als_scheduler.reqs = null;
		if (als_scheduler.repOK())
			fail("Not yet implemented");

		als_scheduler.timer = null;
		if (als_scheduler.repOK())
			fail("Not yet implemented");

		als_scheduler.sdreqs = null;
		if (als_scheduler.repOK())
			fail("Not yet implemented");
	}

	@Test
	public void testPickcheck() {
		Request main = new Request(Enumkind.FR, 8, Enumstate.UP, 0);
		elevator.changemain(main);
		elevator.state = Enumstate.UP;

		elevator.pos = 3;
		Request testrequest = new Request(Enumkind.FR, 4, Enumstate.UP, 1);
		boolean result = als_scheduler.pickcheck(elevator, testrequest);
		if (result == false)
			fail("Not yet implemented");

		elevator.pos = 4;
		testrequest = new Request(Enumkind.ER, 6, Enumstate.UP, 1);
		result = als_scheduler.pickcheck(elevator, testrequest);
		if (result == false)
			fail("Not yet implemented");

		main = new Request(Enumkind.FR, 2, Enumstate.UP, 0);
		elevator.changemain(main);
		elevator.state = Enumstate.DOWN;

		elevator.pos = 5;
		testrequest = new Request(Enumkind.FR, 4, Enumstate.DOWN, 1);
		result = als_scheduler.pickcheck(elevator, testrequest);
		if (result == false)
			fail("Not yet implemented");

		elevator.pos = 3;
		testrequest = new Request(Enumkind.ER, 2, Enumstate.DOWN, 1);
		result = als_scheduler.pickcheck(elevator, testrequest);
		if (result == false)
			fail("Not yet implemented");
	}

	@Test
	public void testSetmain() {
		Request main = new Request(Enumkind.FR, 8, Enumstate.UP, 15);
		elevator.changemain(main);
		elevator.state = Enumstate.UP;
		als_scheduler.timer.goes(15);

		Request r1 = new Request(Enumkind.FR, 4, Enumstate.UP, 20);
		Request r2 = new Request(Enumkind.FR, 5, Enumstate.UP, 19);
		Request r3 = new Request(Enumkind.FR, 9, Enumstate.DOWN, 20);
		Request r4 = new Request(Enumkind.FR, 1, Enumstate.UP, 20);

		als_scheduler.reqs.addterm(r3);
		als_scheduler.reqs.addterm(r4);

		als_scheduler.sdreqs.addterm(r1);
		als_scheduler.sdreqs.addterm(r2);

		als_scheduler.setmain(elevator);
		Request request = elevator.mainreq;
		if (!request.equales(main))
			fail("Not yet 1");

		elevator.mainreq = null;
		als_scheduler.setmain(elevator);
		request = elevator.mainreq;
		if (!request.equales(r2))
			fail("Not yet 2");

		while (als_scheduler.sdreqs.getsize() != 0)
			als_scheduler.sdreqs.remove(0);
		elevator.mainreq = null;
		als_scheduler.setmain(elevator);
		request = elevator.mainreq;
		if (!request.equales(r3))
			fail("Not yet implemented");

		while (als_scheduler.reqs.getsize() != 0)
			als_scheduler.reqs.remove(0);
		elevator.mainreq = null;
		als_scheduler.setmain(elevator);
		request = elevator.mainreq;
		if (request != null)
			fail("Not yet implemented");
	}

	@Test
	public void testSchedule() {
		Request main = new Request(Enumkind.FR, 10, Enumstate.UP, 15);
		elevator.changemain(main);
		elevator.state = Enumstate.UP;
		als_scheduler.timer.goes(18);
		elevator.pos = 1;

		Request r1 = new Request(Enumkind.FR, 6, Enumstate.UP, 19);
		Request r2 = new Request(Enumkind.FR, 6, Enumstate.UP, 20);
		Request r3 = new Request(Enumkind.FR, 10, Enumstate.UP, 17);
		Request r4 = new Request(Enumkind.FR, 1, Enumstate.UP, 20);

		als_scheduler.reqs.addterm(r3);
		als_scheduler.reqs.addterm(r1);
		als_scheduler.reqs.addterm(r2);
		als_scheduler.reqs.addterm(r4);

		als_scheduler.schedule(elevator);

		als_scheduler.timer.goes(1);
		elevator.pos = 3;
		als_scheduler.schedule(elevator);

		als_scheduler.timer.goes(1);
		elevator.pos = 5;
		als_scheduler.schedule(elevator);

		boolean f1 = false;
		for(int i = 0; i < als_scheduler.sdreqs.size; i++) {
			Request request = als_scheduler.sdreqs.get(i);
			if(request.equales(r1))
				f1 = true;
		}
		if(!f1)
			fail("Not yet implemented");

		als_scheduler.timer.goes(0.5);
		elevator.pos = 6;
		als_scheduler.schedule(elevator);

		als_scheduler.timer.goes(0.5);
		elevator.pos = 7;
		als_scheduler.schedule(elevator);

		boolean f2 = false;
		for(int i = 0; i < als_scheduler.reqs.size; i++) {
			Request request = als_scheduler.reqs.get(i);
			if(request.equales(r3))
				f2 = true;
		}
		if(f2)
			fail("Not yet implemented");
	}

	@Test
	public void testCommand() {
		Request main = new Request(Enumkind.FR, 10, Enumstate.UP, 15);
		elevator.changemain(main);
		elevator.state = Enumstate.UP;
		als_scheduler.timer.goes(18);
		elevator.pos = 1;

		Request r1 = new Request(Enumkind.FR, 6, Enumstate.UP, 19);
		Request r2 = new Request(Enumkind.FR, 6, Enumstate.UP, 20);
		Request r3 = new Request(Enumkind.ER, 10, Enumstate.UP, 17);
		Request r4 = new Request(Enumkind.FR, 1, Enumstate.UP, 20);

		als_scheduler.reqs.addterm(r3);
		als_scheduler.reqs.addterm(r1);
		als_scheduler.reqs.addterm(r2);
		als_scheduler.reqs.addterm(r4);

		als_scheduler.command(elevator);
		
		elevator.mainreq = null;
		als_scheduler.command(elevator);
	}

}
