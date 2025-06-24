package lab10;

/**
 * Actividad 1.2 - Inserción de claves en un B-Tree de orden 3
 * Este ejemplo inserta múltiples claves en un B-Tree y muestra el estado del árbol después de cada inserción.
 */
public class Actividad1_2_Insertar {
    public static void main(String[] args) {
        System.out.println("=== Actividad 1.2 - Inserción de claves ===\n");

        // Se crea un B-Tree de orden 3 (máximo 2 claves por nodo antes de dividirse)
        BTree<Integer> btree = new BTree<>(3);

        // Claves a insertar, elegidas para provocar divisiones y reorganización del árbol
        int[] claves = {100, 50, 20, 70, 10, 30, 80, 90, 200, 25, 15, 5, 65, 35, 60, 18, 93, 94};

        for (int clave : claves) {
            System.out.println("Insertando: " + clave);

            // Inserta la clave actual en el B-Tree
            btree.insert(clave);

            // Imprime el árbol después de la inserción para observar los cambios
            btree.printTree();
            System.out.println("---------------------------");
        }

        // Muestra el recorrido In-Order final para verificar el orden de los elementos
        System.out.println("\n=== Recorrido In-Order final ===");
        System.out.println(btree.inOrderTraversal());
    }
}
