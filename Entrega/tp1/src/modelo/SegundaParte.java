package modelo;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static modelo.Huffman.construyeArbolHuffman;

public class SegundaParte {
    private String sistemaOperativo;
    private ArrayList<Character> simbolos;
    private int orden;
    private ArrayList<String> datos3C, datos5C, datos7C;
    private ArrayList<String> codigo3C, codigo5C, codigo7C;
    private HashMap<String, Integer> frecuencias3C, frecuencias5C, frecuencias7C;
    private HashMap<String, Double> informacion3C, informacion5C, informacion7C;
    private double entropia3C, entropia5C, entropia7C,longitudmedia3C,longitudmedia5C,longitudmedia7C;
    private boolean esCodBlq3C, esCodBlq5C, esCodBlq7C;
    private boolean  esNoSing3C, esNoSing5C, esNoSing7C;
    private boolean esInst3C, esInst5C, esInst7C;
    private double kraftMcMillan3C, kraftMcMillan5C, kraftMcMillan7C;
    private boolean esUnivoco3C, esUnivoco5C, esUnivoco7C;
    private boolean esCompacto3C, esCompacto5C, esCompacto7C;
    private double rendimiento3C, rendimiento5C, rendimiento7C;
    private double redundancia3C, redundancia5C, redundancia7C;
    private Map<String,String> arbolHuffman3C,arbolHuffman5C,arbolHuffman7C;
    private String reconstruccion3C,reconstruccion5C,reconstruccion7C;

