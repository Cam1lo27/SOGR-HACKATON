// Anthropic D · Límites de velocidad (API de Claude · Rate limits)
// Dos cubetas de tokens que se recargan sin parar y empiezan llenas:
//   solicitudes: capacidad 3,   recarga 1 por segundo
//   tokens:      capacidad 100, recarga 20 por segundo
// "REQUEST t nuevos de_cache" necesita 1 solicitud y "nuevos" tokens
// (los de caché no gastan). Si alcanza -> "ok" y se descuenta; si no, no se
// gasta nada y se responde "429 retry-after=N" con los segundos (redondeados
// hacia arriba) hasta que ambas cubetas alcancen.
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    static class Cubeta {
        final long capacidad, recarga;
        long tokens, ultimo = 0;
        Cubeta(long capacidad, long recarga) { this.capacidad = capacidad; this.recarga = recarga; this.tokens = capacidad; }

        void recargar(long t) {
            tokens = Math.min(capacidad, tokens + recarga * (t - ultimo));
            ultimo = t;
        }

        long esperaPara(long necesita) {
            if (tokens >= necesita) return 0;
            long falta = necesita - tokens;
            return (falta + recarga - 1) / recarga; // división hacia arriba
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        Cubeta solicitudes = new Cubeta(3, 1);
        Cubeta tokens = new Cubeta(100, 20);
        String linea;
        while ((linea = in.readLine()) != null) {
            StringTokenizer tk = new StringTokenizer(linea);
            if (!tk.hasMoreTokens() || !tk.nextToken().equals("REQUEST")) continue;
            long t = Long.parseLong(tk.nextToken());
            long nuevos = Long.parseLong(tk.nextToken());
            // el tercer número (tokens de caché) no cuenta para el límite
            solicitudes.recargar(t);
            tokens.recargar(t);
            if (solicitudes.tokens >= 1 && tokens.tokens >= nuevos) {
                solicitudes.tokens -= 1;
                tokens.tokens -= nuevos;
                out.println("ok");
            } else {
                long espera = Math.max(solicitudes.esperaPara(1), tokens.esperaPara(nuevos));
                out.println("429 retry-after=" + espera);
            }
        }
        out.flush();
    }
}
