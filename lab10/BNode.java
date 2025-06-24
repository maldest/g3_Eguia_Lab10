package lab10;
import java.util.ArrayList;

import lab10.Exceptions.NegativeValueException;
import lab10.Exceptions.NullValueException;

/**
 * Clase que representa un nodo de un B-Tree
 * @param <E> Tipo de elementos almacenados en el nodo, debe ser Comparable
 */
public class BNode<E extends Comparable<E>> {
    protected ArrayList<E> keys;          // Lista de claves almacenadas en el nodo
    protected ArrayList<BNode<E>> childs; // Lista de referencias a hijos
    public int idNode;
    private static int counter = 0;
    protected int count;                  // Número actual de claves en el nodo
    
    
    /**
     * Constructor que crea un nodo con capacidad para n claves
     * @param n Orden del nodo (máximo número de claves)
     * @throws NegativeValueException Si n es menor o igual a 0
     */
    public BNode(int n) {
        this.idNode = counter++;

        if (n <= 0) {
            throw new Exceptions.NegativeValueException("El orden del nodo debe ser positivo");
        }
        
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n + 1); // n claves → n+1 hijos
        this.count = 0;
        
        // Inicializar las listas con valores nulos
        for (int i = 0; i < n; i++) {
            this.keys.add(null);
        }
        for (int i = 0; i < n + 1; i++) {
            this.childs.add(null);
        }
    }

    /**
     * Verifica si el nodo es una hoja
     * @return true si no tiene hijos, false en caso contrario
     */
    public boolean isLeaf() {
        return childs.get(0) == null;
    }

    /**
     * Inserta una clave en la posición especificada manteniendo el orden
     * @param key Clave a insertar
     * @param index Posición donde insertar
     * @throws NullValueException Si la clave es nula
     */
    public void insertKey(E key, int index) {
        if (key == null) {
            throw new Exceptions.NullValueException("No se puede insertar una clave nula");
        }
        
        // Desplazar las claves para hacer espacio
        for (int i = count; i > index; i--) {
            keys.set(i, keys.get(i - 1));
        }
        keys.set(index, key);
        count++;
    }

    /**
     * Elimina una clave del nodo
     * @param index Posición de la clave a eliminar
     * @return Clave eliminada
     */
    public E removeKey(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Índice inválido");
        }
        
        E removedKey = keys.get(index);
        
        // Desplazar las claves para llenar el espacio
        for (int i = index; i < count - 1; i++) {
            keys.set(i, keys.get(i + 1));
        }
        keys.set(count - 1, null); // Limpiar la última posición
        count--;
        
        return removedKey;
    }

    /**
     * Inserta un hijo en la posición especificada
     * @param child Nodo hijo a insertar
     * @param index Posición donde insertar
     */
    public void insertChild(BNode<E> child, int index) {
        childs.add(index, child); // ArrayList lo desplaza automáticamente
        // Elimina el último si excedes el tamaño permitido
        if (childs.size() > keys.size() + 1) {
            childs.remove(childs.size() - 1);
        }
    }


    /**
     * Elimina un hijo del nodo
     * @param index Posición del hijo a eliminar
     * @return Nodo hijo eliminado
     */

     /**
    public BNode<E> removeChild(int index) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException("Índice inválido");
        }
        
        BNode<E> removedChild = childs.get(index);
        
        // Desplazar los hijos para llenar el espacio
        for (int i = index; i < count; i++) {
            childs.set(i, childs.get(i + 1));
        }
        childs.set(count, null); // Limpiar la última posición
        
        return removedChild;
    }
    */
    public BNode<E> removeChild(int index) {
    if (index < 0 || index >= childs.size()) {
        throw new IndexOutOfBoundsException("Índice inválido: " + index);
    }

    BNode<E> removedChild = childs.get(index);

    // Desplazar hijos hacia la izquierda para llenar el espacio
    for (int i = index; i < childs.size() - 1; i++) {
        childs.set(i, childs.get(i + 1));
    }

    // Eliminar el último (duplicado)
    childs.set(childs.size() - 1, null);

    return removedChild;
}


    /**
     * Obtiene la clave en la posición especificada
     * @param index Posición de la clave
     * @return Clave solicitada
     */
    public E getKey(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Índice inválido");
        }
        return keys.get(index);
    }

    /**
     * Obtiene el hijo en la posición especificada
     * @param index Posición del hijo
     * @return Nodo hijo solicitado
     */
    public BNode<E> getChild(int index) {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException("Índice inválido");
        }
        return childs.get(index);
    }

    /**
     * Busca la posición donde debería estar una clave
     * @param key Clave a buscar
     * @return Posición donde se encuentra o debería estar
     */
    public int findKeyPosition(E key) {
        int pos = 0;
        while (pos < count && key.compareTo(keys.get(pos)) > 0) {
            pos++;
        }
        return pos;
    }

    /**
     * Verifica si el nodo está lleno
     * @return true si está lleno, false en caso contrario
     */
    public boolean isFull() {
        return count == keys.size();
    }

    /**
     * Verifica si el nodo tiene el mínimo de claves requeridas
     * @return true si tiene suficientes claves, false en caso contrario
     */
    public boolean hasMinKeys() {
        return count >= (keys.size() / 2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(keys.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}