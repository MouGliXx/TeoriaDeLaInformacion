package tp1;

import modelo.PrimeraParte;
import modelo.SegundaParte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
    
        //LECTURA ARCHIVO
        BufferedReader archivo;
        String inputFileName, datos = null;

        inputFileName = "tp1/DatosTP1.txt";
        
        File url = new File(inputFileName);

        try {
            archivo = new BufferedReader(new FileReader(url));
            datos = archivo.readLine();

            //PRIMERA PARTE
            PrimeraParte primeraParte = new PrimeraParte(datos);
            System.out.println("Primera parte:");
            try {
                primeraParte.generarArchivoIncisoA();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso A");
            }
            try {
                primeraParte.generarArchivoIncisoC();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso C");
            }


            //SEGUNDA PARTE
            SegundaParte segundaParte = new SegundaParte(datos);

            System.out.println("Segunda parte:");
            try {
                segundaParte.generarArchivoIncisoA();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso A");
            }
            try {
                segundaParte.generarArchivoIncisoB();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso B");
            }
            try {
                segundaParte.generarArchivoIncisoC();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso C");
            }
            try {
                segundaParte.generarArchivoIncisoD();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso D");
            }
            try {
                segundaParte.generarArchivoIncisoE1();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso E");
            }
            try {
                segundaParte.generarArchivoIncisoE1binario();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso E");
            }
            try {
                segundaParte.generarArchivoIncisoE2();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo de texto del Inciso E");
            }
            try {
                segundaParte.generarArchivoIncisoE2binario();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo binario del Inciso E");
            }
            try {
                segundaParte.generarArchivoIncisoE3();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso E");
            }
            try {
                segundaParte.generarArchivoIncisoE3binario();
            } catch (IOException e) {
                System.out.println("NO se pudo generar el archivo del Inciso E");
            }

        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }
    }

}