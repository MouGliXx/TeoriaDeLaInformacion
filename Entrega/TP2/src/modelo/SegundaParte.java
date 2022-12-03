package modelo;

public class SegundaParte {
    private static int j;
    private static int i;
    private double[][] matriz;
    private double[] probPriori;
    private double[][] probPosteriori;
    private double[] probSalida;
    private double[][] probSucesoSimultaneo;
    private double entropiaPriori;
    private double[] entropiaPosteriori;
    private double equivocacion;
    private double informacionMutua;
    private double entropiaAfin;
    private double entropiaSalida;
    private double informacionMutuaSimetrica;
    private double ruido;
    private double perdida;

    public SegundaParte(int nroFilas, int nroColumnas,double[][] matriz, double[] vector){
        SegundaParte.setI(nroFilas);
        SegundaParte.setJ(nroColumnas);
        this.matriz = matriz;
        this.probPriori = vector;
        this.probSalida = calcProbSalida();
        this.probPosteriori = calcProbPosteriori();
        this.probSucesoSimultaneo = calcularProbSucesoSimultaneo();
        this.entropiaPriori = calcEntropiaPriori();
        this.entropiaPosteriori = calcEntropiaPosteriori();
        this.equivocacion = calcularEquivocacion();
        this.informacionMutua = calcInformacionMutua();
        this.entropiaAfin = calcEntropiaAfin();
        this.entropiaSalida = calcEntropiaSalida();
        this.informacionMutuaSimetrica = calcInformacionMutuaSimetrica();
        this.ruido = calcRuido();
        this.perdida = calcPerdida();
    }

    public static void setI(int nroFilas){
        SegundaParte.i = nroFilas;
    }

    public static void setJ(int nroColumnas){
        SegundaParte.j = nroColumnas;
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

    public double getEntropiaAfin (){
        return entropiaAfin;
    }

    public double getEntropiaSalida (){
        return entropiaSalida;
    }

    public double getPerdida(){
        return perdida;
    }

    public double getRuido(){
        return ruido;
    }

    public double getInformacionMutuaSimetrica (){
        return informacionMutuaSimetrica;
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

    private double calcEntropiaSalida(){
        double entropia = 0;
        for (int f = 0 ; f < j ; f++)
            entropia += probSalida[f]*(Math.log(1/probSalida[f])/Math.log(2.));
        return entropia;
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
                    suma += probPosteriori[f][c] * (Math.log(1 / probPosteriori[f][c]) / Math.log(2.));
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

    private double calcPerdida(){
        return this.entropiaAfin-this.entropiaPriori;
    }

    private double calcRuido(){
        return this.entropiaAfin-this.entropiaSalida;
    }

    private double calcInformacionMutuaSimetrica(){
        return this.entropiaPriori+this.entropiaSalida-this.entropiaAfin;
    }

    private double calcEntropiaAfin(){
        for (int f = 0; f < i; f++)
            for (int c = 0 ; c < j ; c++)
                if (probPosteriori[f][c]!=0.)
                    entropiaAfin += probSucesoSimultaneo[f][c] * (Math.log(1./probSucesoSimultaneo[f][c] ) / Math.log(2.));
        return entropiaAfin;
    }

    public void mostrarVectorEntropiaPosteriori (){
        for (double v : entropiaPosteriori)
            System.out.print(v + " ");
        System.out.println();
    }

    public void mostrarVectorSalida (){
        for (double v : probSalida)
            System.out.print(v + " ");
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

    public void mostrarResultados(){
        System.out.println("PROBABILIDAD DE SALIDA");
        mostrarVectorSalida();
        System.out.println();
        System.out.println("PROBABILIDAD A POSTERIORI");
        mostrarProbabilidadPosteriori();
        System.out.println("PROBABILIDAD DEL SUCESO SIMULTANEO");
        mostrarProbabilidadSucesoSimultaneo();
        System.out.println();
        System.out.println("H(A) = "+getEntropiaPriori());
        System.out.println();
        System.out.println("ENTROPIA SALIDA "+getEntropiaSalida());
        System.out.println();
        System.out.println("ENTROPIA A POSTERIORI ");
        mostrarVectorEntropiaPosteriori();
        System.out.println();
        System.out.println("H(A/B) = "+getEquivocacion());
        System.out.println();
        System.out.println("INFORMACION MUTUA "+getInformacionMutua());
        System.out.println();
        System.out.println("INFORMACION MUTUA SIMETRICA "+ getInformacionMutuaSimetrica());
        System.out.println();
        System.out.println("ENTROPIA AFIN "+getEntropiaAfin());
        System.out.println();
        System.out.println("PERDIDA "+getPerdida());
        System.out.println();
        System.out.println("RUIDO "+getRuido());
        System.out.println();
    }
}
