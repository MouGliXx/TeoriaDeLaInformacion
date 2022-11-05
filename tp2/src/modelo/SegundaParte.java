package modelo;

public class SegundaParte {

    private static int i=2;
    private static int j=2;
    private double[][] matriz = new double[i][j];
    private double[] probPriori = new double[i];
    private double[][] probPosteriori;
    private double[] probSalida;
    private double[] probSucesoSimultaneo;
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
        double [] v = new double[i];
        for (c = 0 ; c < i ; c++){
            suma = 0;
            for (f = 0 ; f < j ; f++){
                suma += matriz[f][c]*probPriori[f];
            }
            v[c] = suma;
        }
        return v;
    }

    private double[][] calcProbPosteriori(){
        int f,c;
        double [][] v = new double[i][j];
        for (c = 0 ; c < i ; c++)
            for (f = 0 ; f < j ; f++)
                v[f][c] = matriz[f][c]*probPriori[f]/probSalida[c];
        return v;
    }
    private double[] calcularProbSucesoSimultaneo(){
        double [] v = new double[i];
        for (int c = 0 ; c < i ; c++)
            v[c] =  matriz[c][c]*probPriori[c];
        return v;
    }

    private double calcEntropiaPriori(){
        double entropia = 0;
        for (int i = 0 ; i < j ; i++)
            entropia += probPriori[i]*(Math.log(1/probPriori[i])/Math.log(2.));
        return entropia;
    }

    private double[] calcEntropiaPosteriori(){
        double[] entropia = new double[i];
        double suma ;
        for (int c = 0 ; c < j ; c++) {
            suma = 0.;
            for (int f = 0; f < i; f++)
                suma += probPosteriori[f][c] * (Math.log(1 / probPosteriori[f][c]) / Math.log(2.));
            entropia[c]= suma;
        }
        return entropia;
    }

    public double calcularEquivocacion (){
        double equivocacion = 0;
        for (int c = 0 ; c < j ; c++)
            for (int f = 0; f < i; f++)
                equivocacion += probSalida[f] * entropiaPosteriori[f];
        return equivocacion;
    }

    private double calcInformacionMutua(){
        return  equivocacion*entropiaPriori;
    }

    public void mostrarVectorEntropiaPosteriori (){
        for (int c = 0 ; c < i ; c++)
            System.out.print(entropiaPosteriori[c]+" ");
    }
    public void mostrarVectorSalida (){
        for (int c = 0 ; c < i ; c++)
                System.out.print(probSalida[c]+" ");
    }

    public void mostrarProbabilidadSucesoSimultaneo (){
        for (int c = 0 ; c < i ; c++)
            System.out.print(probSucesoSimultaneo[c]+" ");
    }

    public void mostrarProbabilidadPosteriori (){
        for (int f = 0 ; f < i ; f++) {
            for (int c = 0; c < i; c++)
                System.out.print(probPosteriori[f][c] + " ");
            System.out.println();
        }
    }
}
