import java.io.IOException;
import java.util.StringTokenizer;

// classe que representa no da arvore
class Node {
    public String matricula;
    public int divida;
    public Node left;
    public Node right;
    public int nivel;

    // Construtores
    Node(String matricula, int divida) {
        this.matricula = matricula;
        this.divida = divida;
        left = null;
        right = null;
        nivel = 1;
    }

    // Comparadores. Unico criterio e a String matricula. Restantes campos sao ignorados
    int compareTo(String matricula) {
        return this.matricula.compareTo(matricula);
    }

}

// classe que representa arvore AVL
class AVL {

    /* raiz da arvore. Null quando arvore vazia */
    private Node root = null;

    /* construtores */
    public AVL() {
        root = null;
    }


    // encontra determinado veiculo na arvore (null se nao encontrado)

    public Node get(String matricula) {
        Node no = root;
        while (no != null) {
            if (no.compareTo(matricula) == 0) {
                return no;
            }
            no = ((no.compareTo(matricula) > 0) ? no.left : no.right);
        }
        return null;
    }

    // encontrar o nivel da arvore
    private int getNivel(Node node) {
        if (node == null)
            return 0;

        return node.nivel;
    }

    private int max(int a, int b) {
        if(a>b){
            return a;
        }
        else {
            return b;
        }
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node aux = x.right;

        //rotacao
        x.right = y;
        y.left = aux;

        //atualiza niveis
        y.nivel = max(getNivel(y.left), getNivel(y.right)) + 1;
        x.nivel = max(getNivel(x.left), getNivel(x.right)) + 1;

        //novo no raiz
        return x;
    }


    private Node leftRotate(Node x) {
        Node y = x.right;
        Node aux = y.left;

        //rotacao
        y.left = x;
        x.right = aux;

        //atualiza niveis
        x.nivel = max(getNivel(x.left), getNivel(x.right)) + 1;
        y.nivel = max(getNivel(y.left), getNivel(y.right)) + 1;

        //novo no raiz
        return y;
    }

    // balanco da arvore
    private int getFatorEquilibrio(Node node) {
        if (node == null)
            return 0;
        return getNivel(node.left) - getNivel(node.right); // balanco
    }

    // adiciona novo no a arvore. Se ja existe atualiza valor acumulado
    public void add(String matricula, int divida) {
        root = add(matricula, divida, root);
        return;
    }

    private Node add(String matricula, int valor, Node node) {
        // veiculo ainda nao existe, cria novo no para este veiculo
        if (node == null) {
            return new Node (matricula, valor);
        }

        // veiculo nao encontrado
        if((node.compareTo(matricula) > 0)){
            node.left = add(matricula,valor,node.left);
        }
        else if((node.compareTo(matricula) < 0) ){
            node.right = add(matricula,valor,node.right);
        }
        else { // veiculo existe. adiciona a valor acumulado a divida
            node.divida+=valor;
            return node;
        }

        node.nivel = max(getNivel(node.left),getNivel(node.right)) + 1;

        int b = getFatorEquilibrio(node);

        // rotacao esquerda
        if (b < -1 && matricula.compareTo(node.right.matricula)>0){
            return leftRotate(node);
        }

        // rotacao direita
        if (b > 1 && matricula.compareTo(node.left.matricula) < 0 ){
            return rightRotate(node);
        }

        //rotacao dupla a esquerda
        if(b < -1 && matricula.compareTo(node.right.matricula) < 0 ){
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        //rotacao dupla a direita
        if(b > 1 && matricula.compareTo(node.left.matricula)>0 ){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        return node;
    }

    public void remove(String matricula){
        root = remove(matricula, root);
    }

    private Node remove(String matricula, Node no) {
        if (no == null) {   // arvore vazia ou no nao encontrado
            return null;
        }

        if (no.compareTo(matricula) == 0) {  // remove este no
            if (no.left == null) {              // um unico filho, "linka" e sai
                return no.right;
            } else if (no.right == null) {      // idem...
                return no.left;
            } else {                            // dois filhos...
                // troca valores com rigthmost e depois remove...
                Node aux = getRightmost(no.left);
                no.matricula = aux.matricula;
                no.divida = aux.divida;
                aux.matricula = matricula; // divida nao precisa de ser mudado...
                no.left = remove(matricula, no.left);
            }
        } else {                                // continua a descer pela esq ou dir
            if (no.compareTo(matricula) > 0) {
                // pela esquerda...
                no.left = remove(matricula, no.left);
            } else {        // ou pela direita...
                no.right = remove(matricula, no.right);
            }
        }

        // depois da remocao, pode ser preciso voltar a equilibrar a arvore
        // atualiza nivel do no atual
        no.nivel = max(getNivel(no.left),getNivel(no.right)) + 1;
        int b = getFatorEquilibrio(no);

        //rotacao a direita
        if ( b > 1 && getFatorEquilibrio(no.left) > 0 ) {
            return rightRotate(no);
        }
        // rotacao dupla direita
        if ( b > 1 && getFatorEquilibrio(no.left) < 0 ) {
            no.left = leftRotate(no.left);
            return rightRotate(no);
        }
        //rotacao esquerda
        if ( b < -1 && getFatorEquilibrio(no.right) < 0 )
            return leftRotate(no);

        //  rotacao dupla esquerda
        if (b < -1 && getFatorEquilibrio(no.right) > 0 ) {
            no.right = rightRotate(no.right);
            return leftRotate(no);
        }
        return no;
    }


    protected Node getRightmost(Node no) {
        return ((no.right == null) ? no : getRightmost(no.right));
    }

    // imprime em ordem. para debugging e verificacao
    void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node no){
        if (no==null)
            return;
        printInOrder(no.left);
        System.out.println(no.matricula + " VALOR EM DIVIDA " + no.divida);
        printInOrder(no.right);

    }
}
public class TP2_TarefaB{
    public static void main(String[] arguments) {
        String input, comando;
        int valor;
        String matricula;
        StringTokenizer st;

        AVL tree = new AVL();

        do {  // enquanto houver mais linhas para ler...
            input = readLn(200);
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("PORTICO")){
                matricula = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                tree.add (matricula, valor);
                Node no = tree.get(matricula);
                if (no.divida == 0)
                {
                    tree.remove(matricula);
                }
            }
            else if (comando.equals("PAGAMENTO")){
                matricula = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                tree.add (matricula, (valor * -1));
                Node no = tree.get(matricula);
                if (no.divida == 0)
                {
                    tree.remove(matricula);
                }
            }
            else if (comando.equals("SALDO")){
                matricula = new String(st.nextToken());
                Node no = tree.get(matricula);
                if (no==null)
                    System.out.println(matricula + " SEM REGISTO");
                else
                    System.out.println(matricula + " VALOR EM DIVIDA " + no.divida);
            }
            else if (comando.equals("LISTA")){
                tree.printInOrder();
            }
            else if (comando.equals("TERMINA"))
                return;
        } while (true);
    }

    // leitura do input
    static String readLn (int maxLg){ //utility function to read from stdin
        byte lin[] = new byte [maxLg];
        int lg = 0, car = -1;
        try {
            while (lg < maxLg){
                car = System.in.read();
                if ((car < 0) || (car == '\n'))
                    break;
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
