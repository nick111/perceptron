import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public class Perceptron extends JFrame {

	static String inFileAddress = "perc.txt";
	static int xwidth = 300;
	static int ywidth = 300;
	static int xyohaku = xwidth / 10;
	static int yyohaku = ywidth / 10;
	Value value = new Value();

		// ファイルに２次元で２グループの点たちがあって、それらを分かつ直線の関数を求める
		// 必要なものは、ファイルの点たちをおさめる配列。
		// それらは３次元にする必要がある（足した次元の方は値をすべて１にする）
		// また、グループをもつ必要がある。
		// 
		// あとは、重み係数を表す点も必要（こいつも３次元にする）
		// 
		// 目標としては点（グループで色を変える）と、直線を表す図を描画できるといい。

	
		

		// 点の描画
		  public void paint(Graphics g){
			    super.paint(g);
			    Graphics2D g2 = (Graphics2D)g;
			    
			    axis(g2);
			    plot(g2);
			    chokusenDraw(g2);
			    kekkaKisai(g2);
			  }
		
		  public Perceptron(){
			    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
				// ファイルから点を読み込み、配列に格納する。
				List<String[]> pointData = FileUtil.getStringsByQuot(inFileAddress);
				
				Hyouhonpoint[] hyouhonpoints = new Hyouhonpoint[pointData.size()];
				
				for(int i = 0; i < pointData.size(); i++){
					hyouhonpoints[i] = new Hyouhonpoint();
					if(pointData.get(i)[0].equals("a")){
						hyouhonpoints[i].groupId = GoupId.a;
					}else{
						hyouhonpoints[i].groupId = GoupId.b;
					}
					hyouhonpoints[i].x1 = Double.valueOf(pointData.get(i)[1]);
					hyouhonpoints[i].x2 = Double.valueOf(pointData.get(i)[2]);
				}
				value.hyouhonpoints = hyouhonpoints;
				
				boolean isAllRight = false;
				
				
				// 指定された点に対し重みを調節する処理を繰り返す
				// １回通して１度でも調節が発生したら再度同じ点たちを読み込んで調節を行う
				// 一度も調節が発生しない回になったら繰り返しをやめる
				
				while(!isAllRight){

					isAllRight = true;
					
					for(int k = 0; k < hyouhonpoints.length; k++){
						
						
						// 重みベクトルと入力ベクトルの内積を求める
						double g = value.w0 * hyouhonpoints[k].x0 + value.w1 * hyouhonpoints[k].x1 + value.w2 * hyouhonpoints[k].x2;
						
						if((g > 0 && hyouhonpoints[k].groupId == GoupId.a) || (g <= 0 && hyouhonpoints[k].groupId == GoupId.b)){
							
						}else{
							if(hyouhonpoints[k].groupId == GoupId.a){
								value.w0 = value.w0 + value.p * hyouhonpoints[k].x0;
								value.w1 = value.w1 + value.p * hyouhonpoints[k].x1;
								value.w2 = value.w2 + value.p * hyouhonpoints[k].x2;							
							}else{
								value.w0 = value.w0 - value.p * hyouhonpoints[k].x0;
								value.w1 = value.w1 - value.p * hyouhonpoints[k].x1;
								value.w2 = value.w2 - value.p * hyouhonpoints[k].x2;
							}

							
							isAllRight = false;
						}
						
					}
				}
				
		  }
			    

		  
		// 直線の描画
		
		
		
		
		
		
		
		
		
		   /* 座標軸を書くメソッド */
		   public void axis(Graphics g) {
			   Graphics2D g2 = (Graphics2D)g;

			   g2.setColor(Color.BLACK); 
			   g2.setStroke(new BasicStroke(1.0f));

			   
			   // x軸描画
			   g2.draw(new Line2D.Double(xyohaku, yyohaku + ywidth, xyohaku + 2 * xwidth, yyohaku + ywidth));
			   
			   // y軸描画
			   g2.draw(new Line2D.Double(xyohaku + xwidth, xyohaku, xyohaku + xwidth, yyohaku + 2 * ywidth));
			   
			   // 値を表示
			   g2.setColor(new Color(800000)); 
			   g2.drawString("O", xwidth + xyohaku , ywidth + yyohaku);
			   g2.drawString(String.valueOf(xwidth), xyohaku + 2 * xwidth, yyohaku + ywidth);
			   g2.drawString(String.valueOf(-(xwidth)), xyohaku, yyohaku + ywidth);
			   g2.drawString(String.valueOf(ywidth), xyohaku + xwidth, yyohaku);
			   g2.drawString(String.valueOf(-(ywidth)), xyohaku + xwidth, yyohaku + 2 * ywidth);
		   }
		
		   /* 点をプロットするメソッド */
		   public void plot(Graphics g) {
			   Graphics2D g2 = (Graphics2D)g;
			   g2.setStroke(new BasicStroke(5.0f));
			  
			    for(int i = 0; i < value.hyouhonpoints.length; i++){
			    	if(value.hyouhonpoints[i].groupId == GoupId.a){
						   g2.setColor(Color.BLUE); 
			    	}else{
			    		g2.setColor(Color.RED);
			    	}
			    	Line2D line = new Line2D.Double(henkan(value.hyouhonpoints[i].getPoint2D()), henkan(value.hyouhonpoints[i].getPoint2D()));
				    g2.draw(line);
			    }
			   
			   
		   }
		
		
		
		   public static void main(String[] args) {
			   JFrame f = new Perceptron();
			   f.setTitle("Percptron");
			   f.setSize(2 * (xwidth + xyohaku), 2 * (ywidth + yyohaku));
			   f.setBackground(Color.WHITE);
			   f.setVisible(true);
			   }
	
		   /* 点の位置を座標軸に合わせて変換するメソッド */
		   public Point2D.Double henkan(Point2D.Double point){
			   return new Point2D.Double(point.getX() + xwidth + xyohaku, ywidth + yyohaku - point.getY());
		   }

		   /* y切片と傾きの値から直線のLine2D.Doubleを返すメソッドメソッド */
		   public Line2D.Double getStraightLine(double a0, double a1){
			   
			   List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();
			   
			   if((a0 + a1 * xwidth <= ywidth) && (a0 + a1 * xwidth > -(ywidth))){
				   pointList.add(henkan(new Point2D.Double(xwidth, a0 + a1 * xwidth)));
			   }
			   if(( (-(ywidth) - a0) / a1 <= xwidth) && ((-(ywidth) - a0) / a1 > -(xwidth))){
				   pointList.add(henkan(new Point2D.Double((-(ywidth) - a0) / a1, -(ywidth))));
			   }
			   if((a0 + a1 * (-(xwidth)) < ywidth) && (a0 + a1 * (-(xwidth)) >= -(ywidth))){
				   pointList.add(henkan(new Point2D.Double((-(xwidth)), a0 + a1 * (-(xwidth)))));
			   }
			   if(( (ywidth - a0) / a1 < xwidth) && ((ywidth - a0) /a1  >= -(xwidth))){
				   pointList.add(henkan(new Point2D.Double((ywidth - a0) / a1, ywidth)));
			   }
			   
			   return new Line2D.Double(pointList.get(0), pointList.get(1));
		   }
		   
		   /* パーセプトロンで求めた直線を書くメソッド */
		   public void chokusenDraw(Graphics g) {
			   Graphics2D g2 = (Graphics2D)g;
			   g2.setColor(Color.RED); 
			   g2.setStroke(new BasicStroke(3.0f));
			   
			   // 直線の式は 0 = w0 + w1*x + w2*y だから
			   // 傾きは a = -(w1/w2)
			   // y切片は b = -(w0/w2)
			   
			   g2.draw(getStraightLine(-(value.w0/value.w2), -(value.w1/value.w2) ));
			   
		   }
		   public void kekkaKisai(Graphics g){
			   
			   String message1 = "w0:" + value.w0 + "  w1:" + value.w1 +"  w2:" +value.w2;
			   String message2 = "y=" + (-(value.w1/value.w2)) + "x +" + (-(value.w0/value.w2)) ;
			   
			   g.drawString(message1, 0, (int)(yyohaku + ywidth *2));
			   g.drawString(message2, 0, (int)(yyohaku + ywidth *2 + yyohaku/2));
				
			   
		   }
		   
}
