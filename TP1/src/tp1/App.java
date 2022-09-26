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

        inputFileName = "DatosTP1.txt";
        File url = new File(inputFileName);

        try {
            archivo = new BufferedReader(new FileReader(url));
            datos = archivo.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //PRIMERA PARTE
        PrimeraParte primeraParte = new PrimeraParte(datos);

//        try {
//            System.out.println("Primera parte:");
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //SEGUNDA PARTE
        SegundaParte segundaParte = new SegundaParte(datos);

        try {
            System.out.println("Segunda parte:");
            segundaParte.generarArchivoIncisoA();
            segundaParte.generarArchivoIncisoB();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
