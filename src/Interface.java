import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.AnalyseurLexical;
import com.AnalyseurSyntaxique;
import com.SemantiqueException;
import com.SyntaxeException;



import java.io.*;
import java.awt.event.*;
import java.awt.*;


public class Interface extends JFrame{
	JPanel p=new JPanel();
	String text="";
	JLabel label;
	JTextArea textfield;
	 
	
	private JMenuBar menuBar=new JMenuBar();
	private JMenu menu1=new JMenu("Fichier");
	private JMenu menu2=new JMenu("Editer");
	private JMenu menu3=new JMenu("Compile");
	private JMenu menu4=new JMenu("Run");
	private JMenu menu5=new JMenu("Mode");
    private JMenuItem menuItem1=new JMenuItem("Nouveau");
	private JMenuItem menuItem2=new JMenuItem("Ouvrir");
	private JMenuItem menuItem3=new JMenuItem("Enregistrer");
	private JMenuItem menuItem4=new JMenuItem("Copier");
	private JMenuItem menuItem5=new JMenuItem("Couper");
	private JMenuItem menuItem6=new JMenuItem("Coller");
	private JMenuItem menuItem7=new JMenuItem("Sélectionner Tout");

	
    
	
	
	
public Interface() {
	//ajout menu 
	setLayout(new FlowLayout());
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);
		menuBar.add(menu4);
		menuBar.add(menu5);

		menu1.add(menuItem1);
		setJMenuBar(menuBar);
		menu1.add(menuItem2);
		setJMenuBar(menuBar);
		menu1.add(menuItem3);
		setJMenuBar(menuBar);
		menu2.add(menuItem4);
		setJMenuBar(menuBar);
		menu2.add(menuItem5);
		setJMenuBar(menuBar);
		menu2.add(menuItem6);
		setJMenuBar(menuBar);
		menu2.add(menuItem7);
		setJMenuBar(menuBar);
		label = new JLabel("");
		add(label);
		
		textfield = new JTextArea(100,100);

		addMenuItem(menu5, "Dark", Color.BLACK);
	 	addMenuItem(menu5, "White", Color.WHITE);
	 	addMenuItem(menu5, "Blue", Color.BLUE);
	 	menu5.setMnemonic( 'M' );
		nouveau nv = new nouveau();
		menuItem1.setIcon( new ImageIcon( "src/add.png" ) );
		menu1.setMnemonic( 'F' );
		menuItem2.setMnemonic( 'N' );
		menuItem1.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK) );
		menuItem1.addActionListener(nv);
		

		ovr v = new ovr();
		menuItem2.setIcon( new ImageIcon( "src/icons.png" ) );
        menuItem2.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK) );
		menuItem2.addActionListener(v);
		
		enregistrer s = new enregistrer();
		menuItem3.setIcon( new ImageIcon( "src/save.png" ) );
        menuItem3.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK) );
		menuItem3.addActionListener(s);
		
		copier cp = new copier();
		menuItem4.setIcon( new ImageIcon( "src/copy.png" ) );
		menu2.setMnemonic('E');
        menuItem4.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK) );
		menuItem4.addActionListener(cp);
		
		couper coup = new couper();
		menuItem5.setIcon( new ImageIcon( "src/cut.png" ) );
        menuItem5.setMnemonic( 'X' );
        menuItem5.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK) );
		menuItem5.addActionListener(coup);
		
		
		coller cl = new coller();
		menuItem6.setIcon( new ImageIcon( "src/paste.png" ) );
        menuItem6.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK) );
		menuItem6.addActionListener(cl);
		
		select se = new select();
		menuItem7.setIcon( new ImageIcon( "src/checked.png" ) );
        menuItem7.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK) );
		menuItem7.addActionListener(se);
		
		compiler comp=new compiler();
		menu3.setMnemonic( 'C' );
	    menu3.addActionListener(comp);
	    menu4.setMnemonic( 'R' );
	    
	    
		
		label.setForeground(Color.DARK_GRAY);
		textfield.setBackground(Color.WHITE);
		
	setTitle("Compilateur");
	setSize(1200,1400);
	
	
	setVisible(true);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
