package edu.eci.arsw.math;

import java.util.Arrays;

public class LifeThread extends Thread{

    private int ini;
    private int fin;
    private double sum = 0;
    public final Object lock;
    private byte[] digits;

    public LifeThread(int ini, int fin, byte[] digits, Object lock){

        this.ini = ini;
        this.fin = fin;
        this.digits = digits;
        this.lock = lock;

    }

    public void run(){

        Long start = System.currentTimeMillis();
        for (int i = ini; i < fin; i++) {
            if(System.currentTimeMillis()-start <= 5000) {
                if (i % PiDigits.DigitsPerSum == 0) {
                    sum = 4 * PiDigits.sum(1, ini)
                            - 2 * PiDigits.sum(4, ini)
                            - PiDigits.sum(5, ini)
                            - PiDigits.sum(6, ini);
                    ini += PiDigits.DigitsPerSum;
                }

                sum = 16 * (sum - Math.floor(sum));
                digits[i] = (byte) sum;
            }else {
                synchronized (lock){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}