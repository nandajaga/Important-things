package extentReport.testng.listeners;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * The class has method to customize the Console output and returns the output in String Format
 * Created by sbioi 29/06/2020
 */
public class ConsoleOutputStream {
    private ByteArrayOutputStream baos;
    private PrintStream oldStream;
    private boolean bln;
    private OutputStream outputStreamer;

    // Method is called 1st to customize   console and capture the log
    public synchronized void start() {
        if (bln) {
            return;
        }

        bln = true;
        try {
            oldStream = System.out;
        } catch (Exception e) {
            e.getMessage();
        }
        baos = new ByteArrayOutputStream();

        outputStreamer =
                new OutputStreamer(Arrays.asList(oldStream, baos));
        PrintStream custom = new PrintStream(outputStreamer);

        System.setOut(custom);
    }

// Method is used to  stop customized console and  setting the old console back and returning the console data in String format

    public synchronized String stop() throws IOException {
        if (!bln) {
            return "";
        }

        outputStreamer =
                new OutputStreamer(Arrays.asList(oldStream, baos));
        outputStreamer.flush();
        System.setOut(oldStream);

        String consolData = baos.toString();

        baos = null;
        oldStream = null;
        bln = false;
        return consolData;
    }

//Method is used to write , flush and close stream

    private static class OutputStreamer extends OutputStream {
        private List<OutputStream> outputStreams;

        public OutputStreamer(List<OutputStream> outputStreamer) {
            this.outputStreams = Collections.unmodifiableList(outputStreamer);
        }

        @Override
        public void write(int b) throws IOException {
            for (OutputStream os : outputStreams) {
                os.write(b);
            }
        }

        @Override
        public void flush() throws IOException {
            for (OutputStream os : outputStreams) {
                os.flush();
            }
        }

        @Override
        public void close() throws IOException {
            for (OutputStream os : outputStreams) {
                os.close();
            }
        }
    }
}