

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestOutcast {
	@Test
	public void testOutcast1(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getRealSyn(), UtilsTestWordNet.getRealHyp());
		
		Outcast outcast = new Outcast(wordNet);
		
		String[] nouns = "horse zebra cat bear table".split(" ");
		
		String res = outcast.outcast(nouns);
		
		assertEquals("table", res);
	}


	@Test
	public void testOutcast2(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getRealSyn(), UtilsTestWordNet.getRealHyp());
		
		Outcast outcast = new Outcast(wordNet);
		
		String[] nouns = "water soda bed orange_juice milk apple_juice tea coffee".split(" ");
		
		String res = outcast.outcast(nouns);
		
		assertEquals("bed", res);
	}


	@Test
	public void testOutcast3(){
		WordNet wordNet = new WordNet(UtilsTestWordNet.getRealSyn(), UtilsTestWordNet.getRealHyp());
		
		Outcast outcast = new Outcast(wordNet);
		
		String[] nouns = "apple pear peach banana lime lemon blueberry strawberry mango watermelon potato".split(" ");
		
		String res = outcast.outcast(nouns);
		
		assertEquals("potato", res);
	}
}