    public SegundaParte(String datos) {
        sistemaOperativo = System.getProperty("os.name");
        simbolos = extraeSimbolos(datos);
        orden = simbolos.size();
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
        kraftMcMillan3C = inecuacionKraftMacMillan(codigo3C,simbolos);
        kraftMcMillan5C = inecuacionKraftMacMillan(codigo5C,simbolos);
        kraftMcMillan7C = inecuacionKraftMacMillan(codigo7C,simbolos);
        esUnivoco3C = esUnivoco(kraftMcMillan3C);
        esUnivoco5C = esUnivoco(kraftMcMillan5C);
        esUnivoco7C = esUnivoco(kraftMcMillan7C);
        longitudmedia3C = longitudMedia(frecuencias3C,datos3C.size(),codigo3C);
        longitudmedia5C = longitudMedia(frecuencias5C,datos5C.size(),codigo5C);
        longitudmedia7C = longitudMedia(frecuencias7C,datos7C.size(),codigo7C);
        esCompacto3C = esCompacto(entropia3C, longitudmedia3C);
        esCompacto5C = esCompacto(entropia5C, longitudmedia5C);
        esCompacto7C = esCompacto(entropia7C, longitudmedia7C);
        rendimiento3C = calculaRendimiento(entropia3C,3);
        rendimiento5C = calculaRendimiento(entropia5C,5);
        rendimiento7C = calculaRendimiento(entropia7C,7);
        redundancia3C = calculaRedundancia(rendimiento3C);
        redundancia5C = calculaRedundancia(rendimiento5C);
        redundancia7C = calculaRedundancia(rendimiento7C);
        arbolHuffman3C = construyeArbolHuffman(frecuencias3C);
        arbolHuffman5C = construyeArbolHuffman(frecuencias5C);
        arbolHuffman7C = construyeArbolHuffman(frecuencias7C);
        reconstruccion3C=reconstruyeArbolOriginalCodificado(arbolHuffman3C,datos3C);
        reconstruccion5C=reconstruyeArbolOriginalCodificado(arbolHuffman5C,datos5C);
        reconstruccion7C=reconstruyeArbolOriginalCodificado(arbolHuffman7C,datos7C);
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

    public double logOrden(double x) {
        return Math.log(x) / Math.log(orden);
    }

    public HashMap<String, Double> calculaInformacion(HashMap<String, Integer> frecuencias, int total) {
        HashMap<String, Double> informacion = new HashMap<>();
        for (Map.Entry<String,Integer> entry : frecuencias.entrySet()) {
            double probabilidad = (double) entry.getValue() / total;
            informacion.put(entry.getKey(), logOrden(1 / probabilidad));
            
        }
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

    public double inecuacionKraftMacMillan(ArrayList<String> codigo,ArrayList<Character> simbolos){
        double cantidadPalabras = codigo.size();
        double cantidadCaracteres = simbolos.size();
        double longitudCadena = codigo.get(0).length();

        return (codigo.isEmpty()) ? -1 : cantidadPalabras * Math.pow(cantidadCaracteres, -longitudCadena);
    }

    public boolean esUnivoco(double inecuacionKraft) {
        return (inecuacionKraft <= 1);
    }

    public double longitudMedia(HashMap<String, Integer> frecuencias, int total, ArrayList<String> codigo){
        double longitudCadena, longitudMedia = 0;

        if (codigo.isEmpty())
            return 0;

        longitudCadena = codigo.get(0).length();

        for (String key : codigo) {
            double probabilidad = (double) frecuencias.get(key) / total;
            longitudMedia+=probabilidad*longitudCadena;
        }

        return longitudMedia;
    }

    public boolean esCompacto(double entropia, double longitudMedia) {
        return entropia <= longitudMedia;
    }

    public double calculaRendimiento(double entropia ,int largo) {
        return entropia / largo;
    }

    public double calculaRedundancia(double rendimiento){
        return 1-rendimiento;
    }
    
    public String reconstruyeArbolOriginalCodificado(Map<String,String> arbolHuffman,ArrayList<String> cadenas){
        String reconstruccion="";
        for (String cadena : cadenas) {

            reconstruccion += arbolHuffman.get(cadena);
        }
        return reconstruccion;
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
        for (Map.Entry<String,Double> entry : informacion3C.entrySet()) {
            try {
                bfwriter.write("\tI(" + entry.getKey() + ") = " + entry.getValue() + " unidades de orden " + orden + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bfwriter.write("\nInformacion Codigo5C:\n");
        for (Map.Entry<String,Double> entry : informacion5C.entrySet()) {
            try {
                bfwriter.write("\tI(" + entry.getKey() + ") = " + entry.getValue() + " unidades de orden " + orden + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bfwriter.write("\nInformacion Codigo7C:\n");
        for (Map.Entry<String,Double> entry : informacion7C.entrySet()) {
            try {
                bfwriter.write("\tI(" + entry.getKey() + ") = " + entry.getValue() + " unidades de orden " + orden + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bfwriter.write("\nEntropia:\n");
        bfwriter.write("\tH(Codigo3C) = " + entropia3C + " unidades de orden " + orden + "\n");
        bfwriter.write("\tH(Codigo5C) = " + entropia5C + " unidades de orden " + orden + "\n");
        bfwriter.write("\tH(Codigo7C) = " + entropia7C + " unidades de orden " + orden + "\n");

        System.out.println("\tArchivo 'IncisoA.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
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
                if (esUnivoco3C) {
                    bfwriter.write("\tEs univocamente decodificable.\n");
                    if (esInst3C) {
                        bfwriter.write("\tEs instantaneo.\n");
                    } else {
                        bfwriter.write("\tNo es instantaneo.\n");
                    }
                } else {
                    bfwriter.write("\tNo es univocamente decodificable.\n");
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
                if (esUnivoco5C) {
                    bfwriter.write("\tEs univocamente decodificable.\n");
                    if (esInst5C) {
                        bfwriter.write("\tEs instantaneo.\n");
                    } else {
                        bfwriter.write("\tNo es instantaneo.\n");
                    }
                } else {
                    bfwriter.write("\tNo es univocamente decodificable.\n");
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
                if (esUnivoco7C) {
                    bfwriter.write("\tEs univocamente decodificable.\n");
                    if (esInst7C) {
                        bfwriter.write("\tEs instantaneo.\n");
                    } else {
                        bfwriter.write("\tNo es instantaneo.\n");
                    }
                } else {
                    bfwriter.write("\tNo es univocamente decodificable.\n");
                }
            } else {
                bfwriter.write("\tEs singular.\n");
            }
        } else {
            bfwriter.write("\tNo es codigo bloque.\n");
        }

        System.out.println("\tArchivo 'IncisoB.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoC() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoC.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoC.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("c) Establecer en cada caso la Inecuación de Kraft, MacMillan, Longitud Media del código y si\n" +
                           "cumplen con la condición de ser compactos. Obtener conclusiones.\n");

        bfwriter.write("\nCodigo3C:\n");
        bfwriter.write("\tInecuacion Kraft-MacMillan = " + kraftMcMillan3C + ((kraftMcMillan3C <= 1) ? " (<=1 cumple condicion)\n" : "(no cumple condicion)\n"));
        bfwriter.write("\tLongitud media = " + longitudmedia3C + "\n");
        if (esCompacto3C)
            bfwriter.write("\tEl codigo es compacto.\n");
        else
            bfwriter.write("\tEl codigo no es compacto.\n");

        bfwriter.write("\nCodigo5C:\n");
        bfwriter.write("\tInecuacion Kraft-MacMillan = " + kraftMcMillan5C + ((kraftMcMillan5C <= 1) ? " (<=1 cumple condicion)\n" : "(no cumple condicion)\n"));
        bfwriter.write("\tLongitud media = " + longitudmedia5C + "\n");
        if (esCompacto5C)
            bfwriter.write("\tEl codigo es compacto.\n");
        else
            bfwriter.write("\tEl codigo no es compacto.\n");

        bfwriter.write("\nCodigo7C:\n");
        bfwriter.write("\tInecuacion Kraft-MacMillan = " + kraftMcMillan7C + ((kraftMcMillan7C <= 1) ? " (<=1 cumple condicion)\n" : "(no cumple condicion)\n"));
        bfwriter.write("\tLongitud media = " + longitudmedia7C + "\n");
        if (esCompacto7C)
            bfwriter.write("\tEl codigo es compacto.\n");
        else
            bfwriter.write("\tEl codigo no es compacto.\n");

        System.out.println("\tArchivo 'IncisoC.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoD() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoD.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoD.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("d) Determinar el rendimiento y redundancia de cada código.\n");

        bfwriter.write("\nCodigo3C:\n");
        bfwriter.write("\tRendimiento: " + rendimiento3C + "\n");
        bfwriter.write("\tRedundancia: " + redundancia3C + "\n");

        bfwriter.write("\nCodigo5C:\n");
        bfwriter.write("\tRendimiento: " + rendimiento5C + "\n");
        bfwriter.write("\tRedundancia: " + redundancia5C + "\n");

        bfwriter.write("\nCodigo7C:\n");
        bfwriter.write("\tRendimiento: " + rendimiento7C + "\n");
        bfwriter.write("\tRedundancia: " + redundancia7C + "\n");

        System.out.println("\tArchivo 'IncisoD.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoE1() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE1.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE1.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("e) Codificar los símbolos de los códigos anteriores según Huffman o Shanon-Fano (a elección)\n" +
                "y reconstruir el archivo (en tres archivos, uno por codificación).\n");

        bfwriter.write("\tArbol Huffman: \n" + arbolHuffman3C + "\n");

        System.out.println("\tArchivo 'IncisoE1.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoE1binario() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE1.dat";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE1.dat";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        byte aux;
        byte ochoBits;
        int limite,i,n=0;


        while (n<(reconstruccion3C.length()-1)) {
            if(reconstruccion3C.length()-n-1>8)
                limite=8;
            else
                limite=reconstruccion3C.length()-n-1;
            i=0;
            ochoBits=0b0;
            while(i<limite) {
                if (reconstruccion3C.charAt(n) == '1') {
                    aux = 0b1;
                    ochoBits = (byte) (ochoBits << 1);
                    ochoBits |= (aux);
                }
                else
                    ochoBits= (byte) (ochoBits<<1);
                i++;
                n++;

            }
            bfwriter.write(ochoBits);
        }


        System.out.println("\tArchivo 'IncisoE1.dat' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }



    public void generarArchivoIncisoE2() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE2.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE2.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("e) Codificar los símbolos de los códigos anteriores según Huffman o Shanon-Fano (a elección)\n" +
                "y reconstruir el archivo (en tres archivos, uno por codificación).\n");

        bfwriter.write("\tArbol Huffman: \n" + arbolHuffman5C + "\n");

        System.out.println("\tArchivo 'IncisoE2.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public static String toBinary(byte n, int len)
    {
        String binary = "";
        for (long i = (1L << len - 1); i > 0; i = i / 2) {
            binary += (n & i) != 0 ? "1" : "0";
        }
        return binary;
    }

    public void generarArchivoIncisoE2binario() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE2.dat";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE2.dat";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        byte aux;
        byte ochoBits;
        int limite,i,n=0;


        while (n<(reconstruccion5C.length()-1)) {
            if(reconstruccion5C.length()-n-1>8)
                limite=8;
            else
                limite=reconstruccion5C.length()-n-1;
            i=0;
            ochoBits=0b0;
            while(i<limite) {
                if (reconstruccion5C.charAt(n) == '1') {
                    aux = 0b1;
                    ochoBits = (byte) (ochoBits << 1);
                    ochoBits |= (aux);
                }
                else
                    ochoBits= (byte) (ochoBits<<1);
                i++;
                n++;

            }
            bfwriter.write(ochoBits);
        }


        System.out.println("\tArchivo 'IncisoE2.dat' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoE3() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE3.txt";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE3.txt";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("e) Codificar los símbolos de los códigos anteriores según Huffman o Shanon-Fano (a elección)\n" +
                "y reconstruir el archivo (en tres archivos, uno por codificación).\n");

        bfwriter.write("\tArbol Huffman: \n" + arbolHuffman7C + "\n");

        System.out.println("\tArchivo 'IncisoE3.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoE3binario() throws IOException {
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Segunda Parte/IncisoE3.dat";
        else
            outputFileName = "../Archivos Generados/Segunda Parte/IncisoE3.dat";

        fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        byte aux;
        byte ochoBits;
        int limite,i,n=0;


        while (n<(reconstruccion7C.length()-1)) {
            if(reconstruccion7C.length()-n-1>8)
                limite=8;
            else
                limite=reconstruccion7C.length()-n-1;
            i=0;
            ochoBits=0b0;
            while(i<limite) {
                if (reconstruccion7C.charAt(n) == '1') {
                    aux = 0b1;
                    ochoBits = (byte) (ochoBits << 1);
                    ochoBits |= (aux);
                }
                else
                    ochoBits= (byte) (ochoBits<<1);
                i++;
                n++;

            }
            bfwriter.write(ochoBits);
        }


        System.out.println("\tArchivo 'IncisoE3.dat' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }
}