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
    private HashMap<String, Double> informacion;
    private double entropia;
    private int orden;

    public PrimeraParte(BufferedReader archivo) {
        sistemaOperativo = System.getProperty("os.name");
        diccionario = generaDiccionario(archivo);
        simbolos = extraeSimbolos(diccionario);
        orden = simbolos.size();
        frecuencias = calculaFrecuencias(diccionario);
        codigo = identificaPalabrasCodigo(frecuencias);
        informacion = calculaInformacion(frecuencias, diccionario.size());
        entropia = calculaEntropia(codigo, informacion, frecuencias, diccionario.size());

//        System.out.println(orden);
//        System.out.println(entropia);

        //PRUEBAS
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

        ShannonFano shannonFano = new ShannonFano(codigo, frecuencias, entropia);
//        codificacionShannonFano = shannonFano.construyeArbolShannonFano(codigo);

//        ShannonFano shannonFano = new ShannonFano(auxCod, auxFr, entropia);
//        codificacionShannonFano = shannonFano.construyeArbolShannonFano(auxCod);
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

    public ArrayList<String> identificaPalabrasCodigo(HashMap<String, Integer> frecuencias) {
        ArrayList<String> alfabeto = new ArrayList<>();

        for (String palabra: frecuencias.keySet()) {
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

    public double logOrden(double x) {
        return Math.log(x) / Math.log(orden);
    }

    public HashMap<String, Double> calculaInformacion(HashMap<String, Integer> frecuencias, int total) {
        HashMap<String, Double> informacion = new HashMap<>();

        frecuencias.forEach((palabra, fr) -> {
            double probabilidad = (double) fr / total;
            informacion.put(palabra, logOrden(1 / probabilidad));
        });

        return informacion;
    }

    public double calculaEntropia(ArrayList<String> codigo, HashMap<String, Double> informacion, HashMap<String, Integer> frecuencias, int total) {
        double resultado = 0;

        for (String key : codigo) {
            double probabilidad = (double) frecuencias.get(key) / total;
            resultado += informacion.get(key) * probabilidad;
        }

        return resultado;
    }
}
