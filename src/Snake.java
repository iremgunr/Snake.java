import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake{
    int width = 500;
    int height = 500;
    int speed = 120;
    int direction = 0;
    int size = 20;
    int lenght = 40;
    int point = 0;
    boolean wait = false;
    boolean resume = true;
    boolean intro = true;
    int over = 0;
    Point feed;
    Point[] snake;
    Timer t;
    JFrame window;
    Platform pl;

    public static void main(String[] args) {

        Snake snake = new Snake();
    }

    public Snake() {
        snake = new Point[lenght];
        for(int i = 0; i < lenght; i++)
            snake[i] = new Point(size * (lenght-i), size);
        feed_snake();
        window = new JFrame("  SNAKE  ");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pl = new Platform();
        window.add(pl);
        window.setSize(width , height);
        window.setResizable(false);
        window.setVisible(true);

        window.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 80) waiting();
                if(intro && e.getKeyCode() == 10) start();
                if(over != 0 && e.getKeyCode() == 10) restart();
                if(!resume) return;
                switch (e.getKeyCode()) {
                    case 87:
                    case 38:
                        if(direction != 2)
                            direction = 3;
                        break;
                    case 65:
                    case 37:
                        if(direction != 0)
                            direction = 1;
                        break;
                    case 83:
                    case 40:
                        if(direction != 3)
                            direction = 2;
                        break;
                    case 68:
                    case 39:
                        if(direction != 1)
                            direction = 0;
                        break;
                }
                resume = false;
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void waiting() {
        if(over != 0 || intro) return;
        if(wait){
            wait = false;
            t.start();
        } else {
            wait = true;
            t.stop();
            pl.repaint();
        }
    }

    private void restart() {
        point = 0;
        direction = 0;
        over = 0;
        lenght = 1;
        snake = new Point[lenght];
        for(int i = 0; i < lenght; i++)
            snake[i] = new Point(size * (lenght-i), size);
        t.start();
        feed_snake();

    }

    public void feed_snake() {
        while(true) {
            Random shake = new Random();
            feed = new Point(shake.nextInt((width / size) - 2) * size, shake.nextInt((height/size) - 2) * size);
            if(feed.x < size || feed.y < size || feed.x > width- (size * 3) - 1 || feed.y > height - (size * 6) - 1) continue;
            boolean smash = false;
            for(int i = 0; i < snake.length; i++) {
                if(snake[i].x == feed.x && snake[i].y == feed.y) {
                    smash = true;
                }
            }
            if(!smash) break;
        }
    }

    public void start() {
        intro = false;
        t.start();
    }

    public class Platform extends JPanel {
        private static final long serialVersionUID = 1L;

        public Platform() {
            this.setPreferredSize(new Dimension(550, 550));
            Dimension screen = getToolkit().getScreenSize();
            window.setLocation((screen.width - width) / 2, (screen.height - height) / 2);

            ActionListener duty = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    int _x = 0;
                    int _y = 0;
                    switch (direction) {
                        case 0:
                            _x = size;
                            break;
                        case 1:
                            _x = size * -1;
                            break;
                        case 2:
                            _y = size;
                            break;
                        case 3:
                            _y = size * -1;
                            break;
                    }

                    for(int i = snake.length - 1; i >= 0; i--) {
                        if(i == 0) {
                            snake[i] = new Point(snake[i].x + _x, snake[i].y + _y);
                        } else {
                            snake[i] = new Point(snake[i-1].x, snake[i-1].y);
                        }
                    }
                    if(smash_control()){
                        t.stop();
                    }

                    feeding_control();

                    resume = true;
                    repaint();
                }

                private void feeding_control() {
                    if(snake[0].x == feed.x && snake[0].y == feed.y) {
                        point = point + 1;
                        feed_snake();
                        grow_snake();
                    }
                }

                private void grow_snake() {
                    Point[] klon = new Point[lenght];
                    klon = snake;
                    lenght++;
                    snake = new Point[lenght];

                    for(int g=0;g<=(klon.length-1);g++)
                    {
                        snake[g]=klon[g];
                    }

                    snake[snake.length-1] = new Point(snake[snake.length-2].x, snake[snake.length-2].y);
                }

                private boolean smash_control() {
                    if(snake[0].x < size || snake[0].y < size || snake[0].x > width - (size * 3) || snake[0].y > height - (size * 6)) {
                        over = 1;
                        return true;
                    }
                    for(int i = 0; i < snake.length; i++) {
                        if(snake[i].x == snake[0].x && snake[i].y == snake[0].y && i != 0) {
                            over = 2;
                            return true;
                        }
                    }
                    return false;
                }
            };

            t = new Timer(speed, duty);
        }

        public void paint(Graphics g) {
            Graphics2D gr = (Graphics2D) g;
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font r = new Font("Bodoni MT Poster Compressed", Font.BOLD, 12);

            if(intro) {
                gr.setColor(Color.white);
                gr.fillRect(0, 0, width, height);
                gr.setColor(Color.pink);
                gr.setFont(r);
                gr.drawString("SNAKE ^_^", 220, 150);
                gr.drawString("WELCOME TO GOKCE AND IREM'S SNAKE GAME, PRESS 'ENTER' TO START =) ", 12, 250);
                gr.drawString("Press 'P' button to pause .",12,300);

                return;
            }
            if(over != 0) {
                gr.setColor(Color.red);
                gr.fillRect(0, 0, width, height);
                gr.setColor(Color.black);
                if(over == 2) {
                    gr.drawString(" You eat yourself :D" , 150, 150);
                    gr.drawString(point + "YOU GET ;)", 100, 100);
                } else {
                    gr.drawString(" LOSER :D ", 100, 130);
                    gr.drawString(point + " YOU GET ;) ", 100, 100);
                }

                gr.drawString(" PRESS ENTER TO RESTART ", 100, 160);
                return;
            }

            gr.setColor(Color.white);
            gr.fillRect(10, 10, width, height);
            gr.setColor(Color.PINK);
            gr.drawRect(size, size, width - (size * 3) - 1, height - (size * 6) - 1);
            gr.drawString(" POINT: " + point, 10, 465);
            for(int i = 0; i < snake.length; i++) {
                gr.drawRect(snake[i].x, snake[i].y, size, size);
            }

            gr.fillOval(feed.x, feed.y, size, size);
            if(wait) {
                gr.setColor(Color.ORANGE);
                gr.fillRect(0, 0, width, height);
                gr.setColor(Color.BLUE);
                gr.drawString(" CONTINUE TO GAME!! ", 50, 150);
                gr.drawString(" PRESS 'P' TO CONTINUE ", 50, 170);

                return;
            }
        }
    }
}