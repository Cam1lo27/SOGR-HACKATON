// Anthropic C · Costos con caché de prompts (API de Claude · Prompt caching y precios)
// Precios de ejemplo: $3 por millón de tokens de entrada y $15 por millón de salida.
// Escribir en caché cuesta 1,25 veces la entrada y leer de caché 0,1 veces.
// Todo se calcula en millonésimas de dólar con enteros (long) para no tener errores
// de redondeo:  costo = (entrada*300 + escritura*375 + lectura*30 + salida*1500) / 100
//              sin_cache = (entrada + escritura + lectura)*3 + salida*15
// Cada línea es un JSON plano de números, así que basta con buscar cada campo.
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    static long campo(String json, String nombre) {
        int p = json.indexOf("\"" + nombre + "\"");
        p = json.indexOf(':', p) + 1;
        while (Character.isWhitespace(json.charAt(p))) p++;
        int f = p;
        while (f < json.length() && (Character.isDigit(json.charAt(f)) || json.charAt(f) == '-')) f++;
        return Long.parseLong(json.substring(p, f));
    }

    static String usd(long micro) {
        return String.format("$%d.%06d", micro / 1_000_000, micro % 1_000_000);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        long total = 0, sinCache = 0;
        int n = 0, aciertos = 0;
        String linea;
        while ((linea = in.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            long entrada = campo(linea, "input_tokens");
            long escritura = campo(linea, "cache_creation_input_tokens");
            long lectura = campo(linea, "cache_read_input_tokens");
            long salida = campo(linea, "output_tokens");
            long costo = (entrada * 300 + escritura * 375 + lectura * 30 + salida * 1500) / 100;
            n++;
            total += costo;
            sinCache += (entrada + escritura + lectura) * 3 + salida * 15;
            if (lectura > 0) aciertos++;
            out.println("solicitud " + n + ": " + usd(costo));
        }
        out.println("total: " + usd(total));
        out.println("sin_cache: " + usd(sinCache));
        out.println("ahorro: " + usd(sinCache - total));
        out.println("aciertos_de_cache: " + aciertos + "/" + n);
        out.flush();
    }
}
