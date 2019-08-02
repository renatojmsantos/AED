import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

class Filme{
    private int ano;
    private int views;
    private String nome;

    public Filme(int ano, int views, String nome) {
        this.ano = ano;
        this.views = views;
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}


class Quicksort{

    // ======== ORDENAR POR ORDEM CRESCENTE A DATA ===========

    private int partitionData(ArrayList<Filme> filmes, int inicio, int fim){ //inicio e fim da arraylist
        Filme pivot = filmes.get(fim);
        int i = (inicio - 1); // menor elemento

        for(int j = inicio; j < fim; j++){
            if(filmes.get(j).getAno() <= pivot.getAno()){
                i++;
                // troca
                Filme temp = filmes.get(i);
                filmes.set(i,filmes.get(j));
                filmes.set(j,temp);
            }
        }
        Filme temp = filmes.get(i+1);
        filmes.set(i+1,filmes.get(fim));
        filmes.set(fim,temp);

        return i+1;
    }

    void sortData(ArrayList<Filme> f, int inicio, int fim){
        if(inicio < fim){
            int m = partitionData(f, inicio, fim); //meio
            sortData(f, inicio, m-1);
            sortData(f, m + 1, fim);
        }
    }

    // ======== ORDENAR POR ORDEM DECRESCENTE AS VIEWS ===========
    private int partitionPop(ArrayList<Filme> filmes, int inicio, int fim){
        Filme pivot = filmes.get(fim);
        int i = (inicio - 1); // menor elemento

        for(int j = inicio; j < fim; j++){
            if(filmes.get(j).getViews() >= pivot.getViews()){
                i++;
                // troca
                Filme temp = filmes.get(i);
                filmes.set(i,filmes.get(j));
                filmes.set(j,temp);
            }
        }
        Filme temp = filmes.get(i+1);
        filmes.set(i+1,filmes.get(fim));
        filmes.set(fim,temp);

        return i+1;
    }

    void sortPop(ArrayList<Filme> f, int inicio, int fim){
        if(inicio < fim){
            int m = partitionPop(f, inicio, fim);
            sortPop(f, inicio, m-1);
            sortPop(f, m + 1, fim);
        }
    }

    // ======== ORDENAR POR ORDEM CRESCENTE O NOME ===========

    private int partitionName(ArrayList<Filme> filmes, int inicio, int fim){
        Filme pivot = filmes.get(fim);
        int i = (inicio - 1); // menor elemento

        for(int j = inicio; j < fim; j++){
            if(filmes.get(j).getNome().compareTo(pivot.getNome()) <= 0){
                i++;
                // troca
                Filme temp = filmes.get(i);
                filmes.set(i,filmes.get(j));
                filmes.set(j,temp);
            }
        }
        Filme temp = filmes.get(i+1);
        filmes.set(i+1,filmes.get(fim));
        filmes.set(fim,temp);

        return i+1;
    }

    void sortName(ArrayList<Filme> f, int inicio, int fim){
        if(inicio < fim){
            int m = partitionName(f, inicio, fim);
            sortName(f, inicio, m-1);
            sortName(f, m + 1, fim);
        }
    }
}


public class Main {

    /* https://howtodoinjava.com/java/string/remove-leading-whitespaces/ */
    public static String removeLeadingSpaces(String param)
    {
        if (param == null) {
            return null;
        }

        if(param.isEmpty()) {
            return "";
        }

        int arrayIndex = 0;
        while(true)
        {
            if (!Character.isWhitespace(param.charAt(arrayIndex++))) {
                break;
            }
        }
        return param.substring(arrayIndex-1);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int nr_filmes = sc.nextInt();
        ArrayList<Filme> filmes = new ArrayList<Filme>();

        for(int i=0; i<nr_filmes; i++){
            int year = sc.nextInt();
            int views = sc.nextInt();
            String name = sc.nextLine();

            filmes.add(new Filme(year,views,name));
        }

        // ANO --- YYYY MM DD

        // DATA - ORDENA PELA DATA DE LANCAMENTO (CRESCENTE)
        // POPULARIDADE - ORDENA PELAS VIEWS (DECRESCENTE)
        // NOME - ORDENA POR ORDEM ALFABETICA (CRESCENTE)

        StringTokenizer st;
        String comando, input;
        int n;
        do{
            input = sc.nextLine();

            st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                comando = st.nextToken();

                Quicksort ord = new Quicksort();

                switch (comando) {
                    case "DATA":
                        n = Integer.parseInt(st.nextToken());
                        ord.sortData(filmes,0, filmes.size() - 1);
                        String out_data = "";
                        for (int j = 0; j < filmes.size(); j++) {
                            out_data = filmes.get(n - 1).getNome();
                        }
                        System.out.println(removeLeadingSpaces(out_data));
                        break;
                    case "POPULARIDADE":
                        n = Integer.parseInt(st.nextToken());
                        ord.sortPop(filmes,0, filmes.size() - 1);
                        String out_views = "";
                        for (int j = 0; j < filmes.size(); j++) {
                            out_views = filmes.get(n - 1).getNome();
                        }
                        System.out.println(removeLeadingSpaces(out_views));
                        break;
                    case "NOME":
                        n = Integer.parseInt(st.nextToken());
                        ord.sortName(filmes, 0, filmes.size() - 1);
                        String out_name = "";
                        for (int j = 0; j < filmes.size(); j++) {
                            out_name = filmes.get(n - 1).getNome();
                        }
                        System.out.println(removeLeadingSpaces(out_name));
                        break;
                    case "TERMINA":
                        return;
                }
            }

        }while (true);
    }
}
