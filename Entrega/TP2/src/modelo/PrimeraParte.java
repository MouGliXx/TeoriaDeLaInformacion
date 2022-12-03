package modelo;

import java.io.*;
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

    public PrimeraParte(BufferedReader archivo) throws FileNotFoundException {
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

        decodificaArchivo("Archivos Generados/Primera Parte/Huffman.Huf","Huf");
        decodificaArchivo("Archivos Generados/Primera Parte/ShannonFano.Fan","Fan");
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

    public String codificaHuffman(Map<String,String> arbolHuffman, ArrayList<String> diccionario) {
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

            outputFileName = "Archivos Generados/Primera Parte/ShannonFano.txt";

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
        byte ochoBits=0b0, aux;
        int bitsRestantes=8,j,i;
        int byteFaltantes;
        int maxpalabra=longitudMaximaPalabra((HashMap<String, String>) shannonFano),maxCodificacion=longitudMaximaCodificacion((HashMap<String, String>) shannonFano);

            outputFileName = "Archivos Generados/Primera Parte/ShannonFano.Fan";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);


            bfwriter.write(shannonFano.size());
            bfwriter.write(maxpalabra);
            bfwriter.write(maxCodificacion);

            int numBytes=0;

            for (Map.Entry<String, String> entry : shannonFano.entrySet()) {
                if (entry.getKey().length() < maxpalabra) {
                    byteFaltantes = maxpalabra - entry.getKey().length();
                    for (j = 0; j < byteFaltantes; j++) {
                        ochoBits = 0;
                        bfwriter.write(ochoBits);
                    }
                }
                for (j = 0; j < entry.getKey().length(); j++) {
                    bfwriter.write(entry.getKey().charAt(j));
                }

                if (entry.getValue().length() < maxCodificacion) {
                    byteFaltantes = maxCodificacion - entry.getValue().length();
                    for (j = 0; j < byteFaltantes; j++) {
                        ochoBits = 0;
                        bfwriter.write(ochoBits);
                    }
                }
                for (j = 0; j < entry.getValue().length(); j++) {
                    bfwriter.write(entry.getValue().charAt(j));
                }
            }

            int largo;
            bitsRestantes=8;
            ochoBits = 0b0;

            for (String palabra : diccionario) {
                largo=shannonFano.get(palabra).length();
                i = 0;
                while (i < largo) {
                    if(bitsRestantes==0){
                        bfwriter.write(ochoBits);
                        ochoBits = 0b0;
                        bitsRestantes=8;
                    }
                    if (shannonFano.get(palabra).charAt(i) == '1') {
                        ochoBits |= (0b00000001 << (bitsRestantes-1));

                        /*aux = 0b1;
                        ochoBits = (byte) (ochoBits << 1);
                        ochoBits |= (aux);*/
                    }
                    bitsRestantes--;

                    i++;
                }
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
        int i,j, limite;
        int byteFaltantes;
        int maxpalabra=longitudMaximaPalabra((HashMap<String, String>) arbolHuffman),maxCodificacion=longitudMaximaCodificacion((HashMap<String, String>) arbolHuffman);

            outputFileName = "Archivos Generados/Primera Parte/Huffman.Huf";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            bfwriter.write(arbolHuffman.size());
            bfwriter.write(maxpalabra);
            bfwriter.write(maxCodificacion);

            int numBytes=0;


            for (Map.Entry<String, String> entry : arbolHuffman.entrySet()) {
                if (entry.getKey().length() < maxpalabra) {
                    byteFaltantes = maxpalabra - entry.getKey().length();
                    for (j = 0; j < byteFaltantes; j++) {
                        ochoBits = 0;
                        bfwriter.write(ochoBits);
                    }
                }
                for (j = 0; j < entry.getKey().length(); j++) {
                    bfwriter.write(entry.getKey().charAt(j));
                }

                if (entry.getValue().length() < maxCodificacion) {
                    byteFaltantes = maxCodificacion - entry.getValue().length();
                    for (j = 0; j < byteFaltantes; j++) {
                        ochoBits = 0;
                        bfwriter.write(ochoBits);
                    }
                }
                for (j = 0; j < entry.getValue().length(); j++) {
                    bfwriter.write(entry.getValue().charAt(j));
                }
            }

            int largo;
            int bitsRestantes=8;
            ochoBits = 0b0;

            for (String palabra : diccionario) {
                largo=huffman.get(palabra).length();
                i = 0;
                while (i < largo) {
                    if(bitsRestantes==0){
                        bfwriter.write(ochoBits);
                        ochoBits = 0b0;
                        bitsRestantes=8;
                    }
                    if (huffman.get(palabra).charAt(i) == '1') {
                        ochoBits |= (0b00000001 << (bitsRestantes-1));

                        /*aux = 0b1;
                        ochoBits = (byte) (ochoBits << 1);
                        ochoBits |= (aux);*/
                    }
                    bitsRestantes--;

                    i++;
                }
            }

            System.out.println("\tArchivo 'Huffman.Huf' creado satisfactoriamente.");
            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void decodificaArchivo(String url,String nombreArchivo){
        String outputFileName;
        FileWriter fileWriter;

            outputFileName = "Archivos Generados/Primera Parte/reconstruido."+nombreArchivo;

        int pos=0,j;

        try {
            BufferedReader archivo = new BufferedReader(new FileReader(url));
            //RandomAccessFile archivo=new RandomAccessFile(url, "rw");
            Map<String,String> tabla = new HashMap<>();

            int numSimbolos=archivo.read();
            int maxPalabra=archivo.read();
            int maxCodificacion=archivo.read();

            String linea;
            String datos="";


            while ((linea = archivo.readLine()) != null) {
                datos+=linea;
            }


            String palabra,codificacion;

            //decodifico la tabla

            for (j=0;j<numSimbolos;j++){
                palabra="";
                codificacion="";
                for (int k=0;k<maxPalabra;k++){
                    if(datos.charAt(pos)!=0)
                        palabra+=datos.charAt(pos);
                    pos++;
                }
                for (int l=0;l<maxCodificacion;l++){
                    if(datos.charAt(pos)!=0)
                        codificacion+=datos.charAt(pos);
                    pos++;

                }
                tabla.put(codificacion,palabra);
            }

            //decodifico los datos


            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);


            String cadenaATraducir="";
            String cadenaActual="";
            String aux="";

            while(datos.length()>pos){
                aux="";
                cadenaActual=Integer.toBinaryString(datos.charAt(pos));
                if(cadenaActual.length()>8){
                    cadenaActual=cadenaActual.substring(8);
                }
                else{
                    for(int i=0;i<8-cadenaActual.length();i++){
                        aux+="0";
                    }
                    cadenaActual=aux+cadenaActual;
                }
                cadenaATraducir+=cadenaActual;
                pos++;
            }

            int nuevapos=0;
            String codificado="";


            while (cadenaATraducir.length()>nuevapos){
                if(tabla.containsKey(codificado)){
                    bfwriter.write(tabla.get(codificado));
                    bfwriter.write(" ");
                    codificado="";
                    codificado+=cadenaATraducir.charAt(nuevapos);
                }
                else{
                    codificado+=cadenaATraducir.charAt(nuevapos);
                }
                nuevapos++;


            }

            bfwriter.close();
            fileWriter.close();

            System.out.println("\tArchivo 'reconstruccion."+nombreArchivo+"' creado satisfactoriamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void generaArchivoHuffman() {
        String outputFileName;
        FileWriter fileWriter;

            outputFileName = "Archivos Generados/Primera Parte/Huffman.txt";

        try {
            fileWriter = new FileWriter(outputFileName, false);
            BufferedWriter bfwriter = new BufferedWriter(fileWriter);

            bfwriter.write("Cantidad de simbolos = " + cantidadSimbolosHuffman + "\n");
            bfwriter.write("Longitud media = " + longitudMediaHuffman + "\n");
            bfwriter.write("Rendimiento = " + rendimientoHuffman+ "\n");
            bfwriter.write("Redundancia = " + redundanciaHuffman + "\n");
            bfwriter.write("\nDiccionario de Codificación por Huffman\n\n");
            bfwriter.write("SIMBOLO\t\t\t\tCODIFICACION\n");

            for (String palabra : frecuencias.keySet()) {
                bfwriter.write(palabra + "\t\t\t\t" + arbolHuffman.get(palabra) + "\n");
            }

            System.out.println("\tArchivo 'Huffman.txt' modificado satisfactoriamente...");

            bfwriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
