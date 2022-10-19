package modelo;

import java.util.*;

class Nodo {
    String cadena;
    Integer frecuencia;
    Nodo izq = null, der = null;

    public Nodo(String cadena, Integer frecuencia, Nodo izq, Nodo der) {
        this.cadena = cadena;
        this.frecuencia = frecuencia;
        this.izq = izq;
        this.der = der;
    }

    public Nodo(String cadena, Integer frecuencia) {
        this.cadena=cadena;
        this.frecuencia=frecuencia;
    }
}

class comparadorNodo implements  Comparator<Nodo>{
    public int compare(Nodo n1,Nodo n2){
        return n1.frecuencia-n2.frecuencia;
    }
}

public class Huffman {
    public static boolean esHoja(Nodo root){
        return root.izq==null && root.der==null;
    }

    public static void encode(Nodo root, String str, Map<String, String> huffmanCode)
    {
        if (root == null) {
            return;
        }

        // Es un nodo hoja
        if (esHoja(root)) {
            huffmanCode.put(root.cadena, str.length() > 0 ? str : "1");
        }

        encode(root.izq, str + '0', huffmanCode);
        encode(root.der, str + '1', huffmanCode);
    }

    public static Map<String,String> construyeArbolHuffman(Map<String,Integer> datos)
    {
        PriorityQueue<Nodo> colaDatos = new PriorityQueue<>(new comparadorNodo());

        Iterator<String> it=datos.keySet().iterator();


        while(it.hasNext()){
            String key=it.next();
            colaDatos.add(new Nodo(key, datos.get(key)));
        }


        while(colaDatos.size()>1){
            Nodo izq=colaDatos.poll();
            Nodo der=colaDatos.poll();

            int suma=izq.frecuencia+der.frecuencia;
            colaDatos.add(new Nodo(null,suma,izq,der));
        }

        Map<String,String> huffman=new HashMap<>();
        Nodo raiz=colaDatos.peek();
        encode(raiz,"",huffman);


        return huffman;

    }




}


