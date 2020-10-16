package queue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The shared link queue should store a linked list of strings, each of which
 * has a URL for a web document.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class SharedLinkQueue {
    public static final int MAX_LINK_QUEUE_SIZE = 50000;

    public static Queue<String> sharedLinkQueue = new LinkedList<>();
    public static HashSet<String> set = new HashSet<>();
    public static int linksFound;

    /**
     * Adds a link to the queue if it has not yet been seen. This class should
     * store a HashSet<String> of string URLs that have been seen so far. You
     * should use to HashSet object to ensure that a link has not been added more
     * than once.
     *
     * The queue should have a maximum size of 50000 links. If there is no room
     * on the queue, then the thread should call wait() on the queue. After adding
     * a new link to the queue, you should call notify() on the queue.
     *
     * @param url
     * @throws InterruptedException
     */
    public static void addLink(String url) throws InterruptedException {
        synchronized (sharedLinkQueue) {
            while (sharedLinkQueue.size() == MAX_LINK_QUEUE_SIZE) {
                sharedLinkQueue.wait();
            }

            if(set.add(url) == true) {
                System.out.println("Adding link by: " + Thread.currentThread().getName());
                System.out.println("Link queue size before adding: " + sharedLinkQueue.size());

                sharedLinkQueue.add(url);
                linksFound ++;
                System.out.println("Link queue size after adding: " + sharedLinkQueue.size());
                sharedLinkQueue.notifyAll();
            }
        }
    }

    /**
     * Returns a link from the queue. If the queue is empty, then the thread
     * should call wait() on the queue. After removing a link from the queue,
     * call notify() on the queue before returning the URL.
     *
     * @return url
     * @throws InterruptedException
     */
    public static String getNextLink() throws InterruptedException {
        synchronized (sharedLinkQueue) {
            while (sharedLinkQueue.isEmpty()) {
                sharedLinkQueue.wait();
            }

            // print out a trace
            System.out.println("Getting link by: " + Thread.currentThread().getName());
            System.out.println("Link queue size before removing: " + sharedLinkQueue.size());

            String url = sharedLinkQueue.poll();
            System.out.println("Link queue size after removing: " + sharedLinkQueue.size());
            sharedLinkQueue.notifyAll();

            return url;
        }
    }

    /**
     * Return the total number of unique links found so far through the use of
     * the addLink() method.
     *
     * @return the size of the queue
     */
    public static int getLinksFound() {
        synchronized (sharedLinkQueue) {
            return linksFound;
        }
    }
}
