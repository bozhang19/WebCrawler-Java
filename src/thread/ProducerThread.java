package thread;

import queue.SharedLinkQueue;
import queue.SharedPageQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents a producer thread.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class ProducerThread extends Thread {
    public static int errorCount;
    public static int numStarted;

    /**
     * Creates a producer thread object.
     */
    public ProducerThread() {
    }

    /**
     * Creates a producer thread object with the given thread name.
     *
     * @param name  the given thread name
     */
    public ProducerThread(String name) {
        super(name);
    }

    /**
     * Repeatedly do the following:
     *  - pull a link from the link queue,
     *  - download the HTML page text at the given URL,
     *  - store the HTML page text on the page queue as a string.
     */
    @Override
    public void run() {
        numStarted ++;

        while (true) {
            try {
                // pull a link from the link queue
                URL url = new URL(SharedLinkQueue.getNextLink());

                // download the HTML page text at the given URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader download = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // store the HTML page text on the page queue as a string
                String pageText = "";
                String inputLine;
                while ((inputLine = download.readLine()) != null) {
                    pageText += inputLine;
                }
                SharedPageQueue.addPage(pageText);

                download.close();
            } catch (MalformedURLException e) {
                errorCount ++;
                System.out.println("MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                errorCount ++;
                System.out.println("IOException: " + e.getMessage());
            } catch (InterruptedException e) {
                errorCount ++;
                System.out.println("InterruptedException: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the total number of errors.
     *
     * @return errorTotal  the total number of errors
     */
    public static int getErrorTotal() {
        return errorCount;
    }

    /**
     * Returns the total number of producer threads started.
     *
     * @return numStarted  the total number of producer threads
     */
    public static int getNumStarted() {
        return numStarted;
    }
}
