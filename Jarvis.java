
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;
/*
import java.time.Duration;
import java.time.Instant;
*/

class Ponto {
    int x, y;
    Ponto(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + " " +y;
    }
}

class PointCompare implements Comparator<Ponto>{
    public int compare(Ponto p1, Ponto p2) {
        if(p1.x < p2.x) {
            return -1;
        }
        else if(p1.x > p2.x) {
            return 1;
        }
        else {
            if(p1.y < p2.y) {
                return -1;
            }
            else if(p1.y > p2.y) {
                return 1;
            }
            else {
                return 0;
            }
        }

    }
}

public class Jarvis {

    public static int verifica(Ponto p1, Ponto p2, Ponto p3){
        int a = (p2.getY() - p1.getY());
        int b = (p2.getX() - p1.getX());

        int px = (p3.getX() - p2.getX());
        int py = (p3.getY() - p2.getY());

        return (a * px) - (b * py); // verificar sentido anti relogio, se c = 0 -> colinear


    }

    public static void algJarvis(ArrayList<Ponto> pontos){

        ArrayList<Ponto> resultado = new ArrayList<Ponto>();

        int pontoEsq = 0;
        for (int p = 0; p < pontos.size();p++){
            if(pontos.get(p).getX() < pontos.get(pontoEsq).getX()){
                pontoEsq = p;
            }
        }

        int p1 = pontoEsq; //comeca no ponto mais a esquerda

        do{
            // se o resultado ainda n tiver aquele ponto
            resultado.add(pontos.get(p1));

            int p2 = (p1 + 1) % pontos.size();

            for(int i=0;i<pontos.size();i++){
                if(verifica(pontos.get(p1),pontos.get(i),pontos.get(p2)) < 0 ){
                    p2 = i;
                }
            }
            p1 = p2;
        }while (p1 != pontoEsq);

        Collections.sort(resultado, new PointCompare());

        for(Ponto p: resultado){
            System.out.println(p.toString());
        }
    }

    public static Scanner sc;
    public static void main(String[] args)
    {
        //Instant start = Instant.now();

        sc = new Scanner(System.in);
        int np = sc.nextInt();

        ArrayList<Ponto> pontos = new ArrayList<Ponto>();

        int px,py;
        for (int j=0;j < np; j++){
            px = sc.nextInt();
            py = sc.nextInt();

            pontos.add(new Ponto(px,py));
        }

        algJarvis(pontos);
        /*
        Instant finish = Instant.now();
        float time = Duration.between(start,finish).toMillis();
        System.out.println("\nTempo: " + time + " ms");*/
    }
}