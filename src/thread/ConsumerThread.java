package thread;

import queue.SharedLinkQueue;
import queue.SharedPageQueue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a consumer thread object.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class ConsumerThread extends Thread {
    public static int keywordCount;
    public static int numStarted;

    private String keyword;

    /**
     * Creates a consumer thread object with the given thread name.
     */
    public ConsumerThread(String name) {
        super(name);
    }

    /**
     * Creates a consumer thread with the given thread name and keyword to search.
     *
     * @param name  the given name of the thread
     * @param keyword  the keyword to search
     */
    public ConsumerThread(String name, String keyword) {
        super(name);
        this.keyword = keyword;
    }

    /**
     * Repeatedly do the following:
     *  - pull a page form the page queue,
     * 	- search the page for all links in anchor (<a href="">) elements,
     * 	- add each link found to the link queue,
     * 	- search the page for any keywords specified by the user of the web crawler,
     * 	- keep track of how many keywords are encountered.
     */
    @Override
    public void run() {
        numStarted ++;

        // continue to pull words from the queue
        while (true) {
            try {
                // pull a page from the page queue
                String pageText = SharedPageQueue.getNextPage();

                Pattern pattern = Pattern.compile("href=\"(http:.*?)\"");
                Matcher matcher = pattern.matcher(pageText);

                // search the page for all links in anchor (<a href="">) elements
                while (matcher.find()) {
                    // add each link found to the link queue
                    String link = matcher.group(1);
                    SharedLinkQueue.addLink(link);
                }

                // if there is a keyword to search
                if(keyword != null) {
                    String[] parts = pageText.split(keyword);
                    keywordCount += parts.length - 1;
                }
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the total number of the keyword
     *
     * @return keywordTotal
     */
    public static int getKeywordTotal() {
        return keywordCount;
    }

    public static int getNumStarted() {
        return numStarted;
    }
}
