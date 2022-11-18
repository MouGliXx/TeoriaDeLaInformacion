package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class PrimeraParte {
    private String sistemaOperativo;
    private ArrayList<String> diccionario;
    private ArrayList<Character> simbolos;
    private int orden;

    public PrimeraParte(BufferedReader archivo) {
        this.sistemaOperativo = System.getProperty("os.name");
        diccionario = generaDiccionario(archivo);
        simbolos = extraeSimbolos(diccionario);
        orden = simbolos.size();

        System.out.println(diccionario.size());
        System.out.println(diccionario);
        System.out.println(simbolos);
        System.out.println(orden);
    }

    private ArrayList<String> generaDiccionario(BufferedReader archivo) {
        ArrayList<String> datos = new ArrayList<>();
        String linea;

        try {
            while ((linea = archivo.readLine()) != null) {
                String[] palabras = linea.split("\\s+");
                for (String palabra : palabras) {
                    datos.add(palabra);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datos;
    }

    public ArrayList<Character> extraeSimbolos(ArrayList<String> diccionario) {
        ArrayList<Character> simbolos = new ArrayList<>();

        for (String palabra : diccionario) {
            for (int i = 0; i < palabra.length(); i++) {
                if (!simbolos.contains(palabra.charAt(i))) {
                    simbolos.add(palabra.charAt(i));
                }
            }
        }

        return simbolos;
    }
}
