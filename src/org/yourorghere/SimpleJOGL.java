package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;




/**
* SimpleJOGL.java <BR>
* author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
*
* This version is equal to Brian Paul's version 1.2 1999/10/21
*/
public class SimpleJOGL implements GLEventListener {
static BufferedImage image1 = null,image2 = null, image3 = null;
static Texture t1 = null, t2 = null, t3 = null;
static Koparka koparka;

private static float xrot = 0.0f, yrot = 0.0f;
static float x, z;

public static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat�o otaczaj�ce
public static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat�o rozproszone
public static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat�o odbite
public static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja ?wiat�a

public static void main(String[] args) {
Frame frame = new Frame("Simple JOGL Application");
GLCanvas canvas = new GLCanvas();

canvas.addGLEventListener(new SimpleJOGL());
frame.add(canvas);
frame.setSize(640, 480);
final Animator animator = new Animator(canvas);
frame.addWindowListener(new WindowAdapter() {

@Override
public void windowClosing(WindowEvent e) {
// Run this on another thread than the AWT event queue to
// make sure the call to Animator.stop() completes before
// exiting
new Thread(new Runnable() {

public void run() {
animator.stop();
System.exit(0);
}
}).start();
}
});

//Obs�uga klawiszy strza�ek
frame.addKeyListener(new KeyListener()
{
public void keyPressed(KeyEvent e)
{
if(e.getKeyCode() == KeyEvent.VK_UP)
xrot -= 1.0f;
if(e.getKeyCode() == KeyEvent.VK_DOWN)
xrot +=1.0f;
if(e.getKeyCode() == KeyEvent.VK_RIGHT)
yrot += 1.0f;
if(e.getKeyCode() == KeyEvent.VK_LEFT)
yrot -=1.0f;
if(e.getKeyChar() == 'q')
    ambientLight = new float[]{ambientLight[0]-0.1f, ambientLight[0]-0.1f, ambientLight[0]-0.1f, ambientLight[0]-0.1f};
if(e.getKeyChar() == 'w')
    ambientLight = new float[]{ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f};
if(e.getKeyChar() == 'a')
    diffuseLight = new float[]{diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f};
if(e.getKeyChar() == 's')
    diffuseLight = new float[]{diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f};
if(e.getKeyChar() == 'z')
    specular = new float[]{specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f};
if(e.getKeyChar() == 'x')
    specular = new float[]{specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f};
if(e.getKeyChar() == 'k')
    lightPos = new float[]{lightPos[0]-0.1f, lightPos[0]-0.1f, lightPos[0]-0.1f, lightPos[0]-0.1f};
if(e.getKeyChar() == 'l')
    lightPos = new float[]{lightPos[0]+0.1f, lightPos[0]+0.1f, lightPos[0]+0.1f, lightPos[0]+0.1f};

if(e.getKeyChar() == '1')
{
    koparka.kat1-=1.0f;
    if(koparka.kat1<-60.0f)
    {
       koparka.kat1=-60.0f;
    }
    System.out.println(koparka.kat1);
}
if(e.getKeyChar() == '2')
{
    koparka.kat1+=1.0f;
    if(koparka.kat1>30.0f)
    {
       koparka.kat1=30.0f;
    }
    System.out.println(koparka.kat1);
}

if(e.getKeyChar() == '3')
 koparka.kat2-=1.0f;

if(e.getKeyChar() == '4')
 koparka.kat2+=1.0f;

if(e.getKeyChar() == '5')
 koparka.kat3-=1.0f;
if(e.getKeyChar() == '6')
 koparka.kat3+=1.0f;

if(e.getKeyChar() == '7')
{
 koparka.kat4-=1.0f;
System.out.println(koparka.kat4);
}
if(e.getKeyChar() == '8')
{
 koparka.kat4+=1.0f;
System.out.println(koparka.kat4);
}

if(e.getKeyCode() == KeyEvent.VK_F1){
    przesun(-1.0f);
    
}
if(e.getKeyCode() == KeyEvent.VK_F2){
    przesun(1.0f);
}


}
public void keyReleased(KeyEvent e){}
public void keyTyped(KeyEvent e){}
});

// Center frame
frame.setLocationRelativeTo(null);
frame.setVisible(true);
animator.start();
}

public void init(GLAutoDrawable drawable) {
// Use debug pipeline
// drawable.setGL(new DebugGL(drawable.getGL()));

GL gl = drawable.getGL();
System.err.println("INIT GL IS: " + gl.getClass().getName());
//koparka = new Koparka();
// Enable VSync
gl.setSwapInterval(1);

// Setup the drawing area and shading mode
/*gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
gl.glEnable(GL.GL_CULL_FACE);*/

//warto?ci sk�adowe o?wietlenia i koordynaty ?r�d�a ?wiat�a

//(czwarty parametr okre?la odleg�o?� ?r�d�a:
//0.0f-niesko�czona; 1.0f-okre?lona przez pozosta�e parametry)
gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
//ustawienie parametr�w ?r�d�a ?wiat�a nr. 0
gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat�o otaczaj�ce
gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,1); //?wiat�o rozproszone
gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,2); //?wiat�o odbite
gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,3); //pozycja ?wiat�a
gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r�d�a ?wiat�a nr. 0
  gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie �ledzenia kolor�w
        //kolory b�d� ustalane za pomoc� glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno�ci i odblaskowo�ci obiekt�w
        float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo�ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

