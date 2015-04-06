import java.awt.*;
import java.awt.image.*;


public class FillArea {
	int MAXSIZE=1024;
	int MINX,MINY,MAXX,MAXY;
	
	int[] lx=new int[MAXSIZE];
	int[] rx=new int[MAXSIZE];
	int[] y=new int[MAXSIZE];
	int[] oy=new int[MAXSIZE];
	
	int sIdx,eIdx;
	
	BufferedImage bufImg=null;
	int paintNum,bgNum;
	
	FillArea(int x,int y,BufferedImage img,Color color,int minX,int minY,int maxX,int maxY){
		bufImg=img;
		paintNum=color.getRGB();
		bgNum=img.getRGB(x, y);
		MINX=minX;MINY=minY;MAXX=maxX;MAXY=maxY;
		paint(x, y);
	}
	public BufferedImage getImg(){
		return bufImg;
	}
	
	private void scanLine(int lx1,int rx1,int y1,int oy1){
		while ( lx1 <= rx1 ) {

		    /* 非領域色を飛ばす */
		    for ( ; lx1 < rx1 ; lx1++ ){
		      if ( bufImg.getRGB(lx1,y1)==bgNum) break;
		    }
		    if(bufImg.getRGB(lx1,y1)!=bgNum) break;

		    lx[eIdx] = lx1;

		    /* 領域色を飛ばす */
		    for ( ; lx1 <= rx1 ; lx1++ ){
		      if (bufImg.getRGB(lx1, y1) != bgNum ) break;
		    }
		    rx[eIdx] = lx1 - 1;
		    y[eIdx] = y1;
		    oy[eIdx] = oy1;

		    if ( ++eIdx == MAXSIZE )
		      eIdx = 0;
		}
	}
	
	private void paint(int x1, int y1){
	  int lx2, rx2; /* 塗り潰す線分の両端のX座標 */
	  int ly2;     /* 塗り潰す線分のY座標 */
	  int oy2;     /* 親ラインのY座標 */
	  int i2;
	  if ( paintNum== bgNum ) return;    /* 領域色と描画色が等しければ処理不要 */

	  sIdx = 0;
	  eIdx = 1;
	  lx[sIdx] = rx[sIdx] = x1;
	  y[sIdx] = oy[sIdx] = y1;

	  do {
	    lx2 = lx[sIdx];
	    rx2 = rx[sIdx];
	    ly2 = y[sIdx];
	    oy2 = oy[sIdx];

	    int lxsav = lx2 - 1;
	    int rxsav = rx2 + 1;

	    if ( ++sIdx == MAXSIZE ) sIdx = 0;

	    /* 処理済のシードなら無視 */
	    if ( bufImg.getRGB( lx2, ly2 ) != bgNum ){
	    	continue;
	    }
	    /* 右方向の境界を探す */
	    while ( rx2 < MAXX-1 ) {
	      if ( bufImg.getRGB( rx2 + 1, ly2 ) != bgNum ) break;
	      rx2++;
	    }
	    /* 左方向の境界を探す */
	    while ( lx2 > MINX ) {
	      if ( bufImg.getRGB( lx2 - 1, ly2 ) != bgNum ) break;
	      lx2--;
	    }
	    /* lx-rxの線分を描画 */
	    for ( int i = lx2; i <= rx2; i++ ){
	    	bufImg.setRGB( i, ly2, paintNum );
	    }

	    /* 真上のスキャンラインを走査する */
	    if ( ly2  > MINY ) {
	      if ( ly2 - 1 == oy2 ) {
	        scanLine( lx2, lxsav, ly2 - 1, ly2);
	        scanLine( rxsav, rx2, ly2 - 1, ly2);
	      } else {
	        scanLine( lx2, rx2, ly2 - 1, ly2);
	      }
	    }

	    /* 真下のスキャンラインを走査する */
	    if ( ly2 + 1 < MAXY ) {
	      if ( ly2 + 1 == oy2 ) {
	        scanLine( lx2, lxsav, ly2 + 1, ly2);
	        scanLine( rxsav, rx2, ly2 + 1, ly2);
	      } else {
	        scanLine( lx2, rx2, ly2 + 1, ly2);
	      }
	    }

	  } while ( sIdx != eIdx );
	}
	
}
