package make_game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Block_game {
	
	static class myframe extends JFrame { //이너 클래스,JFrame 상속
		
		//constant 상수 //상수는 보통 대문자로 한데. 기억해둬
		static int ball_width = 20; //볼의 크기
		static int ball_height = 20; //볼의 높이
		static int block_rows = 5; //블럭 세로
		static int block_columns = 10; //블럭의 가로
		static int block_width = 40; // 블럭의 너비
		static int block_height = 20; //블럭의 높이
		static int block_gap = 3; //블럭간의 거리
		static int bar_width = 80; //아랫 바 의 너비
		static int bar_height = 20; //바의 높이
		static int canvas_width = 400 + (block_gap*block_columns) - block_gap; //화면의 크기(블럭들의 총 크기, 블럭간의 거리)
		static int canvas_height = 600;
		
		
		//variable 변수
		static mypanel mypanel = null; //캔버스를 그릴 역할?
		static int score = 0; //점수
		static Timer timer = null;
		static block[][] block = new block[block_rows][block_columns]; //상수에 있던 값만큼의 2차배열
		static bar bar = new bar();
		static ball ball = new ball();
		static int barxtarget = bar.x; // targetx = 보관?
		static int dir = 0; //0:업 라이트, 1:다운 라이트, 2:업 래프트, 3:다운 래프트 볼이 움직이는 방향?
		static int ballspeed = 5; //공의 속도
		
		
		
		static class ball {
			int x = canvas_width/2 - ball_width/2; //공이 중간에 있어서 캔버스/2 바랑 좀 떨어져 있어야 해서 -볼크기/2
			int y = canvas_height/2 - ball_height/2;
			int width = ball_width;
			int height = ball_height; //위치 값
		}
		
		static class bar {
			int x = canvas_width/2 - bar_width/2;
			int y = canvas_height-100; //위치
			int width = bar_width;
			int height = bar_height;
		}
		static class block{
			int x; // 얘넨 나중에 for문으로 블럭 만들꺼임ㅇㅇ
			int y;
			int width = block_width;
			int height = block_height;
			int color = 0; // 0:흰(10점) 1:노랑(20점) 2:파랑(30점) 3:마젠타 4:빨강
			boolean ishidden = false; //충돌후 사라짐
		}
		
		static class mypanel extends JPanel { //canvas for draw
			public mypanel() {
				this.setSize(canvas_width,canvas_height);
				this.setBackground(Color.BLACK);
			}
			@Override
			public void paint(Graphics g) { //오버라이드 
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g;
				
				drawUI(g2d); //뭔가 그리는 코든데 그게 너무 길어서 매써드 하나 만든데				
			}
			private void drawUI(Graphics2D g2d) {
				//draw block
				for(int i =0; i <block_rows;i++) {
					for(int j = 0; j<block_columns; j++) {
						if(block[i][j].ishidden) {
							continue;
						}
						if(block[i][j].color == 0) { //화이트
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
						//네모? 메모를 부르는 함수
					}
					
					//draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman",Font.BOLD,20));//글씨체, 굴림?, 사이즈
					g2d.drawString("Score : " + score, canvas_width/2-30,20) ;
					
					//draw ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, ball_width, ball_height); //타원형을 만드는 매써드 fillOval
					
					//darw bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}
		
		public myframe(String title) { //생성자
			super(title); // 아래에서 받아온 문자열
			this.setVisible(true);
			this.setSize(canvas_width,canvas_height); //사이즈
			this.setLocation(400,300); // 실행한 창의 위치
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initDate(); //변수들 초기화 해준다는데
			
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
				public void keyPressed(KeyEvent e) { // 키보드 입력값이 객체 e를 통해 들어옴
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
				public void actionPerformed(ActionEvent e) { //타이머 이벤트
					movement();
					chekCollision();
					checkCollisionBlock();
					mypanel.repaint(); // redraw?
				}
			});
			timer.restart(); // 스타트 타임
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
		new myframe("Block game"); //타이틀
		
	}

}
