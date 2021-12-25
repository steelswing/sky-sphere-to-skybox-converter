/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package net.steelswing.sphere2box.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.steelswing.sphere2box.GLUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

/**
 * File: TexturePNG.java
 * Created on 25.12.2021, 1:26:51
 *
 * @author LWJGL2
 */
public class TexturePNG {

    private static final IntBuffer dataBuffer = GLUtil.createDirectIntBuffer(4194304);

    private int id = -1;
    private final BufferedImage bufferedImage;
    private final TextureFilter filter;

    public TexturePNG(BufferedImage bufferedImage, TextureFilter filter) {
        this.bufferedImage = bufferedImage;
        this.filter = filter;
    }

    public TexturePNG(File file, TextureFilter filter) throws IOException {
        this(ImageIO.read(file), filter);
    }

    public void load() {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i1 = 4194304 / width;

        int[] pixel = new int[i1 * width];
        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer) null);

        for (int i = 0; i < width * height; i += width * i1) {
            int startY = i / width;
            int h = Math.min(i1, height - startY);
            int length = width * h;
            bufferedImage.getRGB(0, startY, width, h, pixel, 0, width);

            dataBuffer.clear();
            dataBuffer.put(pixel, 0, length);
            dataBuffer.position(0).limit(length);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, startY, width, h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }

        TextureFilter.setTextureParameters(filter);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 4);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


    /**
     * Get texture id
     *
     * @return id of the texture
     */
    public int getTextureId() {
        if (id == -1) {
            this.id = GL11.glGenTextures();
        }

        return id;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTextureId());
    }

}
