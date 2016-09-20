import com.gustavofao.textprocessor.TextProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by faogustavo on 20/09/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TextProcessor processor = new TextProcessor(TextProcessor.ProcessMode.BOTH);
        StringBuffer text = new StringBuffer();

        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\gusta\\Documents\\documentos-unifra\\6_semestre\\Otimizacao\\Dicionario\\input"));
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                text.append(line);
                text.append(" ");
            }
        }

        for (String word : processor.process(text.toString())) {
            System.out.print(word + " ");
        }
    }
}
