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
        String fileName, datos = null;

        fileName = "DatosTP1.txt";
        File url = new File(fileName);

        try {
            archivo = new BufferedReader(new FileReader(url));
            datos = archivo.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        ArrayList<String> alfabeto3C = Modelo2.identificaPalabrasCodigo(datos3C);
        ArrayList<String> alfabeto5C = Modelo2.identificaPalabrasCodigo(datos5C);
        ArrayList<String> alfabeto7C = Modelo2.identificaPalabrasCodigo(datos7C);

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
        double entropia3C = Modelo2.calculaEntropia(alfabeto3C, informacion3C, frecuencias3C, datos3C.size());
        double entropia5C = Modelo2.calculaEntropia(alfabeto5C,informacion5C, frecuencias5C, datos5C.size());
        double entropia7C = Modelo2.calculaEntropia(alfabeto7C,informacion7C, frecuencias7C, datos7C.size());

        System.out.println(entropia3C);
        System.out.println(entropia5C);
        System.out.println(entropia7C);
    }
}
