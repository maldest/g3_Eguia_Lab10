package lab10;

public class Ejercicio2 {
    public static void main(String[] args) {
        BTree<Integer> tree = new BTree<>(3);

        int[] valores = {40, 20, 60, 10, 30, 50, 70, 5, 15, 25, 35, 45, 55};
        System.out.println("=== Insertando valores ===");
        for (int v : valores) {
            System.out.println("Insertando: " + v);
            tree.insert(v);
            tree.printTree();
            System.out.println("------------------");
        }

        System.out.println("\n=== Eliminando claves ===");
        int[] eliminar = {60, 70, 50, 55};
        for (int v : eliminar) {
            System.out.println("Eliminando: " + v);
            tree.remove(v);
            tree.printTree();
            System.out.println("------------------");
        }
    }
}
