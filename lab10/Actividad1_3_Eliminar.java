package lab10;


/**
 * Actividad 1.3 - Eliminación de claves en un B-Tree de orden 3
 * Este ejemplo prueba la eliminación de varias claves y muestra el estado del árbol tras cada eliminación.
 */
public class Actividad1_3_Eliminar {
    public static void main(String[] args) {
        System.out.println("=== Actividad 1.3 - Eliminación de claves ===\n");

        // Paso 1: Crear el B-Tree con orden 3
        BTree<Integer> btree = new BTree<>(3);

        // Paso 2: Insertar un conjunto de claves
        int[] clavesIniciales = {100, 50, 20, 70, 10, 30, 80, 90, 200, 25, 15, 5, 65, 35, 60, 18, 93, 94};
        for (int clave : clavesIniciales) {
            btree.insert(clave);
        }

        System.out.println("Árbol inicial:");
        btree.printTree();
        System.out.println("\n---------------------------\n");

        // Paso 3: Eliminar claves seleccionadas y mostrar el árbol después de cada eliminación
        Integer[] clavesAEliminar = {100, 50, 30, 5, 25};
        for (Integer clave : clavesAEliminar) {
            System.out.println("Eliminando: " + clave);
            btree.remove(clave);
            btree.printTree();
            System.out.println("---------------------------");
        }

        // Paso 4: Mostrar recorrido In-Order final
        System.out.println("\n=== Recorrido In-Order final ===");
        System.out.println(btree.inOrderTraversal());
    }
}
