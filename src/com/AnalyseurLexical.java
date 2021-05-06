package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class AnalyseurLexical {
    private Scanner fileReader;
    private String text;
	private int i= 0;
    public static String UNITECOURANT;
    public final static String ENDFILE = UniteLexicale.endFile;

    public AnalyseurLexical(String path) throws FileNotFoundException {
        fileReader = new Scanner(new BufferedReader(new FileReader(path)));
        
    }
    
    //les unités non terminals de l'analyseur lexicales sont les unités terminales de l'analyseur synt
    public String uniteSuivante(){
        String uniteCourant;
        AnalyseurLexical.UNITECOURANT = "";
        if(this.fileReader.hasNext()) {
            uniteCourant = this.fileReader.next();
            //
            AnalyseurLexical.UNITECOURANT = uniteCourant;
            //System.out.println(UNITECOURANT);
            int i = 0;
            if (uniteCourant.charAt(i) == '+'){
                if(i + 1 == uniteCourant.length())
                    return UniteLexicale.plus;
                else if(uniteCourant.charAt(++i) == '+'){
                    if(i + 1 == uniteCourant.length())
                        return UniteLexicale.incr;
                    return UniteLexicale.erreur;
                }
                else if(estChiffre(uniteCourant.charAt(i)))
                {
                    if (i + 1 == uniteCourant.length()){
                        return UniteLexicale.entier;
                    }
                    i++;
                    while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))){ i++; }
                    if (i == uniteCourant.length())
                        return UniteLexicale.entier;
                    if (uniteCourant.charAt(i) == '.') {
                        i++;
                        while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))) { i++; }
                        if (i == uniteCourant.length())
                            return UniteLexicale.reel;
                        return UniteLexicale.erreur;
                    }
                }
            }

            else if (uniteCourant.charAt(i) == '-') {
                if (i + 1 == uniteCourant.length()) {
                    return UniteLexicale.moins;
                } else if (uniteCourant.charAt(++i) == '-') {
                    if (i + 1 == uniteCourant.length())
                        return UniteLexicale.decr;
                    return UniteLexicale.erreur;
                }
                else if(estChiffre(uniteCourant.charAt(i)))
                {
                    if (i + 1 == uniteCourant.length()){
                        return UniteLexicale.entier;
                    }
                    i++;
                    while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))){ i++; }
                    if (i == uniteCourant.length())
                        return UniteLexicale.entier;
                    if (uniteCourant.charAt(i) == '.') {
                        i++;
                        while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))) { i++; }
                        if (i == uniteCourant.length())
                            return UniteLexicale.reel;
                        return UniteLexicale.erreur;
                    }
                }

            }

            else if (uniteCourant.charAt(i) == '='){
                if ( i + 1 == uniteCourant.length()){
                    return UniteLexicale.affec;
                }
                else if (uniteCourant.charAt(++i) == '='){
                    if (i + 1 == uniteCourant.length())
                        return UniteLexicale.egal;
                    return UniteLexicale.erreur;
                }
            }

            else if (uniteCourant.charAt(i) == '<'){
                if ( i + 1 == uniteCourant.length())
                    return UniteLexicale.inf;
                else if (uniteCourant.charAt(++i) == '>' ) {
                    if ( i + 1 == uniteCourant.length())
                        return UniteLexicale.diff;
                }
                else if (uniteCourant.charAt(i) == '='){
                    if (i + 1 == uniteCourant.length())
                        return UniteLexicale.infEgal;
                    return UniteLexicale.erreur;
                }
            }

            else if (uniteCourant.charAt(i) == '>'){
                if (i + 1 == uniteCourant.length()){
                    return UniteLexicale.sup;
                }
                else if (uniteCourant.charAt(++i) == '=') {
                    if (i + 1 == uniteCourant.length())
                        return UniteLexicale.supEgal;
                    return UniteLexicale.erreur;
                }
            }

            else if (estChiffre(uniteCourant.charAt(i))){
                if (i + 1 == uniteCourant.length()){
                    return UniteLexicale.entier;
                }
                i++;
                while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))){ i++; }
                if (i == uniteCourant.length())
                    return UniteLexicale.entier;
                if (uniteCourant.charAt(i) == '.') {
                    i++;
                    while (i < uniteCourant.length() && estChiffre(uniteCourant.charAt(i))) { i++; }
                    if (i == uniteCourant.length())
                        return UniteLexicale.reel;
                    return UniteLexicale.erreur;
                }
            }
            else if (uniteCourant.charAt(i) == '"' ){
                if (i + 1 == uniteCourant.length() ){
                    return UniteLexicale.griffe;
                }
                while(uniteCourant.charAt(uniteCourant.length() -1) != '"'){
                    if(!fileReader.hasNext())
                        return UniteLexicale.erreur;
                    uniteCourant = fileReader.next();
                    AnalyseurLexical.UNITECOURANT += " " + uniteCourant;
                }
                return UniteLexicale.chaine;
            }

            else if(uniteCourant.equals("and"))
                return UniteLexicale.et;

            else if(uniteCourant.equals("int"))
                return UniteLexicale.integer;

            else if(uniteCourant.equals("float"))
                return UniteLexicale.real;

            else if(uniteCourant.equals("string"))
                return UniteLexicale.string;

            else if(uniteCourant.equals("or"))
                return UniteLexicale.ou;

            else if (uniteCourant.equals("constante")){
                return UniteLexicale.constante;
            }

            else if (uniteCourant.equals("boolean")){
                return UniteLexicale.bool;
            }
            else if (uniteCourant.equals("true")){
                return UniteLexicale.vrai;
            }
            else if (uniteCourant.equals("false")){
                return UniteLexicale.faux;
            }

            else if (uniteCourant.equals("if")){
                return UniteLexicale.si;
            }

            else if (uniteCourant.equals("else")){
                return UniteLexicale.sinon;
            }

            else if (uniteCourant.equals("while")){
                return UniteLexicale.tantQue;
            }
            else if(uniteCourant.equals("debut")) {
                return UniteLexicale.debutProgram;
            }
            else if(uniteCourant.equals("fin")) {
                return UniteLexicale.finProgram;
            }
            else if(uniteCourant.equals("print")) {
                return UniteLexicale.ecrire;
            }
            else if(uniteCourant.equals("read")) {
                return UniteLexicale.lire;
            }
            else if(uniteCourant.equals(","))
                return UniteLexicale.virgule;

            // ac*1014a
            else if (estLettre(uniteCourant.charAt(i))){
                /* On verifie si l'ident est constituée d'une seule lettre */
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.ident;
                i++;
                // on lit tout les lettres et tout les chiffres et tout les _
                while ( i < uniteCourant.length() && (estLettre(uniteCourant.charAt(i)) || estChiffre(uniteCourant.charAt(i)) || uniteCourant.charAt(i) == '_'))
                {
                    i++;
                }
                // verifier si le dernier elemnt est une lettre
                if (i == uniteCourant.length() && estLettre(uniteCourant.charAt(i-1)))
                    return UniteLexicale.ident;
                return UniteLexicale.erreur;

            }

            else if (uniteCourant.charAt(i) == '(') {
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.parentOuv;
            }

            else if (uniteCourant.charAt(i) == ')') {
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.parentFerme;
            }

            else if (uniteCourant.charAt(i) == '{') {
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.accoladeOuv;
            }

            else if (uniteCourant.charAt(i) == '}'){
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.accoladeFerm;
            }

            else if (uniteCourant.charAt(i) == ';'){
                if (i + 1 == uniteCourant.length()){
                    return UniteLexicale.finLigne;
                }
            }


            else if (uniteCourant.charAt(i) == '['){
                if ( i + 1 == uniteCourant.length())
                    return UniteLexicale.crochetOuv;
            }
            else if (uniteCourant.charAt(i) == ']'){
                if (i + 1 == uniteCourant.length())
                    return UniteLexicale.crochetFerm;
            }



            else if (uniteCourant.charAt(i) == '/') {
                if (i + 1 == uniteCourant.length()) {
                    return UniteLexicale.div;
                }
                else if (uniteCourant.charAt(++i) == '*') {
                    if(fileReader.hasNext()) {
                        uniteCourant = fileReader.next();
                        while (!uniteCourant.equals("*/") && fileReader.hasNext())
                            uniteCourant = fileReader.next();
                        if(fileReader.hasNext())
                            return UniteLexicale.commentaire;
                        return UniteLexicale.erreur;
                    }
                    else return UniteLexicale.endFile;
                }
            }

            else if (uniteCourant.charAt(i) == '*'){
                if ( i + 1 == uniteCourant.length()){
                    return  UniteLexicale.prod;
                }
                else if (uniteCourant.charAt(++i) == '/'){
                    return  UniteLexicale.finCommnt;
                }

            }

        }else{
            return UniteLexicale.endFile;
        }
        return UniteLexicale.erreur;
    }

    private boolean estChiffre(char c){
        return c >= '0' && c <= '9';
    }

    private boolean estLettre(char c){
        return c >='a' && c <= 'z'|| c >='A' && c <= 'A';
    }
}