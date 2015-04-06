import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;



public class Menu extends JFrame implements ActionListener,ChangeListener{
	private static final long serialVersionUID = 42L;
	DrawPanel draw,draw2;
	SimpleDraw main,main2;
	Status star;
	StampButton stpButton;
	PenSliderSize pss,pssE;
	JColorChooser colorchooser=new JColorChooser(Color.black);
	JButton penR =new JButton("round");
	JButton penS =new JButton("square");
	JSlider sliderP,sliderE;
	float penwidth=20.f,erazewidth=20.f;
	JFileChooser fileChooser;
	JTextField text;
	JPanel penColorPanel;
	int [] textArray ={8,10,12,14,16,18,20,22,24,26,28,36,48,72};
	String textfont="MS Gothic";
	int textsize=2;
	Color textcolor=Color.black;
	
	Menu (DrawPanel pl,SimpleDraw sd,Status st){
		draw=pl;
		main=sd;
		star=st;
		this.setTitle("Menu");
		
		Container cont= getContentPane();
		cont.add(makeMenu(),BorderLayout.CENTER);
		this.setVisible(true);

		this.setBounds(820,100,500,600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JTabbedPane makeMenu(){
		JTabbedPane tabbmenu= new JTabbedPane();
		
		JPanel penmenu=new JPanel();
		penMenu(penmenu);
		tabbmenu.addTab("pen",penmenu);
		
		JPanel erazermenu=new JPanel();
		erazerMenu(erazermenu);
		tabbmenu.addTab("erazer",erazermenu);
		
		JPanel stampmenu=new JPanel();
		stampMenu(stampmenu);
		tabbmenu.addTab("stamp",stampmenu);
		
		JPanel textmenu=new JPanel();
		textMenu(textmenu);
		tabbmenu.addTab("text",textmenu);
		
		return tabbmenu;
	}
	
	private JButton addButton(String name,String com){
		JButton bt=new JButton(name);
		bt.addActionListener(this);
		bt.setActionCommand(com);
		return bt;
	}
	
	public void penMenu(JPanel penmenu){
		penmenu.setLayout(new BoxLayout(penmenu,BoxLayout.Y_AXIS));
		
		JPanel panel1 =new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.X_AXIS));
		JButton pen=addButton("pen","pen");
		panel1.add(pen);
		penColorPanel=new JPanel();
		penColorPanel.setBackground(star.getPenColor());
		panel1.add(penColorPanel);
		penmenu.add(panel1);
		
		colorchooser.getSelectionModel().addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent evt) {
		  		Color color =colorchooser.getColor();
				star.setPenColor(color);
				main.setPenMode(0);
				penCC(color);
		        }
		      });
		penmenu.add(colorchooser);
				
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,5));
		sliderP= new JSlider(1,80,20);
		sliderP.setOrientation(SwingConstants.VERTICAL);
		sliderP.setMajorTickSpacing(10);
		sliderP.setPaintTicks(true);
		sliderP.addChangeListener(this);
		pss=new PenSliderSize(star,0);
		pss=pss.getPSS();

		panel2.add(new JLabel(""));
		panel2.add(sliderP);
		panel2.add(pss);
		
		JPanel panel3= new JPanel();
		panel3.setLayout(new GridLayout(4,1));
		penR.addActionListener(this);
		penS.addActionListener(this);
		panel3.add(new JLabel(""));
		panel3.add(penR);
		panel3.add(penS);
		panel2.add(panel3);
		panel2.add(new JLabel(""));
		penmenu.add(panel2);
	}
	public void penCC(Color co){
		penColorPanel.setBackground(co);
	}
	
	public void erazerMenu(JPanel erazermenu){
		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(400,200));
		panel2.setLayout(new GridLayout(1,3));
		
		sliderE= new JSlider(SwingConstants.VERTICAL,1,80,20);
		sliderE.addChangeListener(this);
		sliderE.setMajorTickSpacing(10);
		sliderE.setPaintTicks(true);
		panel2.add(sliderE);
		pssE=new PenSliderSize(star,1);
		pssE=pssE.getPSS();
		panel2.add(pssE);
		JButton eraze=addButton("erazer","eraze");
		panel2.add(eraze);
		
		erazermenu.add(panel2);
	}
	
	public void stampMenu(JPanel stpmenu){
		stpmenu.setLayout(new BoxLayout(stpmenu,BoxLayout.Y_AXIS));
		
		JPanel panel =new JPanel();
		panel.setLayout(new GridLayout(3,2));
		JButton stp3=new JButton(new ImageIcon("./img/rect.jpg"));
		stp3.setActionCommand("stp3");
		stp3.addActionListener(this);
		panel.add(stp3);
		JButton stp6=new JButton(new ImageIcon("./img/rect2.jpg"));
		stp6.setActionCommand("stp6");
		stp6.addActionListener(this);
		panel.add(stp6);
		JButton stp2=new JButton(new ImageIcon("./img/circle.jpg"));
		stp2.setActionCommand("stp2");
		stp2.addActionListener(this);
		panel.add(stp2);
		JButton stp5=new JButton(new ImageIcon("./img/circle2.jpg"));
		stp5.setActionCommand("stp5");
		stp5.addActionListener(this);
		panel.add(stp5);
		JButton stp1=new JButton(new ImageIcon("./img/pen.jpg"));
		stp1.setActionCommand("stp1");
		stp1.addActionListener(this);
		panel.add(stp1);
		JButton stp4=new JButton(new ImageIcon("./img/erazer.jpg"));
		stp4.setActionCommand("stp4");
		stp4.addActionListener(this);
		panel.add(stp4);
		stpmenu.add(panel);
		
		JPanel panel2=new JPanel();
		JLabel label=new JLabel("Read File?");
		JButton fbutton=new JButton("choose");
		fbutton.addActionListener(this);
		fbutton.setActionCommand("fbutton");
		panel2.add(label);
		panel2.add(fbutton);
		stpmenu.add(panel2);
		
		JPanel panel3=new JPanel();
		stpButton=new StampButton(main);
		panel3=stpButton.getPanel();
		stpmenu.add(panel3);
	}
	
	public void textMenu(JPanel textmenu){
		JPanel panel=new JPanel();
		panel.setPreferredSize(new Dimension(240,200));
		JLabel label1=new JLabel("テキストを入力:");
		text=new JTextField();
		JLabel label2=new JLabel("");
		JButton button=new JButton("ok");
		button.setActionCommand("textset");
		button.addActionListener(this);
		JLabel label3 =new JLabel("フォント:");
		JComboBox cb1=new JComboBox();
		cb1.addItem("MS Gothic");
		cb1.addItem("MS 明朝");
		cb1.addItem("Arial");
		cb1.addItem("Arial BOLD ITALIC");
		cb1.setSelectedIndex(0);
		cb1.setEnabled(true);
		cb1.setActionCommand("cb");
		cb1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb=(JComboBox)e.getSource();
				textfont=(String)cb.getSelectedItem();
			}
		});
		JLabel label5=new JLabel("サイズ:");
		JComboBox cb2=new JComboBox();
		int i;
		for(i=0;i<14;i++){
			cb2.addItem(textArray[i]);
		}
		cb2.setSelectedIndex(textsize);
		cb2.setEnabled(true);
		cb2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb=(JComboBox)e.getSource();
				textsize=cb.getSelectedIndex();
			}
		});
		JLabel label4 =new JLabel("文字色:");
		JButton coch=new JButton("ColorChooser");
		coch.setActionCommand("coch");
		coch.addActionListener(this);
		
		panel.setLayout(new GridLayout(5,2));
		panel.add(label1);
		panel.add(text);
		panel.add(label3);
		panel.add(cb1);
		panel.add(label5);
		panel.add(cb2);
		panel.add(label4);
		panel.add(coch);
		panel.add(label2);
		panel.add(button);
		textmenu.add(panel);	
	}
	
	public void actionPerformed(ActionEvent e){	
		if(e.getActionCommand()=="eraze"){
			main.setPenMode(10);
		}else if(e.getActionCommand()=="pen"){
			main.setPenMode(0);
		}else if(e.getSource()==penR){
			draw.setPenShape(BasicStroke.CAP_ROUND);
			pss.shapePSS(0);
		}else if(e.getSource()==penS){
			draw.setPenShape(BasicStroke.CAP_SQUARE);
			pss.shapePSS(1);
		}else if(e.getActionCommand()=="coch"){
			Color color=JColorChooser.showDialog(this,"choose a color",Color.black);
			if(color!=null)textcolor=color;
		}else if(e.getActionCommand()=="textset"){
			draw.textFont(new Font(textfont,Font.PLAIN,textArray[textsize]),textcolor);
			main.setText(text.getText());
			main.setPenMode(5);
		}else if(e.getActionCommand()=="stp2"){
			main.setPenMode(8);
		}else if(e.getActionCommand()=="stp3"){
			main.setPenMode(9);
		}else if(e.getActionCommand()=="stp5"){
			main.setPenMode(11);
		}else if(e.getActionCommand()=="stp6"){
			main.setPenMode(12);
		}else if(e.getActionCommand()=="stp1"){
			main.setStamp("./img/pen.jpg");
		}else if(e.getActionCommand()=="stp4"){
			main.setStamp("./img/erazer.jpg");
		}else if(e.getActionCommand()=="fbutton"){
			fileChooser =new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(main);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	BufferedImage pictureImage;
	    		try {
	    			pictureImage = ImageIO.read(fileChooser.getSelectedFile());
	    			stpButton.setImage((Image)pictureImage,fileChooser.getSelectedFile());
	    		} catch(Exception e2){
	    			JOptionPane.showMessageDialog(main, "Error: readingfile="+fileChooser.getSelectedFile().getName());
	    			return;
	    		}
	        }
		}
		
	}
	
	public void stateChanged(ChangeEvent e){			
		if((JSlider)e.getSource()==sliderP){
			penwidth=(float)sliderP.getValue();
			star.setPenWidth(penwidth);
			draw.setPen();
			pss.kousinPSS();
		}else if((JSlider)e.getSource()==sliderE){
			erazewidth=(float)sliderE.getValue();
			star.setErazeWidth(erazewidth);
			draw.setErazer();
			pssE.kousinPSS();
		}		
	}
	
	public void newDraw(DrawPanel dr,SimpleDraw sd){
//	draw2=draw;
	//	main2=main;
		draw=dr;
		main=sd;
	}
	public void chooseDraw(DrawPanel dr,SimpleDraw sd){
		if(draw!=dr){
			//draw2=draw;
			//main2=main;
			draw=dr;
			main=sd;
		}
	}
	
}
