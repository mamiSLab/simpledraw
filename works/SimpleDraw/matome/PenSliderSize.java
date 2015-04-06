import javax.swing.*;
import java.awt.*;

public class PenSliderSize extends JPanel {
	int arg;
	int flag=0,pore;
	Status star;
	PenSliderSize(Status st,int i){
		star=st;
		pore=i;
		if(i==0)arg=(int)star.getPenWidth();
		else arg=(int)star.getErazeWidth();
		repaint();
	}

	public void paint(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		g2.setBackground(Color.white);
		g2.clearRect(0,15,100,120);
		if(pore==0)arg=(int)star.getPenWidth();
		else arg=(int)star.getErazeWidth();
		if(flag==0)g2.fillOval(50-arg/2,70-arg/2,arg,arg);	
		else g2.fillRect(50-arg/2,70-arg/2,arg,arg);
		g2.drawOval(30,50,40,40);	
		g2.drawOval(10,30,80,80);	
		g2.drawLine(50, 15, 50, 70);
		g2.drawLine(50, 70, 90, 30);
	}
	
	public void shapePSS(int fl){
		flag=fl;
		repaint();
	}
	
	public PenSliderSize getPSS(){
		repaint();
		return this;
	}
	public void kousinPSS(){
		repaint();
	}
	
}
