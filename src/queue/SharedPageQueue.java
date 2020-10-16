package queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The shared page queue should store a linked list of strings, each of which
 * has the text of a web document.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class SharedPageQueue {
    private static final int MAX_PAGE_QUEUE_SIZE = 50000;
    private static Queue<String> sharedPageQueue = new LinkedList<>();
    private static int pagesFound;

    /**
     * Add a new page to the queue. The queue should have a maximum size
     * of 50000 pages. If there is no room on the queue, then the thread
     * should call wait() on the queue. After adding a new page to the queue,
     * call notify() on the queue.
     *
     * @param pageText
     * @throws InterruptedException
     */
    public static void addPage(String pageText) throws InterruptedException {
        synchronized (sharedPageQueue) { // todo: problem?
            while (sharedPageQueue.size() == MAX_PAGE_QUEUE_SIZE) {
                sharedPageQueue.wait();
            }

            // print out a trace
            System.out.println("Adding page by: " + Thread.currentThread().getName());
            System.out.println("Page queue size before adding: " + sharedPageQueue.size());

            sharedPageQueue.add(pageText);
            pagesFound ++;
            System.out.println("Page queue size after adding: " + sharedPageQueue.size());
            sharedPageQueue.notifyAll();
        }
    }

    /**
     * Returns a page from the queue. If the queue is empty, then the thread
     * should call wait() on the queue. After removing a page from the queue,
     * call notify() on the queue before returning the page text.
     *
     * @return pageText
     * @throws InterruptedException
     */
    public static String getNextPage() throws InterruptedException {
        synchronized (sharedPageQueue) {
            while (sharedPageQueue.isEmpty()) {
                sharedPageQueue.wait();
            }

            // print out a trace
            System.out.println("Getting page by: " + Thread.currentThread().getName());
            System.out.println("Page queue size before removing: " + sharedPageQueue.size());

            String pageText = sharedPageQueue.poll();
            System.out.println("Page queue size after removing: " + sharedPageQueue.size());
            sharedPageQueue.notifyAll();

            return pageText;
        }
    }

    /**
     * Returns the total number of pages that have been added to the queue
     * through the addPage() method.
     *
     * @return size of the pages queue
     */
    public static int getPagesDownloaded() {
        synchronized (sharedPageQueue) {
            return pagesFound;
        }
    }
}
