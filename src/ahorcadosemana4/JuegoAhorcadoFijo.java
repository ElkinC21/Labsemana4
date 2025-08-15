
package ahorcadosemana4;




public class JuegoAhorcadoFijo   {   

private int intentos=6;
private String palabraactual;
private String palabrasecreta;

    public JuegoAhorcadoFijo(String palabra){
this.palabrasecreta=palabra;
}
public void actualizarImagen(){

}
public int restarintentos(){
return intentos-=1;
}
public void actualizarpalabra(char letra){
palabraactual += letra;
}
public void verificarLetra(char letra){
    for (int i = 0; i < palabrasecreta.length(); i++) {
     
     
     }
    }
}

