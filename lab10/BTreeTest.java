package lab10;

import java.util.ArrayList;
import java.util.Random;

public class BTreeTest {
    public static void main(String[] args) {
        try {
            // 1. Crear un B-Tree de orden 3
            System.out.println("=== Creando B-Tree de orden 3 ===");
            BTree<Integer> bTree = new BTree<>(3);
            
            // 2. Insertar valores iniciales
            System.out.println("\n=== Insertando valores ===");
            int[] initialValues = {300, 20, 150, 12, 25, 80, 142, 176, 206, 297, 
                                  380, 395, 412, 430, 480, 520, 451, 493, 506, 521, 600};
            
            for (int value : initialValues) {
                System.out.println("Insertando: " + value);
                bTree.insert(value);
                bTree.printTree();
                System.out.println("----------------------");
            }
            
            // 3. Buscar elementos
            System.out.println("\n=== Busquedas ===");
            testSearch(bTree, 300, true);
            testSearch(bTree, 25, true);
            testSearch(bTree, 999, false);
            testSearch(bTree, 520, true);
            
            // 4. Eliminar elementos
            System.out.println("\n=== Eliminaciones ===");
            testRemove(bTree, 300, true);  // Eliminar valor existente
            testRemove(bTree, 999, false); // Eliminar valor no existente
            testRemove(bTree, 520, true);  // Eliminar otro valor existente
            
            // 5. Recorrido in-order
            System.out.println("\n=== Recorrido In-Order ===");
            ArrayList<Integer> inOrder = bTree.inOrderTraversal();
            System.out.println("Elementos ordenados: " + inOrder);
            
            // 6. Prueba con strings
            System.out.println("\n=== Probando con Strings ===");
            BTree<String> stringTree = new BTree<>(2);
            stringTree.insert("Hola");
            stringTree.insert("Mundo");
            stringTree.insert("B-Tree");
            stringTree.insert("Java");
            stringTree.insert("Estructuras");
            stringTree.insert("Datos");
            
            System.out.println("Contiene 'Java'? " + stringTree.contains("Java"));
            System.out.println("Contiene 'Python'? " + stringTree.contains("Python"));
            System.out.println("Elementos ordenados: " + stringTree.inOrderTraversal());
            
            // 7. Prueba de excepciones
            System.out.println("\n=== Probando excepciones ===");
            testExceptions();
            
        } catch (Exception e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testSearch(BTree<Integer> tree, int value, boolean expected) {
        boolean result = tree.contains(value);
        System.out.printf("Buscar %d: %s (Esperado: %s) %s%n",
                         value, result, expected, 
                         (result == expected) ? "✓" : "✗ ERROR");
    }
    
    private static void testRemove(BTree<Integer> tree, int value, boolean expected) {
        boolean result = tree.remove(value);
        System.out.printf("Eliminar %d: %s (Esperado: %s) %s%n",
                        value, result, expected,
                        (result == expected) ? "✓" : "✗ ERROR");
        System.out.println("Árbol después de eliminar:");
        tree.printTree();
        System.out.println("----------------------");
    }
    
    private static void testExceptions() {
        try {
            // 1. Probar orden inválido
            System.out.println("Intentando crear B-Tree con orden 1...");
            new BTree<>(1);
            System.out.println("✗ ERROR: No se lanzó excepción");
        } catch (Exceptions.IllegalArgumentBTreeException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }
        
        try {
            // 2. Probar inserción nula
            BTree<Integer> tree = new BTree<>(2);
            System.out.println("Intentando insertar null...");
            tree.insert(null);
            System.out.println("✗ ERROR: No se lanzó excepción");
        } catch (Exceptions.NullValueException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }
        
        try {
            // 3. Probar búsqueda nula
            BTree<Integer> tree = new BTree<>(2);
            System.out.println("Intentando buscar null...");
            tree.contains(null);
            System.out.println("✗ ERROR: No se lanzó excepción");
        } catch (Exceptions.NullValueException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }
        
        try {
            // 4. Probar eliminación en árbol vacío
            BTree<Integer> tree = new BTree<>(2);
            System.out.println("Intentando eliminar de árbol vacío...");
            tree.remove(10);
            System.out.println("✗ ERROR: No se lanzó excepción");
        } catch (Exceptions.IllegalOperationException e) {
            System.out.println("✓ Excepción capturada: " + e.getMessage());
        }
    }
    
    // Método adicional para prueba con datos aleatorios
    public static void stressTest() {
        System.out.println("\n=== Prueba de estrés con 100 elementos ===");
        BTree<Integer> tree = new BTree<>(4);
        Random random = new Random();
        
        // Insertar 100 números aleatorios
        for (int i = 0; i < 100; i++) {
            int num = random.nextInt(1000);
            tree.insert(num);
        }
        
        System.out.println("Total elementos: " + tree.size());
        System.out.println("Árbol completo:");
        tree.printTree();
        
        // Verificar que el recorrido in-order esté ordenado
        ArrayList<Integer> inOrder = tree.inOrderTraversal();
        boolean sorted = true;
        for (int i = 1; i < inOrder.size(); i++) {
            if (inOrder.get(i - 1) > inOrder.get(i)) {
                sorted = false;
                break;
            }
        }
        System.out.println("Recorrido in-order ordenado correctamente? " + sorted);
    }
}