import java.util.Arrays;

public class Main {

    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;
    public static float checkSum;
    public static float checkSum1T;
    public static float checkSum2T;
    private static float[] arrayForDefM = new float[SIZE];
    private static  float[] arrayForTreadM = new float[SIZE];


    public static void main(String[] args) throws InterruptedException {
        oneThreadMethod();
        System.out.println("--");
        twoThreadMethod();
        System.out.println("Идентичность массивов = "+Arrays.equals(arrayForTreadM, arrayForDefM));
    }

    private static void oneThreadMethod() {
        Arrays.fill(arrayForDefM, 0, arrayForDefM.length, 1f);
        long allTime = System.currentTimeMillis();
        for (int i = 0; i < arrayForDefM.length; i++) {
            arrayForDefM[i] = (float) (arrayForDefM[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
        }
        System.out.println("Время на выполнение затраченное в 1 потоке = "+(System.currentTimeMillis() - allTime));
    }

    private static void twoThreadMethod() throws InterruptedException {
        Arrays.fill(arrayForTreadM, 0, arrayForTreadM.length, 1f);
        long allTime = System.currentTimeMillis();
        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];
        System.arraycopy(arrayForTreadM, 0, arr1, 0, HALF);
        System.arraycopy(arrayForTreadM, HALF, arr2, 0, HALF);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr1.length; i++) {
                    arr1[i] = (float) (arr1[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
                }
            }
        });
        //тут как лямбда выражение
        Thread thread2 = new Thread(() -> {
            for (int i = HALF; i < (arr2.length + HALF); i++) {
                arr2[i - HALF] = (float) (arr2[i - HALF] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.arraycopy(arr1, 0, arrayForTreadM, 0, HALF);
        System.arraycopy(arr2, 0, arrayForTreadM, HALF, HALF);
        System.out.println("Время на выполнение затраченное в 2-х потоках = "+(System.currentTimeMillis() - allTime));
    }

}
