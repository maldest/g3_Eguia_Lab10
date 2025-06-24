package lab10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import lab10.Exceptions.IllegalArgumentBTreeException;
import lab10.Exceptions.IllegalOperationException;
import lab10.Exceptions.NullValueException;

/**
 * Implementación completa de un árbol B con manejo de excepciones mejorado.
 * @param <E> Tipo de elementos almacenados en el árbol. Debe ser Comparable.
 */
public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private final int order; // Número máximo de claves por nodo
    private int size;        // Cantidad total de elementos en el árbol

    /**
     * Crea un árbol B vacío.
     * @param order Orden del árbol (mínimo 2)
     * @throws IllegalArgumentBTreeException si el orden es menor que 2
     */
    public BTree(int order) {
        if (order < 2) {
            throw new Exceptions.IllegalArgumentBTreeException(
                "El orden del B-Tree debe ser al menos 2. Recibido: " + order);
        }
        this.order = order;
        this.root = new BNode<>(order);
        this.size = 0;
    }

    /**
     * Inserta una clave en el árbol.
     * @param key Elemento a insertar
     * @throws NullValueException si la clave es null
     */
    public void insert(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("No se puede insertar una clave nula");
        }

        if (root.isFull()) {
            BNode<E> newRoot = new BNode<>(order);
            newRoot.insertChild(root, 0);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, key);
        size++;
    }

    private void insertNonFull(BNode<E> node, E key) {
        int i = node.count - 1;

        if (node.isLeaf()) {
            // Busca el punto de inserción y mueve claves si es necesario
            while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                i--;
            }
            node.insertKey(key, i + 1);
        } else {
            // Encuentra el hijo apropiado
            while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                i--;
            }
            i++;

            // Si el hijo está lleno, lo divide
            if (node.getChild(i).isFull()) {
                splitChild(node, i);
                if (key.compareTo(node.getKey(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.getChild(i), key);
        }
    }

    /**
     * Divide un nodo hijo que está lleno.
     * @param parent Nodo padre
     * @param childIndex Índice del hijo lleno
     */
    private void splitChild(BNode<E> parent, int childIndex) {
        BNode<E> child = parent.getChild(childIndex);
        BNode<E> newChild = new BNode<>(order);
        int mid = order / 2;

        // Mueve las claves al nuevo nodo
        for (int j = 0; j < order - mid - 1; j++) {
            newChild.insertKey(child.getKey(mid + 1 + j), j);
        }

        // Mueve los hijos si no es hoja
        if (!child.isLeaf()) {
            for (int j = 0; j < order - mid; j++) {
                newChild.insertChild(child.getChild(mid + 1 + j), j);
            }
        }

        // Mueve la clave mediana al padre
        E midKey = child.getKey(mid);
        parent.insertKey(midKey, childIndex);

        // Enlaza el nuevo hijo al padre
        parent.insertChild(newChild, childIndex + 1);

        // Elimina las claves transferidas del hijo original
        for (int j = mid; j < order; j++) {
            child.removeKey(mid);
        }
    }

    /**
     * Verifica si el árbol contiene una clave.
     * @param key Clave a buscar
     * @return true si se encuentra, false en caso contrario
     * @throws NullValueException si la clave es null
     */
    public boolean contains(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("No se puede buscar una clave nula");
        }
        return search(root, key);
    }

    private boolean search(BNode<E> node, E key) {
        int i = 0;
        while (i < node.count && key.compareTo(node.getKey(i)) > 0) {
            i++;
        }

        if (i < node.count && key.equals(node.getKey(i))) {
            return true;
        }
        return !node.isLeaf() && search(node.getChild(i), key);
    }

    /**
     * Elimina una clave del árbol.
     * @param key Clave a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws NullValueException si la clave es null
     * @throws IllegalOperationException si el árbol está vacío
     */
    public boolean remove(E key) {
        if (key == null) {
            throw new Exceptions.NullValueException("No se puede eliminar una clave nula");
        }
        if (isEmpty()) {
            throw new Exceptions.IllegalOperationException("No se puede eliminar de un árbol vacío");
        }

        boolean found = contains(key);
        if (found) {
            delete(root, key);
            size--;

            // Si la raíz se queda vacía, actualízala
            if (root.count == 0 && !root.isLeaf()) {
                root = root.getChild(0);
            }
        }
        return found;
    }

    private void delete(BNode<E> node, E key) {
        int idx = node.findKeyPosition(key);

        // Clave encontrada en este nodo
        if (idx < node.count && node.getKey(idx).equals(key)) {
            if (node.isLeaf()) {
                node.removeKey(idx);
            } else {
                deleteInternalNode(node, idx);
            }
        } 
        // Clave no encontrada, continuar búsqueda
        else {
            if (node.isLeaf()) return;

            boolean isLastChild = (idx == node.count);
            BNode<E> child = node.getChild(idx);

            // Asegura que el hijo tenga suficientes claves
            if (child.count <= order / 2) {
                fillChild(node, idx);
            }

            // Ajusta el índice si el hijo fue fusionado
            if (isLastChild && idx > node.count) {
                delete(node.getChild(idx - 1), key);
            } else {
                delete(node.getChild(idx), key);
            }
        }
    }

