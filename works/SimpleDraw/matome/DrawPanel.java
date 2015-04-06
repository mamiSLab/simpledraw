import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class DrawPanel extends JPanel {
	private static final long serialVersionUID = 42L;
	SimpleDraw main;	
	Menu menu;
	Status star;
	Color currentColor=Color.black;		//現在のペンの色
	Color bgColor=Color.white;			//背景色,消しゴムの色
	Color textColor;					//テキストの色
	Float currentWidth=20.f;//,erazerWidth=20.f;		//ペンの太さ
	BufferedImage bufferImage=null;		//バッファ
	Graphics2D bufferGraphics=null;		//描画用グラフィック
	Raster coRaster;					//コピペ用保管ラスタ
	Image clip;							//コピー,カットしたイメージ
	int penShape=BasicStroke.CAP_ROUND;		//ペンの形
	int bufWidth=600;					//バッファ横サイズ
	int bufHeight=520;					//バッファ縦サイズ　
	int lens=1;					//表示倍率
	int nowBuf=0,maxBuf=128;		//今いるラスタ位置と最大
	Raster[] reRaster;				//ラスタの保管
	
	DrawPanel(SimpleDraw dr,Status st){			//はじめに生成される
		main=dr;
		star=st;
		this.setPreferredSize(new Dimension(bufWidth*lens,bufHeight*lens));
		this.createBuffer(bufWidth,bufHeight);
		bufferGraphics.clearRect(0, 0, bufWidth,bufHeight); 
		reRaster=new Raster[maxBuf];
		reRaster[maxBuf-1]=null;
	}
	DrawPanel(SimpleDraw dr,int width,int height,Color color,Menu mn,Status st){	//新規作成のとき生成される
		main=dr;
		menu=mn;
		star=st;
		bufWidth=width;bufHeight=height;bgColor=color;
		this.setPreferredSize(new Dimension(bufWidth*lens,bufHeight*lens));
		this.createBuffer(bufWidth,bufHeight);
		bufferGraphics.clearRect(0, 0, bufWidth,bufHeight); 
		reRaster=new Raster[maxBuf];
		reRaster[maxBuf-1]=null;
	}
	public void setMenu(Menu mn){
		menu=mn;
	}
	public void setPen(){				//ペンをセットする
		currentColor=star.getPenColor();
		currentWidth=star.getPenWidth();
	}
	public void setErazer(){			//消しゴムをセットする
		currentColor=bgColor;
		currentWidth=star.getErazeWidth();
	}
	
	public void setBGColor(Color newColor){			//背景の色(消しゴムも)変える
		reMil();
		int bgRGB=bgColor.getRGB();
		if(currentColor==bgColor)currentColor=newColor;
		bgColor=newColor;
		int newRGB=bgColor.getRGB();
		int i,j;
		for(i=bufferImage.getMinX();i<bufferImage.getMinX()+bufferImage.getWidth();i++){
			for(j=bufferImage.getMinY();j<bufferImage.getMinY()+bufferImage.getHeight();j++){
				if(bufferImage.getRGB(i, j)==bgRGB){
					bufferImage.setRGB(i, j, newRGB);
				}
			}		
		}
		bufferGraphics.setBackground(bgColor);
		repaint();
	}
	
	public void allClear(){				//全消し
		reMil();
		bufferGraphics.clearRect(0, 0, bufWidth,bufHeight); 
		repaint();
	}
	
	public void spoit(int x,int y){			//x,yで指定された場所の色をとってくる
		if((x/lens>=bufferImage.getMinX())&&(x/lens<bufferImage.getMinX()+bufWidth)&&
				(y/lens>=bufferImage.getMinY())&&(y/lens<bufferImage.getMinY()+bufHeight)){
			Color spoitColor=new Color(bufferImage.getRGB((x-4)/lens, (y-4)/lens));
			currentColor=spoitColor;
			star.setPenColor(spoitColor);
			menu.penCC(spoitColor);
		}
	}
	public void selectSpoit(int x,int y){			//スポイト
		if((x/lens>=bufferImage.getMinX())&&(x/lens<bufferImage.getMinX()+bufWidth)&&
				(y/lens>=bufferImage.getMinY())&&(y/lens<bufferImage.getMinY()+bufHeight)){
			Color spoitColor=new Color(bufferImage.getRGB((x-4)/lens, (y-4)/lens));
			menu.penCC(spoitColor);
		}
	}
	
	public void baketsu(int x,int y){					//塗りつぶし
		int minX=bufferImage.getMinX();
		int minY=bufferImage.getMinY();
		if((x/lens>=minX)&&(x/lens<minX+bufWidth)&&
				(y/lens>=minY)&&(y/lens<minY+bufHeight)){
			coRaster=bufferImage.getData();
			reMil();
			currentColor=star.getPenColor();
			try{
				FillArea fil=new FillArea((x-4)/lens,(y-4)/lens,bufferImage,currentColor,
								minX,minY,minX+bufWidth,minY+bufHeight);
				bufferImage=fil.getImg();
				fil=null;
				repaint();
			}catch(ArrayIndexOutOfBoundsException e){
				JOptionPane.showMessageDialog(main, "Sorry.It cannot painted");
				bufferImage.setData(coRaster);
				repaint();
			}
		}
	}
	
	
	public void setPenShape(int newShape){  //ペン先の形を変える
		penShape=newShape;
	}
	
	private void createBuffer(int width, int height) {		//バッファを作成
		bufferImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
		bufferGraphics=bufferImage.createGraphics(); 
		bufferGraphics.setBackground(bgColor);		
	}
	
	public void drawLine(int x1, int y1, int x2, int y2){	//線を描く
		bufferGraphics.setColor(currentColor);
		bufferGraphics.setStroke(new BasicStroke(currentWidth,penShape,BasicStroke.JOIN_MITER));
		bufferGraphics.drawLine((x1-4)/lens, (y1-4)/lens, (x2-4)/lens, (y2-4)/lens);
		repaint();
	}
	public void selStart(){
		coRaster=bufferImage.getData();		//バッファの画像データをとっておく
		reMil();
	}	
	public void selPen(int x1,int y1,int x2,int y2){		//選択範囲を破線表示
		int x0,y0,wid,hei;
		bufferImage.setData(coRaster);
		repaint();
		float dash[]={3.0f};
		bufferGraphics.setColor(Color.black);
		bufferGraphics.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,3.0f,dash,0.0f));
		if((x1-x2)>0){x0=x2-4;wid=x1-x2-1;
		}else{x0=x1-4;wid=x2-x1-1;
		}
		if((y1-y2)>0){y0=y2-4;hei=y1-y2-1;
		}else{y0=y1-4;hei=y2-y1-1;
		}		
		bufferGraphics.drawRect(x0/lens,y0/lens,wid/lens,hei/lens);
		repaint();
	}
	public void copyZone(int x1,int y1,int x2,int y2,int co){	//指定範囲をコピーラスタに保管
		int x0,y0,wid,hei;		
		if((x1-x2)>0){x0=x2-4;wid=x1-x2+4;
		}else{x0=x1-4;wid=x2-x1+4;
		}
		if((y1-y2)>0){y0=y2-4;hei=y1-y2+4;
		}else{y0=y1-4;hei=y2-y1+4;
		}
		BufferedImage buf=new BufferedImage(bufWidth,bufHeight,BufferedImage.TYPE_INT_BGR);
		bufferImage.setData(coRaster);
		buf.setData(coRaster);
		ImageFilter filter =new CropImageFilter(x0/lens,y0/lens,wid/lens,hei/lens);
		FilteredImageSource fis= new FilteredImageSource(buf.getSource(),filter);
		clip=createImage(fis);
		repaint();
		if(co==0)bufferGraphics.clearRect(x0/lens,y0/lens,wid/lens,hei/lens);	//カットの場合
		main.setPenMode(0);
	}
	public void paste(int x1,int y1){				//コピーラスタにあるイメージを描画
		reMil();
		ImageObserver iob=null;
		bufferGraphics.drawImage(clip, (x1-4)/lens, (y1-4)/lens, iob);
		repaint();
		main.setPenMode(0);
	}

	public void textFont(Font font,Color co){				//Textのフォントを変える
		bufferGraphics.setFont(font);
		textColor=co;
	}
	public void selText(String str,int x,int y){			//Text
		reMil();
		bufferGraphics.setColor(textColor);
		bufferGraphics.drawString(str, (x-4)/lens, y/lens);
		repaint();
		bufferGraphics.setColor(currentColor);
	}
	public void stamp(int x1,int y1,int x2,int y2,Image img){	//stamp	<-Drag
		int x0,y0,wid,hei;
		bufferImage.setData(coRaster);
		repaint();
		float dash[]={3.0f};
		bufferGraphics.setColor(Color.black);
		bufferGraphics.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,3.0f,dash,0.0f));
		if((x1-x2)>0){x0=x2-4;wid=x1-x2-1;
		}else{x0=x1-4;wid=x2-x1-1;
		}
		if((y1-y2)>0){y0=y2-4;hei=y1-y2-1;
		}else{y0=y1-4;hei=y2-y1-1;
		}		
		ImageObserver iob=null;
		bufferGraphics.drawImage(img,x0/lens,y0/lens,wid/lens,hei/lens,iob);
		repaint();
	}public void stamp(int x1,int y1,Image img){		//stamp	<-Click
		reMil();
		ImageObserver iob=null;
		bufferGraphics.drawImage(img,x1/lens,y1/lens,iob);
		repaint();
	}
	public void zukei(int x1,int y1,int x2,int y2,int flag){	//stampで図形を描く
		int x0,y0,wid,hei;
		bufferImage.setData(coRaster);
		repaint();
		bufferGraphics.setColor(star.getPenColor());
		bufferGraphics.setStroke(new BasicStroke(currentWidth,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		if((x1-x2)>0){x0=x2-4;wid=x1-x2-1;
		}else{x0=x1-4;wid=x2-x1-1;
		}
		if((y1-y2)>0){y0=y2-4;hei=y1-y2-1;
		}else{y0=y1-4;hei=y2-y1-1;
		}		
		if(flag==9)bufferGraphics.fillRect(x0/lens,y0/lens,wid/lens,hei/lens);
		else if(flag==8)bufferGraphics.fillOval(x0/lens,y0/lens,wid/lens,hei/lens);
		else if(flag==11)bufferGraphics.drawOval(x0/lens,y0/lens,wid/lens,hei/lens);
		else if(flag==12)bufferGraphics.drawRect(x0/lens,y0/lens,wid/lens,hei/lens);
		repaint();
	}
	
	public void zoom(int lenz){		//拡大倍率を変える
		lens=lenz;
		this.setPreferredSize(new Dimension(bufWidth*lens,bufHeight*lens));
		repaint();
		revalidate();
	}
	
	public void reMil(){			//戻り用ラスタにかさねていく
		nowBuf++;if(nowBuf==maxBuf-1)nowBuf=0;	
		reRaster[nowBuf]=bufferImage.getData();	
	}
	
	public void reDo(){				//戻る
		if(nowBuf==0&&reRaster[maxBuf-1]==null){	//戻る物がない
		}else{
			reRaster[nowBuf+1]=bufferImage.getData();
			if(nowBuf==0)nowBuf=maxBuf-1;
			bufferImage.setData(reRaster[nowBuf]);
			nowBuf--;
			repaint();
		}
	}
	public void goTo(){				//進む
		int fuBuf=nowBuf+2;		
		if(fuBuf==maxBuf-1)fuBuf=0;
		if(reRaster[fuBuf]!=null){	
			bufferImage.setData(reRaster[fuBuf]);
			nowBuf++;
			if(nowBuf==maxBuf-1)nowBuf=0;
			repaint();
		}			
	}
	
	public void paintComponent(Graphics g){			//画面に描画
		super.paintComponent(g);
		if(null!=bufferImage)g.drawImage(bufferImage,0,0,bufWidth*lens,bufHeight*lens,this);
		
	}
	
	public void openFile(File file2open){			//ファイルを開く
		BufferedImage pictureImage;
		try {
			pictureImage = ImageIO.read(file2open);
		} catch(Exception e){
			JOptionPane.showMessageDialog(main, "Error: readingfile="+file2open.getName());
			return;
		}
		//画像に合わせたサイズでbufferImageとbufferGraphicsを作りなおして画像を読み込む
			//ImageIO.readの戻り値をbufferImageに代入するのでは駄目みたいです。
		this.setPreferredSize(new Dimension((pictureImage.getWidth())*lens,(pictureImage.getHeight())*lens));
		bufWidth=pictureImage.getWidth();
		bufHeight=pictureImage.getHeight();
		this.createBuffer(bufWidth,bufHeight);
		bufferGraphics.drawImage(pictureImage,0,0,this);
		main.setSize(pictureImage.getWidth()+40, pictureImage.getHeight()+70);
		repaint(); //画像を表示するためにpaintComponentを呼ぶ
	}
	public static String dotCheck(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return "NO_DOT_KKS";
	}
	public String saveFile(File flname){			//ファイルにセーブ
		try{
			String kks=dotCheck(flname.getName());
			if(kks=="NO_DOT_KKS"){
				File flRename=new File(flname.getAbsolutePath()+".jpg");
				flname.renameTo(flRename);
				ImageIO.write(bufferImage, "jpg", flRename);
			}else{
				ImageIO.write(bufferImage, kks, flname);
			}
		}catch(Exception e){
			return "Error: writing file="+flname.getName();
		}
		return "Saved!";
	}
}
