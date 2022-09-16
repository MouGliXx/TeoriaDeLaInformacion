package app;

import java.util.ArrayList;

public class Modelo2 {

    public static ArrayList<String> diferenciaPalabras(String datos, int cantCaracteres) {
        ArrayList<String> codigo = new ArrayList<>();
        String aux;
        int i = 0, n = datos.length() % cantCaracteres;

        while (i < datos.length()) {
            aux = "";
            for (int k = 0; k < cantCaracteres; k++) {
                if (i + k < datos.length())
                    aux += datos.charAt(i + k);
            }
            codigo.add(aux);
            i += cantCaracteres;
        }

        if (n != 0)
            codigo.remove(codigo.size() - 1);

        return codigo;
    }
}
