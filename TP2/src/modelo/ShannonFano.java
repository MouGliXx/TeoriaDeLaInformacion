package modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class ShannonFano {
    private HashMap<String, Integer> frecuencias;

    public ShannonFano(HashMap<String, Integer> frecuencias) {
        this.frecuencias = frecuencias;
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
        int diferenciaMinima = 99999, k = 99999, diferencia;

        if (!codigo.isEmpty()) {
            for (int i = 1; i < codigo.size(); i++) {
                mitadSuperior = cortaMitadSuperior(codigo, i);
                mitadInferior = cortaMitadInferior(codigo, i);

                diferencia = Math.abs(sumaTotalFrencuencias(mitadSuperior) - sumaTotalFrencuencias(mitadInferior));

                if (diferencia <= diferenciaMinima) {
                    diferenciaMinima = diferencia;
                    k = i;
                }
            }
        }

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

    public void encode(ArrayList<String> codigo, String codificacion, HashMap<String, String> ShannonFano) {
        int k;

        if (codigo.size() == 1) { //Es hoja
            ShannonFano.put(codigo.get(0), codificacion.length() > 0 ? codificacion : "1");
        } else {
            k = calculaK(codigo);

//            System.out.println(k);

            ArrayList<String> mitadSuperior = cortaMitadSuperior(codigo, k);
            ArrayList<String> mitadInferior = cortaMitadInferior(codigo, k);

//            System.out.println("mitadSuperior" + mitadSuperior);
//            System.out.println("mitadInferior" + mitadInferior);

            encode(mitadSuperior, codificacion + '1', ShannonFano);
            encode(mitadInferior, codificacion + '0', ShannonFano);
        }
    }

    public HashMap<String, String> construyeArbolShannonFano(ArrayList<String> codigo) {
        HashMap<String, String> shannonFano = new HashMap<>();

        encode(codigo, "", shannonFano);

        return shannonFano;
    }
}
