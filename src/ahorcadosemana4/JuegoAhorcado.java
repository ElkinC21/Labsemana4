/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ahorcadosemana4;

import java.util.ArrayList;

public interface JuegoAhorcado {

    ArrayList<String> getPalabrasSecretas();
    
    String inicializarPalabraSecreta();

    void jugar();
}

