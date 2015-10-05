package com.github.uryyyyyyy.normal;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("usage: <some string>");
            return;
        }
        System.out.println("----start----");
        System.out.println(args[0]);
        System.out.println("----finish----");
    }
}