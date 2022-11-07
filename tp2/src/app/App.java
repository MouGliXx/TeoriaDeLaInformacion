package app;

import modelo.SegundaParte;

public class App {

    public static void main (String[] args){

        double[][] matriz = new double[5][3];
        double[] probPriori = new double[5];

        double[][] m = new double[2][2];
        double[] v = new double[2];
        m[0][0]= 2./3.;m[0][1]= 1./3.;
        m[1][0]= 0.1;m[1][1]= 0.9;
        v[0]=0.75;v[1]=0.25;

        double[][] m1 = new double[3][4];
        double[] v1 = new double[3];
        m1[0][0]= 0.25;m1[0][1]= 0.25;m1[0][2]= 0.25;m1[0][3]= 0.25;
        m1[1][0]= 0.25;m1[1][1]= 0.25;m1[1][2]= 0.;m1[1][3]= 0.5;
        m1[2][0]= 0.5;m1[2][1]= 0.;m1[2][2]= 0.5;m1[2][3]= 0.;
        v1[0]=0.25;v1[1]=0.25;v1[2]=0.5;

        matriz[0][0]=0.3;matriz[0][1]=0.27;matriz[0][2]=0.43;
        matriz[1][0]=0.36;matriz[1][1]=0.4;matriz[1][2]=0.24;
        matriz[2][0]=0.3;matriz[2][1]=0.27;matriz[2][2]=0.43;
        matriz[3][0]=0.27;matriz[3][1]=0.4;matriz[3][2]=0.33;
        matriz[4][0]=0.3;matriz[4][1]=0.36;matriz[4][2]=0.34;

        probPriori[0]=0.2;probPriori[1]=0.1;probPriori[2]=0.3;probPriori[3]=0.3;probPriori[4]=0.1;

        SegundaParte segundaParte = new SegundaParte(m1,v1);

        System.out.println("PROBABILIDAD DE SALIDA");
        segundaParte.mostrarVectorSalida();
        System.out.println();
        System.out.println("PROBABILIDAD A POSTERIORI");
        segundaParte.mostrarProbabilidadPosteriori();
        System.out.println("PROBABILIDAD DEL SUCESO SIMULTANEO");
        segundaParte.mostrarProbabilidadSucesoSimultaneo();
        System.out.println();
        System.out.println("H(A) = "+segundaParte.getEntropiaPriori());
        System.out.println();
        System.out.println("ENTROPIA A POSTERIORI ");
        segundaParte.mostrarVectorEntropiaPosteriori();
        System.out.println();
        System.out.println("H(A/B) = "+segundaParte.getEquivocacion());
        System.out.println();
        System.out.println("INFORMACION MUTUA "+segundaParte.getInformacionMutua());
    }
}
