package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * In this program, the analysis should detect a flow.
 * @author Martin Mohr
 * Test.TestProgram.main([Ljava/lang/String;)V
 */
public class TestProgram {

	public static void main(String[] args){
		try {
			FileInputStream fis = new FileInputStream("foo.txt");

			final FileOutputStream fos = new FileOutputStream("bar.txt");

			final byte[] buf = new byte[1000];
			int z = System.in.read();
			fis.read(buf); // source
			fos.write(buf); // sink
			System.out.println(z);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
