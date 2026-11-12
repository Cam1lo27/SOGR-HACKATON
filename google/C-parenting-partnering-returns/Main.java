
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
            int[][] act = new int[n][3]; // inicio, fin, posición original
            for (int i = 0; i < n; i++) {
                st.nextToken(); act[i][0] = (int) st.nval;
                st.nextToken(); act[i][1] = (int) st.nval;
                act[i][2] = i;
            }
            Arrays.sort(act, (a, b) -> Integer.compare(a[0], b[0]));
            char[] res = new char[n];
            int finC = 0, finJ = 0;
            boolean posible = true;
            for (int[] a : act) {
                if (finC <= a[0]) { res[a[2]] = 'C'; finC = a[1]; }
                else if (finJ <= a[0]) { res[a[2]] = 'J'; finJ = a[1]; }
                else { posible = false; break; }
            }
            out.append("Case #").append(caso).append(": ")
               .append(posible ? new String(res) : "IMPOSSIBLE").append('\n');
        }
        System.out.print(out);
    }
}
