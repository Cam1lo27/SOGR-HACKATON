// Anthropic A · Respuesta en streaming (API de Claude · Streaming)
// Se leen los eventos "event:/data:" en orden. Cada línea "data:" trae un JSON:
//   message_start       -> input_tokens y se reinician los bloques
//   content_block_delta -> se agrega el texto al bloque de su index
//   message_delta       -> stop_reason y output_tokens
//   message_stop        -> se imprimen las 4 líneas del mensaje
// "ping" y los demás eventos se ignoran. Java no trae librería de JSON,
// así que hay un lector de JSON pequeño al final del archivo.
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

        TreeMap<Long, StringBuilder> bloques = new TreeMap<>();
        Object inputTokens = null, outputTokens = null, stopReason = null;
        String linea;
        while ((linea = in.readLine()) != null) {
            linea = linea.trim();
            if (!linea.startsWith("data:")) continue;
            Map<String, Object> d = Json.objeto(linea.substring(5).trim());
            String tipo = (String) d.get("type");
            switch (tipo) {
                case "message_start" -> {
                    bloques.clear();
                    Map<String, Object> msg = Json.objeto(d.get("message"));
                    inputTokens = Json.objeto(msg.get("usage")).get("input_tokens");
                    outputTokens = null;
                    stopReason = null;
                }
                case "content_block_delta" -> {
                    Map<String, Object> delta = Json.objeto(d.get("delta"));
                    if ("text_delta".equals(delta.get("type"))) {
                        long idx = (Long) d.get("index");
                        bloques.computeIfAbsent(idx, k -> new StringBuilder()).append((String) delta.get("text"));
                    }
                }
                case "message_delta" -> {
                    stopReason = Json.objeto(d.get("delta")).get("stop_reason");
                    outputTokens = Json.objeto(d.get("usage")).get("output_tokens");
                }
                case "message_stop" -> {
                    StringBuilder texto = new StringBuilder();
                    for (StringBuilder b : bloques.values()) texto.append(b);
                    out.println("texto: " + texto);
                    out.println("stop_reason: " + stopReason);
                    out.println("input_tokens: " + inputTokens);
                    out.println("output_tokens: " + outputTokens);
                }
                default -> { } // ping, content_block_start, content_block_stop
            }
        }
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
