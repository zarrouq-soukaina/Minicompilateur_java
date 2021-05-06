package com;

import com.AnalyseurLexical;
import com.UniteLexicale;
import com.Symbole;
import com.SemantiqueException;
import com.SyntaxeException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class AnalyseurSyntaxique {
    private static String uniteCourante;
    private static AnalyseurLexical analyseurLexical;
    private static ArrayList <Symbole> tableSymbole;
    public AnalyseurSyntaxique() throws FileNotFoundException {
        analyseurLexical = new AnalyseurLexical("src/com/Text.txt");
    }
    private boolean isOperateur(){
        //uniteCourante = analyseurLexical.uniteSuivante();
        return UniteLexicale.plus.equals(uniteCourante)
                || UniteLexicale.moins.equals(uniteCourante)
                || UniteLexicale.prod.equals(uniteCourante)
                || UniteLexicale.div.equals(uniteCourante);
    }
    private boolean isOperande() throws SemantiqueException{
        //uniteCourante = analyseurLexical.uniteSuivante();
        if (UniteLexicale.ident.equals(uniteCourante))
            return variableMustBeDeclared(AnalyseurLexical.UNITECOURANT);
        return UniteLexicale.entier.equals(uniteCourante)
                || UniteLexicale.reel.equals(uniteCourante)
                || UniteLexicale.chaine.equals(uniteCourante);
    }
    private boolean isExpressionArithemetique() throws SyntaxeException, SemantiqueException{
        if(isOperande() && isOperandeTypeExpressArithOk(uniteCourante)){
            uniteCourante = analyseurLexical.uniteSuivante();
            if(isOperateur()){
                uniteCourante = analyseurLexical.uniteSuivante();
                if(isOperande() && isOperandeTypeExpressArithOk(uniteCourante)){
                    uniteCourante = analyseurLexical.uniteSuivante();
                    while (isOperateur()){
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if(!isOperande() || !isOperandeTypeExpressArithOk(uniteCourante))
                            throw new SyntaxeException("Expression non valide");
                        uniteCourante = analyseurLexical.uniteSuivante();
                    }
                    return true;
                }
                throw new SyntaxeException("Expression non valide");
            }
            else{
                if(uniteCourante.equals(UniteLexicale.finLigne))
                    return true;
                else if(uniteCourante.equals(UniteLexicale.parentFerme))
                    return true;
                else if(isOperateurConditionnel())
                    return true;
                else if(uniteCourante.equals(UniteLexicale.accoladeOuv))
                    return true;
                else if (uniteCourante.equals(UniteLexicale.et))
                    return true ;
                else if (uniteCourante.equals(UniteLexicale.ou))
                    return true ;
                else if (uniteCourante.equals(UniteLexicale.diff))
                    return true ;
                throw new SyntaxeException("Expression non valide");
            }
        }
        throw new SyntaxeException("Expression non valide");
    }

    //verifie si le type de l'identifiant est le meme que  le type de l'operateur
    private void verifieTypeForITOEAA(String typeIdent) throws SemantiqueException{
        String type = null;
        if(uniteCourante.equals(UniteLexicale.ident))
            type = trouverTypeident(AnalyseurLexical.UNITECOURANT);
        if(type == null){
            if(!Objects.equals(getCorrespondance(typeIdent), uniteCourante)) throw new SemantiqueException("type erroné !!");
        }
        else if(!typeIdent.equals(type)) throw new SemantiqueException("type erroné !!");
    }

    private boolean isTermeOrIsExpressionArithmetiqueInAffectation(String typeIdent) throws SyntaxeException, SemantiqueException{
        if(isOperande()){
            verifieTypeForITOEAA(typeIdent);
            uniteCourante = analyseurLexical.uniteSuivante();
            if(isOperateur()){
                uniteCourante = analyseurLexical.uniteSuivante();
                if(isOperande()){
                    verifieTypeForITOEAA(typeIdent);
                    uniteCourante = analyseurLexical.uniteSuivante();
                    while (isOperateur()){
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if(isOperande()) {
                            verifieTypeForITOEAA(typeIdent);
                        }else throw new SyntaxeException("Expression non valide");
                        uniteCourante = analyseurLexical.uniteSuivante();
                    }
                    return true;
                }
                throw new SyntaxeException("Expression non valide");
            }
            else{
                if(uniteCourante.equals(UniteLexicale.finLigne)) {
                    return true;
                }
                throw new SyntaxeException("Expression non valide");
            }
        }
        throw new SyntaxeException("Expression non valide");
    }

    private boolean isTerme() throws SemantiqueException{
        //uniteCourante = analyseurLexical.uniteSuivante();
        if (UniteLexicale.ident.equals(uniteCourante))
            return variableMustBeDeclared(AnalyseurLexical.UNITECOURANT);
        return  UniteLexicale.entier.equals(uniteCourante)
                || UniteLexicale.reel.equals(uniteCourante)
                || UniteLexicale.chaine.equals(uniteCourante);
    }
    private boolean isTypeVariable() throws  SyntaxeException{
        //uniteCourante = analyseurLexical.uniteSuivante();
        return UniteLexicale.integer.equals(uniteCourante)
                || UniteLexicale.real.equals(uniteCourante)
                || UniteLexicale.bool.equals(uniteCourante)
                || UniteLexicale.string.equals(uniteCourante);
    }
    private boolean declarationVariable() throws SyntaxeException, SemantiqueException {
        if(isTypeVariable()){
            String typeVariable = uniteCourante;
            uniteCourante = analyseurLexical.uniteSuivante();
            if(uniteCourante.equals(UniteLexicale.ident)){
                isVariableAlreadyDeclaree(AnalyseurLexical.UNITECOURANT);
                Symbole s = new Symbole();
                s.setIdentifant(AnalyseurLexical.UNITECOURANT);
                s.setType(typeVariable);
                tableSymbole.add(s);
                uniteCourante = analyseurLexical.uniteSuivante();
                if(uniteCourante.equals(UniteLexicale.affec))
                {
                    uniteCourante = analyseurLexical.uniteSuivante();
                    if(!isTerme())
                        throw new SyntaxeException("Declaration de variable non valide: " +
                            "une '=' est suivie obligatoirement par: (ident | entier | reel | chaine)");
                    isTypeOk(s.getIdentifant(), uniteCourante);
                    uniteCourante = analyseurLexical.uniteSuivante();
                }
                if(uniteCourante.equals(UniteLexicale.finLigne))
                    return true;
                while (uniteCourante.equals(UniteLexicale.virgule)){
                    uniteCourante = analyseurLexical.uniteSuivante();
                    if(!uniteCourante.equals(UniteLexicale.ident))
                        throw new SyntaxeException("Declaration de variable non" +
                                " valide: une virgule est suivie" +
                                "obligatoirement par un identifiant");
                    s = new Symbole();
                    s.setIdentifant(AnalyseurLexical.UNITECOURANT);
                    s.setType(typeVariable);
                    tableSymbole.add(s);
                    uniteCourante = analyseurLexical.uniteSuivante();
                    if(uniteCourante.equals(UniteLexicale.affec))
                    {
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if(!isTerme())
                            throw new SyntaxeException("Declaration de variable non valide: " +
                                    "une '=' est suivie obligatoirement par: (ident | entier | reel | chaine)");
                        isTypeOk(s.getIdentifant(),uniteCourante);
                        uniteCourante = analyseurLexical.uniteSuivante();
                    }
                    else if(uniteCourante.equals(UniteLexicale.finLigne))
                    {
                        return true;
                    }
                }
                if(uniteCourante.equals(UniteLexicale.finLigne))
                {
                    return true;
                }
                throw new SyntaxeException("Declaration de variable non valide: ';' manquant");
            }
            throw new SyntaxeException("Declaration de variable non valide");
        }
        throw new SyntaxeException("Declaration de variable non valide");
    }

    private boolean declarationConstante() throws SyntaxeException , SemantiqueException{
        if (uniteCourante.equals(UniteLexicale.constante)) {
            uniteCourante = analyseurLexical.uniteSuivante();
            if (isTypeVariable()) {
                String typeVariable = uniteCourante;
                uniteCourante = analyseurLexical.uniteSuivante();
                if (uniteCourante.equals(UniteLexicale.ident)) {
                    isVariableAlreadyDeclaree(AnalyseurLexical.UNITECOURANT);
                    Symbole s = new Symbole();
                    s.setConstante(true);
                    s.setIdentifant(AnalyseurLexical.UNITECOURANT);
                    s.setType(typeVariable);
                    tableSymbole.add(s);
                    uniteCourante = analyseurLexical.uniteSuivante();
                    if (uniteCourante.equals(UniteLexicale.affec)) {
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if (!isTerme())
                            throw new SyntaxeException("Declaration de constante non valide: \" +\n" +
                                    "\"une '=' est suivie obligatoirement par: (ident | entier | reel | chaine)");
                        isTypeOk(s.getIdentifant(),uniteCourante);
                        uniteCourante = analyseurLexical.uniteSuivante();
                    }
                    if (uniteCourante.equals(UniteLexicale.finLigne)) {
                        return true;
                    }
                    throw new SyntaxeException("Declaration de constante non valide: ';' est attendu");
                }
                throw new SyntaxeException("Declaration de constante non valide: 'ident' est attendu");
            }
            throw new SyntaxeException("Declaration de constante non valide: 'type de constante' est attendu");
        }
        throw new SyntaxeException("Declaration de constante non valide: 'constante' est attendu");

                    /*
                    while (uniteCourante.equals(UniteLexicale.virgule)) {
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if (!uniteCourante.equals(UniteLexicale.ident)) {
                            throw new SyntaxeException("Declaration de constante non valide: 'ident' est required");
                        }
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if (!uniteCourante.equals(UniteLexicale.affec)) {
                            throw new SyntaxeException("Declaration de constante  non valide: '=' est required");
                        }
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if (!isTerme()) {
                            throw new SyntaxeException("Declaration de constante non valide: " +
                                    "une '=' est suivie obligatoirement par: (ident | entier | reel | chaine)");
                        }
                        uniteCourante = analyseurLexical.uniteSuivante();
                        if (uniteCourante.equals(UniteLexicale.finLigne)) {
                            return true;
                        }
                    }
                    return true ;*/
    }

    private boolean affectation () throws SyntaxeException,SemantiqueException {
        if(uniteCourante.equals(UniteLexicale.ident)){
            variableMustBeDeclared(AnalyseurLexical.UNITECOURANT);
            isConstanteAndWantToDoAffect(AnalyseurLexical.UNITECOURANT);
            String type = trouverTypeident(AnalyseurLexical.UNITECOURANT);
            uniteCourante = analyseurLexical.uniteSuivante();
            if (!uniteCourante.equals(UniteLexicale.affec)){
                throw new SyntaxeException("Affectation non valide");
            }
            uniteCourante = analyseurLexical.uniteSuivante();
            if(!isTermeOrIsExpressionArithmetiqueInAffectation(type))
                throw new SyntaxeException("Affectation non valide");
            if(uniteCourante.equals(UniteLexicale.finLigne))
                return true;
            throw new SyntaxeException("Affectation non valide");
        }
        throw new SyntaxeException("Affectation non valide");
    }

    private boolean isOperateurConditionnel(){
        return uniteCourante.equals(UniteLexicale.inf)
                || uniteCourante.equals(UniteLexicale.sup)
                || uniteCourante.equals(UniteLexicale.infEgal)
                || uniteCourante.equals(UniteLexicale.supEgal)
                || uniteCourante.equals(UniteLexicale.egal)
                || uniteCourante.equals(UniteLexicale.diff);
    }

    private boolean isBoolean(){
        return uniteCourante.equals(UniteLexicale.vrai)
                || uniteCourante.equals(UniteLexicale.faux);
    }

    private boolean isCondition() throws SyntaxeException,SemantiqueException{
        // verfies la grammaire d'expression arith et de terme
        isExpressionArithemetique();
        if(!isOperateurConditionnel())
            throw new SyntaxeException("Expression conditionnelle non valide");
        uniteCourante = analyseurLexical.uniteSuivante();
        isExpressionArithemetique();
        while (uniteCourante.equals(UniteLexicale.ou)
                || uniteCourante.equals(UniteLexicale.et)
                || uniteCourante.equals(UniteLexicale.diff)
        ){
            uniteCourante = analyseurLexical.uniteSuivante();
            isExpressionArithemetique();
            if(!isOperateurConditionnel())
                throw new SyntaxeException("Expression conditionnelle non valide");
            uniteCourante = analyseurLexical.uniteSuivante();
            isExpressionArithemetique();
        }
        return true;
    }

    private boolean isConcatenation() throws SyntaxeException, SemantiqueException{
        if(!uniteCourante.equals(UniteLexicale.chaine))
            throw new SyntaxeException("Concatenation non valide.");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.plus))
            throw new SyntaxeException("Concatenation non valide.");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.chaine))
            throw new SyntaxeException("Concatenation non valide.");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(uniteCourante.equals(UniteLexicale.finLigne))
            return true;
        throw new SyntaxeException("Concatenation non valide: ';' manquant.");
    }

    private boolean isAlternative() throws SyntaxeException, SemantiqueException{
        if(!uniteCourante.equals(UniteLexicale.si))
            throw new SyntaxeException("Erreur de syntaxe");
        uniteCourante = analyseurLexical.uniteSuivante();
        isCondition();
        if(!uniteCourante.equals(UniteLexicale.accoladeOuv))
            throw new SyntaxeException("Erreur de syntaxe: '{' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        while(!uniteCourante.equals(UniteLexicale.accoladeFerm)){
            instruction();
            uniteCourante = analyseurLexical.uniteSuivante();
        }
        /*if(!uniteCourante.equals(UniteLexicale.accoladeFerm))
            throw new SyntaxeException("Erreur de syntaxe: '}' attendu");*/

        return true;
    }

    private boolean isRepetitive()  throws SyntaxeException, SemantiqueException{
        if (!uniteCourante.equals(UniteLexicale.tantQue))
            throw new SyntaxeException("Erreur de syntaxe");
        uniteCourante = analyseurLexical.uniteSuivante();
        isCondition();
        if(!uniteCourante.equals(UniteLexicale.accoladeOuv))
            throw new SyntaxeException("Erreur de syntaxe: '{' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        while(!uniteCourante.equals(UniteLexicale.accoladeFerm)){
            instruction();
            //System.out.println(uniteCourante);
            uniteCourante = analyseurLexical.uniteSuivante();
        }
        /*if(!uniteCourante.equals(UniteLexicale.accoladeFerm))
            throw new SyntaxeException("Erreur de syntaxe: '}' attendu");*/
        return true;
    }

    private boolean ecriture() throws SyntaxeException,SemantiqueException{
        if(!uniteCourante.equals(UniteLexicale.ecrire))
            throw new SyntaxeException("Erreur de syntaxe: print");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.parentOuv))
            throw new SyntaxeException("Erreur de syntaxe: '(' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(uniteCourante.equals(UniteLexicale.chaine)){
            uniteCourante = analyseurLexical.uniteSuivante();
            if(!uniteCourante.equals(UniteLexicale.parentFerme))
                throw new SyntaxeException("Erreur de syntaxe: ')' attendu");
        }else if(isOperande()){
            isExpressionArithemetique();
            if(!uniteCourante.equals(UniteLexicale.parentFerme))
                throw new SyntaxeException("Erreur de syntaxe: ')' attendu");
        }
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.finLigne))
            throw new SyntaxeException("Erreur de syntaxe: ';' attendu");
        return true;
    }

    private boolean lire() throws SyntaxeException {
        if(!uniteCourante.equals(UniteLexicale.lire))
            throw new SyntaxeException("Erreur de syntaxe: read");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.parentOuv))
            throw new SyntaxeException("Erreur de syntaxe: '(' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.ident))
            throw new SyntaxeException("Erreur de syntaxe: 'ident' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.parentFerme))
            throw new SyntaxeException("Erreur de syntaxe: ')' attendu");
        uniteCourante = analyseurLexical.uniteSuivante();
        if(!uniteCourante.equals(UniteLexicale.finLigne))
            throw new SyntaxeException("Erreur de syntaxe: ';' attendu");
        return true;
    }

    private void instruction() throws SyntaxeException, SemantiqueException{

        if(isTypeVariable())
        {
            declarationVariable();
        }
        else if(uniteCourante.equals(UniteLexicale.constante)) { declarationConstante(); }

        else if(uniteCourante.equals(UniteLexicale.ecrire)) { ecriture(); }

        else if(uniteCourante.equals(UniteLexicale.lire)) { lire(); }

        else if(uniteCourante.equals(UniteLexicale.si)) { isAlternative(); }

        else if(uniteCourante.equals(UniteLexicale.tantQue)) { isRepetitive(); }

        else if(uniteCourante.equals(UniteLexicale.sinon)) {
                uniteCourante = analyseurLexical.uniteSuivante();
                if(!uniteCourante.equals(UniteLexicale.accoladeOuv))
                    throw new SyntaxeException("Erreur de syntaxe: '{' attendu");
                uniteCourante = analyseurLexical.uniteSuivante();
                while(!uniteCourante.equals(UniteLexicale.accoladeFerm)){
                    instruction();
                    uniteCourante = analyseurLexical.uniteSuivante();
                }
            /*if(!uniteCourante.equals(UniteLexicale.accoladeFerm))
                throw new SyntaxeException("Erreur de syntaxe: '}' attendu");*/
        }

        else if(uniteCourante.equals(UniteLexicale.ident)) { affectation(); }

        else if (uniteCourante.equals(UniteLexicale.commentaire));
        else if(uniteCourante.equals(UniteLexicale.finProgram));
        else
        {
            throw new SyntaxeException("Erreur synthaxique au niveau de : " + AnalyseurLexical.UNITECOURANT +
                    "\nAstuce bien separer les elements par des espaces ");
        }
    }
 // grammaire de programme
    public void runAnalyseSyntaxique() throws SyntaxeException , SemantiqueException {
        tableSymbole = new ArrayList<>();
        uniteCourante = analyseurLexical.uniteSuivante();
        if (uniteCourante.equals(UniteLexicale.debutProgram)) {
            uniteCourante = analyseurLexical.uniteSuivante();
            while (!uniteCourante.equals(UniteLexicale.finProgram )) {
                instruction();
                if(uniteCourante.equals(UniteLexicale.finProgram))
                    break;
                uniteCourante = analyseurLexical.uniteSuivante();
            }
        }
    }

    private boolean isTypeOk (String ident , String typeDeDonne) throws SemantiqueException{
        boolean isOk = false;
        Symbole tampon = new Symbole();
        for (Symbole s : tableSymbole){
            if(s.getIdentifant().equals(ident)){
                tampon = s;
                if(Objects.equals(getCorrespondance(s.getType()), typeDeDonne)){
                    isOk = true;
                }
                break;
            }
        }
        if(isOk)
            return true;
        //on cherche le type de b
        if(typeDeDonne.equals(UniteLexicale.ident)){
            for (Symbole s : tableSymbole){
                if(s.getIdentifant().equals(AnalyseurLexical.UNITECOURANT)){
                    if(!Objects.equals(getCorrespondance(s.getType()), getCorrespondance(tampon.getType())) ){
                        throw new SemantiqueException(" type erroné!!");
                    }else return true;
                }
            }
        }
        throw new SemantiqueException(" type erroné!!");
    }

    private boolean isVariableAlreadyDeclaree(String ident) throws SemantiqueException {
        for (Symbole s : tableSymbole) {
            if (s.getIdentifant().equals(ident))
                throw new SemantiqueException("variable  deja declarée !!");
        }
        return false;
    }

    private boolean isOperandeTypeExpressArithOk( String operande ) throws SemantiqueException{
        if (operande.equals(UniteLexicale.chaine))
            throw new SemantiqueException("Operande ne respecte pas le bon type");
        return true;
    }

    private boolean variableMustBeDeclared(String ident) throws SemantiqueException{
        for (Symbole s : tableSymbole){
            if (s.getIdentifant().equals(ident)){
                return true;
            }
        }
        throw new SemantiqueException("variable doit etre daclarée ");
    }

    private String getCorrespondance (String mr){
        if(mr.equals(UniteLexicale.integer))
            return UniteLexicale.entier;
        if(mr.equals(UniteLexicale.real))
            return UniteLexicale.reel;
        if(mr.equals(UniteLexicale.string))
            return UniteLexicale.chaine;
        return null;
    }

    private String trouverTypeident(String ident){
        for (Symbole s : tableSymbole) {
            if (s.getIdentifant().equals(ident))
                return s.getType();
        }
        return null;
    }

    private boolean isConstanteAndWantToDoAffect(String ident) throws SemantiqueException{
        for (Symbole s : tableSymbole) {
            if (s.getIdentifant().equals(ident)){
                if(s.isConstante())
                    throw new SemantiqueException("On ne peut pas affecter une valeur a une constante!!");
            }
        }
        return true;
    }
}