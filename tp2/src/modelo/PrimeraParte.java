package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static modelo.Huffman.construyeArbolHuffman;

public class PrimeraParte {
    private String sistemaOperativo;
    private ArrayList<String> diccionario, codigo;
    private HashMap<String, Integer> frecuencias;
    private HashMap<String, Double> informacion;
    private double entropia;
    private Map<String,String> arbolHuffman;
    private double rendimientoHuffman,redundanciaHuffman,longitudMediaHuffman;
    private int cantidadSimbolosHuffman;

    public PrimeraParte(BufferedReader archivo) {
        sistemaOperativo = System.getProperty("os.name");
        diccionario = generaDiccionario(archivo);
        frecuencias = calculaFrecuencias(diccionario);
        codigo = identificaPalabrasCodigo(frecuencias);
        informacion = calculaInformacion(frecuencias, diccionario.size());
        entropia = calculaEntropia(informacion, frecuencias, diccionario.size());

        ShannonFano shannonFano = new ShannonFano(codigo, frecuencias, entropia, diccionario.size());

        generaArchivoShannonFano(frecuencias.size(), shannonFano);
        generaCompresionShannonFano(diccionario, shannonFano.getShannonFano());

        arbolHuffman = construyeArbolHuffman(frecuencias);
        rendimientoHuffman=calculaRendimiento(calculaLongitudMediaHuffman(arbolHuffman,frecuencias,diccionario),entropia);
        redundanciaHuffman=calculaRedundancia(rendimientoHuffman);
        longitudMediaHuffman=calculaLongitudMediaHuffman(arbolHuffman,frecuencias,diccionario);
        cantidadSimbolosHuffman=arbolHuffman.size();

        generaCompresionHuffman(diccionario,arbolHuffman);
        generaArchivoHuffman();
    }

