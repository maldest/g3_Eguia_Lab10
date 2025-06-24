package lab10;

public class Ejercicio1 {
    public static void main(String[] args) {
        BTree<Integer> arbol = new BTree<>(3);
        int[] valores = {52, 30, 60, 10, 40, 55, 70};
        for (int v : valores) {
            arbol.insert(v);
        }

        System.out.println("Árbol generado:");
        arbol.printTree();
        System.out.println();

        testSearch(arbol, 52);
        testSearch(arbol, 70);
        testSearch(arbol, 33);
    }

    private static void testSearch(BTree<Integer> tree, int clave) {
        System.out.println("Buscando " + clave + ":");
        boolean encontrado = tree.search(clave);
        System.out.println("Resultado: " + encontrado);
        System.out.println("---------------------------");
    }

    
}
