package app;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        /**
         * Leo los datos del archivo y se los asigno a un String
         * **/
        BufferedReader archivo;
        String inputFileName, datos = null;

        inputFileName = "DatosTP1.txt";
        File url = new File(inputFileName);

        try {
            archivo = new BufferedReader(new FileReader(url));
            datos = archivo.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Extraigo los diferentes simbolos de los datos
         * **/
        ArrayList<Character> simbolos = Modelo2.extraeSimbolos(datos);

//        System.out.println(simbolos);

        /**
         * Separo los datos en palabras de 3, 5 y 7 caracteres
         * **/
        ArrayList<String> datos3C = Modelo2.diferenciaPalabras(datos, 3);
        ArrayList<String> datos5C = Modelo2.diferenciaPalabras(datos, 5);
        ArrayList<String> datos7C = Modelo2.diferenciaPalabras(datos, 7);

//        System.out.println(datos3C.size());
//        System.out.println(datos5C.size());
//        System.out.println(datos7C.size());

        /**
         * Obtengo las palabras codigo de la secuencia total
         * **/
        ArrayList<String> codigo3C = Modelo2.identificaPalabrasCodigo(datos3C);
        ArrayList<String> codigo5C = Modelo2.identificaPalabrasCodigo(datos5C);
        ArrayList<String> codigo7C = Modelo2.identificaPalabrasCodigo(datos7C);

//        System.out.println(alfabeto3C.size());
//        System.out.println(alfabeto5C.size());
//        System.out.println(alfabeto7C.size());

        /**
         * Calculo las frecuencias de cada palabra codigo
         * **/
        HashMap<String, Integer> frecuencias3C = Modelo2.calculaFrecuencias(datos3C);
        HashMap<String, Integer> frecuencias5C = Modelo2.calculaFrecuencias(datos5C);
        HashMap<String, Integer> frecuencias7C = Modelo2.calculaFrecuencias(datos7C);

//        System.out.println(frecuencias3C);
//        System.out.println(frecuencias5C);
//        System.out.println(frecuencias7C);

        /**
         * Calculo la cantidad de informacion de cada palabra codigo
         * **/
        HashMap<String, Double> informacion3C = Modelo2.calculaInformacion(frecuencias3C, datos3C.size());
        HashMap<String, Double> informacion5C = Modelo2.calculaInformacion(frecuencias5C, datos5C.size());
        HashMap<String, Double> informacion7C = Modelo2.calculaInformacion(frecuencias7C, datos7C.size());

//        System.out.println(informacion3C);
//        System.out.println(informacion5C);
//        System.out.println(informacion7C);

        /**
         * Calculo la entropia de cada alfabeto con palabras de 3, 5 y 7 caracteres
         * **/
        double entropia3C = Modelo2.calculaEntropia(codigo3C, informacion3C, frecuencias3C, datos3C.size());
        double entropia5C = Modelo2.calculaEntropia(codigo5C,informacion5C, frecuencias5C, datos5C.size());
        double entropia7C = Modelo2.calculaEntropia(codigo7C,informacion7C, frecuencias7C, datos7C.size());

//        System.out.println(entropia3C);
//        System.out.println(entropia5C);
//        System.out.println(entropia7C);

        try {
            Modelo2.generarArchivoIncisoA(informacion3C, informacion5C, informacion7C, entropia3C, entropia5C, entropia7C);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Verifico si los alfabetos son bloque
         * **/
        boolean esCodBlq3C = Modelo2.esCodigoBloque(simbolos, codigo3C, codigo3C.size());
        boolean esCodBlq5C = Modelo2.esCodigoBloque(simbolos, codigo5C, codigo5C.size());
        boolean esCodBlq7C = Modelo2.esCodigoBloque(simbolos, codigo7C, codigo7C.size());


//        System.out.println("\nEl alfabeto3C es codigo bloque: " + Modelo2.esCodigoBloque(simbolos, alfabeto3C, alfabeto3C.size()));
//        System.out.println("El alfabeto5C es codigo bloque: " + Modelo2.esCodigoBloque(simbolos, alfabeto5C, alfabeto5C.size()));
//        System.out.println("El alfabeto7C es codigo bloque: " + Modelo2.esCodigoBloque(simbolos, alfabeto7C, alfabeto7C.size()));

        /**
         * Verifico si los alfabetos son NO singulares
         * **/
        boolean esNoSing3C = Modelo2.esNoSingular(codigo3C);
        boolean esNoSing5C = Modelo2.esNoSingular(codigo5C);
        boolean esNoSing7C = Modelo2.esNoSingular(codigo7C);

//        System.out.println("\nEl alfabeto3C es NO singular: " + Modelo2.esNoSingular(alfabeto3C));
//        System.out.println("El alfabeto5C es NO singular: " + Modelo2.esNoSingular(alfabeto5C));
//        System.out.println("El alfabeto7C es NO singular: " + Modelo2.esNoSingular(alfabeto7C));

        /**
         * Verifico si los alfabetos son instantaneos
         * **/
        boolean esInst3C = Modelo2.esInstantaneo(codigo3C);
        boolean esInst5C = Modelo2.esInstantaneo(codigo5C);
        boolean esInst7C = Modelo2.esInstantaneo(codigo7C);

//        System.out.println("\nEl alfabeto3C es instantaneo: " + Modelo2.esInstantaneo(alfabeto3C));
//        System.out.println("El alfabeto5C es instantaneo: " + Modelo2.esInstantaneo(alfabeto5C));
//        System.out.println("El alfabeto7C es instantaneo: " + Modelo2.esInstantaneo(alfabeto7C));

        try {
            Modelo2.generarArchivoIncisoB(esCodBlq3C, esCodBlq5C, esCodBlq7C,
                                          esNoSing3C, esNoSing5C, esNoSing7C,
                                          esInst3C, esInst5C, esInst7C);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
