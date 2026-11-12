// Anthropic E · Evaluación de respuestas (evaluaciones calificadas con código)
// Una respuesta pasa si, al normalizar la respuesta y lo esperado (minúsculas, sin
// tildes y todo lo que no sea letra o número convertido en espacio), lo esperado
// aparece como palabras completas dentro de la respuesta: "42" no pasa con "420".
// Se imprime "categoria: aprobadas/total" en orden alfabético y al final
// "total: a/n (p%)" con un decimal.
// Java no trae librería de JSON, así que hay un lector de JSON pequeño al final del archivo.
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;

public class Main {
    static String normalizar(String s) {
        String sinTildes = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }

    static boolean pasa(String respuesta, String esperado) {
        return (" " + normalizar(respuesta) + " ").contains(" " + normalizar(esperado) + " ");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        TreeMap<String, int[]> categorias = new TreeMap<>(); // {aprobadas, total}
        int aprobadas = 0, total = 0;
        String linea;
        while ((linea = in.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            Map<String, Object> caso = Json.objeto(linea);
            int[] c = categorias.computeIfAbsent((String) caso.get("categoria"), k -> new int[2]);
            c[1]++;
            total++;
            if (pasa((String) caso.get("respuesta"), (String) caso.get("esperado"))) {
                c[0]++;
                aprobadas++;
            }
        }
        for (Map.Entry<String, int[]> e : categorias.entrySet()) {
            out.println(e.getKey() + ": " + e.getValue()[0] + "/" + e.getValue()[1]);
        }
        // porcentaje con un decimal, redondeado con enteros: décimas = aprobadas*1000/total
        long decimas = total == 0 ? 0 : (2L * aprobadas * 1000 + total) / (2L * total);
        out.println("total: " + aprobadas + "/" + total + " (" + (decimas / 10) + "." + (decimas % 10) + "%)");
        out.flush();
    }

    // Lector de JSON mínimo: objetos -> Map, listas -> List, números enteros -> Long,
    // decimales -> Double, texto -> String, true/false -> Boolean, null -> null.
    static class Json {
        private final String s;
        private int i = 0;

        private Json(String s) { this.s = s; }

        @SuppressWarnings("unchecked")
        static Map<String, Object> objeto(Object o) {
            if (o instanceof String txt) o = new Json(txt).valor();
            return (Map<String, Object>) o;
        }

        private void espacios() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }

        private Object valor() {
            espacios();
            char c = s.charAt(i);
            if (c == '{') {
                Map<String, Object> m = new LinkedHashMap<>();
                i++; espacios();
                if (s.charAt(i) == '}') { i++; return m; }
                while (true) {
                    espacios();
                    String k = texto();
                    espacios(); i++; // ':'
                    m.put(k, valor());
                    espacios();
                    if (s.charAt(i++) == '}') return m; // si no, era ','
                }
            }
            if (c == '[') {
                List<Object> l = new ArrayList<>();
                i++; espacios();
                if (s.charAt(i) == ']') { i++; return l; }
                while (true) {
                    l.add(valor());
                    espacios();
                    if (s.charAt(i++) == ']') return l;
                }
            }
            if (c == '"') return texto();
            if (s.startsWith("true", i)) { i += 4; return Boolean.TRUE; }
            if (s.startsWith("false", i)) { i += 5; return Boolean.FALSE; }
            if (s.startsWith("null", i)) { i += 4; return null; }
            int ini = i;
            while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
            String num = s.substring(ini, i);
            return (num.contains(".") || num.contains("e") || num.contains("E")) ? (Object) Double.parseDouble(num) : (Object) Long.parseLong(num);
        }

        private String texto() {
            StringBuilder b = new StringBuilder();
            i++; // comilla inicial
            while (true) {
                char c = s.charAt(i++);
                if (c == '"') return b.toString();
                if (c != '\\') { b.append(c); continue; }
                char e = s.charAt(i++);
                switch (e) {
                    case 'n' -> b.append('\n');
                    case 't' -> b.append('\t');
                    case 'r' -> b.append('\r');
                    case 'b' -> b.append('\b');
                    case 'f' -> b.append('\f');
                    case 'u' -> { b.append((char) Integer.parseInt(s.substring(i, i + 4), 16)); i += 4; }
                    default -> b.append(e); // \" \\ \/
                }
            }
        }
    }
}
