/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package net.steelswing.sphere2box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * File: GLUtil.java
 * Created on 25.12.2021, 1:28:59
 *
 * @author LWJGL2
 */
public class GLUtil {

    /**
     * Creates and returns a direct byte buffer with the specified capacity.Applies native ordering to speed up access.
     * @param par0
     * @return 
     */
    public static synchronized ByteBuffer createDirectByteBuffer(int par0) {
        return ByteBuffer.allocateDirect(par0).order(ByteOrder.nativeOrder());
    }

    /**
     * Creates and returns a direct int buffer with the specified capacity.Applies native ordering to speed up access.
     * @param par0
     * @return 
     */
    public static IntBuffer createDirectIntBuffer(int par0) {
        return createDirectByteBuffer(par0 << 2).asIntBuffer();
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity.Applies native ordering to speed up access.
     * @param par0
     * @return 
     */
    public static FloatBuffer createDirectFloatBuffer(int par0) {
        return createDirectByteBuffer(par0 << 2).asFloatBuffer();
    }
}
