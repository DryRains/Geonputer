package OS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Compiler.Compiler;
import System.CPU;
import System.IODevice;
import System.Memory;

public class Main {

	public static void main(String[] args) throws IOException {
		//OS - 본래OS는 메모리에 올려져야 함 ..!
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("1. Compile & Linking");
		System.out.println("2. Loading & Run Program");
		String input = inputReader.readLine().trim();
		
		if(input.equals("1")) {
			Compiler compiler = new Compiler();
			compiler.start();
			
			//컴파일된 결과물(컴파일한 파일이름, 오브젝트 코드를 링커에게 전달)
			String fileName = compiler.getFileName();
			String objectCode = compiler.getObjectCode();
			Linker linker = new Linker();
			//링커는 실행가능한 프로그램 형태로 만듬
			linker.link(fileName,objectCode);
		}
		else if(input.equals("2")){
			//메모리 안에 있는 OS가 CPU,Memory,IOD를 세팅하는 아이러니 -> 일단 넘어가기
			CPU cpu = new CPU();
			Memory memory = new Memory();
			IODevice ioDevice = new IODevice();
			Loader loader = new Loader(memory);
			//컨트롤,데이터 버스 놓는 과정
			cpu.associate(memory);
			cpu.associate(ioDevice);
			memory.associate(cpu.MAR, cpu.MBR);
			memory.associate(ioDevice);
			
			//실제 컴퓨터 구조와는 관계없는 association
			cpu.associate(loader);
			loader.start();
			System.out.println("<Inturrupt Mode Setting>\n1.NonBlocking Mode"
					+ "\n2.Blocking Mode");
			input = inputReader.readLine().trim();
			if(input.equals("1")) cpu.startNonBlockingMode();
			else if(input.equals("2")) cpu.startBlockingMode();
		}
		
	}

}
