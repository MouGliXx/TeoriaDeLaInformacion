package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class PrimeraParte {
    String sistemaOperativo;
    private int n;
    private final TreeMap<Character,Integer> simbolos = new TreeMap<>();
    double[][] matrizProbabilidades;
    double[] vectorEstacionario;
    boolean esMemoriaNula, esErgodica;
    double entropia;

    public PrimeraParte(String datos) {
        sistemaOperativo = System.getProperty("os.name");
        generaHashSimbolos(datos);
        matrizProbabilidades = generarMatrizProbabilidades(datos);
        esMemoriaNula = memoriaNoNula(matrizProbabilidades);
        esErgodica = esErgodica(matrizProbabilidades);
        vectorEstacionario = calcularVector(matrizProbabilidades);
        entropia = calcularEntropia(matrizProbabilidades,vectorEstacionario);
    }

    public double[][] generarMatrizProbabilidades(String datos) {
        double[][] prob = new double[n][n];
        int i, j, fila, columna;
        int[] totales = new int[n];

        for (i = 1; i < datos.length(); i++) {
            columna = simbolos.get(datos.charAt(i));
            fila = simbolos.get(datos.charAt(i-1));
            prob[fila][columna] += 1;
            totales[columna] +=1;
        }

        for (i = 0; i < n; i++)
            for (j = 0; j < n; j++)
                prob[i][j] /= totales[j];

        return prob;
    }

    private double[][] generarMatrizIdentidad() {
        double[][] matrizI = new double[n][n];
        int f, c;

        for (f = 0; f < n; f++)
            for (c = 0; c < n; c++) {
                if (f == c)
                    matrizI[f][c] = 1.;
                else
                    matrizI[f][c] = 0.;
            }
        return matrizI;
    }

    public boolean esErgodica(double[][] matriz) {
        int f, c = 0;
        double suma;
        boolean ergodica = true;

        while (ergodica && c < n) {
            suma = 0;
            for (f = 0; f < n; f++)
                if (f != c)
                    suma += matriz[f][c];
            if (suma == 0)
                ergodica = false;
            c += 1;
        }

        return ergodica;
    }

    public void generaHashSimbolos(String datos) {
        int t, c=0;
        char letra;

        for (t = 1; t <datos.length(); t++) {
            letra = datos.charAt(t);
            if (!(simbolos.containsKey(letra))) {
                simbolos.put(letra, c);
                c += 1;
            }
        }

        this.n = simbolos.size();
    }

    public double[][] restaMatrices(double[][] matrizProbabilidades) {
        int i,j;
        double[][] restaMatrices = new double[n][n];
        double[][] matrizIdentidad = generarMatrizIdentidad();

        for (i = 0; i < n; i++)
            for (j = 0; j < n; j++)
                restaMatrices[i][j] = matrizProbabilidades[i][j] - matrizIdentidad [i][j];

        return restaMatrices;
    }

    public double[] calcularVector (double[][] matriz){
        double[] vectorEstacionario;

        double[][] restaMatrices = restaMatrices(matriz);
        double[][] matrizAmpliada = creaMatrizAmpliada(restaMatrices);
        matrizAmpliada = triangulacionGauss(matrizAmpliada);
        matrizAmpliada = imponerCondicion (matrizAmpliada);
        matrizAmpliada = triangulacionGauss(matrizAmpliada);
        vectorEstacionario = sustitucionRegresiva(matrizAmpliada);

        return vectorEstacionario;
    }

    public double[][] imponerCondicion (double[][]matriz){
        int t;

        for (t = 0; t<= n; t++)
            matriz[n-1][t] = 1.;

        return matriz;
    }

    public boolean memoriaNoNula (double[][] matriz){
        boolean filaIguales = false;
        int f = 0,c = 0;

        while (c<n && !filaIguales){
            while (!filaIguales && f<n-1){
                if (matriz[f][c] != matriz[f][c+1])
                    filaIguales = true;
                f+=1;
            }
            c+=1;
        }

        return filaIguales;
    }

    public double[][] triangulacionGauss(double[][] matrizAmpliada) {
        int i, j, t, r;

        for (i = 0; i < n; i++) {
            for (t = i + 1; t < n + 1 ; t++)
                matrizAmpliada[i][t] /= matrizAmpliada[i][i];
            matrizAmpliada[i][i] = 1.;
            for (j = i+1; j < n; j++) {
                for (r = i + 1; r < n + 1; r++)
                    matrizAmpliada[j][r] = matrizAmpliada[j][r] - matrizAmpliada[i][r]*matrizAmpliada[j][i];
                matrizAmpliada[j][i] = 0.;
            }
        }

        return matrizAmpliada;
    }

    private double[][] creaMatrizAmpliada(double[][] matriz){
        int f,c;
        double[][] mAmpliada = new double[n][n+1];

        for (f = 0; f < n; f++)
            for (c = 0; c < n; c++)
                mAmpliada[f][c] = matriz[f][c];

        for (f = 0; f < n; f++)
            mAmpliada[f][n]=0.;

        return mAmpliada;
    }

    private double[] sustitucionRegresiva(double[][] matrizAmpliada){
        double suma;
        int f,c;
        double[] vectorEstacionario = new double[n];

        vectorEstacionario[n-1] = matrizAmpliada[n-1][n] / matrizAmpliada[n-1][n-1];
        for (f = n-2; f >= 0; f--) {
            suma = 0.;
            for (c = f+1; c < n; c++)
                suma += matrizAmpliada[f][c] * vectorEstacionario[c];
            vectorEstacionario[f]= (matrizAmpliada[f][n]-suma) / matrizAmpliada[f][f];
        }

        return vectorEstacionario;
    }

    public double calcularEntropia(double[][]matrizProbabilidades, double[]vectorEstacionario){
        int i,j;
        double entropia = 0., suma, log2;

        for(i = 0; i < n; i++){
            suma = 0.;
            for (j = 0; j < n; j++){
                if (matrizProbabilidades[j][i] != 0)
                    log2 = Math.log(1/matrizProbabilidades[j][i])/Math.log(2);
                else
                    log2 = 0.;
                suma += matrizProbabilidades[j][i]*log2;
            }
            entropia += (vectorEstacionario[i]*suma);
        }

        return entropia;
    }

    public void generarArchivoIncisoA() throws IOException {
        int f = 0, c;
        char [] llaves = new char[n];
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/IncisoA.txt";
        else
            outputFileName = "../Archivos Generados/Primera Parte/IncisoA.txt";

        fileWriter = new FileWriter(outputFileName,false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("MATRIZ DE PROBABILIDADES CONDICIONALES\n");


        for (Map.Entry<Character,Integer> entry : simbolos.entrySet()) {
            bfwriter.write("                  "+ entry.getKey());
            llaves[f] = entry.getKey();
            f +=1;
        }
        bfwriter.write("\n");
        for (f = 0; f < n; f++) {
            bfwriter.write(llaves[f]);
            for (c = 0; c < n; c++) {
                bfwriter.write("    "+matrizProbabilidades[f][c]);
            }
            bfwriter.write("\n");
        }

        bfwriter.write("\nFUENTE DE MEMORIA NO NULA: "+this.esMemoriaNula);

        System.out.println("\tArchivo 'IncisoA.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }

    public void generarArchivoIncisoC() throws IOException {
        int t;
        String outputFileName;
        FileWriter fileWriter;

        if (sistemaOperativo.startsWith("Windows"))
            outputFileName = "Archivos Generados/Primera Parte/IncisoC.txt";
        else
            outputFileName = "../Archivos Generados/Primera Parte/IncisoC.txt";

        fileWriter = new FileWriter(outputFileName,false);
        BufferedWriter bfwriter = new BufferedWriter(fileWriter);

        bfwriter.write("\nFUENTE ERGODICA : "+this.esErgodica+"\n");
        bfwriter.write("\nVECTOR ESTACIONARIO : \n");
        bfwriter.write("\n");

        for (t = 0; t<n;t++)
            bfwriter.write(" V"+t+"    "+vectorEstacionario[t]+"\n");

        bfwriter.write("\n");
        bfwriter.write("\nENTROPIA DE LA FUENTE: "+this.entropia);

        System.out.println("\tArchivo 'IncisoC.txt' modificado satisfactoriamente...");
        bfwriter.close();
        fileWriter.close();
    }
}