try
{
image1 = ImageIO.read(getClass().getResourceAsStream("/bok.jpg"));
image2 = ImageIO.read(getClass().getResourceAsStream("/trawa.jpg"));
image3 = ImageIO.read(getClass().getResourceAsStream("/niebo.jpg"));
}
catch(Exception exc)
{
JOptionPane.showMessageDialog(null, exc.toString());
return;
}

t1 = TextureIO.newTexture(image1, false);
t2 = TextureIO.newTexture(image2, false);
t3 = TextureIO.newTexture(image3, false);
gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
 GL.GL_BLEND | GL.GL_MODULATE);
gl.glEnable(GL.GL_TEXTURE_2D);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);

koparka = new Koparka();

}


public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
GL gl = drawable.getGL();
GLU glu = new GLU();

if (height <= 0) { // avoid a divide by zero error!

height = 1;
}
final float h = (float) width / (float) height;
gl.glViewport(0, 0, width, height);
gl.glMatrixMode(GL.GL_PROJECTION);
gl.glLoadIdentity();
glu.gluPerspective(90.0f, h, 0.1, 300.0);
gl.glMatrixMode(GL.GL_MODELVIEW);
gl.glLoadIdentity();


}
//private float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3)
//{
// float[] norm = new float[3];
// float[] wektor0 = new float[3];
// float[] wektor1 = new float[3];
//
// for(int i=0;i<3;i++)
// {
// wektor0[i]=punkty[i+ind1]-punkty[i+ind2];
// wektor1[i]=punkty[i+ind2]-punkty[i+ind3];
// }
//
// norm[0]=wektor0[1]*wektor1[2]-wektor0[2]*wektor1[1];
// norm[1]=wektor0[2]*wektor1[0]-wektor0[0]*wektor1[2];
// norm[2]=wektor0[0]*wektor1[1]-wektor0[1]*wektor1[0];
// float d=
//(float)Math.sqrt((norm[0]*norm[0])+(norm[1]*norm[1])+ (norm[2]*norm[2]) );
// if(d==0.0f)
// d=1.0f;
// norm[0]/=d;
// norm[1]/=d;
// norm[2]/=d;
//
// return norm;
//}
void walec(GL gl)
 {
//wywo�ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta�ci
     
gl.glEnable(GL.GL_NORMALIZE);
float x,y,kat;
gl.glBegin(GL.GL_QUAD_STRIP);
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),0.0f);
gl.glVertex3f(x, y, -1.0f);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
gl.glNormal3f(0.0f,0.0f,-1.0f);
x=y=kat=0.0f;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glVertex3f(x, y, -1.0f);
}
gl.glEnd();
gl.glNormal3f(0.0f,0.0f,1.0f);
x=y=kat=0.0f;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
for(kat = 2.0f*(float)Math.PI; kat > 0.0f ; kat -= (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
}

void stozek(GL gl)
{
//wywo�ujemy automatyczne normalizowanie normalnych
    
gl.glEnable(GL.GL_NORMALIZE);
float x,y,kat;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = (float)Math.sin(kat);
y = (float)Math.cos(kat);
gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),-2.0f);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
for(kat = 2.0f*(float)Math.PI; kat > 0.0f; kat -= (Math.PI/32.0f))
{
x = (float)Math.sin(kat);
y = (float)Math.cos(kat);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
}

