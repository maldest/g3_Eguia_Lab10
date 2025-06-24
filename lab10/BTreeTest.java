package lab10;

import java.util.ArrayList;
import lab10.Exceptions.IllegalArgumentBTreeException;
import lab10.Exceptions.IllegalOperationException;
import lab10.Exceptions.NullValueException;

public class BTreeTest {
    public static void main(String[] args) {
        try {
            // 1. Crear un B-Tree de orden 3
            System.out.println("=== Creando B-Tree de orden 3 ===");
            BTree<Integer> bTree = new BTree<>(3);

            // 2. Insertar valores iniciales
            System.out.println("\n=== Insertando valores ===");
            int[] valores = {300, 20, 150, 12, 25, 80, 142, 176, 206, 297,
                             380, 395, 412, 430, 480, 520, 451, 493, 506, 521, 600};

            for (int v : valores) {
                System.out.println("Insertando: " + v);
                bTree.insert(v);
                bTree.printTree();
                System.out.println("----------------------");
            }

            // 3. Buscar elementos
            System.out.println("\n=== Busquedas ===");
            testBuscar(bTree, 300, true);
            testBuscar(bTree, 25, true);
            testBuscar(bTree, 999, false);
            testBuscar(bTree, 520, true);

            // 4. Eliminar elementos
            System.out.println("\n=== Eliminaciones ===");
            testEliminar(bTree, 300, true);
            testEliminar(bTree, 999, false);
            testEliminar(bTree, 520, true);

            // 5. Recorrido in-order
            System.out.println("\n=== Recorrido In-Order ===");
            ArrayList<Integer> inOrder = bTree.inOrderTraversal();
            System.out.println("In-Order: " + inOrder);

            // 6. Prueba con Strings
            System.out.println("\n=== Probando con Strings ===");
            BTree<String> arbolString = new BTree<>(2);
            String[] palabras = {"Hola", "Mundo", "B-Tree", "Java", "Estructuras", "Datos"};
            for (String s : palabras) {
                arbolString.insert(s);
            }
            System.out.println("Contiene 'Java'? " + arbolString.contains("Java"));
            System.out.println("Contiene 'Python'? " + arbolString.contains("Python"));
            System.out.println("In-Order Strings: " + arbolString.inOrderTraversal());

            // 7. Pruebas de excepciones
            System.out.println("\n=== Probando excepciones ===");
            probarExcepciones();

        } catch (Exception e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testBuscar(BTree<Integer> tree, int valor, boolean esperado) {
        boolean existe = tree.contains(valor);
        System.out.printf("Buscar %d: %s (Esperado: %s) %s%n",
                          valor, existe, esperado,
                          (existe == esperado ? "✓" : "✗ ERROR"));
    }

    private static void testEliminar(BTree<Integer> tree, int valor, boolean esperado) {
        boolean result = tree.remove(valor);
        System.out.printf("Eliminar %d: %s (Esperado: %s) %s%n",
                          valor, result, esperado,
                          (result == esperado ? "✓" : "✗ ERROR"));
        System.out.println("Árbol tras eliminación:");
        tree.printTree();
        System.out.println("----------------------");
    }

private static void probarExcepciones() {
    // Orden inválido
    try {
        System.out.println("Intentando crear B-Tree orden 1...");
        new BTree<>(1);
        System.out.println("✗ ERROR: no se lanzó IllegalArgumentBTreeException");
    } catch (IllegalArgumentBTreeException e) {
        System.out.println("✓ Capturada: " + e.getMessage());
    }

    // Inserción null
    try {
        BTree<Integer> t = new BTree<>(2);
        System.out.println("Intentando insertar null...");
        t.insert(null);
        System.out.println("✗ ERROR: no se lanzó NullValueException");
    } catch (NullValueException e) {
        System.out.println("✓ Capturada: " + e.getMessage());
    }

    // Búsqueda null
    try {
        BTree<Integer> t = new BTree<>(2);
        System.out.println("Intentando buscar null...");
        t.contains(null);
        System.out.println("✗ ERROR: no se lanzó NullValueException");
    } catch (NullValueException e) {
        System.out.println("✓ Capturada: " + e.getMessage());
    }

    // Eliminación en árbol vacío
    try {
        BTree<Integer> t = new BTree<>(2);
        System.out.println("Intentando eliminar en árbol vacío...");
        t.remove(10);
        System.out.println("✗ ERROR: no se lanzó IllegalOperationException");
    } catch (IllegalOperationException e) {
        System.out.println("✓ Capturada: " + e.getMessage());
    }
}
}