/**
 * Elimina una clave de un nodo interno, manejando redistribución o fusión según sea necesario.
 * @param node El nodo interno del cual se eliminará la clave.
 * @param idx El índice de la clave que se va a eliminar.
 */
private void deleteInternalNode(BNode<E> node, int idx) {
    BNode<E> leftChild = node.getChild(idx);
    BNode<E> rightChild = node.getChild(idx + 1);

    // Caso 1: Pedir prestado del hijo izquierdo si tiene suficientes claves
    if (leftChild.count > order / 2) {
        E predecessor = getMaxKey(leftChild);        // Obtener el predecesor
        node.keys.set(idx, predecessor);             // Reemplazar la clave con el predecesor
        delete(leftChild, predecessor);              // Eliminar el predecesor en el hijo izquierdo
    }
    // Caso 2: Pedir prestado del hijo derecho si tiene suficientes claves
    else if (rightChild.count > order / 2) {
        E successor = getMinKey(rightChild);         // Obtener el sucesor
        node.keys.set(idx, successor);               // Reemplazar la clave con el sucesor
        delete(rightChild, successor);               // Eliminar el sucesor en el hijo derecho
    }
    // Caso 3: Fusionar ambos hijos si están en capacidad mínima
    else {
        mergeChildren(node, idx);                    // Fusionar hijo izquierdo + clave + hijo derecho
        delete(leftChild, node.getKey(idx));         // Eliminar la clave fusionada desde el nuevo nodo combinado
    }
}

/**
 * Obtiene la clave máxima de un subárbol (usado para encontrar el predecesor).
 * @param node La raíz del subárbol.
 * @return La clave máxima encontrada.
 */
private E getMaxKey(BNode<E> node) {
    while (!node.isLeaf()) {
        node = node.getChild(node.count);            // Avanzar al hijo más a la derecha
    }
    return node.getKey(node.count - 1);              // Última clave del nodo hoja
}

/**
 * Obtiene la clave mínima de un subárbol (usado para encontrar el sucesor).
 * @param node La raíz del subárbol.
 * @return La clave mínima encontrada.
 */
private E getMinKey(BNode<E> node) {
    while (!node.isLeaf()) {
        node = node.getChild(0);                     // Avanzar al hijo más a la izquierda
    }
    return node.getKey(0);                           // Primera clave del nodo hoja
}

/**
 * Fusiona una clave del nodo padre y su hijo derecho dentro del hijo izquierdo.
 * @param parent El nodo padre.
 * @param idx El índice de la clave que se va a fusionar.
 */
