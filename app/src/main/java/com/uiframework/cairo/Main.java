package com.uiframework.cairo;

/**
 * The main entry point for the CAIRO WebAssembly UI Framework.
 * Compiled via TeaVM to run natively in the browser.
 */
public class Main {

    /**
     * Application entry point. 
     * Note: Standard output is redirected to the browser's developer console.
     *
     * @param args Command line arguments (unused in the browser context).
     */
    public static void main(String[] args) {
        System.out.println("CAIRO Framework Initialized.");
    }
}