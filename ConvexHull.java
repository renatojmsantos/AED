import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import java.time.Duration;
import java.time.Instant;

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
        return x + " " + y;
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

public class ConvexHull {

    public static void run(ArrayList<Ponto> pontos)
    {
        ArrayList<Ponto> res = new ArrayList<Ponto>();

        for(int i=0;i < pontos.size() - 1;i++){
            for (int j= i + 1; j <pontos.size();j++){

                boolean linha = true;
                int lado = 0;

                Ponto p1 = pontos.get(i);
                Ponto p2 = pontos.get(j);

                int a = p2.getY() - p1.getY();
                int b = p1.getX() - p2.getX();
                int c = p1.getX() * p2.getY() - p1.getY() * p2.getX();

                for (Ponto p3 : pontos) {
                    int check = (a * p3.getX() + b * p3.getY()) - c;

                    if (lado != 0) {
                        if (check > 0 && lado < 0 ) {
                            linha = false;
                            break;
                        } else if ( check < 0 && lado > 0 ) {
                            linha = false;
                            break;
                        }
                    } else {
                        if (check > 0 ) {
                            lado = 1;
                        } else if (check < 0) {
                            lado = -1;
                        }
                    }
                }
                if(linha){
                    res.add(p1);
                    res.add(p2);
                }
            }
        }
        Collections.sort(res, new PointCompare());

        ArrayList<Ponto> saida = removeDuplicado(res);

        for(int i = 0; i <= saida.size() - 1; i ++){
            System.out.println(saida.get(i).toString());
        }

    }

    public static ArrayList<Ponto> removeDuplicado(ArrayList<Ponto> ponto){
        ArrayList<Ponto> novaLista = new ArrayList<Ponto>();

        for(Ponto p: ponto){
            if(!novaLista.contains(p)){
                novaLista.add(p);
            }
        }
        return novaLista;
    }


    public static Scanner sc;
    public static void main(String[] args)
    {
        Instant start = Instant.now();

        ArrayList<Ponto> pontos = new ArrayList<Ponto>();

        sc = new Scanner(System.in);
        int np = sc.nextInt();

        int px,py;
        for (int j=0;j < np; j++){
            px = sc.nextInt();
            py = sc.nextInt();

            pontos.add(new Ponto(px,py));
        }
        run(pontos);


        Instant finish = Instant.now();
        float time = Duration.between(start,finish).toMillis();
        System.out.println("\nTempo: " + time + " ms");
    }
}