private void mergeChildren(BNode<E> parent, int idx) {
    BNode<E> leftChild = parent.getChild(idx);
    BNode<E> rightChild = parent.getChild(idx + 1);

    // Mover la clave del padre al final del hijo izquierdo
    leftChild.insertKey(parent.getKey(idx), leftChild.count);

    // Mover todas las claves del hijo derecho al hijo izquierdo
    for (int i = 0; i < rightChild.count; i++) {
        leftChild.insertKey(rightChild.getKey(i), leftChild.count);
    }

    // Si no es hoja, mover también los hijos del hijo derecho
    if (!rightChild.isLeaf()) {
        for (int i = 0; i <= rightChild.count; i++) {
            leftChild.insertChild(rightChild.getChild(i), leftChild.childs.size());
        }
    }

    // Eliminar la clave fusionada y el hijo derecho del padre
    parent.removeKey(idx);
    parent.removeChild(idx + 1);
}


/**
 * Asegura que un nodo hijo tenga suficientes claves, ya sea tomando prestado de sus hermanos
 * o fusionándose con alguno de ellos.
 * @param parent El nodo padre.
 * @param childIdx El índice del hijo que necesita ser llenado.
 */
private void fillChild(BNode<E> parent, int childIdx) {
    BNode<E> child = parent.getChild(childIdx);

    // Caso 1: Pedir prestado del hermano izquierdo si es posible
    if (childIdx > 0 && parent.getChild(childIdx - 1).count > order / 2) {
        BNode<E> leftSibling = parent.getChild(childIdx - 1);
        
        // Mover una clave del padre al hijo
        child.insertKey(parent.getKey(childIdx - 1), 0);
        // Reemplazar la clave en el padre con la última clave del hermano izquierdo
        parent.keys.set(childIdx - 1, leftSibling.getKey(leftSibling.count - 1));
        leftSibling.removeKey(leftSibling.count - 1);

        // Mover el puntero de hijo si no es hoja
        if (!leftSibling.isLeaf()) {
            child.insertChild(leftSibling.removeChild(leftSibling.count), 0);
        }
    }
    // Caso 2: Pedir prestado del hermano derecho si es posible
    else if (childIdx < parent.count && parent.getChild(childIdx + 1).count > order / 2) {
        BNode<E> rightSibling = parent.getChild(childIdx + 1);
        
        // Mover una clave del padre al hijo
        child.insertKey(parent.getKey(childIdx), child.count);
        // Reemplazar la clave del padre con la primera clave del hermano derecho
        parent.keys.set(childIdx, rightSibling.getKey(0));
        rightSibling.removeKey(0);

        // Mover el puntero de hijo si no es hoja
        if (!rightSibling.isLeaf()) {
            child.insertChild(rightSibling.removeChild(0), child.childs.size());
        }
    }
    // Caso 3: Fusionarse con un hermano si no es posible pedir prestado
    else {
        if (childIdx > 0) {
            mergeChildren(parent, childIdx - 1);  // Fusiona con el hermano izquierdo
        } else {
            mergeChildren(parent, childIdx);      // Fusiona con el hermano derecho
        }
    }
}


    /**
     * @return Número de elementos en el árbol
     */
    public int size() {
        return size;
    }

    /**
     * @return true si el árbol está vacío, false en caso contrario
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna todos los elementos en orden.
     * @return Lista de elementos en recorrido in-order
     */
    public ArrayList<E> inOrderTraversal() {
        ArrayList<E> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BNode<E> node, ArrayList<E> result) {
        if (node == null) return;

        for (int i = 0; i < node.count; i++) {
            if (!node.isLeaf()) {
                inOrder(node.getChild(i), result);
            }
            result.add(node.getKey(i));
        }

        if (!node.isLeaf()) {
            inOrder(node.getChild(node.count), result);
        }
    }

    /**
     * Imprime el árbol nivel por nivel.
     */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("El B-Tree está vacío");
            return;
        }

        Queue<BNode<E>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder levelStr = new StringBuilder();

            for (int i = 0; i < levelSize; i++) {
                BNode<E> current = queue.poll();
                levelStr.append(current.toString()).append(" ");

                if (!current.isLeaf()) {
                    for (int j = 0; j <= current.count; j++) {
                        queue.add(current.getChild(j));
                    }
                }
            }
            System.out.println(levelStr);
        }
    }
}
