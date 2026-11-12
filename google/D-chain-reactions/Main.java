
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        StringBuilder out = new StringBuilder();
        st.nextToken();
        int t = (int) st.nval;
        for (int caso = 1; caso <= t; caso++) {
            st.nextToken();
            int n = (int) st.nval;
            long[] f = new long[n + 1];
            int[] p = new int[n + 1];
            for (int i = 1; i <= n; i++) { st.nextToken(); f[i] = (long) st.nval; }
            for (int i = 1; i <= n; i++) { st.nextToken(); p[i] = (int) st.nval; }
            long[] suma = new long[n + 1];
            long[] minimo = new long[n + 1];
            boolean[] tieneHijos = new boolean[n + 1];
            Arrays.fill(minimo, Long.MAX_VALUE);
            long total = 0;
            for (int i = n; i >= 1; i--) {
                long sube;
                if (!tieneHijos[i]) {
                    sube = f[i];
                } else {
                    total += suma[i] - minimo[i];
                    sube = Math.max(f[i], minimo[i]);
                }
                int padre = p[i];
                if (padre == 0) {
                    total += sube;
                } else {
                    tieneHijos[padre] = true;
                    suma[padre] += sube;
                    minimo[padre] = Math.min(minimo[padre], sube);
                }
            }
            out.append("Case #").append(caso).append(": ").append(total).append('\n');
        }
        System.out.print(out);
    }
}
