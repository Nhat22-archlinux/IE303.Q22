import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JPanel implements ActionListener {

    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    Bird bird;
    ArrayList<Pipe> pipes = new ArrayList<>();
    Random random = new Random();

    int birdStartX = 50;
    int birdStartY = 250;
    int birdWidth = 34;
    int birdHeight = 24;

    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeXVelocity = -4;

    int gravity = 1;
    int jumpStrength = -12;

    Timer gameLoop;
    Timer placePipesTimer;

    int score = 0;
    boolean gameStarted = false;
    boolean gameOver = false;

    JButton startButton;
    JButton restartButton;

    public Main() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        setLayout(null);

        backgroundImage = new ImageIcon("assets/flappybirdbg.png").getImage();
        birdImage = new ImageIcon("assets/flappybird.png").getImage();
        topPipeImage = new ImageIcon("assets/toppipe.png").getImage();
        bottomPipeImage = new ImageIcon("assets/bottompipe.png").getImage();

        bird = new Bird(birdStartX, birdStartY, birdWidth, birdHeight);

        startButton = new JButton("Start");
        startButton.setBounds(130, 280, 100, 40);
        add(startButton);

        restartButton = new JButton("Restart");
        restartButton.setBounds(120, 350, 120, 40);
        restartButton.setVisible(false);
        add(restartButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                requestFocusInWindow();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
                requestFocusInWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (gameStarted && !gameOver &&
                        (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER)) {
                    bird.velocityY = jumpStrength;
                }
            }
        });

        gameLoop = new Timer(1000 / 60, this);

        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted && !gameOver) {
                    placePipes();
                }
            }
        });
    }

    public void startGame() {
        gameStarted = true;
        gameOver = false;
        score = 0;
        pipes.clear();
        bird = new Bird(birdStartX, birdStartY, birdWidth, birdHeight);

        startButton.setVisible(false);
        restartButton.setVisible(false);

        gameLoop.start();
        placePipesTimer.start();
        repaint();
    }

    public void restartGame() {
        gameStarted = true;
        gameOver = false;
        score = 0;
        pipes.clear();
        bird = new Bird(birdStartX, birdStartY, birdWidth, birdHeight);

        restartButton.setVisible(false);

        gameLoop.start();
        placePipesTimer.start();
        repaint();
    }

    public void placePipes() {
        int randomPipeY = -(pipeHeight / 4) - random.nextInt(pipeHeight / 2);
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImage, boardWidth, randomPipeY, pipeWidth, pipeHeight);
        Pipe bottomPipe = new Pipe(bottomPipeImage, boardWidth, randomPipeY + pipeHeight + openingSpace, pipeWidth, pipeHeight);

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    public void move() {
        bird.velocityY += gravity;
        bird.y += bird.velocityY;

        if (bird.y < 0) {
            bird.y = 0;
            bird.velocityY = 0;
        }

        if (bird.y + bird.height >= boardHeight) {
            bird.y = boardHeight - bird.height;
            endGame();
        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += pipeXVelocity;

            if (!pipe.passed && pipe.image == topPipeImage && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score++;
            }

            if (collision(bird, pipe)) {
                endGame();
            }
        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            if (pipe.x + pipe.width < 0) {
                pipes.remove(i);
                i--;
            }
        }
    }

    public void endGame() {
        gameOver = true;
        gameLoop.stop();
        placePipesTimer.stop();
        restartButton.setVisible(true);
        repaint();
    }

    public boolean collision(Bird bird, Pipe pipe) {
        return bird.x < pipe.x + pipe.width &&
                bird.x + bird.width > pipe.x &&
                bird.y < pipe.y + pipe.height &&
                bird.y + bird.height > pipe.y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        if (gameStarted) {
            g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

            for (Pipe pipe : pipes) {
                g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("Score: " + score, 20, 40);
        }

        if (!gameStarted) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("Flappy Bird", 95, 200);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Nhan Start de bat dau", 90, 240);
        }

        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER", 85, 260);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Final Score: " + score, 95, 310);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
            repaint();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        Main gamePanel = new Main();

        frame.add(gamePanel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gamePanel.requestFocusInWindow();
    }
}