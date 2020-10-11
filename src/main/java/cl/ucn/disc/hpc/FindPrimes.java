package cl.ucn.disc.hpc;

/*
    1. Crear una funcion que permita distinguir si un numero es primo o no, retornando true o false
    2. Contar la cantidad de numeros primos que existen entre 2 y 10000.
    3. Escribir un codigo que resuelva el punto 2, utilizando 1,2,4,...N nucleos

    @author Diego Bravo
*/

import java.util.ArrayList;

public class FindPrimes implements Runnable {

    // Range
    private final long from, to;

    // The minimum value
    private static final Integer BEGIN = 1;

    // Limit of the count
    private static final Integer LIMIT = 10000;


    // Array that will contain the prime numbers
    public ArrayList<Long> primeList = new ArrayList<>();

    /**
     * The constructor of the class
     * @param from the initial range
     * @param to the range limit
     */
    public FindPrimes(long from, long to) {
        this.from = from;
        this.to = to;
    }

    /**
     * This function checks the primality of a number
     * @param number = the number to evaluate
     * @return TRUE for the prime numbers, FALSE if not
     */
    public static boolean isPrime(final long number) {

        // Check if the number is negative (border validation)
        if (number < 0) {
            throw new IllegalArgumentException("Negative numbers can't be prime");
        }

        // 1 is not a prime number
        if (number == 1) {
            return false;
        }

        // Check from 2 to n/2
        for (int i = 2; i <= number / 2; i++) {
            if ((number % i) == 0) {
                return false;
            }
        }

        return true;

    }

    /**
     * The thread run this method that search for the prime numbers
     */
    @Override
    public void run() {

        for (long i = from; i <= to; i++) {
            if (isPrime(i)) {
                primeList.add(i);
            }
        }

    }

    /**
     * This method find the prime numbers between 1 and 10000 using N threads
     * @param threads = number of threads
     * @throws InterruptedException
     */
    public static void findPrimes(int threads) throws InterruptedException {

        int threads_available = threads;

        long from = BEGIN, increaseValue = LIMIT/threads, to = increaseValue;

        long start_time = System.currentTimeMillis();

        while (threads_available != 0) {

            System.out.println("from = " + (from) + " to = " + (to));

            FindPrimes findPrimes = new FindPrimes(from, to);
            Thread thread = new Thread(findPrimes);

            thread.start();
            thread.join();

            to = to + increaseValue;
            from = from + increaseValue;

            // System.out.println("// new values: from = " + (from) + " to = " + (to));

            // findPrimes.getPrimeList().forEach(prime -> System.out.println(prime));

            threads_available--;

        }

        long end_time = System.currentTimeMillis();
        long execution_time = end_time - start_time;

        // Print the results
        System.out.print("Threads: " + threads + " | ");
        System.out.print("The execution time was " + execution_time + " ms" + "\n" + "\n");


    }

    /**
     * @return primeList: the arraylist that contains the prime numbers
     */
    public ArrayList<Long> getPrimeList() {
        return primeList;
    }


    public static void main(String[] args) throws Exception {

        // Using 1 thread
        findPrimes(1);
        // Using 2 threads
        findPrimes(2);
        // Using 4 threads
        findPrimes(4);

    }
}
