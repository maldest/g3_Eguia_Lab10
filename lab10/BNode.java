package lab10;
import java.util.ArrayList;

public class BNode<E> {
    protected ArrayList<E> keys;           // claves del nodo
    protected ArrayList<BNode<E>> childs;    // hijos del nodo
    protected int count;                   // cantidad de claves en el nodo
    protected static int idCounter = 0;      // contador para idNode
    protected int idNode;                  // identificador único del nodo

    // Constructor del nodo, inicializa las claves y los hijos
    public BNode(int n) {
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);
        this.count = 0;
        this.idNode = idCounter++; 
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

    public boolean searchNode(E key) {
        for (int i = 0; i < count; i++) {
            if (keys.get(i).equals(key)) { // Si la clave se encuentra en el nodo
                return true;  // La clave fue encontrada
            }
        }
        
        return false;//si no encuentra nada devuelve false
    }

    public String toString(){
       StringBuilder sb = new StringBuilder("Node ID: " + idNode + " Keys: ");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i)).append(" ");
        }
        return sb.toString();

    }
}
