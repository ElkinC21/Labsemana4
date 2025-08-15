
package ahorcadosemana4;

import java.util.ArrayList;

public interface JuegoAhorcado {

    ArrayList<String> getPalabrasSecretas();
    
    String inicializarPalabraSecreta();

    void jugar();
}

