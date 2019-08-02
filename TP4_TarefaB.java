import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Math;


import java.time.Duration;
import java.time.Instant;

class HashEntry {

    public String matricula;
    public int divida;

    // Construtor
    HashEntry(String matricula, int divida) {
        this.matricula = matricula;
        this.divida = divida;
    }

    public String getMatricula() {
        return matricula;
    }

    // Comparadores. Unico criterio e a String matricula. Restantes campos sao ignorados
    int compareTo(String matricula) {
        return this.matricula.compareTo(matricula);
    }
}

// hash table
class Probing {

    private int TABLE_SIZE;
    
    private int colisao = 0;

    private HashEntry[] table;

    Probing() {
        TABLE_SIZE = 86000; // 100 000
        table = new HashEntry[TABLE_SIZE];
    }

    //https://stackoverflow.com/questions/2624192/good-hash-function-for-strings/2624210
    //https://computinglife.wordpress.com/2008/11/20/why-do-hash-functions-use-prime-numbers
    private Integer getKeyIndex(String matricula){
        int hash = 7;
        for (int i = 0; i < matricula.length(); i++) {
            hash = (hash*31 + matricula.charAt(i)) % TABLE_SIZE;
        }
        return hash;
    }

    // quadratic probing: https://www.geeksforgeeks.org/hashing-set-3-open-addressing/

    // encontra determinado veiculo (null se nao encontrado)
    public HashEntry get(String matricula){
        int index = Math.abs(this.getKeyIndex(matricula));

        int n=1;
        HashEntry entry = table[index];
        while (entry != null) {
            if (entry.compareTo(matricula) == 0){
                return entry;
            }
            //index = (index + 1) % TABLE_SIZE; //linear
            index = (index + n*n++) % TABLE_SIZE; //quadratica
        }
        return null;
    }

    // adiciona novo indice. Se ja existe atualiza divida acumulado
    public void add(String matricula, int divida) {
        int index = Math.abs(this.getKeyIndex(matricula));

        int n=1;
        if(table[index] != null){
            HashEntry entry = table[index];

            while (entry.compareTo(matricula) != 0) {
                //index = (index + 1) % TABLE_SIZE; //proximo indice
                index = (index + n*n++) % TABLE_SIZE;
                colisao++;
                System.out.println();
                System.out.println(" ========================= COLISAO: " + colisao + " ===========================");

            }

            if (entry.compareTo(matricula) == 0) {
                entry.divida += divida;
            }
        } else {
            table[index] = new HashEntry(matricula,divida);
        }
    }

    public void remove(String matricula){
        int index = Math.abs(this.getKeyIndex(matricula));

        int n=1;

        if(table[index] == null){
            return;
        }

        while (table[index] != null)
        {
            if (table[index].getMatricula().equals(matricula) ) {
                table[index] = null;
                return;
            }
            //index = (index + 1) % TABLE_SIZE;
            index = (index + n*n++) % TABLE_SIZE;

        }
    }

    void printInOrder(){
        printInOrder(table);
    }

    private void printInOrder(HashEntry[] entry){
        if (entry==null)
            return;

        for(int i=0 ; i< TABLE_SIZE; i++){
            System.out.println(entry[i].matricula + " VALOR EM DIVIDA " + entry[i].divida);
        }
    }
}

public class TP4_TarefaB{

    public static void main(String[] arguments) {

        Instant start = Instant.now();

        String input, comando;
        int valor;
        String matricula;
        StringTokenizer st;

        Probing hashTable = new Probing();

        do {  // enquanto houver mais linhas para ler...
            input = readLn(200);
            assert input != null;
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("PORTICO")){
                matricula = st.nextToken();
                valor = Integer.parseInt(st.nextToken());
                hashTable.add (matricula, valor);
                HashEntry entry = hashTable.get(matricula);
                if (entry.divida == 0){
                    hashTable.remove(matricula);
                }
            }
            else if (comando.equals("PAGAMENTO")){
                matricula = st.nextToken();
                valor = Integer.parseInt(st.nextToken());
                hashTable.add (matricula, (valor * -1));
                HashEntry entry = hashTable.get(matricula);
                if (entry.divida == 0)
                {
                    hashTable.remove(matricula);
                }
            }
            else if (comando.equals("SALDO")){
                matricula = st.nextToken();

                HashEntry entry = hashTable.get(matricula);
                if (entry == null)
                    System.out.println(matricula + " SEM REGISTO");
                else
                    System.out.println(matricula + " VALOR EM DIVIDA " + entry.divida);
            }
            else if (comando.equals("LISTA")){
                hashTable.printInOrder();
            }
            else if (comando.equals("TERMINA")){
                return;
            }

        Instant finish = Instant.now();
        float time = Duration.between(start,finish).toMillis();
        System.out.println("\nTempo: " + time + " ms");

        } while (true);
    }

    // leitura do input
    static String readLn (int maxLg){ //utility function to read from stdin
        byte lin[] = new byte [maxLg];
        int lg = 0, car = -1;
        try {
            while (lg < maxLg){
                car = System.in.read();
                if ((car < 0) || (car == '\n')) break;
                lin [lg++] += car;
            }
        }
        catch (IOException e){
            return (null);
        }
        if ((car < 0) && (lg == 0)) return (null);  // eof
        return (new String (lin, 0, lg));
    }

}