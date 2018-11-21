/*
 * Ricardo - 21049326
 * Computação grafica - Prof. Solange * 
 */

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.audio.AudioPlayer;

/**
 *
 * @author rsimiao
 */
public class Renderer {

    private static GLWindow window = null;
    public static int screenWidth = 675;
    public static int screenHeight = 675;

    //Cria a janela de rendeziração do JOGL
    public static void init() {

        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setSize(screenWidth, screenHeight);
        //window.setResizable(false);

        Cena cena = new Cena();
        window.addGLEventListener(cena); //adiciona a Cena a Janela  
        window.addKeyListener(cena); //registra o teclado na janela

        //window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //inicia o loop de animação

        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        window.setVisible(true);



    }


    public static void main(String[] args) {
        
        init();
    }
}
