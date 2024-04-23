import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	static final int WIDTH = 500;
	static final int HEIGHT = 500;
	static final int UNIT_SIZE = 15;
	static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

	final int[] x = new int[NUMBER_OF_UNITS];
	final int[] y = new int[NUMBER_OF_UNITS];

	int length = 5;
	int foodEaten;
	int foodX;
	int foodY;
	int delayer = 200;
	char direction = 'D';
	boolean running = false;
	Random random;
	Timer timer;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		play();
	}

	public void play() {
		addFood();
		running = true;

		timer = new Timer(80, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}


	public void move() {

		for (int i = length; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		if (direction == 'L') {
			x[0] -= UNIT_SIZE;
		} else if (direction == 'R') {
			x[0] += UNIT_SIZE;
		} else if (direction == 'U') {
			y[0] -= UNIT_SIZE;
		} else
			y[0] += UNIT_SIZE;
	}

	public void checkFood() {
		if (x[0] == foodX && y[0] == foodY) {
			length++;
			foodEaten++;
			addFood();
		}
	}

	public void draw(Graphics graphics) {
		if (running) {
			timer.setDelay(delayer);
			/*
				print snake game title
			 */
			graphics.setColor(new Color(200, 100, 255, 60));
			graphics.setFont(new Font("Sans serif", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Welcome to TB Snake Home", (WIDTH - metrics.stringWidth("Welcome to TB Snake Home")) / 2, graphics.getFont().getSize() * 2 + 5);


			graphics.setColor(new Color(250, 90, 90));
			graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);


			graphics.setColor(Color.white);
			graphics.fillRect(x[0], y[0], UNIT_SIZE-2 , UNIT_SIZE -2);
			for (int i = 1; i < length; i++) {
				graphics.setColor(new Color(140, 200, 250));
				graphics.fillOval(x[i], y[i], UNIT_SIZE-2 , UNIT_SIZE-2 );
			}

			graphics.setColor(Color.white);
			graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
			metrics = getFontMetrics(graphics.getFont());

			graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
		} else {
			gameOver(graphics);
		}

	}

	public void addFood() {
		foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
		foodY = random.nextInt((HEIGHT) / UNIT_SIZE) * UNIT_SIZE;
	}

	public void checkHit() {
		for (int i = length; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}

		//check if head run into walls
		if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
			running = false;
		}

		if (!running) timer.stop();
	}

	public void gameOver(Graphics graphics) {
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());

		graphics.drawString("|---GAME OVER---|", (WIDTH - metrics.stringWidth("|---GAME OVER---|")) / 2, HEIGHT / 2);
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 50 / 2));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkFood();
			checkHit();
		}
		repaint();

	}

	public void speedUp() {
		if (delayer > 50) delayer -= 25;
	}

	public void speedDown() {
		if (delayer < 500) delayer += 25;
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R') direction = 'L';
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') direction = 'R';
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D') direction = 'U';
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U') direction = 'D';
					break;
				case KeyEvent.VK_PAGE_UP:
					speedUp();
					break;
				case KeyEvent.VK_PAGE_DOWN:
					speedDown();
					break;
				default:
					break;
			}
		}
	}
}

































