package lab10;
import java.util.ArrayList;

public class BNode<E> {
    protected ArrayList<E> keys;           // Claves del nodo
    protected ArrayList<BNode<E>> childs;  // Hijos del nodo
    protected int count;                   // Cantidad de claves en el nodo
    protected static int idCounter = 0;    // Contador para idNode
    protected int idNode;                  // Identificador único del nodo

    // Constructor del nodo, inicializa las claves y los hijos
    public BNode(int n) {
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);
        this.count = 0;
        this.idNode = idCounter++; // Asignamos el id único y lo incrementamos
        for (int i = 0; i < n; i++) {
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    public boolean nodeFull(){
        return count == keys.size();
    }

    public boolean nodeEmpty(){
        return count == 0;
    }

    public boolean searchNode(){
        
    }

}