package make_game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Block_game {
	
	static class myframe extends JFrame { //�̳� Ŭ����,JFrame ���
		
		//constant ��� //����� ���� �빮�ڷ� �ѵ�. ����ص�
		static int ball_width = 20; //���� ũ��
		static int ball_height = 20; //���� ����
		static int block_rows = 5; //�� ����
		static int block_columns = 10; //���� ����
		static int block_width = 40; // ���� �ʺ�
		static int block_height = 20; //���� ����
		static int block_gap = 3; //������ �Ÿ�
		static int bar_width = 80; //�Ʒ� �� �� �ʺ�
		static int bar_height = 20; //���� ����
		static int canvas_width = 400 + (block_gap*block_columns) - block_gap; //ȭ���� ũ��(������ �� ũ��, ������ �Ÿ�)
		static int canvas_height = 600;
		
		
		//variable ����
		static mypanel mypanel = null; //ĵ������ �׸� ����?
		static int score = 0; //����
		static Timer timer = null;
		static block[][] block = new block[block_rows][block_columns]; //����� �ִ� ����ŭ�� 2���迭
		static bar bar = new bar();
		static ball ball = new ball();
		static int barxtarget = bar.x; // targetx = ����?
		static int dir = 0; //0:�� ����Ʈ, 1:�ٿ� ����Ʈ, 2:�� ����Ʈ, 3:�ٿ� ����Ʈ ���� �����̴� ����?
		static int ballspeed = 5; //���� �ӵ�
		
		
		
		static class ball {
			int x = canvas_width/2 - ball_width/2; //���� �߰��� �־ ĵ����/2 �ٶ� �� ������ �־�� �ؼ� -��ũ��/2
			int y = canvas_height/2 - ball_height/2;
			int width = ball_width;
			int height = ball_height; //��ġ ��
		}
		
		static class bar {
			int x = canvas_width/2 - bar_width/2;
			int y = canvas_height-100; //��ġ
			int width = bar_width;
			int height = bar_height;
		}
		static class block{
			int x; // ��� ���߿� for������ �� ���鲨�Ӥ���
			int y;
			int width = block_width;
			int height = block_height;
			int color = 0; // 0:��(10��) 1:���(20��) 2:�Ķ�(30��) 3:����Ÿ 4:����
			boolean ishidden = false; //�浹�� �����
		}
		
		static class mypanel extends JPanel { //canvas for draw
			public mypanel() {
				this.setSize(canvas_width,canvas_height);
				this.setBackground(Color.BLACK);
			}
			@Override
			public void paint(Graphics g) { //�������̵� 
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g;
				
				drawUI(g2d); //���� �׸��� �ڵ絥 �װ� �ʹ� �� �Ž�� �ϳ� ���絥				
			}
			private void drawUI(Graphics2D g2d) {
				//draw block
				for(int i =0; i <block_rows;i++) {
					for(int j = 0; j<block_columns; j++) {
						if(block[i][j].ishidden) {
							continue;
						}
						if(block[i][j].color == 0) { //ȭ��Ʈ
							g2d.setColor(Color.WHITE);
						}
						else if(block[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}
						else if(block[i][j].color == 2) {
							g2d.setColor(Color.BLUE);
						}
						else if(block[i][j].color == 3 ) {
							g2d.setColor(Color.MAGENTA);
						}
						else if(block[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						g2d.fillRect(block[i][j].x, block[i][j].y, 
								block[i][j].width, block[i][j].height);
						//�׸�? �޸� �θ��� �Լ�
					}
					
					//draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman",Font.BOLD,20));//�۾�ü, ����?, ������
					g2d.drawString("Score : " + score, canvas_width/2-30,20) ;
					
					//draw ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, ball_width, ball_height); //Ÿ������ ����� �Ž�� fillOval
					
					//darw bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}
		
		public myframe(String title) { //������
			super(title); // �Ʒ����� �޾ƿ� ���ڿ�
			this.setVisible(true);
			this.setSize(canvas_width,canvas_height); //������
			this.setLocation(400,300); // ������ â�� ��ġ
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initDate(); //������ �ʱ�ȭ ���شٴµ�
			
			mypanel = new mypanel();
			this.add("Center",mypanel);
			
			setkeylistener();
		}
		public void initDate() {
			for(int i =0; i <block_rows;i++) {
				for(int j = 0; j<block_columns; j++) {
					block[i][j] = new block();
					block[i][j].x = block_width*j+block_gap*j;
					block[i][j].y = 100+i*block_height + block_gap*i;
					block[i][j].width = block_width;
					block[i][j].height = block_height;
					block[i][j].color = 4-i;
					block[i][j].ishidden = false;
				}
			}
		}
		public void setkeylistener() {
			this.addKeyListener( new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { // Ű���� �Է°��� ��ü e�� ���� ����
					if( e.getKeyCode() == KeyEvent.VK_LEFT) {
						// System.out.println("Pressed Left Key");
						barxtarget -= 20;
						if(bar.x<barxtarget) {
							barxtarget = bar.x;
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						//System.out.println("Pressed Right Key");
						barxtarget += 20;
						if(bar.x > barxtarget) {
							barxtarget = bar.x;
						}
					}
				}
			});
		}
		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { //Ÿ�̸� �̺�Ʈ
					movement();
					chekCollision();
					checkCollisionBlock();
					mypanel.repaint(); // redraw?
				}
			});
			timer.restart(); // ��ŸƮ Ÿ��
		}
		public void movement() {
			if( bar.x < barxtarget) {
				bar.x += 5;
				System.out.println(".");
			}
			else if( bar.x > barxtarget ) {
				bar.x -= 5;
			}
			if(dir==0) { //0 : Up-Right
				ball.x += ballspeed;
				ball.y -= ballspeed;
			}else if(dir==1) { //1 : Down-Right
				ball.x += ballspeed;
				ball.y += ballspeed;
			}else if(dir==2) { //2 : Up-Left
				ball.x -= ballspeed;
				ball.y -= ballspeed;
			}else if(dir==3) { //3 : Down-Left
				ball.x -= ballspeed;
				ball.y += ballspeed;
			}
		}
		public void chekCollision() {
			
		}
		public void checkCollisionBlock() {
			
		}
	}

	public static void main(String[] args) {
		new myframe("Block game"); //Ÿ��Ʋ
		
	}

}
