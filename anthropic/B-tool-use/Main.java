// Anthropic B · Uso de herramientas (API de Claude · Tool use)
// Cada línea es una respuesta del modelo en JSON.
//   stop_reason = tool_use -> se ejecutan en orden los bloques tool_use
//                             y se imprime "tool_result <id> <resultado>"
//   stop_reason = end_turn -> "final: <texto>"
// Al final: "resumen: N llamadas, E errores" (errores = resultados que empiezan con "error:").
// Java no trae librería de JSON, así que hay un lector de JSON pequeño al final del archivo.
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    static String ejecutar(String nombre, Map<String, Object> input) {
        switch (nombre) {
            case "calculadora": {
                String op = (String) input.get("operacion");
                long a = (Long) input.get("a"), b = (Long) input.get("b");
                switch (op) {
                    case "sumar": return String.valueOf(a + b);
                    case "restar": return String.valueOf(a - b);
                    case "multiplicar": return String.valueOf(a * b);
                    case "dividir": return b == 0 ? "error: división por cero" : String.valueOf(a / b);
                    default: return "error: operación desconocida: " + op;
                }
            }
            case "clima": {
                String ciudad = (String) input.get("ciudad");
                switch (ciudad) {
                    case "Bogotá": return "14 °C";
                    case "Medellín": return "24 °C";
                    case "Cali": return "26 °C";
                    default: return "error: ciudad desconocida: " + ciudad;
                }
            }
            default:
                return "error: herramienta desconocida: " + nombre;
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        int llamadas = 0, errores = 0;
        String linea;
        while ((linea = in.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            Map<String, Object> resp = Json.objeto(linea);
            List<Object> contenido = (List<Object>) resp.get("content");
            if ("tool_use".equals(resp.get("stop_reason"))) {
                for (Object o : contenido) {
                    Map<String, Object> bloque = (Map<String, Object>) o;
                    if (!"tool_use".equals(bloque.get("type"))) continue;
                    String res = ejecutar((String) bloque.get("name"), (Map<String, Object>) bloque.get("input"));
                    llamadas++;
                    if (res.startsWith("error:")) errores++;
                    out.println("tool_result " + bloque.get("id") + " " + res);
                }
            } else if ("end_turn".equals(resp.get("stop_reason"))) {
                StringBuilder texto = new StringBuilder();
                for (Object o : contenido) {
                    Map<String, Object> bloque = (Map<String, Object>) o;
                    if ("text".equals(bloque.get("type"))) texto.append((String) bloque.get("text"));
                }
                out.println("final: " + texto);
            }
        }
        out.println("resumen: " + llamadas + " llamadas, " + errores + " errores");
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
