package interpreteur;
import java.util.HashMap;

public class Memory extends HashMap <Integer,String>{
	
	public void add_m(Integer adr,String val) {
		
		this.put(adr, val);
		
	}
	
	
	public String get_m(Integer adr) {
		
		return this.get(adr);
		
	}
}