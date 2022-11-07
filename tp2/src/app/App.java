package app;

import modelo.SegundaParte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {

    public static void main (String[] args){

        //P(A) Y MATRIZ DE PRUEBA EJERCICIO DE LA GUIA
        double[][] m1 = new double[3][4];
        double[] v1 = new double[3];
        m1[0][0]= 0.25;m1[0][1]= 0.25;m1[0][2]= 0.25;m1[0][3]= 0.25;
        m1[1][0]= 0.25;m1[1][1]= 0.25;m1[1][2]= 0.;m1[1][3]= 0.5;
        m1[2][0]= 0.5;m1[2][1]= 0.;m1[2][2]= 0.5;m1[2][3]= 0.;
        v1[0]=0.25;v1[1]=0.25;v1[2]=0.5;
        //SegundaParte segundaParte = new SegundaParte(3,4,m1,v1);
        //segundaParte.mostrarResultados();

        double[][] matrizC1 = new double[5][3];
        double[] probPrioriC1 = new double[5];
        matrizC1[0][0]=0.3;matrizC1[0][1]=0.27;matrizC1[0][2]=0.43;
        matrizC1[1][0]=0.36;matrizC1[1][1]=0.4;matrizC1[1][2]=0.24;
        matrizC1[2][0]=0.3;matrizC1[2][1]=0.27;matrizC1[2][2]=0.43;
        matrizC1[3][0]=0.27;matrizC1[3][1]=0.4;matrizC1[3][2]=0.33;
        matrizC1[4][0]=0.3;matrizC1[4][1]=0.36;matrizC1[4][2]=0.34;
        probPrioriC1[0]=0.2;probPrioriC1[1]=0.1;probPrioriC1[2]=0.3;probPrioriC1[3]=0.3;probPrioriC1[4]=0.1;

        SegundaParte segundaParteC1 = new SegundaParte(5,3,matrizC1,probPrioriC1);
        System.out.println("CANAL 1");
        segundaParteC1.mostrarResultados();

        double[][] matrizC2 = new double[4][4];
        double[] probPrioriC2 = new double[4];
        matrizC2[0][0]=0.2;matrizC2[0][1]=0.24;matrizC2[0][2]=0.16;matrizC2[0][3]=0.4;
        matrizC2[1][0]=0.24;matrizC2[1][1]=0.3;matrizC2[1][2]=0.16;matrizC2[1][3]=0.3;
        matrizC2[2][0]=0.24;matrizC2[2][1]=0.16;matrizC2[2][2]=0.2;matrizC2[2][3]=0.4;
        matrizC2[3][0]=0.24;matrizC2[3][1]=0.3;matrizC2[3][2]=0.24;matrizC2[3][3]=0.22;
        probPrioriC2[0]=0.25;probPrioriC2[1]=0.33;probPrioriC2[2]=0.27;probPrioriC2[3]=0.15;

        SegundaParte segundaParteC2 = new SegundaParte(4,4,matrizC2,probPrioriC2);
        System.out.println("CANAL 2");
        segundaParteC2.mostrarResultados();

        double[][] matrizC3 = new double[6][4];
        double[] probPrioriC3 = new double[6];
        matrizC3[0][0]=0.2;matrizC3[0][1]=0.24;matrizC3[0][2]=0.16;matrizC3[0][3]=0.4;
        matrizC3[1][0]=0.24;matrizC3[1][1]=0.24;matrizC3[1][2]=0.3;matrizC3[1][3]=0.22;
        matrizC3[2][0]=0.16;matrizC3[2][1]=0.2;matrizC3[2][2]=0.24;matrizC3[2][3]=0.4;
        matrizC3[3][0]=0.24;matrizC3[3][1]=0.3;matrizC3[3][2]=0.16;matrizC3[3][3]=0.3;
        matrizC3[4][0]=0.2;matrizC3[4][1]=0.24;matrizC3[4][2]=0.24;matrizC3[4][3]=0.32;
        matrizC3[5][0]=0.16;matrizC3[5][1]=0.24;matrizC3[5][2]=0.3;matrizC3[5][3]=0.3;
        probPrioriC3[0]=0.15;probPrioriC3[1]=0.1;probPrioriC3[2]=0.2;probPrioriC3[3]=0.25;probPrioriC3[4]=0.14;probPrioriC3[5]=0.16;

        SegundaParte segundaParteC3 = new SegundaParte(6,4,matrizC3,probPrioriC3);
        System.out.println("CANAL 3");
        segundaParteC3.mostrarResultados();
    }
}
