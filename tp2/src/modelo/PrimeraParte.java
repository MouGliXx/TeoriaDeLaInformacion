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
    private ArrayList<Character> simbolos;
    private HashMap<String, Integer> frecuencias;
    private HashMap<String, Double> informacion;
    private double entropia;
    private int orden;
    private Map<String,String> arbolHuffman;
    private double rendimientoHuffman,redundanciaHuffman;

    public PrimeraParte(BufferedReader archivo) {
        sistemaOperativo = System.getProperty("os.name");
        diccionario = generaDiccionario(archivo);
        simbolos = extraeSimbolos(diccionario);
        orden = simbolos.size();
        frecuencias = calculaFrecuencias(diccionario);
        codigo = identificaPalabrasCodigo(frecuencias);
        informacion = calculaInformacion(frecuencias, diccionario.size());
        entropia = calculaEntropia(informacion, frecuencias, diccionario.size());

        System.out.println(entropia);

        ShannonFano shannonFano = new ShannonFano(codigo, frecuencias, entropia);

        generaArchivoShannonFano(diccionario.size(), longitudMaximaPalabra(shannonFano.getShannonFano()), longitudMaximaCodificacion(shannonFano.getShannonFano()));

        arbolHuffman = construyeArbolHuffman(frecuencias);
        rendimientoHuffman=calculaRendimiento(calculaLongitudMediaHuffman(arbolHuffman,frecuencias,diccionario),entropia);
        redundanciaHuffman=calculaRedundancia(rendimientoHuffman);

        generarArchivoHuffman();
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
        System.out.println("La longitud media es: "+longitudMedia);

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
        while(it.hasNext()){
            codificacion+=arbolHuffman.get(it);
        }
        return codificacion;
    }

    public void generaArchivoShannonFano(int cantSimbolos, int longMaxPalabras, int longMaxCodigo) {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/Compresion.Fan";
        else
            outputFileName = "../Archivos Generados/Primera Parte/Compresion.Fan";

        System.out.println(cantSimbolos);
        System.out.println(longMaxPalabras);
        System.out.println(longMaxCodigo);

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            //....
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generarArchivoHuffman() {
        String outputFileName;
        FileWriter fileWriter;
        int longitudMaximaPalabra,longitudMaximaCodificacion;
        String codificacion=codificaHuffman(arbolHuffman,diccionario);

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/Huffman.dat";
        else
            outputFileName = "../Archivos Generados/Primera Parte/Huffman.dat";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            longitudMaximaPalabra=longitudMaximaPalabra((HashMap<String, String>) arbolHuffman);
            longitudMaximaCodificacion=longitudMaximaCodificacion((HashMap<String, String>) arbolHuffman);

            byte aux;
            byte ochoBits;
            int limite,i,n=0;

            while (n < codificacion.length()-1) {
                if(codificacion.length()-1-n>8)
                    limite = 8;
                else
                    limite = codificacion.length()-1-n;

                i = 0;
                ochoBits = 0b0;
                while(i < limite) {
                    if (codificacion.charAt(n) == '1') {
                        aux = 0b1;
                        ochoBits = (byte) (ochoBits << 1);
                        ochoBits |= (aux);
                    } else {
                        ochoBits = (byte) (ochoBits << 1);
                    }

                    i++;
                    n++;
                }
                bfwriter.write(ochoBits);
            }

            //TASA DE COMPRESION
            System.out.println("El rendimiento de huffman es: "+rendimientoHuffman);
            System.out.println("La redundancia de huffman es: "+redundanciaHuffman);



            System.out.println("\tArchivo 'Huffman.dat' modificado satisfactoriamente...");

            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
