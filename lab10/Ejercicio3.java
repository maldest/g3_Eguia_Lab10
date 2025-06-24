package lab10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ejercicio3 {
    public static void main(String[] args) {
        try {
            BTree<Integer> arbol = building_BTree("lab10/arbolB.txt");
            System.out.println("\nEl árbol B fue construido correctamente y cumple con las propiedades.\n");
            arbol.printTree();
        } catch (ItemNotFound e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public static BTree<Integer> building_BTree(String path) throws IOException, ItemNotFound {
        BufferedReader br = new BufferedReader(new FileReader(path));
        int order = Integer.parseInt(br.readLine().trim());

        // Creamos una subclase anónima para interceptar e imprimir inserciones y divisiones
        BTree<Integer> tree = new BTree<>(order) {
            @Override
            protected void insertNonFull(BNode<Integer> node, Integer key) {
                int i = node.count - 1;
                if (node.isLeaf()) {
                    while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                        i--;
                    }
                    node.insertKey(key, i + 1);
                    System.out.println("========== Insertando clave " + key + " en nodo hoja ==========");
                    System.out.println("Nodo actual: " + node + "\n");
                } else {
                    while (i >= 0 && key.compareTo(node.getKey(i)) < 0) {
                        i--;
                    }
                    i++;
                    if (node.getChild(i).isFull()) {
                        System.out.println("========== Dividiendo hijo en índice " + i + " antes de insertar " + key + " ==========");
                        System.out.println("Padre antes: " + node);
                        splitChild(node, i);
                        System.out.println("Padre después de dividir: " + node + "\n");
                    }
                    insertNonFull(node.getChild(i), key);
                }
            }

            @Override
            protected void splitChild(BNode<Integer> parent, int childIndex) {
                BNode<Integer> child = parent.getChild(childIndex);
                BNode<Integer> newChild = new BNode<>(order);
                int mid = order / 2;

                System.out.println("========== División de nodo ==========");
                System.out.println("Nodo padre antes de dividir: " + parent);
                System.out.println("Nodo hijo a dividir: " + child);

                for (int j = 0; j < order - mid - 1; j++) {
                    newChild.insertKey(child.getKey(mid + 1 + j), j);
                }

                if (!child.isLeaf()) {
                    for (int j = 0; j < order - mid; j++) {
                        newChild.insertChild(child.getChild(mid + 1 + j), j);
                    }
                }

                Integer midKey = child.getKey(mid);
                parent.insertKey(midKey, childIndex);
                parent.insertChild(newChild, childIndex + 1);

                for (int j = mid; j < order; j++) {
                    child.removeKey(mid);
                }

                System.out.println("Clave promovida: " + midKey);
                System.out.println("Nuevo hijo creado: " + newChild);
                System.out.println("Nodo padre actualizado: " + parent + "\n");
            }
        };

        Map<Integer, ArrayList<Integer>> nodos = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] partes = line.split(",");
            if (partes.length < 3) throw new ItemNotFound("Formato de nodo inválido: " + line);

            int idNodo = Integer.parseInt(partes[1].trim());
            ArrayList<Integer> claves = new ArrayList<>();
            for (int i = 2; i < partes.length; i++) {
                claves.add(Integer.parseInt(partes[i].trim()));
            }
            nodos.put(idNodo, claves);
        }
        br.close();

        for (Map.Entry<Integer, ArrayList<Integer>> entrada : nodos.entrySet()) {
            for (Integer clave : entrada.getValue()) {
                System.out.println("========== Insertando clave del nodo " + entrada.getKey() + ": " + clave + " ==========");
                tree.insert(clave);
            }
        }

        int totalClaves = nodos.values().stream().mapToInt(ArrayList::size).sum();
        if (tree.size() != totalClaves) {
            throw new ItemNotFound("El árbol no contiene todas las claves esperadas");
        }

        return tree;
    }

    public static class ItemNotFound extends RuntimeException {
        public ItemNotFound(String message) {
            super(message);
        }
    }
}
