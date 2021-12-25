/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package net.steelswing.sphere2box;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import net.steelswing.sphere2box.texture.TextureFilter;
import net.steelswing.sphere2box.texture.TexturePNG;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * File: SphereToBoxConverter.java
 * Created on 25.12.2021, 1:21:06
 *
 * @author LWJGL2
 */
public class SphereToBoxConverter implements Runnable {

    private TexturePNG texture;
    private int w = 512, h = 512;
    private volatile boolean running;

    public static final List<Pair<String, Vector>> DIRECTIONS = new ArrayList<Pair<String, Vector>>() {
        {
            // Numbers
            add(new Pair<>("3", Vector.of(180, 180, -90)));
            add(new Pair<>("4", Vector.of(0, 180, 0)));
            add(new Pair<>("2", Vector.of(90, 180, 0)));
            add(new Pair<>("6", Vector.of(90, 180, 90)));
            add(new Pair<>("5", Vector.of(90, 180, 270)));
            add(new Pair<>("1", Vector.of(90, 180, 180)));

            // NAMES
//            add(new Pair<>("top", Vector.of(180, 180, 0)));
//            add(new Pair<>("bottom", Vector.of(0, 180, 0)));
//            add(new Pair<>("front", Vector.of(90, 180, 0)));
//            add(new Pair<>("left", Vector.of(90, 180, 90)));
//            add(new Pair<>("back", Vector.of(90, 180, 180)));
//            add(new Pair<>("right", Vector.of(90, 180, 270)));
        }
    };

    public SphereToBoxConverter() {
        run();
    }

    @Override
    public void run() {
        try {
            Display.setTitle("Converter. " + Display.getPixelScaleFactor());
            Display.setDisplayMode(new DisplayMode(w, h));
            Display.setResizable(true);
            Display.create();
            Display.setSwapInterval(1);
            glInit();
            running = true;
            while (running && !Display.isCloseRequested()) {
                render();
                Thread.sleep(2L);
                Display.update(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void glInit() throws Exception {
        texture = new TexturePNG(new File("day.png"), TextureFilter.LINEAR);
        texture.load();

        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(90, 1, 0.1f, 1000f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void render() throws Exception {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.2F, 0.2F, 0.2F, 1.0F);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(90, 1, 0.1f, 1000f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        for (Pair<String, Vector> dir : DIRECTIONS) {
            rotate(dir.getKey(), dir.getValue().x, dir.getValue().y, dir.getValue().z);
        }
        running = false;

    }


    public void rotate(String name, float x, float y, float z) throws Exception {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0.f, 0.f, 0.f);
            GL11.glScalef(-1, 1, 1);

            GL11.glRotatef(x, 1.f, 0.f, 0.f);
            GL11.glRotatef(y, 0.f, 1.f, 0.f);
            GL11.glRotatef(z, 0.f, 0.f, 1.f);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_DECAL);
            texture.bind();
            Sphere sphere = new Sphere();
            sphere.setNormals(GL11.GL_SMOOTH);
            sphere.setTextureFlag(true);
//            Q = gluNewQuadric();
//            gluQuadricNormals(Q,  GL11.GL_SMOOTH);
//            gluQuadricTexture(Q,  GL11.GL_TRUE);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
//            gluSphere(Q, 2.0, 100, 100);
            sphere.draw(2, 100, 100);

//            GL11.glDisable(GL11.GL_TEXTURE_2D);
//            buf = GL11.glReadPixels(0, 0, w, h, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE);
            File f = new File("out\\" + name + ".png");

            int[] pixels = new int[w * h];
            IntBuffer pixelBuf = BufferUtils.createIntBuffer(w * h * 4);
            GL11.glReadPixels(0, 0, w, h, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixelBuf);
            pixelBuf.limit(w * h * 4);
            pixelBuf.get(pixels);
            BufferedImage image = ImageTypeSpecifier.createFromBufferedImageType(2).createBufferedImage(w, h);
            image.setRGB(0, 0, w, h, pixels, 0, w);
            ImageIO.write(image, "png", f);
            System.out.println("Output " + pixelBuf.hashCode());

//            running = false;
        }
        GL11.glPopMatrix();
    }

}
