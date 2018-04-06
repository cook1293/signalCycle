package signalCycle;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 通常の信号交差点の信号サイクルアプリケーション
 * @author cook1293
 */

public class SignalNormal extends JFrame{

	MyPanel pn = new MyPanel();
	int x1=100,y1=50,size=20;	//上下車灯
	int x2=50,y2=130;			//左右車灯
	int x3=200,y3=50;			//上下歩灯
	int num1=0,num2=0,num3=0;	//0:無灯, 1:青, 2:黄, 3:赤
	//コンストラクタ
	public SignalNormal(String title){
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(pn);
	}

	//メイン関数
	public static void main(String[] args) {
		//スレッドを動かす
		SignalNormalThread th = new SignalNormalThread();
		th.start();
	}

	//パネル
	class MyPanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			//上下車灯
			g.setColor(Color.white);
			g.fillRect(x1-5,y1-3,size*3+10,size+6);
			if(num1==0){			//無灯
				g.setColor(Color.gray);
				g.fillOval(x1,y1,size,size);
				g.fillOval(x1+size,y1,size,size);
				g.fillOval(x1+2*size,y1,size,size);
			} else if(num1==1){		//青
				g.setColor(Color.gray);
				g.fillOval(x1+size,y1,size,size);
				g.fillOval(x1+2*size,y1,size,size);
				g.setColor(Color.green);
				g.fillOval(x1,y1,size,size);
			} else if(num1==2){		//黄
				g.setColor(Color.gray);
				g.fillOval(x1,y1,size,size);
				g.fillOval(x1+2*size,y1,size,size);
				g.setColor(Color.orange);
				g.fillOval(x1+size,y1,size,size);
			} else if(num1==3){		//赤
				g.setColor(Color.gray);
				g.fillOval(x1,y1,size,size);
				g.fillOval(x1+size,y1,size,size);
				g.setColor(Color.red);
				g.fillOval(x1+2*size,y1,size,size);
			}

			//左右車灯
			g.setColor(Color.white);
			g.fillRect(x2-3,y2-5,size+6,size*3+10);
			if(num2==0){			//無灯
				g.setColor(Color.gray);
				g.fillOval(x2,y2,size,size);
				g.fillOval(x2,y2+size,size,size);
				g.fillOval(x2,y2+2*size,size,size);
			} else if(num2==1){		//青
				g.setColor(Color.gray);
				g.fillOval(x2,y2,size,size);
				g.fillOval(x2,y2+size,size,size);
				g.setColor(Color.green);
				g.fillOval(x2,y2+2*size,size,size);
			} else if(num2==2){		//黄
				g.setColor(Color.gray);
				g.fillOval(x2,y2,size,size);
				g.fillOval(x2,y2+2*size,size,size);
				g.setColor(Color.orange);
				g.fillOval(x2,y2+size,size,size);
			} else if(num2==3){		//赤
				g.setColor(Color.gray);
				g.fillOval(x2,y2+2*size,size,size);
				g.fillOval(x2,y2+size,size,size);
				g.setColor(Color.red);
				g.fillOval(x2,y2,size,size);
			}

			//上下歩灯
			g.setColor(Color.white);
			g.fillRect(x3-3,y3-3,size+6,size*2+8);
			g.setColor(Color.black);
			g.fillRect(x3,y3,size,size);
			g.fillRect(x3,y3+size+2,size,size);
			if(num3==0){			//無灯
				g.setColor(Color.gray);
				g.fillOval(x3+7,y3+2,6,size-4);
				g.fillOval(x3+7,y3+size+4,6,size-4);
			} else if(num3==1){		//青
				g.setColor(Color.gray);
				g.fillOval(x3+7,y3+2,6,size-4);
				g.setColor(Color.green);
				g.fillOval(x3+7,y3+size+4,6,size-4);
			} else if(num3==2){		//赤
				g.setColor(Color.gray);
				g.fillOval(x3+7,y3+size+4,6,size-4);
				g.setColor(Color.red);
				g.fillOval(x3+7,y3+2,6,size-4);
			}
		}
	}
}

//スレッド
class SignalNormalThread extends Thread{
	//スレッド内容
	public void run(){
		SignalNormal frm = new SignalNormal("信号機シミュレーション");
		frm.setSize(300,300);
		frm.setVisible(true);

		try{
			Thread.sleep(1000);
			for(;;){
				//上下青
				frm.num1 = 1;
				frm.num2 = 3;
				frm.num3 = 1;
				frm.repaint();
				Thread.sleep(6000);

				//上下歩灯点滅
				for(int i=0;i<14;i++){
					if(i%2 == 0){
						frm.num3 = 0;
					} else {
						frm.num3 = 1;
					}
					frm.repaint();
					Thread.sleep(250);
				}

				//上下歩灯赤
				frm.num3 = 2;
				frm.repaint();
				Thread.sleep(2200);

				//上下車灯黄
				frm.num1 = 2;
				frm.repaint();
				Thread.sleep(2500);

				//上下車灯赤
				frm.num1 = 3;
				frm.repaint();
				Thread.sleep(2500);


				//左右車灯青
				frm.num2 = 1;
				frm.repaint();
				Thread.sleep(6000);

				//左右車灯黄
				frm.num2 = 2;
				frm.repaint();
				Thread.sleep(2500);

				//左右車灯赤
				frm.num2 = 3;
				frm.repaint();
				Thread.sleep(2500);

			}

		} catch(Exception e){
			//エラー時は何もしない
		}
	}
}


