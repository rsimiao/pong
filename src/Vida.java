
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rsimiao
 */
public class Vida {

    private Textura textura = null;
    private int totalTextura = 1;
    private int filtro = GL2.GL_LINEAR;
    private int wrap = GL2.GL_REPEAT;
    private int modo = GL2.GL_MODULATE;
    public static final String LIFE = "assets/beer.png";

    private GL2 gl;

    public Vida(GL2 gl) {
        this.gl = gl;
        textura = new Textura(totalTextura);
    }

    public void draw() {

        gl.glPushMatrix();
        textura.habilitarTextura(gl);
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        textura.gerarTextura(gl, LIFE, 0);
        primitive();
        textura.desabilitarTextura(gl);
        gl.glPopMatrix();

    }

    public void primitive() {

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(50f, 0.8f);//TOPO ESQUERDA
        gl.glVertex2f(1f, 0.8f); //TODO DIREITA
        gl.glVertex2f(1f, 0.65f); //CHAO DIREITA
        gl.glVertex2f(0.40f, 0.65f); // CHAO ESQUERDA
        gl.glEnd();

    }

}
