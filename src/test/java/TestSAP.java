

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;


public class TestSAP {

	@Test
	public void testLengthSingleNormal(){
		Digraph digraph = createDigraph2();
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(1, 5);
		int lengthSap = sap.length(1, 5);
		
		assertEquals(0, anc);
		assertEquals(2, lengthSap);
	}

	@Test
	public void testLengthMultipleNormal(){
		Digraph digraph = createDigraph2();
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(Arrays.asList(1, 2), Arrays.asList(3, 4));
		int lengthSap = sap.length(Arrays.asList(1, 2), Arrays.asList(3, 4));
		
		assertEquals(3, anc);
		assertEquals(1, lengthSap);
	}

	@Test
	public void testLengthMultipleIntersect(){
		Digraph digraph = createDigraph2();
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(Arrays.asList(1, 3), Arrays.asList(3, 4));
		int lengthSap = sap.length(Arrays.asList(1, 3), Arrays.asList(3, 4));
		
		assertEquals(3, anc);
		assertEquals(0, lengthSap);
	}

	@Test
	public void testDigrapg1SingleNormal(){
		Digraph digraph = createDigraph1();
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(3, 11);
		int lengthSap = sap.length(3, 11);
		
		assertEquals(1, anc);
		assertEquals(4, lengthSap);
	}

	@Test
	public void testDigrapg1MultipleNormal(){
		Digraph digraph = createDigraph1();
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(Arrays.asList(9), Arrays.asList(7, 12));
		int lengthSap = sap.length(Arrays.asList(9), Arrays.asList(7, 12));
		
		assertEquals(5, anc);
		assertEquals(3, lengthSap);
	}

	@Test
	public void testDigraphNoAnc(){
		Digraph digraph = new Digraph(6);
		digraph.addEdge(1, 0);
		digraph.addEdge(1, 2);
		digraph.addEdge(2, 3);
		digraph.addEdge(4, 5);
		
		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(1, 5);
		int lengthSap = sap.length(1, 5);
		
		assertEquals(-1, anc);
		assertEquals(-1, lengthSap);
	}

	@Test
	public void testDigraphCycle(){
		Digraph digraph = new Digraph(6);
		digraph.addEdge(1, 2);
		digraph.addEdge(2, 3);
		digraph.addEdge(3, 4);
		digraph.addEdge(4, 5);
		digraph.addEdge(5, 0);
		digraph.addEdge(0, 1);

		SAP sap = new SAP(digraph);
		int anc = sap.ancestor(1, 5);
		int lengthSap = sap.length(1, 5);
		
		assertEquals(1, anc);
		assertEquals(2, lengthSap);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testDigraphOutOfBound(){
		Digraph digraph = new Digraph(6);
		SAP sap = new SAP(digraph);

		sap.ancestor(1, 7);
	}
	
	
	private Digraph createDigraph1() {
		Digraph digraph = new Digraph(13);
		digraph.addEdge(1, 0);
		digraph.addEdge(2, 0);
		digraph.addEdge(3, 1);
		digraph.addEdge(4, 1);
		digraph.addEdge(5, 1);
		digraph.addEdge(6, 1);
		digraph.addEdge(7, 3);
		digraph.addEdge(8, 3);
		digraph.addEdge(9, 5);
		digraph.addEdge(10, 5);
		digraph.addEdge(11, 10);
		digraph.addEdge(12, 10);
		return digraph;
	}

	
	private Digraph createDigraph2() {
		Digraph digraph = new Digraph(6);
		digraph.addEdge(1, 0);
		digraph.addEdge(1, 2);
		digraph.addEdge(2, 3);
		digraph.addEdge(3, 4);
		digraph.addEdge(4, 5);
		digraph.addEdge(5, 0);
		return digraph;
	}
	
	@Test
	public void testCheckTest1(){
		String file = UtilsTestWordNet.getFullFileName("digraph-wordnet.txt");
		In in = null;
		
		try{
			in = new In(file);
			Digraph digraph = new Digraph(in);
			SAP sap = new SAP(digraph);
			
			int length = sap.length(72894, 12947);
			
			assertEquals(15, length);
		}finally{
			in.close();
		}
	}

	@Test
	public void testCheckTest5(){
		String file = UtilsTestWordNet.getFullFileName("digraph1.txt");
		In in = null;
		
		try{
			in = new In(file);
			Digraph digraph = new Digraph(in);
			SAP sap = new SAP(digraph);
			
			int length = sap.length(1, 4);
			
			assertEquals(1, length);
		}finally{
			in.close();
		}
	}
}
