package OS;

import java.io.FileWriter;
import java.io.IOException;

public class Linker {
	
	public void link(String fileName, String objectCode) {
		//컴파일러가 번역한 기계어 코드(objectCode)를 실행가능한 파일로 만드는 작업
		try {
			FileWriter writer = new FileWriter("Disk_Program/"+fileName);
			writer.write(objectCode);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