private void addMenuItem(JMenu menu, String name, final Color c) {
	JMenuItem item = new JMenuItem(name);
	item.addActionListener(new ActionListener () {
		public void actionPerformed(ActionEvent event) {
		    // Changement de la couleur du fond
		    getContentPane().setBackground(c);
		    // Actualisation de la fenêtre
		    repaint();
		}
	    });
	// Ajout de l'entrée dans le menu
	menu.add(item);
    }	
	
	
	
	

	
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public class compiler implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		try {
			AnalyseurLexical al = new AnalyseurLexical(textfield.getText());
            String cond = "";
            do{
                cond = al.uniteSuivante();
                System.out.println("<" + AnalyseurLexical.UNITECOURANT + " , " + cond + ">");
            }while (!cond.equals(AnalyseurLexical.ENDFILE));


            AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();
            analyseurSyntaxique.runAnalyseSyntaxique();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SyntaxeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SemantiqueException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
  
public class nouveau implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		label.setText("Code Source");
		p.add(textfield);
		
		JScrollPane scrollableText = new JScrollPane(p);   
        scrollableText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
  
        setPreferredSize(new Dimension(50, 50));
        add(scrollableText,BorderLayout.CENTER);  
        
	}
}
 private void ouvrir( File ov){
	textfield.setText("");
	BufferedReader IN = null;
	String ligne = null;
	try{
		IN = new BufferedReader(new FileReader(ov));
	    while((ligne=IN.readLine())!=null){
	    	textfield.append(ligne+"\n");
	    }
	    IN.close();
	}
	catch(Exception ex){
		textfield.setText(""+ex);
	}
}

public class ovr implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		JFileChooser o= new JFileChooser();
		o.setAcceptAllFileFilterUsed(true);
		o.setCurrentDirectory(new File("C:\\Bureau"));
	    int a =o.showOpenDialog(Interface.this);
		if (a ==JFileChooser.APPROVE_OPTION){
			ouvrir(o.getSelectedFile());
		}
	}

	

}
public class enregistrer implements ActionListener{
	
	private PrintWriter wrt;
	public void actionPerformed(ActionEvent e) {
		JFileChooser save= new JFileChooser();
		save.setAcceptAllFileFilterUsed(true);
		save.setCurrentDirectory(new File("C:\\Documents and Settings\\Administrateur\\Bureau"));
		int a=save.showSaveDialog(Interface.this);
		if ( a==save.APPROVE_OPTION){
			try{
				wrt=new PrintWriter(new FileWriter(save.getSelectedFile().getCanonicalPath()));
				wrt.println(textfield.getText());
				wrt.close();
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(null," Impossible d'enregistrer dans ce fichier  ! "," Message de d'Erreur ",JOptionPane.ERROR);
			}	
		}
		else if(a==1){
			//System.exit(0);
		}
	}
}
public class select implements ActionListener{
	public void actionPerformed(ActionEvent e) {
	textfield.selectAll();
	}
}
public class copier implements ActionListener{
	public void actionPerformed(ActionEvent e) {
	textfield.copy();
	}	
}

public class coller implements ActionListener{
	public void actionPerformed(ActionEvent e) {
	textfield.paste();
	}
}
public class couper implements ActionListener{
	public void actionPerformed(ActionEvent e) {
	textfield.cut();
	}

}


	
	
	
	
	
	
	
	
	
	
	
	
public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}catch(Exception e){
			e.printStackTrace();
		}
		Interface ig =new Interface();
	    Image icon = Toolkit.getDefaultToolkit().getImage("src/compiler.png");  
	    ig.setIconImage(icon);  
		ig.setVisible(true);
}}
