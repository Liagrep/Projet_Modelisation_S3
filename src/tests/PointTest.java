package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import affichages.Point;

public class PointTest {
	
	@Test
	public void testAdd(){
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(3, 2, 1);
		Point p3 = new Point(1.5,1.5,1.5);
		assertEquals(Point.add(p1, p2).toString(), new Point(4,4,4).toString());
		assertEquals(Point.add(p1, p3).toString(), new Point(2.5,3.5,4.5).toString());
	}
	@Test
	public void testEqualsCoord(){
		Point p1 = new Point(1, 2, 3);
		Point p2 = new Point(0, 3, 2);
		Point p3 = new Point(1, 2, 3);
		assertEquals(p1.getX(), p3.getX(),0.0001);
		assertEquals(p1.getY(), p3.getY(),0.0001);
		assertEquals(p1.getZ(), p3.getZ(),0.0001);
		assertNotEquals(p1.getX(), p2.getX(),0.0001);
		assertNotEquals(p1.getY(), p2.getY(),0.0001);
		assertNotEquals(p1.getZ(), p2.getZ(),0.0001);
	}
}
