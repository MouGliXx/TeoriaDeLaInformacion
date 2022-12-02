package modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class ShannonFano {
    private HashMap<String, Integer> frecuencias;
    private HashMap<String, String> shannonFano = new HashMap<>();
    private double longitudMedia, rendimiento, redundancia;

    public ShannonFano(HashMap<String, Integer> frecuencias, double entropia, int total) {
        this.frecuencias = frecuencias;
        generaShannonFano(new ArrayList<>(frecuencias.keySet()), "");
        longitudMedia = longitudMedia(frecuencias, total, shannonFano);
        rendimiento = calculaRendimiento(entropia, longitudMedia);
        redundancia = calculaRedundancia(rendimiento);

        for (String key : frecuencias.keySet())
            System.out.println(key + " - " + frecuencias.get(key) + " - " + shannonFano.get(key));
    }

    public HashMap<String, String> getShannonFano() {
        return shannonFano;
    }

    public double getLongitudMedia() {
        return longitudMedia;
    }

    public double getRendimiento() {
        return rendimiento;
    }

    public double getRedundancia() {
        return redundancia;
    }

    public int sumaTotalFrencuencias(ArrayList<String> codigo) {
        int total = 0;

        for (String key : codigo) {
            total += this.frecuencias.get(key);
        }

        return total;
    }

    public int calculaK(ArrayList<String> codigo) {
        ArrayList<String> mitadSuperior, mitadInferior;
        int diferenciaMinima = 99999, k = 0, diferencia;
        int acumulador = 0, total = sumaTotalFrencuencias(codigo);

        if (codigo.size() > 1) {
            System.out.println("entro 000");
            for (String palabra : codigo) {
                acumulador += frecuencias.get(palabra);
                k++;
                if (acumulador >= total / 2) {
                    System.out.println("entro 1");
                    break;
                }
            }
        }

//            for (int i = 1; i < codigo.size(); i++) {
//                mitadSuperior = cortaMitadSuperior(codigo, i);
//                mitadInferior = cortaMitadInferior(codigo, i);
//
//                diferencia = Math.abs(sumaTotalFrencuencias(mitadSuperior) - sumaTotalFrencuencias(mitadInferior));
//
//                if (diferencia <= diferenciaMinima) {
//                    diferenciaMinima = diferencia;
//                    k = i;
//                }
//            }
        System.out.println(k);
        return k;
    }

    public ArrayList<String> cortaMitadSuperior(ArrayList<String> codigo, int k) {
        ArrayList<String> mitadSuperior = new ArrayList<>();
        int i = 0;

        for (String key : codigo) {
            if (i < k) {
                mitadSuperior.add(key);
            } else {
                break;
            }
            i++;
        }

        return mitadSuperior;
    }

    public ArrayList<String> cortaMitadInferior(ArrayList<String> codigo, int k) {
        ArrayList<String> mitadInferior = new ArrayList<>();
        int i = 0;

        for (String key : codigo) {
            if (i >= k) {
                mitadInferior.add(key);
            }
            i++;
        }

        return mitadInferior;
    }

    public void generaShannonFano(ArrayList<String> codigo, String codificacion) {
        int k;

        if (codigo.size() == 1) { //Es hoja
            this.shannonFano.put(codigo.get(0), codificacion.length() > 0 ? codificacion : "1");
        } else {
            k = calculaK(codigo);
            generaShannonFano(cortaMitadSuperior(codigo, k), codificacion + '0');
            generaShannonFano(cortaMitadInferior(codigo, k), codificacion + '1');
        }
    }

    public double longitudMedia(HashMap<String, Integer> frecuencias, int total, HashMap<String, String> shannonFano){
        double probabilidad, longitudCadena, longitudMedia = 0;

        for (String key : frecuencias.keySet()) {
            probabilidad = (double) frecuencias.get(key) / total;
            longitudCadena = shannonFano.get(key).length();
            longitudMedia += probabilidad * longitudCadena;
        }

        return longitudMedia;
    }

    public double calculaRendimiento(double entropia , double L) {
        return entropia / L;
    }

    public double calculaRedundancia(double rendimiento){
        return 1 - rendimiento;
    }
}
