import java.io.IOException;
import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.util.*;


import java.time.Duration;
import java.time.Instant;

// classe que representa no da arvore
// por simplicidade esta "hard-coded" para o problema e com atributos de acesso publico

class Node {
    public String matricula;
    public int divida;
    public Node left;
    public Node right;
    
    // Construtores
    Node(String matricula, int divida) {
        this.matricula = matricula;
        this.divida = divida;
        left = null;
        right = null;
    }
    
    Node(String matricula, int divida, Node l, Node r) {
        this.matricula = matricula;
        this.divida = divida;
        left = l;
        right = r;
    }

    // Comparadores. Unico criterio e a String matricula. Restantes campos sao ignorados
    int compareTo(String matricula) {
        return this.matricula.compareTo(matricula);
    }

    int compareTo(Node otherNode) {
        return this.compareTo(otherNode.matricula);
    }
}



// classe que representa arvore binaria. Implementacao hard-coded para o problema.

class BST {
    
    /* raiz da arvore. Null quando arvore vazia */
    protected Node root = null;
    int nos = 0;

    
    /* construtores */

    public BST() {
        root = null;
    }
    
    public BST (Node no) {
        root = no;
    }
    
    public BST(String matricula, int divida) {
        root = new Node(matricula, divida);
    }
    
    // encontra determinado veiculo na arvore (null se nao encontrado)
    public Node get(Node no) {
        return this.get(no.matricula);
    }
    
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

    // adiciona novo no a arvore. Se ja existe atualiza divida acumulado
    public void add(String matricula, int divida) {
        root = add(matricula, divida, root);
        return;
    }


    // recursivo... pode dar mau resultado para conjuntos grandes e degenerados    
    protected Node add(String matricula, int valor, Node node) {
        // veiculo ainda nao existe, cria novo no para este veiculo.
        if (node == null) {
            return new Node (matricula, valor);
        }
        
        // veiculo existe, adiciona a valor acumulado
        if (node.compareTo(matricula) == 0) {
            node.divida += valor;
        // ainda nao encontrou. desce mais um nivel
        } else {
            if (node.compareTo(matricula) > 0) {
                Node aux = node;
                aux = aux.left;
                while(aux != null){
                    node.left = add(matricula, valor, node.left);
                    nos++;
                }    
            } else {   
                node.right = add(matricula, valor, node.right);
                nos++;
                
            }
        }
        return node;
    }
    

    // remove veiculo da arvore
    public void remove(Node no) {
        remove(no.matricula);
    }
    
    public void remove(String matricula){
        root = remove(matricula, root);
    }
    
    protected Node remove(String matricula, Node no) {
        if (no == null) {	// arvore vazia ou no nao encontrado
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
                aux.matricula = matricula; // valor nao precisa de ser mudado...
                no.left = remove(matricula, no.left);
            }
        } else {                                // continua a descer pela esq ou dir
            if (no.compareTo(matricula) > 0) {
                // pela esquerda...
                no.left = remove(matricula, no.left);
            } else {		// ou pela direita...
                no.right = remove(matricula, no.right);
            }
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
    
    void printInOrder(Node no){
        if (no==null)
            return;
        printInOrder(no.left);
        System.out.println(no.matricula + " VALOR EM DIVIDA " + no.divida);
        printInOrder(no.right);
    }


    //profundidade maxima
    protected int maxDepth(Node node){
        if (node == null)
            return 0;
        else{

            int lDepth = maxDepth(node.left);
            int rDepth = maxDepth(node.right);
  
            
            if (lDepth > rDepth)
                return (lDepth + 1);
             else
                return (rDepth + 1);
        }
    }
    
    //contagem
    protected int count(Node tree){
        int c =  1;             
        if (tree ==null)
            return 0;
        else{
            c += count(tree.left);
            c += count(tree.right);
            return c;
        }
    }
    
    //profundidade total
    protected int totalDepth(Node node, int depth) {
        if(node == null) {
            return 0;
        }

        return totalDepth(node.left, depth + 1) + totalDepth(node.right, depth + 1) + depth;
    }
}


public class TP2_TarefaA{
    
    public static void main(String[] arguments) {

        Instant start = Instant.now();

        String input, comando;
        int valor;
        String matricula;
	   StringTokenizer st;
        
        BST tree = new BST();

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
			{ tree.remove(matricula); }
            }
            else if (comando.equals("PAGAMENTO")){
                matricula = new String(st.nextToken());
                valor = Integer.parseInt(st.nextToken());
                tree.add (matricula, (valor * -1));
		Node no = tree.get(matricula);
		if (no.divida == 0) 
			{ tree.remove(matricula); }
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
            else if (comando.equals("TERMINA")){
                System.out.println("Profundidade MEDIA: " + tree.totalDepth(tree.root,0) / tree.count(tree.root));
                System.out.println("Profundidade MAXIMA: " + tree.maxDepth(tree.root));
                System.out.println("Nos atravessados: " + tree.nos);
                return;
            }
        } while (true);

        
        Instant finish = Instant.now();
        float time = Duration.between(start,finish).toMillis();
        System.out.println("\nTempo: " + time + " ms");
    }
    
    // leitura do input
    static String readLn (int maxLg){ //utility function to read from stdin
        byte lin[] = new byte [maxLg];
        int lg = 0, car = -1;
        String line = "";
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
