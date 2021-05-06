package interpreteur;

import java.io.*;
import java.util.Scanner;


public class Interpreter {
	
	String File_Path;
	String script = "";
	Interpreter_core I;
	
	
	
	public Interpreter(String file_Path) throws FileNotFoundException {
		super();
		this.File_Path = file_Path;
		this.interpret();
		
	}



	public String get_script() throws FileNotFoundException {
		
		Scanner s = new Scanner	(	new DataInputStream	(	new FileInputStream	(	this.File_Path	)	)	);
		String script = "";
		
		while( s.hasNext() ) {
			
			this.script += s.nextLine() + "\n" ;
		}
		
		return this.script ;
	}
	
	public void interpret() throws FileNotFoundException {
		
		I = new Interpreter_core(this.get_script());
		
	}
	
	
	

}