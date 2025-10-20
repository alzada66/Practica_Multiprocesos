import java.io.*;
import java.util.*;

public class Principal {

    public static void main(String[] args) throws Exception {
        File input = new File("src/recursos/puntuaciones.txt");
        if (!input.exists()) {
            System.out.println("No se encontró el archivo de puntuaciones.");
            return;
        }

        List<String> niveles = Arrays.asList("nivel1", "nivel2", "nivel3", "nivel4", "nivel5");
        List<Process> procesos = new ArrayList<>();

        File carpetaSalida = new File("salida");
        if (!carpetaSalida.exists()) carpetaSalida.mkdirs();

        for (String nivel : niveles) {
            ProcessBuilder pb = new ProcessBuilder("java", "main.ProcesarNivel", nivel);
            pb.redirectOutput(new File("salida/ranking_" + nivel + ".txt"));
            pb.redirectErrorStream(true);
            pb.directory(new File("bin"));
            Process proceso = pb.start();
            procesos.add(proceso);
        }

        for (Process p : procesos) {
            p.waitFor();
        }

        mostrarRankingGlobal();
    }

    private static void mostrarRankingGlobal() throws IOException {
        System.out.println("\n=== TOP GLOBAL DE JUGADORES ===");
        File carpeta = new File("salida");
        List<String> todasLineas = new ArrayList<>();

        for (File f : Objects.requireNonNull(carpeta.listFiles())) {
            todasLineas.addAll(java.nio.file.Files.readAllLines(f.toPath()));
        }

        Map<String, Integer> puntuaciones = new HashMap<>();
        for (String linea : todasLineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split(",");
                String nombre = partes[0].trim();
                int puntos = Integer.parseInt(partes[1].trim());
                puntuaciones.put(nombre, puntos);
            }
        }

        puntuaciones.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue() + " pts"));
    }
}
