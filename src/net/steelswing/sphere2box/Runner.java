/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package net.steelswing.sphere2box;

import java.io.File;

/**
 * File: Runner.java
 * Created on 25.12.2021, 1:21:16
 *
 * @author LWJGL2
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        SphereToBoxConverter conv = new SphereToBoxConverter();
    }
}
