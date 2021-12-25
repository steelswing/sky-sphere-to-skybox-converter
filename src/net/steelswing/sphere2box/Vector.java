/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package net.steelswing.sphere2box;

/**
 * File: Vector.java
 * Created on 25.12.2021, 8:05:32
 *
 * @author LWJGL2
 */
public class Vector {

    public int x, y, z;

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public static Vector of(int x, int y, int z) {
        return new Vector(x, y, z);
    }
}
