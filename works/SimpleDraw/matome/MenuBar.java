import java.awt.event.*;
import java.awt.*;
import java.text.NumberFormat;

import javax.swing.*;


public class MenuBar implements ActionListener {
	SimpleDraw main;
	DrawPanel draw;
	Menu menu;
	JMenuBar menubar;
	Status star;
	JFormattedTextField width;
	JFormattedTextField height;
	Color bgcolor=Color.white;
	JFileChooser fileChooser;
	
	MenuBar(SimpleDraw sd,DrawPanel dr,Menu mn,Status st){
		main=sd;
		draw=dr;
		menu=mn;
		star=st;
		menubar =new JMenuBar();
		
		JMenu file = new JMenu("ファイル");
		addItem("新規作成",file,"NewFile");
		addItem("開く",file,"Open");
		addItem("保存",file,"Save");
		menubar.add(file);
		
		JMenu pen =new JMenu("ペン");
		JMenu size=addMenu("ペンの太さ",pen);
		addItem("1px",size,"1px");
		addItem("10px",size,"10px");
		addItem("20px",size,"20px");
		addItem("40px",size,"40px");
		JMenu color=addMenu("カラー",pen);
		addItem("red",color,"red");
		addItem("blue",color,"blue");
		addItem("green",color,"green");
		addItem("ColorChooserから選ぶ",color,"ColorChooser");
		addItem("スポイト",color,"spoit");
		addItem("消しゴム",pen,"erazer");
		menubar.add(pen);
		
		JMenu edit =new JMenu("編集");
		addItem("戻る",edit,"redo");
		addItem("進む",edit,"goto");
		addItem("カット",edit,"cut");
		addItem("コピー",edit,"copy");
		addItem("ペースト",edit,"paste");
		addItem("背景色を変える",edit,"bgchange");
		addItem("全部消す",edit,"allclear");
		menubar.add(edit);	
		
		JMenu zoom =new JMenu("拡大");
		addItem("標準に戻す",zoom,"zoom1");
		addItem("2倍",zoom,"zoom2");
		addItem("4倍",zoom,"zoom4");
		addItem("8倍",zoom,"zoom8");
		menubar.add(zoom);
		
		main.setJMenuBar(menubar);
	}
	public JMenu addMenu(String name,JMenu menu){
		JMenu item =new JMenu(name);
		//item.addActionListener(this);
		//item.setActionCommand(name);
		menu.add(item);
		return item;
	}
	public void addItem(String name,JMenu menu,String com){
		JMenuItem item =new JMenuItem(name);
		item.addActionListener(this);
		item.setActionCommand(com);
		menu.add(item);
	}
	
	private void newFile(){
		final JDialog dialog=new JDialog(main,"新規作成",true);
		Container dcont =dialog.getContentPane();
		
		JPanel pn1 =new JPanel();
		JLabel wtext =new JLabel("width :");
		JLabel htext =new JLabel("height:");
		JLabel ctext =new JLabel("背景色を選ぶ");
		NumberFormat nf =NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
		width =new JFormattedTextField(nf);
		width.setValue(600);
		height =new JFormattedTextField(nf);
		height.setValue(520);
		JButton cbutton=new JButton("ColorChooser");
		cbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Color newC=JColorChooser.showDialog(main, "choose Background color",bgcolor);
				if(newC!=null)bgcolor=newC;
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
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand()=="NewFile"){
			newFile();
		}else if(e.getActionCommand()=="Open"){
			fileChooser =new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(main);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	main.newOpen(fileChooser.getSelectedFile());
	        }
		}else if(e.getActionCommand()=="Save"){
			fileChooser =new JFileChooser();
			int returnVal = fileChooser.showSaveDialog(main);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	draw.saveFile(fileChooser.getSelectedFile());
	        }
		}else if(e.getActionCommand()=="1px"){
			star.setPenWidth(1.0f);
		}else if(e.getActionCommand()=="10px"){
			star.setPenWidth(10.0f);
		}else if(e.getActionCommand()=="20px"){
			star.setPenWidth(20.0f);
		}else if(e.getActionCommand()=="40px"){
			star.setPenWidth(40.0f);
		}else if(e.getActionCommand()=="red"){
			star.setPenColor(Color.red);
			main.setPenMode(0);
			menu.penCC(Color.red);
		}else if(e.getActionCommand()=="blue"){
			star.setPenColor(Color.blue);
			main.setPenMode(0);
			menu.penCC(Color.blue);
		}else if(e.getActionCommand()=="green"){
			star.setPenColor(Color.green);
			main.setPenMode(0);
			menu.penCC(Color.green);
		}else if(e.getActionCommand()=="ColorChooser"){
			Color color=JColorChooser.showDialog(main,"choose a color",Color.white);
			if(color!=null){
				star.setPenColor(color);
				main.setPenMode(0);
				menu.penCC(color);
			}
		}else if(e.getActionCommand()=="spoit"){
			main.setPenMode(4);
		}else if(e.getActionCommand()=="erazer"){
			main.setPenMode(10);
		}else if(e.getActionCommand()=="redo"){
			draw.reDo();
		}else if(e.getActionCommand()=="goto"){
			draw.goTo();
		}else if(e.getActionCommand()=="cut"){
			main.setPenMode(1);
		}else if(e.getActionCommand()=="copy"){
			main.setPenMode(2);
		}else if(e.getActionCommand()=="paste"){
			main.setPenMode(3);
		}else if(e.getActionCommand()=="bgchange"){
			Color color=JColorChooser.showDialog(main,"choose a color",Color.white);
			if(color!=null)draw.setBGColor(color);
		}else if(e.getActionCommand()=="allclear"){
			draw.allClear();
		}else if(e.getActionCommand()=="zoom1"){
			draw.zoom(1);
		}else if(e.getActionCommand()=="zoom2"){
			draw.zoom(2);
		}else if(e.getActionCommand()=="zoom4"){
			draw.zoom(4);
		}else if(e.getActionCommand()=="zoom8"){
			draw.zoom(8);
		}
	}
}
