import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class StampButton extends JPanel implements ActionListener{
	int count=0,flag=0;
	SimpleDraw main;
	JPanel panel;
	JButton bt1;
	JButton bt2;
	JButton bt3;
	JButton bt4;
	File file1;
	File file2;
	File file3;
	File file4;

	
	StampButton(SimpleDraw mn){
		panel=new JPanel();
		main=mn;
		bt1=new JButton();
		bt1.setPreferredSize(new Dimension(150, 100));
		bt1.addActionListener(this);
		bt2=new JButton();
		bt2.setPreferredSize(new Dimension(150, 100));
		bt2.addActionListener(this);
		bt3=new JButton();
		bt3.setPreferredSize(new Dimension(150, 100));
		bt3.addActionListener(this);
		bt4=new JButton();
		bt4.setPreferredSize(new Dimension(150, 100));
		bt4.addActionListener(this);
		panel.setLayout(new GridLayout(2,2));
		panel.add(bt1);
		panel.add(bt2);
		panel.add(bt3);
		panel.add(bt4);
	}
	
	public void actionPerformed(ActionEvent e){
		if((e.getSource()==bt1)&&(count>0||flag==1)){
			main.setStamp(file1.getAbsolutePath());
		}else if((e.getSource()==bt2)&&(count>1||flag==1)){
			main.setStamp(file2.getAbsolutePath());
		}else if((e.getSource()==bt3)&&(count>2||flag==1)){
			main.setStamp(file3.getAbsolutePath());
		}else if((e.getSource()==bt4)&&(count>3||flag==1)){
			main.setStamp(file4.getAbsolutePath());
		}
	}
	
	public void setImage(Image icon,File file){
		count++;
		if(count==1){
			file1=file;
			bt1.setIcon(new ImageIcon(icon));
			main.setStamp(file1.getAbsolutePath());
		}else if(count==2){
			file2=file;
			bt2.setIcon(new ImageIcon(icon));
			main.setStamp(file2.getAbsolutePath());
		}else if(count==3){
			file3=file;
			bt3.setIcon(new ImageIcon(icon));
			main.setStamp(file2.getAbsolutePath());
		}else if(count==4){
			file4=file;
			bt4.setIcon(new ImageIcon(icon));
			main.setStamp(file2.getAbsolutePath());
			count=0;
			flag=1;
		}
	}
	
	public JPanel getPanel(){
		return panel;
	}
}
