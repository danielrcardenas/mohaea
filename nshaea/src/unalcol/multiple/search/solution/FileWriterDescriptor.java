package unalcol.multiple.search.solution;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriterDescriptor {
    public static void write(List<String> lines){
        ExperimentInfo ex = ExperimentInfo.getInstance();
        String filename= "results"+ex.toString()+".txt";
        try(FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {


            for (String line:lines) {
                out.println(line);

            }
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
