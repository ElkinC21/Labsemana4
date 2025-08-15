
package ahorcadosemana4;

import java.util.ArrayList;




public class JuegoAhorcadoFijo extends JuegoAhorcadoBase   {   

    private ArrayList<String> palabrasSecretas=new ArrayList<>();



    public JuegoAhorcadoFijo(){

}
public void actualizarImagen(){

}
public int restarintentos(){
return intentos-=1;
}
public String actualizarpalabraActual(char letra){
palabraActual += letra;
return palabraActual;
}
public boolean verificarLetra(char letra){
    for (int i = 0; i < palabraSecreta.length(); i++) {
     if(letra==palabraSecreta.charAt(i)){
     return true;
     }
     
     }
    return false;
}


    public void agregarPalabrasSecretas(String palabra) {
       palabrasSecretas.add(palabra);
       
    }

  
    public String inicializarPalabraSecreta() {
        
    }

    
    public void jugar() {
      
    }

    

   
}

