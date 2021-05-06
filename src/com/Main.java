package com;

import com.AnalyseurLexical;
import com.AnalyseurSyntaxique;
import com.SyntaxeException;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {

            AnalyseurLexical al = new AnalyseurLexical("src/com/Text.txt");
            String cond = "";
            do{
                cond = al.uniteSuivante();
                System.out.println("<" + AnalyseurLexical.UNITECOURANT + " , " + cond + ">");
            }while (!cond.equals(AnalyseurLexical.ENDFILE));


            AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();
            analyseurSyntaxique.runAnalyseSyntaxique();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}