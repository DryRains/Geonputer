package OS;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import System.Memory;

public class Loader {
	public static final int OnePage = 2048;
	private Memory memory;
	
	public Loader(Memory memory) {
		this.memory = memory;
	}
	
	public void start() { //첫번째로 프로그램 올릴 때 작동
		String program = selectProgram();
		runProcess(program);
	}
	public String selectProgram(){
		File folder = new File("Disk_Program");
        String[] files = folder.list();
        // 파일 목록 출력
        System.out.println("---Program list---");
        for (int i=0; i<files.length; i++) {
            System.out.println(i+". "+files[i]);
        }
        System.out.print("Number of Program to run: ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        return files[index];
	}
	
	public void runProcess(String fileName) {
		//메모리(this)에 이미 올라와있는 프로그램인지 검사
		for(Process processToRun : memory) {
		if(processToRun.getProcessName().equals(fileName)) {
			memory.setCurrentProcess(processToRun);
			System.out.println("Existing Program. Call from Memory: "+memory.getCurrentProcess().getProcessName());
			return;
			}
		}
		//아닐 경우 새로 메모리에 올림
		//Loading
		File file = new File("Disk_Program/"+fileName);
		try {
			Scanner scanner = new Scanner(file);
			//ObjectCode 첫줄에는 Data Segment, CodeSegment의 사이즈가 명시되어있음
			String firstLine = scanner.nextLine();
			String[] firstLineValues = firstLine.split(" ");
			int DSSize = Integer.parseInt(firstLineValues[0]);
			int CSSize = Integer.parseInt(firstLineValues[1]);
			//해당 프로세스의 각 세그먼트 사이즈 정함
			int SSSize = (DSSize*10)+(CSSize*10);//임의로 시스템에서 DS와 CS사이즈에 비례에 할당한다고 가정
			int HSSize = 0;
			int pageSize = OnePage;
			//프로세스에게 내어줄 공간(페이지 용량)할당과정
			if((DSSize+CSSize+SSSize)<=OnePage-200){//200은 여유공간
			//힙 세그먼트는 나머지 세 세그먼트가 할당되고 남은 공간
			pageSize = OnePage;
			}
			else {
				pageSize = OnePage*2; //공간이 충분치 않다면 2개의 페이지 할당 -> 내 프로그램은 그리 크지 않으니 여기까지만
			}
			HSSize = pageSize - (DSSize+CSSize+SSSize);	
			int processNumber = memory.size();
			//계산한 공간만큼의 페이지를 부여하고 프로세스 메모리에 적재
			Process process = new Process(fileName,processNumber,DSSize,CSSize,SSSize,HSSize,pageSize);
			//사이즈 선언부 이후 두번째줄부터 machine instruction임 -> 한줄 한줄씩Code Segment에 세팅
			int index = process.CS;
			while(scanner.hasNext()) {
				//Load to CodeSegment - 2번째줄부터
				//앞에 '0x'를 제거
				int instruction = Integer.parseInt(scanner.nextLine().substring(2),16);
				process.segment[index] = instruction;
				index++;
			}
			scanner.close();
			memory.add(process);
			memory.setCurrentProcess(process);
			memory.getCurrentProcess().setProcessNumber(processNumber);
			System.out.println("New Program. Load into Memory : "+memory.getCurrentProcess().getProcessName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
