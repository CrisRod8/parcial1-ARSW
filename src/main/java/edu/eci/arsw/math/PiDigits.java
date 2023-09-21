package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Scanner;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;


    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */

    public static byte[] getDigits(int start, int count, int n) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        Scanner intro = new Scanner(System.in);

        ArrayList pdts = new ArrayList<>();
        int ini = start;
        int ndata = count/n;
        byte[] digits = new byte[count];
        Object lock = new Object();
        int fin;
        LifeThread pdt = new LifeThread(ini, fin, digits, lock);

        for (int i = 0; i < n; i++){

            pdts.add(new LifeThread(ini, ndata*(i+1), digits, lock));
            ini += ndata;

        }

        for (LifeThread pdt:pdts){

            pdt.start();

        }

        for (LifeThread pdt:pdts){

            try {
                pdt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        int t = n;
        while (t!=0){
            String enter = intro.nextLine();
            if (enter.isEmpty()){
                t = n;
                for (LifeThread pdt:pdts){
                    if(!pdt.isAlive()) t -= 1;
                }
                synchronized (lock){
                    lock.notifyAll();
                }
            }

        }

        return digits;
    }


    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    static double sum(int m, int n) {
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
    private static int hexExponentModulo(int p, int m) {
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
