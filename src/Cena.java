/*
 * Ricardo - 21049326
 * Computação grafica - Prof. Solange * 
 */

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.applet.AppletAudioClip;
import sun.audio.AudioPlayer;

/**
 *
 * @author rsimiao
 */
public class Cena implements GLEventListener, KeyListener {

    private static float x = 0;

    private float cx = 0, cy = 0;
    private float ctx, cty;
    private static boolean isMovingRight = false, isMovingLeft = false;
    private float movementVelocity = 0.025f;
    private final int MAX_SCORE = -11;
    int score = 0;
    TextRenderer scoreText;
    private boolean gameover = false;
    //Referencia para classe Textura
    private Fundo fundo = null;

    /**
     * Initialization of score text and random direction for ball.
     */
    public void init(GLAutoDrawable drawable) {
        playSound("intro.mid");
        GL2 gl = drawable.getGL().getGL2();

        scoreText = new TextRenderer(new Font("Courier new", Font.BOLD, 72));
        fundo = new Fundo(gl);

        //habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);

        randomDirection();

    }

    public static synchronized void playSound(final String music) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    String workingDir = System.getProperty("user.dir");
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            new File(workingDir + "/audio/" + music));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void writeTextS(String text, int x, int y) {
        TextRenderer prompt = new TextRenderer(new Font("SansSerif", Font.BOLD, 10));

        prompt.beginRendering(600, 600);
        prompt.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        prompt.draw(text, x, y);
        prompt.endRendering();

    }

    public void writeText(String text, int x, int y) {
        TextRenderer prompt = new TextRenderer(new Font("SansSerif", Font.BOLD, 10));

        prompt.beginRendering(600, 600);
        prompt.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        prompt.draw(text, x, y);
        prompt.endRendering();

        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cena.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Renders all necessary objects and checks if the game is over.
     */
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gameover = checkGameOver();

        if (gameover) {
            gameOver();
            return;
        }

        scoreText.beginRendering(850, 768);
        scoreText.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreText.draw(Integer.toString(score), 408, 675);
        scoreText.endRendering();

        barra(drawable);
        bolinha(drawable);
        vida(drawable);

        // fundo.build();
        cx += ctx;
        cy += cty;
    }

    public void barra(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        if (isMovingRight && x <= 0.85) {
            x += movementVelocity;
        } else if (isMovingLeft && x >= -0.85) {
            x -= movementVelocity;
        }

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0.20f + x, -0.95f);//TOPO ESQUERDA
        gl.glVertex2f(-0.20f + x, -0.95f); //TODO DIREITA
        gl.glVertex2f(-0.20f + x, -1f); //CHAO DIREITA
        gl.glVertex2f(0.20f + x, -1f); // CHAO ESQUERDA
        gl.glEnd();
    }

    public void vida(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        new Vida(gl).draw();

    }

    /**
     * Renders the ball based on its current x and y-coordinates.
     *
     * @param drawable
     */
    public void bolinha(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        checkCollision();

        float radius = 0.02f;

        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i <= 180; i++) {
            double angle = 2 * ((Math.PI * i) / 180);
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            gl.glVertex2d(x + cx, y + cy);
        }
        gl.glEnd();
    }

    /**
     * Checks if the ball has collided with either of the users' paddles or if
     * the ball has reached either players' score zone.
     */
    private void checkCollision() {
        //lados

        //topo ta bugado
        if (cx > 0.95) {
            //reset();
            score += 5;
            ///cty *= -1;
            ctx *= -1;

            writeText("entrou na logica 1", 100, 400);
        }
        //chao
        if (cx < -0.95) {
            ctx *= -1;
            // ctx *= -1;
            writeText("entrou na logica 2", 100, 350);
        }

        if (cy > 0.95) {
            cty *= -1;
            writeText("entrou na logica 3", 100, 300);
        }

        if (cy < -0.95) {
             reset();
            cty *= 1;
            writeText("entrou na logica 4", 100, 250);
        }

//        //cond1
//        if (cx <= -0.9 && ((cy >= -0.1f + x) && (cy <= 0.1f + x))) {
//            ctx *= -1;
//        }
//
//        //cond2
//        if (cx >= 0.9 && ((cy >= -0.1f + x) && (cy <= 0.1f + x))) {
//            ctx *= -1;
//        }
        if (cy <= -0.95 && ((cx >= -0.1f + x) && (cx <= 0.1f + x))) {
            cty *= -1;
            writeText("entrou na logica 2", 100, 200);
        }

        //ta bugado
        if (cy >= 1 || cy <= -1) {
            cty *= -1;
            score += 5;

            writeText("entrou na logica 3", 100, 150);
        }

        writeTextS(String.format("cx:%f%n cy:%f%n ctx:%f%n cty:%f%n", cx, cy, ctx, cty), 100, 500);

    }

    /**
     * Resets the ball's x and y-coordinates and each players' y-coordinates.
     * Used after every point scored.
     */
    private void reset() {
        cx = 0;
        cy = 0;
        x = 0;
        randomDirection();
    }

    /**
     * If the game is over this method is called to display the game over screen
     * and to display the winner.
     */
    private void gameOver() {
        TextRenderer prompt = new TextRenderer(new Font("SansSerif", Font.BOLD, 72));

        String line1 = "Se fudeu!";
        String line2 = "Jogue mais, acredite nos seus sonhos";

        prompt.beginRendering(1024, 768);
        prompt.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        prompt.draw(line1, 300, 500);
        prompt.draw(line2, 200, 300);
        prompt.endRendering();
    }

    /**
     * Checks if either player has reached the winning score.
     *
     * @return
     */
    private boolean checkGameOver() {
        if (score == MAX_SCORE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Randomly selects a direction for the ball to go.
     */
    private void randomDirection() {
        Random random = new Random();
        int direction = random.nextInt(3);

        if (direction == 0) {
            ctx = 0.01f;
            cty = 0.01f;
        }

        if (direction == 1) {
            ctx = -0.01f;
            cty = 0.01f;
        }

        if (direction == 2) {
            ctx = -0.01f;
            cty = -0.01f;
        }

        if (direction == 3) {
            ctx = 0.01f;
            cty = -0.01f;
        }

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT && x <= 0.85) {
            isMovingRight = true;
        }

        if (key == KeyEvent.VK_LEFT && x >= -0.85) {
            isMovingLeft = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) {
            isMovingRight = false;
        }

        if (key == KeyEvent.VK_LEFT) {
            isMovingLeft = false;
        }
    }

}
