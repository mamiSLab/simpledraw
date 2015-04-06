import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.io.*;

/**
 * @author g1220507
 *
 */
public class SimpleDraw extends JFrame implements MouseListener,MouseMotionListener,ChangeListener,WindowListener{
	int lastx, lasty, newx, newy;	//描画用
	DrawPanel panel;
	Menu menu;
	Tool tool;
	Status star;
	int penMode=0;	//0:paint,1:cut,2:copy,3:paste,4:spoit,5:text,6:stamp,7:baketsu,8:circle,9:rect,10:erazer
					//11:circle2,12:rect2
	int stenx,steny;		//選択範囲の始点
	JViewport view;
	Point point;
	String typing="";
	Image stp;
	private static final long serialVersionUID = 42L;

	public void setPenMode(int pmode){		//ペンモードの変更	
		penMode=pmode;
	}
	public void setText(String str){
		typing=str;
	}
	public void setStamp(String str){
		stp=Toolkit.getDefaultToolkit().createImage(str);
		penMode=6;
	}
	
	public void mouseEntered(MouseEvent arg0) {	
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if(penMode==1||penMode==2)
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	public void mouseExited(MouseEvent arg0) {	
	}
	public void mouseClicked(MouseEvent arg0) {
		menu.chooseDraw(panel,this);
		tool.chooseDraw(panel,this);
		if(penMode==3){
			panel.paste(arg0.getX()+point.x,arg0.getY()+point.y);
		}else if(penMode==4){
			panel.spoit(arg0.getX()+point.x, arg0.getY()+point.y);
			penMode=0;
		}else if(penMode==5){
			panel.selText(typing,arg0.getX()+point.x,arg0.getY()+point.y);
		}else if(penMode==6){
			panel.stamp(arg0.getX()+point.x,arg0.getY()+point.y,stp);
		}else if(penMode==7){
			panel.baketsu(arg0.getX()+point.x,arg0.getY()+point.y);
		}
	}
	public void mousePressed(MouseEvent arg0) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if(penMode==0){
			lastx=arg0.getX()+point.x;
			lasty=arg0.getY()+point.y;
			panel.setPen();
			panel.selStart();
		}else if(penMode==10){
			lastx=arg0.getX()+point.x;
			lasty=arg0.getY()+point.y;
			panel.setErazer();
			panel.selStart();
		}else if(penMode==1||penMode==2||penMode==6||penMode==8||penMode==9||penMode==11||penMode==12){
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			stenx=arg0.getX()+point.x;
			steny=arg0.getY()+point.y;
			panel.selStart();
		}
	}
	public void mouseReleased(MouseEvent arg0) {
		if(penMode==1){
			panel.copyZone(stenx,steny,arg0.getX()+point.x,arg0.getY()+point.y,0);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}else if(penMode==2){
			panel.copyZone(stenx,steny,arg0.getX()+point.x,arg0.getY()+point.y,1);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}else if(penMode==4){
			panel.spoit(arg0.getX()+point.x, arg0.getY()+point.y);
			penMode=0;
		}
	}
	public void mouseDragged(MouseEvent arg0) {
		if(penMode==0||penMode==10){
			newx=arg0.getX()+point.x;
			newy=arg0.getY()+point.y;
			panel.drawLine(lastx,lasty,newx,newy);
			lastx=newx;
			lasty=newy;
		}else if(penMode==1||penMode==2){
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			panel.selPen(stenx,steny,arg0.getX()+point.x,arg0.getY()+point.y);
		}else if(penMode==4){
			panel.spoit(arg0.getX()+point.x, arg0.getY()+point.y);
		}else if(penMode==6){
			panel.stamp(stenx,steny,arg0.getX()+point.x,arg0.getY()+point.y,stp);
		}else if(penMode==8||penMode==9||penMode==11||penMode==12){
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			panel.zukei(stenx,steny,arg0.getX()+point.x,arg0.getY()+point.y,penMode);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void stateChanged(ChangeEvent e){
		point=view.getViewPosition();
	}
	public void windowOpened(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowClosing(WindowEvent e)  {
    	setVisible(false); 
    	star.wcoMinus();
    	if(star.getwco()==0){
    		System.exit(0);
    	}
}
	/**
	 * @param args
	 */
	public void setDraw(DrawPanel panel2){
		panel=panel2;
	}
	public void setMenu(Menu menu2){
		menu=menu2;
	}
	public void setTool(Tool tool2){
		tool=tool2;
	}
	public void setStatus(Status star2){
		star=star2;
	}
	public void setView(JViewport view2){
		view=view2;
	}
	public void newCreate(String title,int width,int height,Color color){	//新規作成
		SimpleDraw newFile =new SimpleDraw();
		newFile.setTitle(title);
		newFile.setBounds(130,110,width+40,height+70);
		JScrollPane scpane =new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scpane.addMouseListener(newFile);
		scpane.addMouseMotionListener(newFile);
		newFile.setStatus(star);
		star.wcoPlus();
		DrawPanel panel2=new DrawPanel(newFile,width,height,color,menu,star);
		JViewport view2=scpane.getViewport();
		view2.setView(panel2);
		view2.addChangeListener(newFile);	
		newFile.setView(view2);
		newFile.setDraw(panel2);
		newFile.getContentPane().add(scpane);
		newFile.setMenu(menu);
		menu.newDraw(panel2,newFile);
		newFile.setTool(tool);
		tool.newDraw(panel2,newFile);
		MenuBar menubar=new MenuBar(newFile,panel2,menu,star);
		newFile.setVisible(true);
		newFile.addWindowListener((WindowListener)newFile);
		//newFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void newOpen(File file){		//ファイルを開く(新しいウィンドウで)
		SimpleDraw newFile =new SimpleDraw();
		newFile.setTitle(file.getName());
		newFile.setBounds(150,110,610,570);
		JScrollPane scpane =new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scpane.addMouseListener(newFile);
		scpane.addMouseMotionListener(newFile);
		newFile.setStatus(star);
		star.wcoPlus();
		DrawPanel panel2=new DrawPanel(newFile,600,520,Color.white,menu,star);
		panel2.openFile(file);
		JViewport view2=scpane.getViewport();
		view2.setView(panel2);
		view2.addChangeListener(newFile);
		newFile.setView(view2);
		newFile.setDraw(panel2);
		newFile.getContentPane().add(scpane);
		newFile.setMenu(menu);
		menu.newDraw(panel2,newFile);
		newFile.setTool(tool);
		tool.newDraw(panel2,newFile);
		MenuBar menubar=new MenuBar(newFile,panel2,menu,star);
		newFile.setVisible(true);	
		newFile.addWindowListener((WindowListener)newFile);
	}
	
	private void init() {
		this.setTitle("Simple Draw");
		this.setBounds(120,100,700,600);		
		JScrollPane scpane =new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scpane.addMouseListener(this);
		scpane.addMouseMotionListener(this);
		star=new Status();
		star.wcoPlus();
		panel=new DrawPanel(this,star);		
		view =scpane.getViewport();
		view.setView(panel);
		view.addChangeListener(this);
		this.getContentPane().add(scpane);
		menu= new Menu(panel,this,star);
		panel.setMenu(menu);
		tool= new Tool(panel,this);
		MenuBar menubar=new MenuBar(this,panel,menu,star);
		this.setVisible(true);
		this.addWindowListener(this);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDraw frame=new SimpleDraw();
		frame.init();
	}

}
