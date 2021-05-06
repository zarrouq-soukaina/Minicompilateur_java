package interpreteur;
import java.util.Scanner;

public class Interpreter_core {

	public String text;
	public Memory m = new Memory();
	public Stack s = new Stack();
	public VIS v = new VIS();
	
	
	
	
	public Interpreter_core(String text) {
		super();
		this.text = text;
		this.initialize();
		this.execute();
	}

	public void initialize() {
		
		String[] lines = this.text.split("\n") ;
		
		for (	String line : lines	) {
			
			if (	line != ""	) {
				
				String[] words = line.split(" ");
				
				String op = words[0];
				
				String opd ="";
				
				if (words.length == 2) { opd = words[1]; }
				
				//System.out.println( op + ' ' + opd);
				
				if (	op.equals("load") 	){ 	this.v.add_inst( new Instruction( Operation.LOAD   , opd ) 	); }
				
				if (	op.equals("loadc")	){ 	this.v.add_inst( new Instruction( Operation.LOADC  , opd ) 	); }
				
				if ( 	op.equals("store")	){ 	this.v.add_inst( new Instruction( Operation.STORE  , opd ) 	); }
				
				if ( 	op.equals("jzero")	){ 	this.v.add_inst( new Instruction( Operation.JZERO  , opd ) 	); }
				
				if ( 	op.equals("jump") 	){ 	this.v.add_inst( new Instruction( Operation.JUMP   , opd )  ); }
				
				if ( 	op.equals("write")	){ 	this.v.add_inst( new Instruction( Operation.WRITE  , opd )  ); }
				
				if ( 	op.equals("writec") ){ 	this.v.add_inst( new Instruction( Operation.WRITEC , opd ) 	); }
				
				if ( 	op.equals("read") 	){	this.v.add_inst( new Instruction( Operation.READ   , opd ) 	); }
					
				if ( 	op.equals("add")	){ 	this.v.add_inst( new Instruction( Operation.ADD  ) 	); }
				
				if ( 	op.equals("sub") 	){ 	this.v.add_inst( new Instruction( Operation.SUB  ) 	); }
				
				if ( 	op.equals("mul") 	){ 	this.v.add_inst( new Instruction( Operation.MUL  ) 	); }
				
				if ( 	op.equals("div") 	){ 	this.v.add_inst( new Instruction( Operation.DIV 	)   ); }
				
				if ( 	op.equals("equal") 	){ 	this.v.add_inst( new Instruction( Operation.EQUAL) 	); }
				
				if ( 	op.equals("nequal") 	){ 	this.v.add_inst( new Instruction( Operation.NEQUAL) 	); }
				
				if ( 	op.equals("inf") 	){ 	this.v.add_inst( new Instruction( Operation.INF  ) 	); }
				
				if ( 	op.equals("infe") 	){ 	this.v.add_inst( new Instruction( Operation.INFE ) 	); }
				
				if ( 	op.equals("sup") 	){ 	this.v.add_inst( new Instruction( Operation.SUP  ) 	); }
				
				if ( 	op.equals("supe") 	){ 	this.v.add_inst( new Instruction( Operation.SUPE ) 	); }
				
				if ( 	op.equals("end") 	){ 	v.add_inst( new Instruction( Operation.END  ) 	); }
				
				
				
			}
			
			

			
		}
		
		
		
	}
	
	public void execute() {
		
		Instruction current_inst = null;
		
		int current_inst_index = 0;
		
		String val = null;
		Integer n1 = null;
		Integer n2 = null;
		
		while ( !this.v.get_inst(current_inst_index).op.equals(Operation.END) 	) {
			
			current_inst = this.v.get(current_inst_index);
			current_inst_index ++ ;
			
			
			switch  ( current_inst.op ) {
			
			case LOAD 	:
							val = m.get_m(	Integer.valueOf( current_inst.opd	) 	);
							s.add_str(val);
							break;
			
			case LOADC 	:
							val = current_inst.opd;
							s.add_str(val);
							break;
				
			case STORE 	:
							m.add_m( Integer.valueOf( current_inst.opd ), s.pop_str() );
							break;
				
			case JZERO 	:
							Integer jzero = Integer.valueOf(s.pop_str());
							if (jzero == 0 ) {	current_inst_index = Integer.valueOf( current_inst.opd ) ;}
				
			case JUMP 	:
							current_inst_index = Integer.valueOf( current_inst.opd );
							break;
				
			case WRITE 	:
							Integer adr = Integer.valueOf( current_inst.opd );
		                    System.out.println(	m.get_m(adr)	);
		                    break;

				
			case WRITEC :
                			System.out.println(	current_inst.opd );
                			break;
				
			case READ 	:
							Scanner sc = new Scanner(System.in);
							Integer number;
						
						    System.out.println("Please enter a Integer");
						    while (!sc.hasNextInt()) {
						        System.out.println("Please enter a valid Integer : ");
						        sc.next(); 
						    }
						    number = sc.nextInt();
						    System.out.println("----------------");
							m.add_m(Integer.valueOf( current_inst.opd ), number.toString());
							break;
				
			case ADD 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							Integer add = n1+n2;
							s.add_str(add.toString());
							break;
				
			case SUB 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							Integer sub = n1-n2;
							s.add_str(sub.toString());
							break;
	
			case MUL	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							Integer mul = n1-n2;
							s.add_str(mul.toString());
							break;
				
			case DIV 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							Integer div = n1-n2;
							s.add_str(div.toString());
							break;
	
				
			case EQUAL 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 == n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }
							
			case NEQUAL 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 != n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }
							
			case INF 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 < n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }	
			case INFE 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 <= n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }
			case SUP 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 > n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }
			case SUPE 	:
							n1 = Integer.valueOf(s.pop_str());
							n2 = Integer.valueOf(s.pop_str());
							if ( n1 >= n2 ) { s.add_str("1"); }
							else { s.add_str("0"); }
						
				
			}
			
		}
		
		
	}
	
	
}
