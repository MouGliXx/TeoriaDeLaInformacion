package modelo;

public class SegundaParte {

    private static int i=3;
    private static int j=4;
    private double[][] matriz = new double[i][j];
    private double[] probPriori = new double[i];
    private double[][] probPosteriori;
    private double[] probSalida;
    private double[][] probSucesoSimultaneo;
    private double entropiaPriori;
    private double[] entropiaPosteriori;
    private double equivocacion;
    private double informacionMutua;


    public SegundaParte(double[][] matriz, double[] vector){
        this.matriz = matriz;
        this.probPriori = vector;
        this.probSalida = calcProbSalida();
        this.probPosteriori = calcProbPosteriori();
        this.probSucesoSimultaneo = calcularProbSucesoSimultaneo();
        this.entropiaPriori = calcEntropiaPriori();
        this.entropiaPosteriori = calcEntropiaPosteriori();
        this.equivocacion = calcularEquivocacion();
        this.informacionMutua = calcInformacionMutua();
    }

    public double getEntropiaPriori (){
        return entropiaPriori;
    }

    public double getInformacionMutua (){
        return informacionMutua;
    }
    public double getEquivocacion (){
        return equivocacion;
    }
    public double[] getEntropiaPosteriori (){
        return entropiaPosteriori;
    }

    private double[] calcProbSalida(){
        int f,c;
        double suma ;
        double [] v = new double[j];
        for (c = 0 ; c < j ; c++){
            suma = 0;
            for (f = 0 ; f < i ; f++){
                suma += matriz[f][c]*probPriori[f];
            }
            v[c] = suma;
        }
        return v;
    }

    private double[][] calcProbPosteriori(){
        int f,c;
        double [][] v = new double[i][j];
        for (c = 0 ; c < j ; c++)
            for (f = 0 ; f < i ; f++)
                v[f][c] = matriz[f][c]*probPriori[f]/probSalida[c];
        return v;
    }
    private double[][] calcularProbSucesoSimultaneo(){
        double [][] v = new double[i][j];
        for (int c = 0 ; c < j ; c++)
            for (int f = 0 ; f < i ; f++)
                v[f][c] =  matriz[f][c]*probPriori[f];
        return v;
    }

    private double calcEntropiaPriori(){
        double entropia = 0;
        for (int f = 0 ; f < i ; f++)
            entropia += probPriori[f]*(Math.log(1/probPriori[f])/Math.log(2.));
        return entropia;
    }

    private double[] calcEntropiaPosteriori(){
        double[] entropia = new double[j];
        double suma ;
        for (int c = 0 ; c < j ; c++) {
            suma = 0.;
            for (int f = 0; f < i; f++) {
                if (probPosteriori[f][c]!=0.)
                    suma += probPosteriori[f][c] * (Math.log(1 / probPosteriori[f][c]) / Math.log((double)j));
            }
            entropia[c]= suma;
        }
        return entropia;
    }

    public double calcularEquivocacion (){
        double equivocacion = 0;
        for (int f = 0; f < i; f++)
            for (int c = 0 ; c < j ; c++)
                if (probPosteriori[f][c]!=0.)
                    equivocacion += probSucesoSimultaneo[f][c] * (Math.log(1 / probPosteriori[f][c]) / Math.log(2.));
        return equivocacion;
    }

    private double calcInformacionMutua(){
        double informacion = 0;
        for (int f = 0; f < i; f++)
            for (int c = 0 ; c < j ; c++)
                if (probPosteriori[f][c]!=0.)
                    informacion += probSucesoSimultaneo[f][c] * (Math.log(probPosteriori[f][c]/probPriori[f] ) / Math.log(2.));
        return informacion;
    }

    public void mostrarVectorEntropiaPosteriori (){
        for (int c = 0 ; c < entropiaPosteriori.length ; c++)
            System.out.print(entropiaPosteriori[c]+" ");
        System.out.println();
    }
    public void mostrarVectorSalida (){
        for (int c = 0 ; c < probSalida.length ; c++)
                System.out.print(probSalida[c]+" ");
        System.out.println();
    }

    public void mostrarProbabilidadSucesoSimultaneo (){
        for (int f = 0 ; f < i ; f++) {
            for (int c = 0; c < j; c++)
                System.out.print(probSucesoSimultaneo[f][c] + " ");
            System.out.println();
        }
    }

    public void mostrarProbabilidadPosteriori (){
        for (int c = 0 ; c < j ; c++) {
            for (int f = 0; f < i; f++)
                System.out.print(probPosteriori[f][c] + " ");
            System.out.println();
        }
        System.out.println();
    }
}