    private ArrayList<String> generaDiccionario(BufferedReader archivo) {
        ArrayList<String> datos = new ArrayList<>();
        String linea;

        try {
            while ((linea = archivo.readLine()) != null) {
                String[] palabras = linea.split("\\s+");
                Collections.addAll(datos, palabras);
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
        return new ArrayList<>(frecuencias.keySet());
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

    public double calculaEntropia(HashMap<String, Double> informacion, HashMap<String, Integer> frecuencias, int total) {
        double resultado = 0;

        for (String key : frecuencias.keySet()) {
            double probabilidad = (double) frecuencias.get(key) / total;
            resultado += informacion.get(key) * probabilidad;
        }

        return resultado;
    }

    public double calculaLongitudMediaHuffman(Map<String,String> arbolHuffman,HashMap<String, Integer> frecuencias,ArrayList<String> diccionario){
        int aparicionesTotales = diccionario.size();
        double longitudMedia = 0;
        double probabilidad;

        for (Map.Entry<String, String> arbolHuff : arbolHuffman.entrySet()) {
            probabilidad=0;
            for (Map.Entry<String, Integer> fre : frecuencias.entrySet()) {
                if(fre.getKey().equals(arbolHuff.getKey())){
                    probabilidad = fre.getValue();
                }
            }
            longitudMedia += arbolHuff.getValue().length() * (probabilidad/aparicionesTotales);
        }
        return longitudMedia;
    }

    public double calculaRendimiento(double longitudMedia,double entropia){
        return entropia/longitudMedia;
    }

    public double calculaRedundancia(double rendimiento){
        return 1-rendimiento;
    }

    public int longitudMaximaPalabra(HashMap<String, String> codificacion) {
        int max = -1;

        for (String palabra : codificacion.keySet()) {
            if (palabra.length() > max) {
                max = palabra.length();
            }
        }

        return max;
    }

    public int longitudMaximaCodificacion(HashMap<String, String> codificacion) {
        int max = -1;

        for (String palabra : codificacion.keySet()) {
            if (codificacion.get(palabra).length() > max) {
                max = codificacion.get(palabra).length();
            }
        }

        return max;
    }

    public String codificaHuffman(Map<String,String> arbolHuffman, ArrayList<String> diccionario){
        Iterator<String> it = diccionario.iterator();
        String codificacion="";

        for (String palabra : diccionario) {
            codificacion+=arbolHuffman.get(palabra);
        }
        return codificacion;
    }

    public void generaArchivoShannonFano(int cantSimbolos, ShannonFano shannonFano) {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/ShannonFano.txt";
        else
            outputFileName = "../Archivos Generados/Primera Parte/ShannonFano.txt";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            bfwriter.write("Cantidad de simbolos = " + cantSimbolos + "\n");
            bfwriter.write("Longitud media = " + shannonFano.getLongitudMedia() + "\n");
            bfwriter.write("Rendimiento = " + shannonFano.getRendimiento() + "\n");
            bfwriter.write("Redundancia = " + shannonFano.getRedundancia() + "\n");
            bfwriter.write("\nDiccionario de Codificación por Shannon-Fano\n\n");
            bfwriter.write("SIMBOLO\t\t\t\tCODIFICACION\n");
            for (String palabra : frecuencias.keySet()) {
                bfwriter.write(palabra + "\t\t\t\t" + shannonFano.getShannonFano().get(palabra) + "\n");
            }

            System.out.println("\tArchivo 'ShannonFano.txt' creado satisfactoriamente.");
            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generaCompresionShannonFano(ArrayList<String> diccionario, Map<String,String> shannonFano) {
        String outputFileName;
        FileWriter fileWriter;
        byte ochoBits, aux;
        int i, limite;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/ShannonFano.Fan";
        else
            outputFileName = "../Archivos Generados/Primera Parte/ShannonFano.Fan";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            for (String palabra : diccionario) {
                if(palabra.length()-1 > 8)
                    limite = 8;
                else
                    limite = palabra.length()-1;

                i = 0;
                ochoBits = 0b0;
                while (i < limite) {
                    if (shannonFano.get(palabra).charAt(i) == '1') {
                        aux = 0b1;
                        ochoBits = (byte) (ochoBits << 1);
                        ochoBits |= (aux);
                    } else {
                        ochoBits = (byte) (ochoBits << 1);
                    }

                    i++;
                }
                bfwriter.write(ochoBits);
            }

            System.out.println("\tArchivo 'ShannonFano.Fan' creado satisfactoriamente.");
            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generaCompresionHuffman(ArrayList<String> diccionario, Map<String,String> huffman) {
        String outputFileName;
        FileWriter fileWriter;
        byte ochoBits, aux;
        int i, limite;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/Huffman.Huf";
        else
            outputFileName = "../Archivos Generados/Primera Parte/Huffman.Huf";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            for (String palabra : diccionario) {
                if(palabra.length()-1 > 8)
                    limite = 8;
                else
                    limite = palabra.length()-1;

                i = 0;
                ochoBits = 0b0;
                while (i < limite) {
                    if (huffman.get(palabra).charAt(i) == '1') {
                        aux = 0b1;
                        ochoBits = (byte) (ochoBits << 1);
                        ochoBits |= (aux);
                    } else {
                        ochoBits = (byte) (ochoBits << 1);
                    }

                    i++;
                }
                bfwriter.write(ochoBits);
            }

            System.out.println("\tArchivo 'Huffman.Huf' creado satisfactoriamente.");
            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generaArchivoHuffman() {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/Huffman.txt";
        else
            outputFileName = "../Archivos Generados/Primera Parte/Huffman.txt";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            bfwriter.write("Cantidad de simbolos = " + cantidadSimbolosHuffman + "\n");
            bfwriter.write("Longitud media = " + longitudMediaHuffman + "\n");
            bfwriter.write("Rendimiento = " + rendimientoHuffman+ "\n");
            bfwriter.write("Redundancia = " + redundanciaHuffman + "\n");
            bfwriter.write("\nDiccionario de Codificación por Huffman\n\n");
            bfwriter.write("SIMBOLO\t\t\t\tCODIFICACION\n");

            for (Map.Entry<String, String> entry : arbolHuffman.entrySet()) {
                bfwriter.write(entry.getKey() + " : "+ entry.getValue() + "- FRECUENCIA: "+frecuencias.get(entry.getKey())+ "\n");
            }
            System.out.println("\tArchivo 'Huffman.txt' modificado satisfactoriamente...");

            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
