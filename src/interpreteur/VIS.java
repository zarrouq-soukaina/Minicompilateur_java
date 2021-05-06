package interpreteur;
import java.util.ArrayList;


public class VIS extends ArrayList<Instruction> {
	
	public void add_inst(Instruction inst) {
		this.add(inst);
	}
	
	public Instruction get_inst(int index) {
		return this.get(index);
	}
}