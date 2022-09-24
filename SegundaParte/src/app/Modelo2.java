package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Modelo2 {

    public static ArrayList<Character> extraeSimbolos(String datos) {
        ArrayList<Character> simbolos = new ArrayList<>();

        for (int i = 0; i < datos.length(); i++) {
            if (!simbolos.contains(datos.charAt(i))) {
                simbolos.add(datos.charAt(i));
            }
        }

        return simbolos;
    }

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

    public static void generarArchivoIncisoA(HashMap<String, Double> informacion3C,
                                             HashMap<String, Double> informacion5C,
                                             HashMap<String, Double> informacion7C,
                                             double entropia3C,
                                             double entropia5C,
                                             double entropia7C) throws IOException{
        FileWriter fileWriter;

        fileWriter = new FileWriter("Archivos Generados/IncisoA.txt", false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("a) Calcular la cantidad de información y entropía.\n");

        bfwriter.write("\nInformacion Alfabeto3C:\n");
        informacion3C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nInformacion Alfabeto5C:\n");
        informacion5C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nInformacion Alfabeto7C:\n");
        informacion7C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nEntropia:\n");
        bfwriter.write("\tH(Alfabeto3C) = " + entropia3C + " bits\n");
        bfwriter.write("\tH(Alfabeto5C) = " + entropia5C + " bits\n");
        bfwriter.write("\tH(Alfabeto7C) = " + entropia7C + " bits\n");

        System.out.println("Archivo 'IncisoA.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public static boolean simbolosCorrectos(ArrayList<Character> simbolos, String palabra) {

        for (int i = 0; i < palabra.length(); i++) {
            if (!simbolos.contains(palabra.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean esCodigoBloque(ArrayList<Character> simbolos, ArrayList<String> PalabraCodigo, int NS) {

        for (int i = 0; i < NS; i++) {
            if (PalabraCodigo.get(i).isEmpty() || !simbolosCorrectos(simbolos, PalabraCodigo.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean esNoSingular(ArrayList<String> PalabraCodigo) {

        for (String palabra: PalabraCodigo) {
            if (Collections.frequency(PalabraCodigo, palabra) != 1) {
                return false;
            }
        }

        return true;
    }

    public static boolean esInstantaneo(ArrayList<String> PalabraCodigo) {

        for (String palabra_prefijo: PalabraCodigo) {
            for (String palabra_iterada: PalabraCodigo) {
                if (!palabra_prefijo.equals(palabra_iterada)) {
                    if (palabra_iterada.startsWith(palabra_prefijo)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void generarArchivoIncisoB(boolean esCodBlq3C, boolean esCodBlq5C, boolean esCodBlq7C,
                                             boolean esNoSing3C, boolean esNoSing5C, boolean esNoSing7C,
                                             boolean esInst3C, boolean esInst5C, boolean esInst7C) throws IOException {
        FileWriter fileWriter;

        fileWriter = new FileWriter("Archivos Generados/IncisoB.txt", false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("b) Los códigos que se obtuvieron, ¿de que tipo son? Justificar su respuesta.\n");

        bfwriter.write("\nAlfabeto3C:\n");
        if (esCodBlq3C) {
            bfwriter.write("\tEs codigo bloque.\n");
            if (esNoSing3C) {
                bfwriter.write("\tEs no singular.\n");
                if (esInst3C) {
                    bfwriter.write("\tEs instantaneo.\n");
                } else {
                    bfwriter.write("\tNo es instantaneo.\n");
                }
            } else {
                bfwriter.write("\tEs singular.\n");
            }
        } else {
            bfwriter.write("\tNo es codigo bloque.\n");
        }

        bfwriter.write("\nAlfabeto5C:\n");
        if (esCodBlq5C) {
            bfwriter.write("\tEs codigo bloque.\n");
            if (esNoSing5C) {
                bfwriter.write("\tEs no singular.\n");
                if (esInst5C) {
                    bfwriter.write("\tEs instantaneo.\n");
                } else {
                    bfwriter.write("\tNo es instantaneo.\n");
                }
            } else {
                bfwriter.write("\tEs singular.\n");
            }
        } else {
            bfwriter.write("\tNo es codigo bloque.\n");
        }

        bfwriter.write("\nAlfabeto7C:\n");
        if (esCodBlq7C) {
            bfwriter.write("\tEs codigo bloque.\n");
            if (esNoSing7C) {
                bfwriter.write("\tEs no singular.\n");
                if (esInst7C) {
                    bfwriter.write("\tEs instantaneo.\n");
                } else {
                    bfwriter.write("\tNo es instantaneo.\n");
                }
            } else {
                bfwriter.write("\tEs singular.\n");
            }
        } else {
            bfwriter.write("\tNo es codigo bloque.\n");
        }

        System.out.println("Archivo 'IncisoB.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }
}
