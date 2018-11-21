
import com.jogamp.opengl.GL2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rsimiao
 */
public class Fundo {

    private Textura textura = null;
    private int totalTextura = 1;
    private int filtro = GL2.GL_LINEAR;
    private int wrap = GL2.GL_CLAMP_TO_EDGE;
    private int modo = GL2.GL_MODULATE;
    public static final String STATIC_BUILDS = "assets/predios_0.png";
    public static final String FAR_BUILDS = "assets/predios_1.png";
    public static final String BACK_BUILDS = "assets/predios_2.png";

    private GL2 gl;

    public Fundo(GL2 gl) {

        this.gl = gl;

        textura = new Textura(totalTextura);

    }

    public void build() {

        plano(BACK_BUILDS);
        plano(FAR_BUILDS);
        plano(STATIC_BUILDS);
    }

    public void plano(String imagem) {
        gl.glPushMatrix();
        textura.habilitarTextura(gl);
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);
        textura.gerarTextura(gl, imagem, 0);

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-1, 1);//TOPO ESQUERDA

        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1); //TODO DIREITA

        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, -1); //CHAO DIREITA

        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-1, -1f); // CHAO ESQUERDA
        gl.glEnd();

        textura.desabilitarTextura(gl);
        gl.glPopMatrix();
    }

}
