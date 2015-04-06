import java.awt.event.*;
import java.awt.*;
import java.text.NumberFormat;

import javax.swing.*;



public class Tool extends JFrame implements ActionListener {
	private static final long serialVersionUID = 42L;
	DrawPanel draw,draw2;
	SimpleDraw main,main2;
	JFormattedTextField width,height;
	Color bgcolor=Color.white;
	
	Tool(DrawPanel pl,SimpleDraw dr){
		draw=pl;
		main=dr;
		getContentPane().setLayout(new BorderLayout());
		
		JToolBar toolbar=new JToolBar();
		toolbar.setFloatable(true);
		getContentPane().add(toolbar,BorderLayout.NORTH);
		toolbar.setLayout(new BoxLayout(toolbar,BoxLayout.Y_AXIS));
		
		addIcon("./img/pen.jpg","pen",toolbar);
		addIcon("./img/erazer.jpg","eraze",toolbar);
		addIcon("./img/new.jpg","newfile",toolbar);
		addIcon("./img/cut.jpg","cut",toolbar);
		addIcon("./img/copy.jpg","copy",toolbar);		
		addIcon("./img/paste.jpg","paste",toolbar);
		addIcon("./img/spoit.jpg","spoit",toolbar);
		addIcon("./img/text.jpg","text",toolbar);
		addIcon("./img/baketsu.jpg","baketsu",toolbar);
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setTitle("Tool");
	    this.setBounds(0,100,90,450);
	    setVisible(true);		
	}
	
	public void addIcon(String file,String com,JToolBar tool){
		JButton item=new JButton(new ImageIcon(file));
		item.setActionCommand(com);
		item.addActionListener(this);
		tool.add(item);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand()=="eraze"){
			main.setPenMode(10);
		}else if(e.getActionCommand()=="pen"){
			main.setPenMode(0);
		}else if(e.getActionCommand()=="newfile"){			
				final JDialog dialog=new JDialog(main,"新規作成",true);
				Container dcont =dialog.getContentPane();
				
				JPanel pn1 =new JPanel();
				JLabel wtext =new JLabel("width :");
				JLabel htext =new JLabel("height:");
				JLabel ctext =new JLabel("背景色を選ぶ");
				NumberFormat nf =NumberFormat.getIntegerInstance();
				width =new JFormattedTextField(nf);
				width.setValue(600);
				height =new JFormattedTextField(nf);
				height.setValue(520);
				JButton cbutton=new JButton("ColorChooser");
				cbutton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						bgcolor=JColorChooser.showDialog(main, "choose Background color",bgcolor);
					}
				});
				pn1.setLayout(new GridLayout(3,2));
				pn1.add(wtext);
				pn1.add(width);
				pn1.add(htext);
				pn1.add(height);
				pn1.add(ctext);
				pn1.add(cbutton);
				dcont.add(pn1,BorderLayout.NORTH);
				
				JPanel pn2 =new JPanel();
				pn2.setLayout(new FlowLayout());
				JButton make=new JButton("ok");
				make.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						int wid=Integer.valueOf(width.getText());
						int hei=Integer.valueOf(height.getText());
						main.newCreate("NewFile",wid,hei,bgcolor);
						dialog.setVisible(false);
					}
				});
				JButton cancel=new JButton("cancel");
				cancel.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dialog.setVisible(false);
					}
				});
				pn2.add(make);
				pn2.add(cancel);
				dcont.add(pn2,BorderLayout.SOUTH);
				
				dialog.setBounds(120,120,230,150);
				dialog.setVisible(true);	
		}else if(e.getActionCommand()=="cut"){
			main.setPenMode(1);
		}else if(e.getActionCommand()=="copy"){
			main.setPenMode(2);
		}else if(e.getActionCommand()=="paste"){
			main.setPenMode(3);
		}else if(e.getActionCommand()=="spoit"){
			main.setPenMode(4);
		}else if(e.getActionCommand()=="text"){
			main.setPenMode(5);
		}else if(e.getActionCommand()=="baketsu"){
			main.setPenMode(7);
		}
	}
	
	public void newDraw(DrawPanel dr,SimpleDraw sd){
		draw2=draw;
		main2=main;
		draw=dr;
		main=sd;
	}
	public void chooseDraw(DrawPanel dr,SimpleDraw sd){
		if(draw!=dr){
			draw2=draw;
			draw=dr;
			main2=main;
			main=sd;
		}
	}
}
