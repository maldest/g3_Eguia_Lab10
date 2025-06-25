package lab10;

public class Ejercicio4 {
    public static void main(String[] args) {
        BTree<RegistroEstudiante> arbol = new BTree<>(4);

        // Insertar estudiantes
        arbol.insert(new RegistroEstudiante(103, "Ana"));
        arbol.insert(new RegistroEstudiante(110, "Luis"));
        arbol.insert(new RegistroEstudiante(101, "Carlos"));
        arbol.insert(new RegistroEstudiante(120, "Lucía"));
        arbol.insert(new RegistroEstudiante(115, "David"));
        arbol.insert(new RegistroEstudiante(125, "Jorge"));
        arbol.insert(new RegistroEstudiante(140, "Camila"));
        arbol.insert(new RegistroEstudiante(108, "Rosa"));
        arbol.insert(new RegistroEstudiante(132, "Ernesto"));
        arbol.insert(new RegistroEstudiante(128, "Denis"));
        arbol.insert(new RegistroEstudiante(145, "Enrique"));
        arbol.insert(new RegistroEstudiante(122, "Karina"));
        arbol.insert(new RegistroEstudiante(108, "Juan")); // Duplicado de código

        // Buscar estudiantes
        buscarNombre(arbol, 115); // David
        buscarNombre(arbol, 132); // Ernesto
        buscarNombre(arbol, 999); // No encontrado

        // Eliminar estudiante con código 101
        arbol.remove(new RegistroEstudiante(101, "")); // Solo importa el código

        // Insertar nuevo estudiante
        arbol.insert(new RegistroEstudiante(106, "Sara"));

        // Buscar Sara
        buscarNombre(arbol, 106);

        // Mostrar árbol
        System.out.println("\nÁrbol actual:");
        arbol.printTree();
    }

    public static void buscarNombre(BTree<RegistroEstudiante> arbol, int codigo) {
        for (RegistroEstudiante e : arbol.inOrderTraversal()) {
            if (e.getCodigo() == codigo) {
                System.out.println("Código " + codigo + " → " + e.getNombre());
                return;
            }
        }
        System.out.println("Código " + codigo + " → No encontrado");
    }
}
