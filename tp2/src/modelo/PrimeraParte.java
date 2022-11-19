package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PrimeraParte {
    private String sistemaOperativo;
    private ArrayList<String> diccionario, codigo;
    private ArrayList<Character> simbolos;
    private HashMap<String, Integer> frecuencias;
    private HashMap<String, String> codificacionShannonFano;
    private int orden;

    public PrimeraParte(BufferedReader archivo) {
        sistemaOperativo = System.getProperty("os.name");
        diccionario = generaDiccionario(archivo);
        simbolos = extraeSimbolos(diccionario);
        orden = simbolos.size();
        codigo = identificaPalabrasCodigo(diccionario);
        frecuencias = calculaFrecuencias(diccionario);

        HashMap<String, Integer> auxFr = new HashMap<>();
        ArrayList<String> auxCod = new ArrayList<>();

        auxCod.add("a");
        auxCod.add("b");
        auxCod.add("c");
        auxCod.add("d");
        auxCod.add("e");
        auxCod.add("f");
        auxCod.add("g");
        auxCod.add("h");

        auxFr.put("a",40);
        auxFr.put("b",20);
        auxFr.put("c",15);
        auxFr.put("d",10);
        auxFr.put("e",6);
        auxFr.put("f",4);
        auxFr.put("g",3);
        auxFr.put("h",2);

//        ShannonFano shannonFano = new ShannonFano(frecuencias);
//        codificacionShannonFano = shannonFano.construyeArbolShannonFano(codigo);

        ShannonFano shannonFano = new ShannonFano(auxFr);
        codificacionShannonFano = shannonFano.construyeArbolShannonFano(auxCod);

        System.out.println(codificacionShannonFano);
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

    public ArrayList<String> identificaPalabrasCodigo(ArrayList<String> codigo) {
        ArrayList<String> alfabeto = new ArrayList<>();

        for (String palabra: codigo) {
            if (!alfabeto.contains(palabra))
                alfabeto.add(palabra);
        }

        return alfabeto;
    }

    public HashMap<String, Integer> calculaFrecuencias(ArrayList<String> datos) {
        Map<String, Integer> frecuencias = new HashMap<>();

        for (String i : datos) {
            Integer j = frecuencias.get(i);
            frecuencias.put(i, (j == null) ? 1 : j + 1);
        }

        //Ordeno de forma descendente
        return frecuencias.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
