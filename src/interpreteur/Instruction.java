package interpreteur;
public class Instruction {
	
	public Operation op;
	public String opd;
	
	public Instruction(Operation op, String opd) {
		super();
		this.op = op;
		this.opd = opd;
	}

	public Instruction(Operation op) {
		super();
		this.op = op;
	}

	public Instruction() {
		super();
	}}
	