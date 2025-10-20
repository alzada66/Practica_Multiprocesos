
import java.io.*;
import java.util.*;

public class procesarNivel {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No se especificó el nivel.");
            return;
        }

        String nivelObjetivo = args[0];
        File input = new File("src/recursos/puntuaciones.txt");

        if (!input.exists()) {
            System.out.println("Archivo puntuaciones.txt no encontrado.");
            return;
        }

        List<String[]> datos = new ArrayList<>();

        // Leer y filtrar por nivel
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3 && partes[2].trim().equalsIgnoreCase(nivelObjetivo)) {
                    datos.add(new String[]{partes[0].trim(), partes[1].trim()});
                }
            }
        }

        if (datos.isEmpty()) {
            System.out.println("No hay datos para " + nivelObjetivo);
            return;
        }

        // Calcular estadísticas
        List<Integer> puntuaciones = new ArrayList<>();
        for (String[] d : datos) {
            puntuaciones.add(Integer.parseInt(d[1]));
        }

        double media = puntuaciones.stream().mapToInt(i -> i).average().orElse(0);
        int max = puntuaciones.stream().max(Integer::compare).orElse(0);
        int min = puntuaciones.stream().min(Integer::compare).orElse(0);

        // Ordenar y mostrar top 5
        datos.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        System.out.println("=== " + nivelObjetivo.toUpperCase() + " ===");
        System.out.println("Top 5 jugadores:");
        datos.stream().limit(5).forEach(d -> System.out.println(d[0] + ", " + d[1]));
        System.out.println("Media: " + media);
        System.out.println("Máx: " + max);
        System.out.println("Mín: " + min);
    }
}
