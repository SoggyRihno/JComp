package me.nslot.jcomp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sum {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File (args[0] +"input.txt"));
        int sum =0;
        while (scanner.hasNext())
            sum+=scanner.nextInt();
        System.out.println(sum);
    }
}

/*
Sum all numbers in input.txt

Sample input :
1 2 3 4 5
Sample output :
15

input
6
3 3 4
8 4 4
23345 5
32345
332455
72345
8 4
42 5
3345345
3
323455 45 34354 3 4

output
4163795

 */