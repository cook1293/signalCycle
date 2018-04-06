package signalCycle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 押しボタン式信号交差点の信号サイクルアプリケーション
 * @author cook1293
 */

public class SignalButton extends JFrame{

	MyPanel pn = new MyPanel();
	int size=20;
	int button=0,btstring=0;	//ボタンが押されたフラグ, 文字列表示フラグ
	int x2=50,y2=130;			//左右車灯
	int x3=120,y3=50;			//上下歩灯
	int num2=0,num3=0;			//0:無灯, 1:青, 2:黄, 3:赤
	int btx,bty;
	long time1,time2,time;		//車灯黄点滅開始時刻, ボタンを押した時刻, 車灯の進行可能時間

	//コンストラクタ
	public SignalButton(String title){
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(pn);
	}

	//メイン関数
	public static void main(String[] args) {
		//スレッドを動かす
		SignalButtonThread th = new SignalButtonThread();
		th.start();
	}

	//パネル
	class MyPanel extends JPanel{

		MyPanel(){
			addMouseListener(new MyListener());		//マウスリスナーの登録
		}

		public void paintComponent(Graphics g){
			super.paintComponent(g);

			//押ボタン
			g.setColor(Color.orange);
			g.fillRect(x3,200,50,60);
			g.setColor(Color.black);
			g.fillRect(x3,210,50,15);
			g.setColor(Color.red);
			g.fillOval(x3+15,235,20,20);

			//押ボタン文字列
			if(btstring==1 && button==1){
				g.setColor(Color.red);
				g.setFont(new Font("SansSerif",Font.PLAIN,7));
				g.drawString("おまちください",x3,220);
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
		class MyListener implements MouseListener{
			public void mouseClicked(MouseEvent me){

				btx = me.getX();
				bty = me.getY();
				//ボタンを押したときの動作
				if(btx>=x3+15 && btx<=x3+35){
					if(bty>=235 && bty<=255){
						if(button==0 && btstring==0){
							button = 1;
							btstring = 1;
						}
					}
				}
			}
			public void mouseEntered(MouseEvent me){}
			public void mouseExited(MouseEvent me){}
			public void mousePressed(MouseEvent me){}
			public void mouseReleased(MouseEvent me){}
		}
	}
}

//スレッド
class SignalButtonThread extends Thread{
	//スレッド内容
	public void run(){
		SignalButton frm = new SignalButton("押ボタン式信号機シミュレーション");
		frm.setSize(300,350);
		frm.setVisible(true);

		try{
			Thread.sleep(1000);
			for(;;){
				frm.time1 =  System.currentTimeMillis();		//車灯黄点滅開始時刻
				//左右車灯黄点滅
				frm.num3 = 2;
				frm.num2 = 0;
				frm.repaint();
				Thread.sleep(700);
				while(frm.button == 0){
					frm.num2 = 2;
					frm.repaint();
					Thread.sleep(700);
					frm.num2 = 0;
					frm.repaint();
					Thread.sleep(700);
				}

				//左右車灯青
				frm.num2 = 1;
				frm.repaint();
				Thread.sleep(3000);

				//サイクルに周期を持たせることで、連続でボタンが押されても、
				//一定の時間分は信号が切り替わらないようにしている。
				do{
					frm.time2 = System.currentTimeMillis();	//ボタンを押した時刻
					frm.time = frm.time2 - frm.time1;
					Thread.sleep(500);
				}while(frm.time < 15000);

				//左右車灯黄
				frm.num2 = 2;
				frm.repaint();
				Thread.sleep(2500);

				//左右車灯赤
				frm.num2 = 3;
				frm.repaint();
				Thread.sleep(2500);

				//上下歩灯青
				frm.num3 = 1;
				frm.btstring = 0;
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
				frm.button = 0;
				frm.repaint();
				Thread.sleep(2500);

			}

		} catch(Exception e){
			//エラー時は何もしない
		}
	}
}



