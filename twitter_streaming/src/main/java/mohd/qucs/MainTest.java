package mohd.qucs;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MainTest {

	@Test
	public void test() {
		try {
			Main.setLogin();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QueryManger queryManager = new QueryManger();
	
	Main.init(queryManager);
	}

}
