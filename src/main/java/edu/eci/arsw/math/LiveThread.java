package edu.eci.arsw.math;

public class LiveThread extends Thread {//La clase LiveThread es una extensión de la clase Thread que representa un hilo para calcular dígitos de Pi.

    private static int DigitsPerSum = 8;//Es un entero estático que indica la cantidad de dígitos a sumar antes de realizar el cálculo.
    private static double Epsilon = 1e-17;//Es un valor de punto flotante que ayuda en el cálculo de la suma.

    private int start;//Es un entero que representa el inicio de la secuencia de dígitos a calcular.

    private int count;//Es un entero que indica el número de dígitos a calcular.

    private byte[] digits;//Es un arreglo de bytes que almacena los dígitos de Pi calculados.

    boolean suspend = false;//Es un indicador booleano que controla si el hilo debe estar suspendido.

    //A continuación están los métodos getters y setters para los campos DigitsPerSum y Epsilon:
    public static int getDigitsPerSum() {
        return DigitsPerSum;
    }
    public static void setDigitsPerSum(int digitsPerSum) {
        DigitsPerSum = digitsPerSum;
    }
    public static double getEpsilon() {
        return Epsilon;
    }
    public static void setEpsilon(double epsilon) {
        Epsilon = epsilon;
    }

    //Ahora métodos getters y setters para los campos start y count:
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    //Métodos getters y setters para el campo digits:
    public byte[] getDigits() {
        return digits;
    }
    public void setDigits(byte[] digits) {
        this.digits = digits;
    }


    public LiveThread(int start, int count){//Constructor que inicializa un objeto LiveThread con un inicio y cantidad de dígitos a calcular:
        this.start = start;
        this.count = count;
        //System.out.println("inicio: "+start+" Digitos procesados: "+count);
    }
    @Override
    public void run(){//Método que se ejecuta cuando se inicia el hilo. Calcula los dígitos de Pi y los almacena en el campo digits.
        digits = getDigits(this.start,this.count);
    }


    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count) {//Método estático que calcula y devuelve un rango de dígitos de Pi.

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

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {//Método privado que calcula una suma específica utilizada en el cálculo de los dígitos de Pi.
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
    private static int hexExponentModulo(int p, int m) {//Método privado que calcula una potencia modular específica utilizada en el cálculo de los dígitos de Pi.
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
    //Métodos que controlan la suspensión y reanudación del hilo:
    public synchronized void suspender() {
        suspend = true;//Establece la variable "suspend" en "true".
    }
    public synchronized void reanudar() {
        suspend = false;//Establece la variable "suspend" en "false".
        notifyAll();//Notifica a los hilos que están en espera que pueden volver a correr.
    }

}