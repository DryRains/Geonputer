package System;
import java.util.Scanner;
public class IODevice {
	private Memory memory;
	public IODevice() {
	
	}
	
	public void stanbyInturrupt() {
		Scanner scanner = new Scanner(System.in);
		while(true){
				String input = scanner.nextLine();
				if(input.equals("p")||input.equals("s")) {
					memory.IOBuffer.add(input);	
				}
			}
		}
	
	public void submitInput() {
		if(CPU.eState==CPU.EState.ePause) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("user input: ");
			int input = scanner.nextInt();
			scanner.nextLine();
			memory.IOIntegerBuffer.add(input);	
		}
	}
	

	public void finalize(){
		//this.scanner.close();
	}
	
	public void associate(Memory memory) {
		this.memory = memory;
	}
       
}
