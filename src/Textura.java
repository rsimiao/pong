
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rsimiao
 */
public class Textura {
    private int idTextura[];
    private Texture texture;
    private int t;
    private float width;
    private float height;
    private int filtro;
    private int modo;
    private int wrap;
    
    //Construtor da Classe Textura
    public Textura(int qtdTexturas)
    {
        idTextura = new int[qtdTexturas];
    }
    
    //getters/setters
    public float getWidth(){    	
    	return width;
    }
    
    public float getHeight(){    	
    	return height;
    }
    
    public void setFiltro(int filtro){
    	this.filtro = filtro;
    }
    public float getFiltro(){    	
    	return filtro;
    }
    
    public void setModo(int modo){
    	this.modo = modo;
    }
    public float getModo(){    	
    	return modo;
    }
    
    public void setWrap(int wrap){
    	this.wrap = wrap;
    }
    public float getWrap(){    	
    	return wrap;
    }
    
    /**Método para gerar a textura - filtros e resulocao
     * 
     * @param gl GL2 - Contexto opengl
     * @param fileName String - Localizacao do arquivo de imagem
     * @param indice int - Indice da imagem
     */ 
    public void gerarTextura(GL2 gl, String fileName, int indice) {
        //carrega varias texturas
        //gl.glGenTextures(idTextura.length, idTextura, 0);
    	
    	gl.glGenTextures(indice, idTextura, 0);
        carregarImagem(fileName);        
        texture.bind(gl);
        
        // Define os filtros de aumento e reducao
        //GL_NEAREST ou GL_LINEAR        
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, filtro);
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, filtro);

        //GL.GL_REPEAT ou GL.GL_CLAMP
        texture.setTexParameteri( gl, GL2.GL_TEXTURE_WRAP_S, wrap );
        texture.setTexParameteri( gl, GL2.GL_TEXTURE_WRAP_T, wrap );
        
        //GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, modo);
    }

    /**Metodo para carregar o arquivo da textura
     * 
     * @param fileName String - Localizacao do arquivo de imagem
     */    
    private void carregarImagem(String fileName)
    {
    	//carrega o arquivo da imagem
    	try {    		
    		texture = TextureIO.newTexture(new File( fileName ),true);                
        } catch (IOException e) {
        	System.out.println("\n=============\nErro na leitura do arquivo " 
        						+ fileName + "\n=============\n");
        }

    	//Obtem a largura e altura da imagem
    	this.width = texture.getWidth();
    	this.height = texture.getHeight();    	
    }//fim loadImage

    /**Metodo para habilitar o uso da textura
     * 
     * @param gl GL2 - Contexto opengl
     */
    public void habilitarTextura(GL2 gl)
    {
        gl.glEnable(GL2.GL_TEXTURE_2D);
    }

    /**Metodo para habilitar o uso da textura automatica
     * 
     * @param gl GL2 - Contexto opengl
     * @param genModo - Modo de geracao da textura
     */
    public void habilitarTexturaAutomatica(GL2 gl, int genModo)
    {
        habilitarTextura(gl);
    
        float planoS[] = {1.0f, 0.0f, 0.0f, 0.0f};
        float planoT[] = {0.0f, 0.0f, 1.0f, 0.0f};
        
        gl.glEnable(GL2.GL_TEXTURE_GEN_S); // Habilita a geracao da textura
        gl.glEnable(GL2.GL_TEXTURE_GEN_T);

        //GL.GL_EYE_LINEAR ou GL_OBJECT_LINEAR ou GL_SPHERE_MAP
        gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, genModo);
        gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, genModo);
        
        //GL_EYE_PLANE ou GL_OBJECT_PLANE
        gl.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, planoS, 0);
        gl.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, planoT, 0); 
    }

    /**Metodo para desabilitar o uso de textura automatica
     * 
     * @param gl GL2 - Contexto opengl
     */
    public void desabilitarTexturaAutomatica(GL2 gl )
    {
        //desabilita o uso de textura
        gl.glDisable(GL2.GL_TEXTURE_GEN_S);
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
        desabilitarTextura(gl);
    }

    /**Metodo para desabilitar o uso de textura
     * 
     * @param gl GL2 - Contexto opengl
     */
    public void desabilitarTextura(GL2 gl)
    {
        gl.glDisable(GL2.GL_TEXTURE_2D);	//	Desabilita uso de textura
    }
}