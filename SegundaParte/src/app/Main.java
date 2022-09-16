package app;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BufferedReader archivo = null;
        String fileName, datos = null;

        fileName = "DatosTP1.txt";
        File url = new File(fileName);

        try {
            archivo = new BufferedReader(new FileReader(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            datos = archivo.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> codigo3C = Modelo2.diferenciaPalabras(datos, 3);
        ArrayList<String> codigo5C = Modelo2.diferenciaPalabras(datos, 5);
        ArrayList<String> codigo7C = Modelo2.diferenciaPalabras(datos, 7);

        System.out.println(datos);
        System.out.println(codigo3C);
        System.out.println(codigo5C);
        System.out.println(codigo7C);
//        System.out.println(codigo3C.size());
//        System.out.println(codigo5C.size());
//        System.out.println(codigo7C.size());
    }
}
