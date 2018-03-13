package week_13;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import week_13.Main.Enumkind;
import week_13.Main.Enumstate;

public class ElevatorTest {
	public static Elevator elevator = new Elevator();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testElevator() {
		boolean re = elevator.repOK();
		if (!re)
			fail("Not yet implemented");
		elevator.pos = -1;
		re = elevator.repOK();
		if (re)
			fail("Not yet implemented");
		elevator.pos = 1;
	}

	@Test
	public void testRepOK() {
		if (elevator.getpos() < 0 || elevator.getstate() == null)
			fail("Not yet implemented");
	}

	@Test
	public void testGetstate() {
		Enumstate ss = elevator.getstate();
		if (ss == null)
			fail("Not yet implemented");
	}

	@Test
	public void testGetmainreq() {
		Request request = new Request(Enumkind.FR, 5, null, 25000);
		elevator.changemain(request);
		Request main = elevator.getmainreq();
		if (main == null)
			fail("Not yet implemented");
	}

	@Test
	public void testGetpos() {
		int p = elevator.getpos();
		if (p < 0)
			fail("Not yet implemented");
	}

	@Test
	public void testChangemain() {
		Request request = new Request(Enumkind.FR, 5, null, 25000);
		int po = elevator.getpos();
		elevator.changemain(request);
		Request main = elevator.mainreq;
		if (!main.equales(request))
			fail("Not yet implemented");
		else {
			if (po > 5 && !elevator.getstate().equals(Enumstate.DOWN))
				fail("State error!");
			if (po < 5 && !elevator.getstate().equals(Enumstate.UP))
				fail("State error!");
			if (po == 5 && !elevator.getstate().equals(Enumstate.STILL))
				fail("State error!");
		}

		request = new Request(Enumkind.FR, 2, null, 25000);
		po = elevator.getpos();
		elevator.changemain(request);
		main = elevator.mainreq;
		if (!main.equales(request))
			fail("Not yet implemented");
		else {
			if (po > 2 && !elevator.getstate().equals(Enumstate.DOWN))
				fail("State error!");
			if (po < 2 && !elevator.getstate().equals(Enumstate.UP))
				fail("State error!");
			if (po == 2 && !elevator.getstate().equals(Enumstate.STILL))
				fail("State error!");
		}

		po = elevator.getpos();
		request = new Request(Enumkind.FR, po, null, 25000);
		elevator.changemain(request);
		main = elevator.mainreq;
		if (!main.equales(request))
			fail("Not yet implemented");
		else if (!elevator.getstate().equals(Enumstate.STILL))
			fail("State error!");
	}


	@Test
	public void testResetstate() {
		elevator.resetstate();
		if (elevator.getstate() != null)
			fail("Not yet implemented");
	}

	@Test
	public void testResetmain() {
		elevator.resetmain();
		if (elevator.getmainreq() != null)
			fail("Not yet implemented");
	}

	@Test
	public void testMoveup() {
		int pos = elevator.getpos();
		elevator.moveup();
		if (elevator.getpos() != pos + 1 || elevator.getstate() != Enumstate.UP)
			fail("Not yet implemented");
	}

	@Test
	public void testMovedown() {
		elevator.moveup();
		int pos = elevator.getpos();
		elevator.movedown();
		if (elevator.getpos() != pos - 1 || elevator.getstate() != Enumstate.DOWN)
			fail("Not yet implemented");
	}

	@Test
	public void testToStringRequestDouble() {
		elevator.state = Enumstate.STILL;
		elevator.pos = 2;
		Request request = new Request(Enumkind.FR, 2, Enumstate.DOWN, 25);
		String s1 = elevator.toString(request, 25.5);
		System.out.println(s1);

		elevator.state = Enumstate.UP;
		elevator.pos = 7;
		request = new Request(Enumkind.ER, 7, Enumstate.UP, 30);
		String s2 = elevator.toString(request, 35.5);
		System.out.println(s2);
		
		elevator.state = Enumstate.STILL;
		elevator.pos = 5;
		request = new Request(Enumkind.FR, 5, Enumstate.UP, 40);
		String s3 = elevator.toString(request, 45);
		System.out.println(s3);
		
		elevator.state = Enumstate.UP;
		elevator.pos = 4;
		request = new Request(Enumkind.ER, 4, Enumstate.UP, 50);
		String s4 = elevator.toString(request, 55);
		System.out.println(s4);
	}

}
