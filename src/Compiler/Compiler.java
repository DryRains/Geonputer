package Compiler;
import java.io.File;
import java.util.Scanner;

public class Compiler {
	private Lexer lex;
	private Parser parser;
	private String objectCode;//컴파일된 결과물 - Linker에게 전달
	
	private String filename;
	private Scanner scanner;
	public Compiler() {
	}
	public void start() {
		this.scanner = new Scanner(System.in);
		filename = selectSourceCode();
		
		initialize();
		run();
		finalize();
	}
	
	public String selectSourceCode(){
		File folder = new File("Disk_SourceCode");
        String[] files = folder.list();
        // 파일 목록 출력
        System.out.println("---SourceCode list---");
        for (int i=0; i<files.length; i++) {
            System.out.println(i+". "+files[i]);
        }
        System.out.print("Number of SourceCode to compile: ");
        int index = scanner.nextInt();
        return files[index];
	}
	
	public void initialize() {
		lex = new Lexer();//lexical analyzer
		lex.initialize("Disk_SourceCode/"+filename);
		parser = new Parser();
		
	}
	
	public void run() {
		this.objectCode = parser.parse(this.lex,this.filename);
	}
	
	public void finalize() {
		lex.finalize();
		this.scanner.close();
	}
	
	public String getObjectCode() {
		return this.objectCode;
	}
	
	public String getFileName() {
		return this.filename;
	}

}
