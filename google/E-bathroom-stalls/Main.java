
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int t = Integer.parseInt(in.readLine().trim());
        for (int caso = 1; caso <= t; caso++) {
            StringTokenizer tk = new StringTokenizer(in.readLine());
            long n = Long.parseLong(tk.nextToken());
            long k = Long.parseLong(tk.nextToken());
            TreeMap<Long, Long> tramos = new TreeMap<>();
            tramos.put(n, 1L);
            while (true) {
                Map.Entry<Long, Long> mayor = tramos.pollLastEntry();
                long x = mayor.getKey(), cuantos = mayor.getValue();
                long izq = x / 2, der = (x - 1) / 2;
                if (k <= cuantos) {
                    out.append("Case #").append(caso).append(": ")
                       .append(izq).append(' ').append(der).append('\n');
                    break;
                }
                k -= cuantos;
                tramos.merge(izq, cuantos, Long::sum);
                tramos.merge(der, cuantos, Long::sum);
            }
        }
        System.out.print(out);
    }
}
