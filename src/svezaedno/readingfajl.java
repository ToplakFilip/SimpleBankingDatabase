package svezaedno;

import banking.UI;

import java.nio.file.Paths;
import java.io.File;
import java.util.Scanner;

public class readingfajl {


    public static void main(String[] args) {
        int brojac = 0;
        try (Scanner scan = new Scanner(new File("src/svezaedno/dataset_91065.txt"))) {

            while (scan.hasNext()) {
                int linija = Integer.parseInt(scan.nextLine());
                if(linija == 0){
                    break;
                }
                if (linija % 2 == 0) {
                    brojac++;
                }
            }
        } catch (Exception e) {
            System.out.println("shit aint found: " + e.getMessage());
        }
        System.out.println(brojac);
    }
}

