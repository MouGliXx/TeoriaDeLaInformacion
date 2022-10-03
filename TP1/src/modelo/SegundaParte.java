package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SegundaParte {
    String sistemaOperativo;
    private ArrayList<Character> simbolos;
    private ArrayList<String> datos3C, datos5C, datos7C;
    private ArrayList<String> codigo3C, codigo5C, codigo7C;
    private HashMap<String, Integer> frecuencias3C, frecuencias5C, frecuencias7C;
    private HashMap<String, Double> informacion3C, informacion5C, informacion7C;
    private Map<String,String> arbolHuffman;
    private double entropia3C, entropia5C, entropia7C,longitudmedia3C,longitudmedia5C,longitudmedia7C;
    private boolean esCodBlq3C, esCodBlq5C, esCodBlq7C;
    private boolean  esNoSing3C, esNoSing5C, esNoSing7C;
    private boolean esInst3C, esInst5C, esInst7C;
    private boolean cumpleKraftMcMillan3,cumpleKraftMcMillan5,cumpleKraftMcMillan7;
    private double calcularendimiento3C,calcularendimiento5C,calcularendimiento7C;
    private double calcularedundancia3C,calcularedundancia5C,calcularedundancia7C;

    public SegundaParte(String datos) {
        sistemaOperativo = System.getProperty("os.name");
        simbolos = extraeSimbolos(datos);
        datos3C = diferenciaPalabras(datos, 3);
        datos5C = diferenciaPalabras(datos, 5);
        datos7C = diferenciaPalabras(datos, 7);
        codigo3C = identificaPalabrasCodigo(datos3C);
        codigo5C = identificaPalabrasCodigo(datos5C);
        codigo7C = identificaPalabrasCodigo(datos7C);
        frecuencias3C = calculaFrecuencias(datos3C);
        frecuencias5C = calculaFrecuencias(datos5C);
        frecuencias7C = calculaFrecuencias(datos7C);
        informacion3C = calculaInformacion(frecuencias3C, datos3C.size());
        informacion5C = calculaInformacion(frecuencias5C, datos5C.size());
        informacion7C = calculaInformacion(frecuencias7C, datos7C.size());
        entropia3C = calculaEntropia(codigo3C, informacion3C, frecuencias3C, datos3C.size());
        entropia5C = calculaEntropia(codigo5C, informacion5C, frecuencias5C, datos5C.size());
        entropia7C = calculaEntropia(codigo7C, informacion7C, frecuencias7C, datos7C.size());
        esCodBlq3C = esCodigoBloque(simbolos, codigo3C);
        esCodBlq5C = esCodigoBloque(simbolos, codigo5C);
        esCodBlq7C = esCodigoBloque(simbolos, codigo7C);
        esNoSing3C = esNoSingular(codigo3C);
        esNoSing5C = esNoSingular(codigo5C);
        esNoSing7C = esNoSingular(codigo7C);
        esInst3C = esInstantaneo(codigo3C);
        esInst5C = esInstantaneo(codigo5C);
        esInst7C = esInstantaneo(codigo7C);
        cumpleKraftMcMillan3=cumpleKraftMcMillan(codigo3C,simbolos);
        cumpleKraftMcMillan5=cumpleKraftMcMillan(codigo5C,simbolos);
        cumpleKraftMcMillan7=cumpleKraftMcMillan(codigo7C,simbolos);
        longitudmedia3C=longitudMedia(frecuencias3C,datos3C.size(),codigo3C);
        longitudmedia5C=longitudMedia(frecuencias5C,datos5C.size(),codigo5C);
        longitudmedia7C=longitudMedia(frecuencias7C,datos7C.size(),codigo7C);
        calcularendimiento3C=calculaRendimiento(entropia3C,3);
        calcularendimiento5C=calculaRendimiento(entropia5C,5);
        calcularendimiento7C=calculaRendimiento(entropia7C,7);
        calcularedundancia3C=calculaRedundancia(calcularendimiento3C);
        calcularedundancia5C=calculaRedundancia(calcularendimiento5C);
        calcularedundancia7C=calculaRedundancia(calcularendimiento7C);
    }

    public  ArrayList<Character> extraeSimbolos(String datos) {
        ArrayList<Character> simbolos = new ArrayList<>();

        for (int i = 0; i < datos.length(); i++) {
            if (!simbolos.contains(datos.charAt(i))) {
                simbolos.add(datos.charAt(i));
            }
        }

        return simbolos;
    }

    public ArrayList<String> diferenciaPalabras(String datos, int cantCaracteres) {
        ArrayList<String> codigo = new ArrayList<>();
        String aux;
        int i = 0;

        while (i < datos.length()) {
            aux = "";
            for (int k = 0; k < cantCaracteres; k++) {
                if (i + k < datos.length())
                    aux += datos.charAt(i + k);
            }
            codigo.add(aux);
            i += cantCaracteres;
        }

        if (datos.length() % cantCaracteres != 0)
            codigo.remove(codigo.size() - 1);

        return codigo;
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
        HashMap<String, Integer> frecuencias = new HashMap<>();

        for (String i : datos) {
            Integer j = frecuencias.get(i);
            frecuencias.put(i, (j == null) ? 1 : j + 1);
        }

        return frecuencias;
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public HashMap<String, Double> calculaInformacion(HashMap<String, Integer> frecuencias, int total) {
        HashMap<String, Double> informacion = new HashMap<>();

        frecuencias.forEach((palabra, fr) -> {
            double probabilidad = (double) fr / total;
            informacion.put(palabra, log2(1 / probabilidad));
        });

        return informacion;
    }

    public double calculaEntropia(ArrayList<String> codigo, HashMap<String, Double> informacion, HashMap<String, Integer> frecuencias, int total) {
        double resultado = 0;

        for (String key : codigo) {
            double probabilidad = (double) frecuencias.get(key) / total;
            resultado += informacion.get(key) * probabilidad;
        }

        System.out.println("La entropia es: "+resultado);

        return resultado;
    }

    public void generarArchivoIncisoA() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoA.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoA.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("a) Calcular la cantidad de información y entropía.\n");

        bfwriter.write("\nInformacion Codigo3C:\n");
        informacion3C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nInformacion Codigo5C:\n");
        informacion5C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nInformacion Codigo7C:\n");
        informacion7C.forEach((key, inf) -> {
            try {
                bfwriter.write("\tI(" + key + ") = " + inf + " bits\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bfwriter.write("\nEntropia:\n");
        bfwriter.write("\tH(Codigo3C) = " + entropia3C + " bits\n");
        bfwriter.write("\tH(Codigo5C) = " + entropia5C + " bits\n");
        bfwriter.write("\tH(Codigo7C) = " + entropia7C + " bits\n");

        System.out.println("Archivo 'IncisoA.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public boolean simbolosCorrectos(ArrayList<Character> simbolos, String palabra) {

        for (int i = 0; i < palabra.length(); i++) {
            if (!simbolos.contains(palabra.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean esCodigoBloque(ArrayList<Character> simbolos, ArrayList<String> codigo) {

        for (int i = 0; i < codigo.size(); i++) {
            if (codigo.get(i).isEmpty() || !simbolosCorrectos(simbolos, codigo.get(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean esNoSingular(ArrayList<String> codigo) {

        for (String palabra: codigo) {
            if (Collections.frequency(codigo, palabra) > 1) {
                return false;
            }
        }

        return true;
    }

    public boolean esInstantaneo(ArrayList<String> codigo) {

        for (String palabra_prefijo: codigo) {
            for (String palabra_iterada: codigo) {
                if (!palabra_prefijo.equals(palabra_iterada)) {
                    if (palabra_iterada.startsWith(palabra_prefijo)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void generarArchivoIncisoB() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoB.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoB.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("b) Los códigos que se obtuvieron, ¿de que tipo son? Justificar su respuesta.\n");

        bfwriter.write("\nCodigo3C:\n");
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

        bfwriter.write("\nCodigo5C:\n");
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

        bfwriter.write("\nCodigo7C:\n");
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

    public boolean cumpleKraftMcMillan(ArrayList<String> codigo,ArrayList<Character> simbolos){
        double numeroCadenasSinRepetir=codigo.size();
        double cantidadCaracteres=simbolos.size();
        double longitudCadena;
        double resultado;
        if (codigo.isEmpty())
            return false;
        else {
            longitudCadena = codigo.get(0).length();
            resultado=numeroCadenasSinRepetir * Math.pow(cantidadCaracteres, -longitudCadena);
            System.out.println("El resultado de Kraft-McMillan es: "+resultado);
            return numeroCadenasSinRepetir * Math.pow(cantidadCaracteres, -longitudCadena) <= 1;
        }
    }

    public double longitudMedia(HashMap<String, Integer> frecuencias, int total, ArrayList<String> codigo){
        double longitudMedia=0;
        double longitudCadena;

        if (codigo.isEmpty())
            return 0;

        longitudCadena = codigo.get(0).length();

        for (String key : codigo) {
            double probabilidad = (double) frecuencias.get(key) / total;
            longitudMedia+=probabilidad*longitudCadena;
        }
        System.out.println("La longitud media es: "+longitudMedia);
        return longitudMedia;
    }

    public double calculaRendimiento(double entropia ,int largo){
        System.out.println("El rendimiento es: "+entropia/largo);
        return entropia/largo;
    }

    public double calculaRedundancia(double rendimiento){
        System.out.println("El rendimiento es: "+ (1-rendimiento));
        return 1-rendimiento;
    }
}