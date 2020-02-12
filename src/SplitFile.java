import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SplitFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream file = new FileInputStream("/home/valentin/test.txt");
		byte[] fileContent = new byte[(int) file.getChannel().size()];
		file.read(fileContent);
		System.out.println(fileContent);
	}

}
