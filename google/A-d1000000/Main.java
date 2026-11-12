
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer st = new StreamTokenizer(in);
        StringBuilder out = new StringBuilder();
        st.nextToken();
        int t = (int) st.nval;
        for (int caso = 1; caso <= t; caso++) {
            st.nextToken();
            int n = (int) st.nval;
            int[] dados = new int[n];
            for (int i = 0; i < n; i++) {
                st.nextToken();
                dados[i] = (int) st.nval;
            }
            Arrays.sort(dados);
            int largo = 0;
            for (int s : dados) {
                if (s > largo) largo++;
            }
            out.append("Case #").append(caso).append(": ").append(largo).append('\n');
        }
        System.out.print(out);
    }
}
