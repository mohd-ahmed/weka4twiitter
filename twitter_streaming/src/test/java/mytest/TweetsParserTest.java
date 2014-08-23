package mytest;

import static org.junit.Assert.*;
import mohd.qucs.TweetsParser;

import org.junit.Test;

public class TweetsParserTest {

	@Test
	public void test() {
		TweetsParser tp = new TweetsParser();
		
		String s="“@loljulianaaa “@_vrxox @loljulianaaa ur hottie” get it all from u”????? ========== ok.";
		System.out.println( s    );
        System.out.println( ": "+tp.parse(s)   );
		
	}

}
