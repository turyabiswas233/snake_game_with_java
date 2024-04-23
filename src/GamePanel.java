import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int UNIT_SIZE = 20;
	static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

	final int[] x = new int[NUMBER_OF_UNITS];
	final int[] y = new int[NUMBER_OF_UNITS];

	int length = 5;
	int foodEaten;
	int foodX;
	int foodY;
	int delayer = 60;
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
		foodEaten = 0;
		running = true;
		delayer = 100;
		addFood();

		timer = new Timer(100, this);
		timer.start();

		initSnake();
	}

	@Override
	public void paintComponent(Graphics graphics) {

		super.paintComponent(graphics);
		draw(graphics);
		makeWall(graphics);
		drawIndicator(graphics);

	}

	public void initSnake() {
		x[0] = 5 * UNIT_SIZE;
		y[0] = 5 * UNIT_SIZE;

		for (int i = length; i > 0; i--) {
			x[i] = x[0];
			y[i] = x[0] - UNIT_SIZE * i;
		}
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

			// design food
			graphics.setColor(Color.yellow);
			graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

			// design snake
			graphics.setColor(Color.white);
			graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
			graphics.setColor(Color.red);
			graphics.fillRect(x[0] + 1, y[0] + 1, UNIT_SIZE / 4, UNIT_SIZE / 4);
			for (int i = 1; i < length; i++) {
				graphics.setColor(new Color(140, 100 + UNIT_SIZE % (i), 150 + UNIT_SIZE % i));
				graphics.fillOval(x[i], y[i], UNIT_SIZE - 2, UNIT_SIZE - 2);
			}
		} else {
			gameOver(graphics);
		}

	}

	public void addFood() {
		foodX = UNIT_SIZE + random.nextInt((WIDTH / UNIT_SIZE) - UNIT_SIZE) * UNIT_SIZE;
		foodY = UNIT_SIZE + random.nextInt((HEIGHT / UNIT_SIZE) - UNIT_SIZE) * UNIT_SIZE;
	}

	public void checkHit() {
		for (int i = length; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
				break;
			}
		}

		// check if head run into walls
		if (x[0] < UNIT_SIZE || x[0] > WIDTH - UNIT_SIZE || y[0] < UNIT_SIZE || y[0] > HEIGHT - UNIT_SIZE) {
			running = false;
		}

		if (!running)
			timer.stop();
	}

	public void gameOver(Graphics graphics) {
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());

		graphics.drawString("|---GAME OVER---|", (WIDTH - metrics.stringWidth("|---GAME OVER---|")) / 2, HEIGHT / 2);
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 50 / 2));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
				HEIGHT / 4);

	}

	public void drawIndicator(Graphics graphics) {
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 15));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("↑", WIDTH - 3 * UNIT_SIZE, 3 * UNIT_SIZE);
		graphics.drawString("↓", WIDTH - 3 * UNIT_SIZE, 4 * UNIT_SIZE);
		graphics.drawString("←", WIDTH - 4 * UNIT_SIZE, 4 * UNIT_SIZE);
		graphics.drawString("→", WIDTH - graphics.getFont().getSize() * 2 - UNIT_SIZE, 4 * UNIT_SIZE);
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		if (running) {
			move();
			checkFood();
			checkHit();
		}

		repaint();
	}

	public void speedUp() {
		if (delayer > 60)
			delayer -= 30;
	}

	public void speedDown() {
		if (delayer < 500)
			delayer += 30;
	}

	public void makeWall(Graphics graphics) {
		for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
			for (int j = 0; j < WIDTH / UNIT_SIZE; j++) {
				if (i == 0 || i == (HEIGHT / UNIT_SIZE - 1)) {
					graphics.setColor(Color.green);
					graphics.fillRect(UNIT_SIZE * j, i * UNIT_SIZE, UNIT_SIZE - 2, UNIT_SIZE - 2);
				} else if (j == 0 || (j == WIDTH / UNIT_SIZE - 1)) {
					graphics.setColor(Color.green);
					graphics.fillRect(UNIT_SIZE * j, i * UNIT_SIZE, UNIT_SIZE - 2, UNIT_SIZE - 2);
				}

			}
		}
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R')
						direction = 'L';
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L')
						direction = 'R';
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D')
						direction = 'U';
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U')
						direction = 'D';
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
