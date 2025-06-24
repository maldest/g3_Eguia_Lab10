package lab10;

import lab10.Exceptions.IllegalArgumentBTreeException;
import lab10.Exceptions.IllegalOperationException;
import lab10.Exceptions.NullValueException;

/**
 * Actividad 1.4 - Pruebas de manejo de excepciones en B-Tree
 */
public class Actividad1_4_Excepciones {
    public static void main(String[] args) {
        System.out.println("=== Actividad 1.4 - Manejo de Excepciones ===\n");

        // Prueba: orden inválido al crear árbol
        try {
            System.out.println("Intentando crear B-Tree de orden 1...");
            BTree<Integer> arbolInvalido = new BTree<>(1);
        } catch (IllegalArgumentBTreeException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }

        // Prueba: insertar valor null
        try {
            BTree<Integer> arbol = new BTree<>(3);
            System.out.println("Intentando insertar null...");
            arbol.insert(null);
        } catch (NullValueException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }

        // Prueba: buscar valor null
        try {
            BTree<Integer> arbol = new BTree<>(3);
            System.out.println("Intentando buscar null...");
            arbol.contains(null);
        } catch (NullValueException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }

        // Prueba: eliminar en árbol vacío
        try {
            BTree<Integer> arbol = new BTree<>(3);
            System.out.println("Intentando eliminar en árbol vacío...");
            arbol.remove(10);
        } catch (IllegalOperationException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }
    }
    public class ItemNotFound extends RuntimeException {
        public ItemNotFound(String message) {
            super(message);
        }
    }

}
