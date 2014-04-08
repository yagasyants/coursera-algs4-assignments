

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestWordNet {
	
	@Test
	public void testInitAndNouns(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getHyp1());
		
		Iterable<String> nouns = wordNet.nouns();
		List<String> listStr = toList(nouns);
		
		assertTrue(listStr.contains("event"));
	}
	
	@Test
	public void testIsNoun(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getHyp1());
		
		assertTrue(wordNet.isNoun("event"));
		assertFalse(wordNet.isNoun("fhdshkjfdl"));
	}
	
	private List<String> toList(Iterable<String> iterable){
		List<String> list = new ArrayList<>();
		for(String str : iterable){
			list.add(str);
		}
		return list;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyNoCycles(){
		new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getFullFileName("cycle_hyp_1.txt"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyRooted(){
		new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getFullFileName("not_root_dag_hyp_1.txt"));
	}
	
	@Test
	public void testVerifyRealFiles(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getRealSyn(), UtilsTestWordNet.getRealHyp());
		
		Iterable<String> nouns = wordNet.nouns();
		List<String> listStr = toList(nouns);
		
		assertTrue(listStr.contains("Alcea"));
	}
	
	@Test
	public void testSapNormal(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getHyp1());
		
		String sap = wordNet.sap("occurrence", "miracle");
		
		assertEquals("event", sap);
	}
	
	@Test
	public void testDistanceNormal(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getHyp1());
		
		int dist = wordNet.distance("occurrence", "miracle");
		
		assertEquals(2, dist);
	}
	
	@Test
	public void testDistanceSame(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getSyn1(), UtilsTestWordNet.getHyp1());
		
		int dist = wordNet.distance("occurrence", "occurrence");
		
		assertEquals(0, dist); 
	}
	

}
