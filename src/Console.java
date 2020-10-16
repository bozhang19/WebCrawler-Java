import queue.SharedLinkQueue;
import queue.SharedPageQueue;
import thread.ConsumerThread;
import thread.ProducerThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is a driver class. It allows the user to add a seed url, create and start
 * a consumer thread, create and start a producer thread, add a keyword to search,
 * and print the stats.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class Console {
    public static final int STARTING_NUM_CONSUMER = 1;
    public static final int STARTING_NUM_PRODUCER = 1;

    public static List<String> keywordsList = new ArrayList<>();

    /**
     * Calls the menu() method.
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        menu();
    }

    /**
     * Prompts the user to choose a option.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public static void menu() throws IOException, InterruptedException {
        System.out.println("1. Add seed url\n2. Add consumer\n3. Add producer\n"
                + "4. Add keyword search\n5. Print stats");

        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        scan.nextLine();

        // add seed url - prompt the user for a url, which is then added to the link queue
        if(choice == 1) {
            addSeed(scan);
        }

        // add consumer - creates and starts a new Parse(consumer) thread. If there are no
        // pages on the page queue, this will result in the thread going to sleep with a
        // called to wait()
        else if(choice == 2) {
            addConsumer();
        }

        // add producer - creates and starts a new Fetch(producer) thread. If there are no
        // links on the link queue, this will result in the thread going to sleep with a
        // called to wait()
        else if(choice == 3) {
            addProducer();
        }

        // add keyword search - creates a new keyword that consumer threads can look for
        // when parsing documents. The program should maintain a List of keywords that
        // each consumer can access.
        else if(choice == 4) {
            addKeyword(scan);
        }

        else if(choice == 5) {
            printStats();
        }

        else {
            System.out.println("Invalid choice, choose again...");
        }

        menu();
    }

    /**
     * Prompt the user for a url,
     * which is then added to the link queue.
     *
     * @param scan
     * @throws InterruptedException
     */
    public static void addSeed(Scanner scan) throws InterruptedException {
        System.out.print("Enter a url(e.g. www.cnn.com): ");
        String url = scan.nextLine();

        // add to the link queue
        SharedLinkQueue.addLink("http://" + url);
    }

    /**
     * Creates and starts a new Parse (consumer) thread.
     *
     * @throws InterruptedException
     */
    public static void addConsumer() throws InterruptedException {
        List<Thread> list = new ArrayList<>();

        // creates consumer thread(s)
        for(int i = 1; i <= STARTING_NUM_CONSUMER; i++) {
            Thread thread = new ConsumerThread("Consumer" + i);
            list.add(thread);
        }

        // starts consumer thread(s)
        for(int i = 0; i < list.size(); i++) {
            list.get(i).start();
        }
    }

    /**
     * Creates and starts a new Parse (consumer) thread with keyword search.
     *
     * @param keyword
     * @throws InterruptedException
     */
    public static void addConsumer(String keyword) throws InterruptedException {
        List<Thread> list = new ArrayList<>();

        // creates consumer thread(s) with the keyword
        for(int i = 1; i <= STARTING_NUM_CONSUMER; i++) {
            Thread thread = new ConsumerThread("Consumer" + i, keyword);
            list.add(thread);
        }

        // starts consumer thread(s) with the keyword
        for(int i = 0; i < list.size(); i++) {
            list.get(i).start();
        }
    }

    /**
     * Creates and starts a new Fetch (producer) thread.
     *
     * @throws InterruptedException
     */
    public static void addProducer() throws InterruptedException {
        List<Thread> list = new ArrayList<>();

        // creates producer thread(s)
        for(int i = 1; i <= STARTING_NUM_PRODUCER; i++) {
            Thread thread = new ProducerThread("Producer" + i);
            list.add(thread);
        }

        // starts producer thread(s)
        for(int i = 0; i < list.size(); i++) {
            list.get(i).start();
        }
    }

    /**
     * This menu option creates a new keyword that consumer threads can look
     * for when parsing documents. Your program should maintain a List of keywords
     * that each consumer can access.
     *
     * @throws InterruptedException
     */
    public static void addKeyword(Scanner scan) throws InterruptedException {
        System.out.print("Enter a keyword to search: ");
        String keyword = scan.nextLine();

        // add the keyword to the keywords list
        keywordsList.add(keyword);

        // start the consumer thread(s) with the keyword search function
        addConsumer(keyword);
    }

    /**
     * Prints the details.
     */
    public static void printStats() {
        System.out.println("Keywords found: " + ConsumerThread.getKeywordTotal());
        System.out.println("Links found: " + SharedLinkQueue.getLinksFound());
        System.out.println("Pages found: " + SharedPageQueue.getPagesDownloaded());
        System.out.println("Failed downloads: " + ProducerThread.getErrorTotal());
        System.out.println("Producers: " + ProducerThread.getNumStarted());
        System.out.println("Consumers: " + ConsumerThread.getNumStarted());
    }
}
