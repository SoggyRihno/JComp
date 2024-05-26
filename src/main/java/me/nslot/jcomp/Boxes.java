package me.nslot.jcomp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Boxes {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(args[0] +"input.txt"));
        int squares = scan.nextInt();
        for (int p =0; p <squares; p++) {

            int x = scan.nextInt();
            int y = scan.nextInt();

            System.out.print("+");
            for (int i = 0; i < x - 2; i++) {
                System.out.print(" -");
            }
            System.out.println(" +");

            for (int i = 0; i < y - 2; i++) {
                System.out.print("+");
                for (int j = 0; j < (x - 2) * 2; j++) {
                    System.out.print(" ");
                }
                System.out.println(" +");
            }

            System.out.print("+");
            for (int i = 0; i < x - 2; i++) {
                System.out.print(" -");
            }
            System.out.println(" +");
            System.out.println();
        }
    }
}
/*
2 < n < 10
Print out a n boxes formed by a height (int) and width (int) as follows :
    2 < height < 100
    2 < width < 100
    corners are "+"
    top and bottom are "-" (side x)
    a space should separate all characters on the top and bottom sides (no space at end)
    left and right are made up of "+" and are the same length as top (side y)
    + - - - +
    +       +
    +       +
    +       +
    +       +
    + - - - +

Sample input :
1
2 3
4 5

Sample output :
+ - +
+   +
+   +
+   +
+   +
+   +
+ - +

+ - - - - - - +
+             +
+             +
+             +
+             +
+             +
+             +
+ - - - - - - +



input
6
3 7
8 8
23 42
22 3
32 3
3 3


output
+ - +
+   +
+   +
+   +
+   +
+   +
+ - +

+ - - - - - - +
+             +
+             +
+             +
+             +
+             +
+             +
+ - - - - - - +

+ - - - - - - - - - - - - - - - - - - - - - +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+                                           +
+ - - - - - - - - - - - - - - - - - - - - - +

+ - - - - - - - - - - - - - - - - - - - - +
+                                         +
+ - - - - - - - - - - - - - - - - - - - - +

+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
+                                                             +
+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +

+ - +
+   +
+ - +

 */