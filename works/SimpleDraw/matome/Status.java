import java.awt.Color;

public class Status {
	Color penColor=Color.black;		//ペンの色
	float penWidth=20.f;
	float erazeWidth=20.f;
	int wco=0;
	
	public Color getPenColor(){return penColor;}
	public void setPenColor(Color color){penColor=color;}
	
	public float getPenWidth(){return penWidth;}
	public void setPenWidth(float width){penWidth=width;}
	public float getErazeWidth(){return erazeWidth;}
	public void setErazeWidth(float width){erazeWidth=width;}
	
	public void wcoPlus(){	wco++;}
	public void wcoMinus(){ wco--;}
	public int getwco(){return wco;}
}
