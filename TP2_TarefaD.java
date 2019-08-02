import java.io.IOException;
import java.util.StringTokenizer;

// classe que representa no da arvore
class Node {
    public String matricula;
    public int divida;
    public Node left;
    public Node right;

    Node(String matricula, int divida) {
        this.matricula = matricula;
        this.divida = divida;
        left = null;
        right = null;
    }

    // Comparadores. Unico criterio e a String matricula
    int compareTo(String matricula) {
        return this.matricula.compareTo(matricula);
    }

}

class Splay{
    /* raiz da arvore. Null quando arvore vazia */
    private Node root = null;

    //construtor
    public Splay() {
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

    private Node rightRotation(Node y){
        Node x = y.left;
        Node aux = x.right;

        //rotacao
        x.right = y;
        y.left = aux;

        //novo no raiz
        return x;
    }


    private Node leftRotate(Node x) {
        Node y = x.right;
        Node aux = y.left;

        //rotacao
        y.left = x;
        x.right = aux;

        //novo no raiz
        return y;
    }


    public Node add(String matricula, int divida){
        // veiculo ainda nao existe, cria novo no para este veiculo.
        if(root==null){
            root = new Node(matricula, divida);
            return root;
        }

        // traz o no da folha mais proximo
        root = splay(matricula,root);

        //novo no
        Node no = new Node(matricula, divida);

        // veiculo existe. adiciona a valor acumulado a divida
        if(matricula.compareTo(root.matricula) == 0){
            root.divida += divida;
        }
        else { // veiculo nao existe -> insere novo no
            if ( matricula.compareTo(root.matricula) < 0 ) {
                no.right = root;
                no.left = root.left;
                root.left = null;
                root = no;

            }else{ // < 0
                no.left = root;
                no.right = root.right;
                root.right = null;
                root = no;
            }
        }
        //novo no
        return no;
    }

    public void remove(String matricula){
        root = remove(matricula, root);
    }

    private Node remove(String matricula, Node node){
        // arvore vazia ou no nao encontrado
        if(node==null){
            return null;
        }

        node = splay(matricula,node);

        Node temp;
        // existe matricula
        if(node.compareTo(matricula) == 0){
            if (node.left == null){ // nao existe no filho esquerdo -> no pai fica o filho direito
                node = node.right;
            }

            else{  // se o no filho esquerdo existir
                temp = node;
                //novo no
                node = splay(matricula,node.left);
                // o filho direito do no anterior passa a ser o novo no filho direito
                node.right = temp.right;
            }

        }
        // retorna novo no da Splay Tree depois de remover
        return node;
    }

    //se a matricula nao estiver presente, a funcao considera o ultimo no acecedido
    //modifica a arvore e retorna nova raiz
    private Node splay(String matricula, Node no) {
        if (no == null){
            return null;
        }

        //matricula no lado esquerdo da sub arvore
        if (matricula.compareTo(no.matricula) < 0) {
            //matricula nao esta na arvore
            if (no.left == null) {
                return no;
            }

            if (matricula.compareTo(no.left.matricula) < 0) {
                //matricula fica como raiz de left-left
                no.left.left = splay(matricula,no.left.left);
                no = rightRotation(no);
            }
            else if (matricula.compareTo(no.left.matricula) > 0) {
                //raiz de left-right
                no.left.right = splay(matricula, no.left.right);

                if (no.left.right != null) {
                    no.left = leftRotate(no.left);
                }
            }
            if (no.left == null){
                return no;
            } else{
                return rightRotation(no);
            }
        }
        // lado direito da sub arvore
        else if (matricula.compareTo(no.matricula) > 0) {
            if (no.right == null) {
                return no;
            }

            if (matricula.compareTo(no.right.matricula) < 0){
                // RIGHT LEFT
                no.right.left  = splay(matricula,no.right.left);
                if (no.right.left != null) {
                    no.right = rightRotation(no.right);
                }
            }
            else if (matricula.compareTo(no.right.matricula) > 0) {
                // RIGHT RIGHT
                no.right.right = splay(matricula, no.right.right);
                no = leftRotate(no);
            }
            if (no.right == null) {
                return no;
            }else{
                return leftRotate(no);
            }
        }
        return no;
    }

    protected void printInOrder(){
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

public class TP2_TarefaD{
    public static void main(String[] arguments) {

        String input, comando;
        int valor;
        String matricula;
        StringTokenizer st;

        Splay tree = new Splay();

        do {
            input = readLn(200);
            st= new StringTokenizer(input.trim());
            comando = st.nextToken();
            if (comando.equals("PORTICO")){
                matricula = st.nextToken();
                valor = Integer.parseInt(st.nextToken());
                tree.add (matricula, valor);
                Node no = tree.get(matricula);
                if (no.divida == 0){
                    tree.remove(matricula);
                }
            }
            else if (comando.equals("PAGAMENTO")){
                matricula = st.nextToken();
                valor = Integer.parseInt(st.nextToken());
                tree.add (matricula, (valor * -1));
                Node no = tree.get(matricula);
                if (no.divida == 0){
                    tree.remove(matricula);
                }
            }
            else if (comando.equals("SALDO")){
                matricula = st.nextToken();
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

    private static String readLn (int maxLg){
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
        if ((car < 0) && (lg == 0)) return (null);
        return (new String (lin, 0, lg));
    }

}
