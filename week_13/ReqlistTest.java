package week_13;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import week_13.Main.Enumkind;
import week_13.Main.Enumstate;

public class ReqlistTest {
	public Reqlist reqlist;

	@Before
	public void setUp() throws Exception {
		reqlist = new Reqlist();
	}

	@Test
	public void testReqlist() {
		if (reqlist == null || reqlist.list == null || reqlist.size != 0)
			fail("Not yet implemented");
	}

	@Test
	public void testreOK() {
		if (!reqlist.repOK())
			fail("Not yet implemented");

		reqlist.size = reqlist.list.size() + 1;
		if (!reqlist.repOK())
			fail("Not yet implemented");

	}

	@Test
	public void testGetsize() {
		Request re = new Request();
		reqlist.addterm(re);
		if (reqlist.getsize() != 1)
			fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		Request re = new Request();
		reqlist.addterm(re);

		if (reqlist.get(0) != re)
			fail("Not yet implemented");
	}

	@Test
	public void testAddterm() {
		Request re = new Request();
		reqlist.addterm(re);
		Request request = reqlist.list.get(0);
		if (!re.equales(request))
			fail("Not yet implemented");
	}

	@Test
	public void testRemoveRequest() {
		Request re = new Request();
		reqlist.addterm(re);
		Request rs = new Request(Enumkind.FR, 2, Enumstate.DOWN, 25);
		reqlist.addterm(rs);
		reqlist.remove(re);

		for(int i = 0; i < reqlist.list.size(); i++) {
			if (reqlist.list.get(i).equales(re))
				fail("Not yet implemented");
		}
	}

	@Test
	public void testRemoveInt() {
		Request re = new Request();
		reqlist.addterm(re);
		Request rs = new Request(Enumkind.FR, 2, Enumstate.DOWN, 25);
		reqlist.addterm(rs);
		reqlist.remove(0);

		for(int i = 0; i < reqlist.list.size(); i++) {
			if (reqlist.list.get(i).equales(re))
				fail("Not yet implemented");
		}
	}

}
