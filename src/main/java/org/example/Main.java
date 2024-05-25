package org.example;

import org.example.database.MySQLDatabase;
import org.example.input.ParseInput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        MySQLDatabase db = MySQLDatabase.getInstance();
        db.createTables();

        String inputString = "";



//<dependency>
//    <groupId>com.fasterxml.jackson.core</groupId>
//    <artifactId>jackson-databind</artifactId>
//    <version>2.12.3</version>
//</dependency>





        while (!inputString.equalsIgnoreCase("quit")) {
            Scanner scanner = new Scanner(System.in);
            inputString = scanner.nextLine();
            inputString = inputString.trim().replaceAll(" +", " ");

            ParseInput.parseInput(inputString);
        }
    }
}