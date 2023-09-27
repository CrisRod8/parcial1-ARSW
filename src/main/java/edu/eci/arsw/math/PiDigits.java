package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {
    //Estas son variables estáticas que se utilizan en el cálculo de los dígitos de Pi.
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private byte[] digits;

    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count) {//Este es un método estático que calcula y devuelve un rango de dígitos de Pi, utilizando la fórmula Bailey-Borwein-Plouffe.

        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }
            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }

        return digits;
    }

    public byte[] getDigits(int start, int count, int n) throws InterruptedException {//Este método al igual que el anterior calcula los dígitos de Pi, utilizando múltiples hilos.
        //Este método recibe tres parámetros: start (el inicio de la secuencia de dígitos), count (el número de dígitos a calcular), y n (el número de hilos para dividir el cálculo).

        //Aquí se inicializa un arreglo de bytes llamado digits para almacenar los dígitos de Pi y se crea una lista de hilos llamada threads.
        //Se calcula el incremento para cada hilo y se maneja el resto en caso de que la división no sea exacta.
        digits = new byte[count];
        ArrayList<LiveThread> threads = new ArrayList<>();
        int increment = count / n;
        int restIncrement = count % n;
        int finish = increment;

        //Creación de hilos
        //En este bloque, se crean y se agregan n hilos a la lista threads.
        //Cada hilo tiene una parte específica de los dígitos de Pi que debe calcular.
        for(int i = 0; i < n; i++){
            if(i == n-1){
                increment += restIncrement;
                finish = increment;
            }
            threads.add(new LiveThread(start,finish));
            start += increment;
        }
        //Aquí se inician todos los hilos en la lista threads.
        for(LiveThread th : threads){
            th.start();
        }
        //En este bloque, el programa espera a que cada hilo termine su cálculo y recopila los dígitos de Pi calculados por cada hilo.
        try {
            int i = 0;
            for (LiveThread t : threads) {
                t.join();
                for (byte b : t.getDigits()) {
                    digits[i] = b;
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error en el hilo");
        }

        //Aquí se mide el tiempo de espera de 5 segundos utilizando System.nanoTime() y TimeUnit.SECONDS.sleep().
        Long startTime = System.nanoTime();
        TimeUnit.SECONDS.sleep(5);
        Long endTime = System.nanoTime();
        Long timeElapsed = endTime - startTime;

        //En este bloque, se pausan los hilos y se espera la entrada del usuario antes de reanudarlos.
        for(LiveThread thread: threads){
            thread.suspender();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Presione Enter para continuar");
        String input = scanner.nextLine();

        if (input.equals("")) {
            // Reanuda los hilos si el usuario presiona Enter.
            for (LiveThread thread: threads) {
                thread.reanudar();
            }
        }

        //Finalmente, se devuelve el arreglo de bytes que contiene los dígitos de Pi.
        byte[] digits = this.digits;
        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {//Este método privado calcula una suma específica utilizada en la fórmula para el cálculo de los dígitos de Pi.
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {//Este método privado calcula una potencia modular específica utilizada en la fórmula para el cálculo de los dígitos de Pi.
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }
        int result = 1;
        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }
        return result;
    }

}