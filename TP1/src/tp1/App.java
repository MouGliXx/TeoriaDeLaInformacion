package tp1;

import modelo.PrimeraParte;
import modelo.SegundaParte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String sistemaOperativo = System.getProperty("os.name");

        //LECTURA ARCHIVO
        BufferedReader archivo;
        String inputFileName, datos = null;

        if (sistemaOperativo.startsWith("Windows"))
            inputFileName = "DatosTP1.txt";
        else
            inputFileName = "../DatosTP1.txt";

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

        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
        }
    }

}