void drzewko(GL gl){
gl.glPushMatrix();
gl.glColor3f(0.0f, 0.0f, 0.0f);
walec(gl);
gl.glTranslatef(0.0f, 0.0f, -1.0f);

gl.glColor3f(0.5f, 0.5f, 0.0f);
stozek(gl);

gl.glTranslatef(0.0f, 0.0f, -1.0f);
gl.glScalef(0.7f, 0.7f, 0.7f);
stozek(gl);

gl.glTranslatef(0.0f, 0.0f, -1.0f);
gl.glScalef(0.9f, 0.9f, 0.9f);
stozek(gl);
gl.glPopMatrix();
}

void lasek(GL gl){
    for(int j=0; j<3; j++){

         gl.glTranslatef(0.0f, 2.5f, 0.0f);
         gl.glPushMatrix();
        for(int i=0; i<8; i++){
            gl.glTranslatef(2.0f, 0.0f, 0.0f);
            drzewko(gl);
        }
        gl.glPopMatrix();
    }
       
    
       
                
 }

static void przesun(float d) {
   
    if(x > -100 && z < 100)
    {
        x-=d*Math.sin(yrot*(3.14f/180.0f));
        z+=d*Math.cos(yrot*(3.14f/180.0f));
    }
}
    
void Rysuj(GL gl, Texture t1, Texture t2, Texture t3)
 {
    //szescian
    gl.glColor3f(1.0f,1.0f,1.0f);
    //za�adowanie tekstury wczytanej wcze�niej z pliku krajobraz.bmp
    gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
    
    
    
    gl.glBegin(GL.GL_QUADS);
    //�ciana przednia
    gl.glNormal3f(0.0f,0.0f,-1.0f);
    gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
    gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,100.0f);
    gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
    //�ciana tylnia
    gl.glNormal3f(0.0f,0.0f,1.0f);
    gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
    gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
    gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
    //�ciana lewa
    gl.glNormal3f(1.0f,0.0f,0.0f);
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
    gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
    gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
    gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
    //�ciana prawa
    gl.glNormal3f(-1.0f,0.0f,0.0f);
    gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
    gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(100.0f,-100.0f,100.0f);
    gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
    gl.glEnd();

    //�ciana dolna
    //za�adowanie tekstury wczytanej wcze�niej z pliku niebo.bmp
     gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
     //ustawienia aby tekstura si� powiela�a
     gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
     gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    gl.glBegin(GL.GL_QUADS);
    gl.glNormal3f(0.0f,1.0f,0.0f);
     //koordynaty ustawienia 16 x 16 kwadrat�w powielonej tekstury na �cianie dolnej
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,-100.0f,100.0f);
    gl.glTexCoord2f(0.0f, 16.0f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
    gl.glTexCoord2f(16.0f, 16.0f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
    gl.glTexCoord2f(16.0f, 0.0f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
    gl.glEnd();

     //�ciana gorna
    //za�adowanie tekstury wczytanej wcze�niej z pliku trawa.bmp
    gl.glBindTexture(GL.GL_TEXTURE_2D, t3.getTextureObject());
    gl.glBegin(GL.GL_QUADS);
    gl.glNormal3f(0.0f,-1.0f,0.0f);
    gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
    gl.glTexCoord2f(1.0f, 1.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
    gl.glTexCoord2f(1.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
    gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
    gl.glEnd();
 }
    
    


public void display(GLAutoDrawable drawable) {
GL gl = drawable.getGL();

// Clear the drawing area
gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
// Reset the current matrix to the "identity"
gl.glLoadIdentity();

//gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni�cie o 6 jednostek
gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok� osi X
gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok� osi Y
gl.glTranslatef(0.0f, 80.0f, 0.0f);
gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
//ustawienie parametr�w ?r�d�a ?wiat�a nr. 0
gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat�o otaczaj�ce
gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat�o rozproszone
gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat�o odbite
gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat�a
gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r�d�a ?wiat�a nr. 0
gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor�w

gl.glTranslatef(x, 0.0f, z);
Rysuj(gl, t1, t2, t3);
//drzewko(gl);
//lasek(gl);
gl.glTranslatef(0.0f, -85.0f, 0.0f);
gl.glScalef(4.0f, 4.0f, 4.0f);
koparka.Rysuj(gl);
//gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());

//gl.glColor3f(0.0f, 0.0f, 0.0f);
//walec(gl);
//gl.glTranslatef(0.0f, 0.0f, -1.0f);
//
//
//
//gl.glColor3f(0.5f, 0.5f, 0.0f);
//stozek(gl);
//
//gl.glTranslatef(0.0f, 0.0f, -1.0f);
//gl.glScalef(0.7f, 0.7f, 0.7f);
//stozek(gl);
//
//gl.glTranslatef(0.0f, 0.0f, -1.0f);
//gl.glScalef(0.9f, 0.9f, 0.9f);
//stozek(gl);


//kolory b�d� ustalane za pomoc� glColor
// Move the "drawing cursor" around
//gl.glTranslatef(-1.5f, 1.0f, -6.0f);

// zad.1
/* gl.glBegin(GL.GL_TRIANGLES);
gl.glColor3f(0.5f, 0.5f, 0.0f);
gl.glVertex3f(-1.0f, 1.0f, -6.0f);
gl.glVertex3f(-2.0f,-1.0f, -6.0f);
gl.glVertex3f( 0.0f,-1.0f, -6.0f);
gl.glEnd();

gl.glTranslatef(-1.2f, -2.0f, -6.0f);
//zad2. a i b
gl.glBegin(GL.GL_QUADS); 
gl.glColor3f(1.4f, 0.2f, 0.0f);
gl.glVertex3f(-1.0f, 1.0f, -1.0f);
gl.glVertex3f(1.0f, 1.0f, -1.0f); 
gl.glVertex3f(1.0f, -1.0f, -1.0f);
gl.glVertex3f(-1.0f, -1.0f, -1.0f);

gl.glEnd(); */
//ZAD 4.
/* float x,y,kat;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f,0.0f,-6.0f); //�rodek
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = 1.5f*(float)Math.sin(kat);
y = 1.5f*(float)Math.cos(kat);
gl.glVertex3f(x, y, -1.0f); //kolejne punkty
}
gl.glEnd();*/


/*gl.glBegin(GL.GL_TRIANGLES);
gl.glColor3f(1.0f, 0.0f, 0.0f);    // Set the current drawing color to red
gl.glVertex3f(0.0f, 1.0f, 0.0f);   // Top
gl.glColor3f(0.0f, 1.0f, 0.0f);    // Set the current drawing color to green
gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
gl.glColor3f(0.0f, 0.0f, 1.0f);    // Set the current drawing color to blue
gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
// Finished Drawing The Triangle
gl.glEnd();

// Move the "drawing cursor" to another position
gl.glTranslatef(3.0f, 0.0f, 0.0f);
// Draw A Quad
gl.glBegin(GL.GL_QUADS);
gl.glColor3f(0.5f, 0.5f, 1.0f);    // Set the current drawing color to light blue
gl.glVertex3f(-1.0f, 1.0f, 0.0f);  // Top Left
gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top Right
gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
// Done Drawing The Quad
gl.glEnd();*/

//gl.glBegin(GL.GL_QUADS);
////�ciana przednia
//gl.glColor3f(1.0f,0.0f,0.0f);
//gl.glNormal3f(0.0f,0.0f,1.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//gl.glEnd();
//
//gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
//gl.glBegin(GL.GL_QUADS);
////sciana tylnia
//gl.glColor3f(0.0f,1.0f,0.0f);
//gl.glNormal3f(0.0f,0.0f,-1.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//gl.glEnd();
////�ciana lewa
//gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
//gl.glBegin(GL.GL_QUADS);
//gl.glColor3f(0.0f,0.0f,1.0f);
//gl.glNormal3f(-1.0f,0.0f,0.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//gl.glEnd();
////�ciana prawa
//gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
//gl.glBegin(GL.GL_QUADS);
//gl.glColor3f(1.0f,1.0f,0.0f);
//gl.glNormal3f(1.0f,0.0f,0.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//gl.glEnd();
////�ciana dolna
//gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
//gl.glBegin(GL.GL_QUADS);
//gl.glColor3f(1.0f,0.0f,1.0f);
//gl.glNormal3f(0.0f,-1.0f,0.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
//gl.glEnd();
////�ciana g�rna
//gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
//gl.glBegin(GL.GL_QUADS);
//gl.glColor3f(1.0f,2.0f,1.0f);
//gl.glNormal3f(0.0f,1.0f,0.0f);
//gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
//gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
//gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
//gl.glEnd();

/*gl.glBegin(GL.GL_TRIANGLES);
//trojkat1
gl.glColor3f(1.0f,0.0f,0.0f);

gl.glVertex3f(-1.0f,-1.0f,0.0f);
gl.glVertex3f(0.0f,0.0f, 2.0f);
gl.glVertex3f(-1.0f,1.0f, 0.0f);*/

//gl.glBegin(GL.GL_TRIANGLES);
////�ciana przednia
//float[] scianka1={-1.0f,-1.0f,0.0f, //wp�rz�dne pierwszego punktu
// 0.0f,0.0f, 2.0f, //wsp�rz�dne drugiego punktu
// -1.0f,1.0f, 0.0f}; //wsp�rz�dne trzeciego punktu
//float[] normalna1 = WyznaczNormalna(scianka1,0,3,6);
//gl.glNormal3fv(normalna1,0);
//gl.glVertex3fv(scianka1,0); //wsp�rz�dne 1-go punktu zaczynaj� si� od indeksu 0
//gl.glVertex3fv(scianka1,3); //wsp�rz�dne 2-go punktu zaczynaj� si� od indeksu 3
//gl.glVertex3fv(scianka1,6); //wsp�rz�dne 3-go punktu zaczynaj� si� od indeksu 6
//gl.glEnd();



//trojkat2
/*gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(-1.0f,1.0f, 0.0f);
gl.glVertex3f(0.0f,0.0f, 2.0f);
gl.glVertex3f(1.0f,1.0f, 0.0f);*/
//gl.glBegin(GL.GL_TRIANGLES);
////�ciana przednia
//float[] scianka2={-1.0f,-1.0f,0.0f, //wp�rz�dne pierwszego punktu
// 0.0f,0.0f, 2.0f, //wsp�rz�dne drugiego punktu
// -1.0f,1.0f, 0.0f}; //wsp�rz�dne trzeciego punktu
//float[] normalna2 = WyznaczNormalna(scianka1,0,3,6);
//gl.glNormal3fv(normalna1,0);
//gl.glVertex3fv(scianka1,0); //wsp�rz�dne 1-go punktu zaczynaj� si� od indeksu 0
//gl.glVertex3fv(scianka1,3); //wsp�rz�dne 2-go punktu zaczynaj� si� od indeksu 3
//gl.glVertex3fv(scianka1,6); //wsp�rz�dne 3-go punktu zaczynaj� si� od indeksu 6
//gl.glEnd();

////trojkat3
//gl.glColor3f(0.0f,0.0f,1.0f);
//gl.glVertex3f(1.0f,1.0f, 0.0f);
//gl.glVertex3f(0.0f,0.0f, 2.0f);
//gl.glVertex3f(1.0f,-1.0f, 0.0f);
//
////trojkat4
//gl.glColor3f(1.0f,1.0f,0.0f);
//gl.glVertex3f(1.0f,-1.0f, 0.0f);
//gl.glVertex3f(0.0f,0.0f, 2.0f);
//gl.glVertex3f(-1.0f,-1.0f, 0.0f);
//
//gl.glEnd();
//
//gl.glBegin(GL.GL_QUADS);
//gl.glColor3f(1.0f,0.0f,1.0f);
//gl.glVertex3f(-1.0f,-1.0f,0.0f);
//gl.glVertex3f(-1.0f,1.0f,0.0f);
//gl.glVertex3f(1.0f,1.0f,0.0f);
//gl.glVertex3f(1.0f,-1.0f,0.0f);
//gl.glEnd();
// Flush all drawing operations to the graphics card
gl.glFlush();
}



public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
}
}