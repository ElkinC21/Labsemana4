/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ahorcadosemana4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author saidn
 */
public abstract class JuegoAhorcadoBase {

    public String palabraSecreta;
    public String palabraActual;
    public int intentos;
    public int limiteIntentos;
    public static ArrayList<Character> letrasUsadas = new ArrayList<>();
    public static ArrayList<String> figuraAhorcado = new ArrayList<>();

    public JuegoAhorcadoBase() {

    }

    public void actualizarPalabraActual(char letra) {}
    
    public void verificarLetra(char letra) {}
    
    public void hasGanado(){}

}
