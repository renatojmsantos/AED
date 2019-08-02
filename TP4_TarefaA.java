import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Math;


import java.time.Duration;
import java.time.Instant;

class HashEntry {

    public String matricula;
    public int divida;

    // next node
    public HashEntry next;

    // Construtor
    HashEntry(String matricula, int divida) {
        this.matricula = matricula;
        this.divida = divida;
        this.next = null;
    }

    public HashEntry getNext() {
        return next;
    }

    public void setNext(HashEntry next) {
        this.next = next;
    }

    // Comparadores. Unico criterio e a String matricula. Restantes campos sao ignorados
    int compareTo(String matricula) {
        return this.matricula.compareTo(matricula);
    }
}

// hash table
class Chaining {

    private final static int TABLE_SIZE = 86000; // 100

    private HashEntry[] table = new HashEntry[TABLE_SIZE];

    private int colisao = 0;

    Chaining() {
        for(int i=0 ; i< TABLE_SIZE; i++){ //inicializar valores
            table[i] = null;
        }
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

    // encontra determinado veiculo (null se nao encontrado)
    public HashEntry get(String matricula){
        //int index = Math.abs(matricula.hashCode() % TABLE_SIZE);
        int index = Math.abs(this.getKeyIndex(matricula));

        HashEntry entry = table[index];
        while (entry != null) {
            if (entry.compareTo(matricula) == 0){
                return entry;
            }
            entry = entry.getNext();
        }
        return null;
    }

    // adiciona novo indice. Se ja existe atualiza divida acumulado
    public HashEntry[] add(String matricula, int divida) {
        //int index = Math.abs(matricula.hashCode() % TABLE_SIZE);
        int index = Math.abs(this.getKeyIndex(matricula));

        if(table[index] != null){
            HashEntry entry = table[index];

            while (entry.compareTo(matricula) != 0 && entry.getNext() != null) {
                entry = entry.getNext();
                colisao++;
                System.out.println();
                System.out.println(" ========================= COLISAO: " + colisao + " ===========================");
            }

            if (entry.compareTo(matricula) == 0) {
                entry.divida += divida;
            } else {
                entry.next = new HashEntry(matricula,divida);
            }
        } else {
            table[index] = new HashEntry(matricula,divida);
            return table;
        }
        return table;
    }

    public void remove(String matricula){
        //int index = Math.abs(matricula.hashCode() % TABLE_SIZE);
        int index = Math.abs(this.getKeyIndex(matricula));

        if(table[index] == null){
            return;
        }

        HashEntry entrada = table[index];
        HashEntry anterior = null;
        while (entrada.getNext() != null && entrada.compareTo(matricula) != 0) {
            anterior = entrada;
            entrada = entrada.next;
        }
        if (entrada.compareTo(matricula) == 0) {
            if (anterior == null) {
                table[index] = entrada.next;
            }
            else{
                anterior.next = entrada.next;
            }
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

public class TP4_TarefaA{

    public static void main(String[] arguments) {

        Instant start = Instant.now();

        String input, comando;
        int valor;
        String matricula;
        StringTokenizer st;

        Chaining hashTable = new Chaining();

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
        //System.out.println("---- COLISAO: " + colisao);
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