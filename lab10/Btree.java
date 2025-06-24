package lab10;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Implementación de un B-Tree genérico
 * @param <E> Tipo de elementos almacenados en el árbol, debe ser Comparable
 */

 public class BTree<E extends Comparable <E>>{
    private BNode<E> root;
    private final int order;    //orden del B-Tree (máximo número de claves por nodo)
    private int size;           //Numero total de elementos en el arbol
 }


 /**
  * Constructor para crear un arbol vacio
  * @param order Orden del arbol al menos 2)
  * @throws IlegalArgumentException en caso es de orden menor
  */
  public BTree(int order){
    if (order < 2){
        throw new Exceptions.IllegalArgumentBTreeException("El orden del B-Tree tiene que ser almenos 2")
    }

    this.order = order;
    this.root = new BNode<>(order);
    this.size = 0;

    
  }