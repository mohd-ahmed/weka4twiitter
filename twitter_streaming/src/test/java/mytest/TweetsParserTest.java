package mytest;

import static org.junit.Assert.*;
import mohd.qucs.TweetsParser;

import org.junit.Test;

public class TweetsParserTest {

	@Test
	public void test() {
		TweetsParser tp = new TweetsParser();
		String sss="@Life_of_Nat I just always ended up trading for espeon =\\";
		System.out.println( sss     );
        System.out.println( tp.parse(sss)   );
		String ssss="Took yo nigga bout a week agoooo \\?/ /?/ ? | | | \\ / \\ / \\ / \\";
         System.out.println( ssss     );
         System.out.println( tp.parse(ssss)   );
		String s="The rain is falling #INotFeelingToSleep :-\\";
		s=tp.parse(s);
		String str =tp.parse("@eetorres13 MY SISTER TRIED TO FLIP HER DQ BLIZZARD IM CRYING ?????? http //t.co/rS1DdZYUDK ???????");
		System.out.println(str);
		System.out.println(s);
	}

}
