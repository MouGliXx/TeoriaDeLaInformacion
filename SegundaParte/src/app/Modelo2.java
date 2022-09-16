package app;

import java.util.ArrayList;
import java.util.HashMap;

public class Modelo2 {

    public static ArrayList<String> diferenciaPalabras(String datos, int cantCaracteres) {
        ArrayList<String> palabras = new ArrayList<>();
        String aux;
        int i = 0;

        while (i < datos.length()) {
            aux = "";
            for (int k = 0; k < cantCaracteres; k++) {
                if (i + k < datos.length())
                    aux += datos.charAt(i + k);
            }
            palabras.add(aux);
            i += cantCaracteres;
        }

        if (datos.length() % cantCaracteres != 0)
            palabras.remove(palabras.size() - 1);

        return palabras;
    }

    //Este metodo por ahi no lo uso
    public static ArrayList<String> identificaPalabrasCodigo(ArrayList<String> palabras) {
        ArrayList<String> alfabeto = new ArrayList<>();

        for (String palabra: palabras) {
            if (!alfabeto.contains(palabra))
                alfabeto.add(palabra);
        }

        return alfabeto;
    }

    public static HashMap<String, Integer> calculaFrecuencias(ArrayList<String> datos) {
        HashMap<String, Integer> frecuencias = new HashMap<>();

        for (String i : datos) {
            Integer j = frecuencias.get(i);
            frecuencias.put(i, (j == null) ? 1 : j + 1);
        }

        return frecuencias;
    }

    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public static HashMap<String, Double> calculaInformacion(HashMap<String, Integer> frecuencias, int total) {
        HashMap<String, Double> informacion = new HashMap<>();

        frecuencias.forEach((key, fr) -> {
            double probabilidad = (double) fr / total;
            informacion.put(key, log2(1 / probabilidad));
        });

        return informacion;
    }

    public static double calculaEntropia(ArrayList<String> alfabeto, HashMap<String, Double> informacion, HashMap<String, Integer> frecuencias, int total) {
        double resultado = 0;

        for (String key : alfabeto) {
            double probabilidad = (double) frecuencias.get(key) / total;
            resultado += informacion.get(key) * probabilidad;
        }

        return resultado;
    }
}
