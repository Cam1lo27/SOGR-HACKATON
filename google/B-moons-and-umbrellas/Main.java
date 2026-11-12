
import java.io.*;

public class Main {
    static final long INF = Long.MAX_VALUE / 4;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int t = Integer.parseInt(in.readLine().trim());
        for (int caso = 1; caso <= t; caso++) {
            String[] p = in.readLine().trim().split("\s+");
            long x = Long.parseLong(p[0]), y = Long.parseLong(p[1]);
            String s = p[2];
            long costoC = 0, costoJ = 0;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                long nuevoC = INF, nuevoJ = INF;
                if (i == 0) {
                    if (ch != 'J') nuevoC = 0;
                    if (ch != 'C') nuevoJ = 0;
                } else {
                    if (ch != 'J') nuevoC = Math.min(costoC, costoJ + y);
                    if (ch != 'C') nuevoJ = Math.min(costoJ, costoC + x);
                }
                costoC = nuevoC;
                costoJ = nuevoJ;
            }
            out.append("Case #").append(caso).append(": ").append(Math.min(costoC, costoJ)).append('\n');
        }
        System.out.print(out);
    }
}
