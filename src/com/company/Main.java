package com.company;

import java.util.ArrayList;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

class MyBigInteger{
    public String value;
    private String timesString="0";
    private String increment;
    static private final String incrementVal ="1";

    public MyBigInteger(){
        this.increment = "0";
        this.value = "0";
    }

    public MyBigInteger(String startValue) {
        this.increment = "0";
        this.value = startValue;
    }

    public String AbbreviatedValue() {
        StringBuilder abr = new StringBuilder(13);
        if(this.value.length()<12)
            return this.value;
        for(int x=0; x<5; x++)
            abr.insert(x, this.value.charAt(x));
        for(int x=0; x<3; x++)
            abr.insert(x+5, ".");
        for(int x=0; x<5; x++)
            abr.insert(x+8, this.value.charAt(this.value.length()-(1+x)));
        return abr.toString();
    }

    public String times(MyBigInteger x) {
        increment = "0";
        while(greaterThan(x.value, increment))
        {
            //Call the plus function
            timesString = plus(this.value, timesString);
            //Create big int number to increment how many times you've looped (or set it equal to 0)
            //Can just call add again with a big int number equal to 1
            increment = plus(increment, incrementVal); //essentially this keeps track of a loop
        }
              //increment = "0";
              String temp = timesString;
              timesString = "0";
              return temp;

    }

    public String plus(MyBigInteger x) {
        String xTemp = x.value, thisTemp = this.value;

        //Initialize the C string with - characters to start
        StringBuilder C = new StringBuilder("-".repeat(Math.max(thisTemp.length(), xTemp.length())));
        if(xTemp.length() > thisTemp.length())
        {
            long difference = xTemp.length() - thisTemp.length();
            thisTemp = "0".repeat((int) difference) + thisTemp;
        }
        else if(xTemp.length() < thisTemp.length())
        {
            long difference = thisTemp.length() - xTemp.length();
            xTemp = "0".repeat((int) difference) + xTemp;
        }
        int carry = 0, sum;
        int dA, dB, dC;
        for(int i=Math.max(xTemp.length(), thisTemp.length())-1; i>=0; i--) {
            dA = Integer.parseInt(String.valueOf(thisTemp.charAt(i)));
            dB = Integer.parseInt(String.valueOf(xTemp.charAt(i)));
            sum = dA + dB + carry;
            if(sum>=10)
            {
                carry = 1;
                sum = sum - 10;
            }
            else
                carry = 0;
           C.replace(i,i+1, String.valueOf(sum));
        }
        if(carry!=0)
            C.insert(0, carry);

        return C.toString();
    }

    public String plus(String a, String b) {
        //Initialize the C string with - characters to start
        StringBuilder C = new StringBuilder("-".repeat(Math.max(b.length(), a.length())));
        if(a.length() > b.length())
        {
            long difference = a.length() - b.length();
            b = "0".repeat((int) difference) + b;
        }
        else if(a.length() < b.length())
        {
            long difference = b.length() - a.length();
            a = "0".repeat((int) difference) + a;
        }
        int carry = 0, sum;
        int dA, dB, dC;
        for(int i=Math.max(a.length(), b.length())-1; i>=0; i--) {
            dA = Integer.parseInt(String.valueOf(b.charAt(i)));
            dB = Integer.parseInt(String.valueOf(a.charAt(i)));
            sum = dA + dB + carry;
            if(sum>=10)
            {
                carry = 1;
                sum = sum - 10;
            }
            else
                carry = 0;
            C.replace(i,i+1, String.valueOf(sum));
        }
        if(carry!=0)
            C.insert(0, carry);

        return C.toString();
    }

    public boolean greaterThan(String a, String b) {

        if(b.length() > a.length())
        {
            long difference = b.length() - a.length();
            a = "0".repeat((int) difference) + a;
        }
        else if(b.length() < a.length())
        {
            long difference = a.length() - b.length();
            b = "0".repeat((int) difference) + b;
        }

        int dA, dB;
        for(int i=0; i<Math.max(b.length(), a.length()); i++) {
            dA = Integer.parseInt(String.valueOf(a.charAt(i)));
            dB = Integer.parseInt(String.valueOf(b.charAt(i)));

            if(dA > dB)
                return true;
            else if (dA < dB)
                return false;
        }
        return false;
    }


}

public class Main {

    public static List<String> cache = new ArrayList<String>();
    static long MAXTIME =  60000000000L;
    static boolean timed_out_loop = false, timed_out_matrix = false, timed_out=false;

    public static void main(String[] args) {
        // write your code here

       MyBigInteger Aint = new MyBigInteger();
       MyBigInteger Bint = new MyBigInteger();
       Aint.value = "300";
       Bint.value = "10";
       Aint.value = Aint.plus(Bint);
        System.out.print(Aint.value + "\n");
        Aint.value = Aint.times(Bint);
        System.out.print(Aint.value + "\n");

        int N=2;
        System.out.printf("%-15s %s\n", "Cache", fibCache(N));
        System.out.printf("%-15s %s\n", "Loop", fibLoop(N));
        System.out.printf("%-15s %s\n", "Matrix", fibMatrix(N));



        int Iters = 500000, avg=5, Nval=0;
        long beforeTime, afterTime;


        long[] oldLTime= new long[Iters], oldMTime= new long[Iters];
        long lTime=0, mTime=0;
        String lPrefix = null, mPrefix = null;
        String n="N", tenX = "10x ratio", Tx="Expected Tx ratio", Tn="Expected Tn ratio",  time="Time", xchar="X", fibX = "fib(x)**";
        MyBigInteger loopVal= new MyBigInteger("0"), matrixVal= new MyBigInteger("0");
        double lDouble = 0, mDouble = 0;
        double meXDouble = 0, meNDouble = 0;
        long averageL, averageM;

        MyBigInteger x1 = new MyBigInteger();
        MyBigInteger x2 = new MyBigInteger();
        String solution, plusPrefix, multPrefix = null;
        String dRatio="Doubling", edRatio="Expected Double";
        long plusTime, multTime = 0;
        long oldPlusTime = 0, oldMultTime = 0;
        double mdouble = 0, pdouble, medouble = 0, pedouble;

        //====----Verification Tests for Arithmetic Algorithms----====
        System.out.printf("%65s %45s\n", "Plus", "Times");
        System.out.printf("%-10s %20s %20s %13s %10s %10s %7s %10s %10s\n", n, "x1", "x2", time, dRatio, edRatio, time, dRatio, edRatio);
        for(int x=1; x<Iters; x*=2)
        {
            x1.value = generateBigInt(x);
            x2.value = generateBigInt(x);

            //Plus

            beforeTime = getCpuTime();
            solution = x1.plus(x2);
            afterTime = getCpuTime();
            plusTime = afterTime - beforeTime;

            pdouble = (double)plusTime/(double)oldPlusTime;
            oldPlusTime = plusTime;
            pedouble = (double)x/(double)(x/2);

            plusPrefix = getSecondType(plusTime);
            plusTime = convertNanoSeconds(plusTime);


            //Mult


            if(!timed_out) {
                beforeTime = getCpuTime();
                solution = x1.times(x2);
                afterTime = getCpuTime();
                multTime = afterTime - beforeTime;

                if(multTime>= MAXTIME)
                    timed_out = true;

                mdouble = (double) multTime / (double) oldMultTime;
                oldMultTime = multTime;
                medouble = (double) (x * x) / (double) ((x / 2) * (x / 2));

                multPrefix = getSecondType(multTime);
                multTime = convertNanoSeconds(multTime);
            }
            //Print Results table


            System.out.printf("%-10d %20s %20s %10d %3s %10.2f %10.2f ", x, x1.AbbreviatedValue(), x2.AbbreviatedValue(), plusTime, plusPrefix, pdouble, pedouble);
            if(!timed_out)
                System.out.printf("%10d %3s %10.2f %10.2f\n",  multTime, multPrefix, mdouble, medouble);
            else
                System.out.printf("%10s", "Timed out\n");
        }


        //====----Verification Tests For Fibs----====

        System.out.printf("\n\n%40s %85s", "Loop", "Matrix\n");
        System.out.printf("%-5s %10s %15s %15s %20s %20s %15s %15s %15s %15s %20s %20s \n", n, xchar, fibX, time, tenX, Tx, Tn, fibX, time, tenX, Tx, Tn);

        for(int x=2; x<Iters; x++) {
            //============================
            //Time the algorithms
            //============================
            
            //Loop Fib
            if(!timed_out_loop) {
                averageL = 0;
                for(int i=0; i<avg; i++) {
                    //Loop
                    beforeTime = getCpuTime();
                    loopVal.value = fibLoop(x);
                    afterTime = getCpuTime();
                    lTime = afterTime - beforeTime;
                    averageL += lTime;
                    if (averageL >= MAXTIME)
                        timed_out_loop = true;
                }
                Nval = loopVal.value.length();
                averageL = averageL/avg; //get the average time it took
                    //Calculate 10x ratio
                lTime = averageL;
                    if (printIf(x)) {
                        lDouble = (double) lTime / oldLTime[x / 10];
                    }
                    oldLTime[x] = lTime;
                    lPrefix = getSecondType(lTime);
                    lTime = convertNanoSeconds(lTime);
            }

            //Matrix
            if(!timed_out_matrix) {
                averageM = 0;
                for(int i=0; i<avg; i++) {
                    beforeTime = getCpuTime();
                    matrixVal.value = fibMatrix(x);
                    afterTime = getCpuTime();
                    mTime = afterTime - beforeTime;
                    averageM += lTime;
                    if(averageM >= MAXTIME)
                        timed_out_matrix = true;
                }
                Nval = matrixVal.value.length();
                averageM = averageM / avg;
                mTime = averageM;
                    //Calculate the 10x ratio
                if(printIf(x)) {
                    mDouble = (double) mTime / oldMTime[x / 10];
                    meXDouble = (double)(Math.log(x)*(x*x))/ (double)(Math.log((double)x/10)*((x/10)*(x/10)));
                    int size = (int) (Math.log10(x)+1);
                    meNDouble=Math.pow(10, size);
                }
                
                oldMTime[x] = mTime;
                
                mPrefix = getSecondType(mTime);
                mTime = convertNanoSeconds(mTime);
            }
            //exit loop if both take too long to run
            if(timed_out_loop && timed_out_matrix)
                break;
            //Print results
            if(printIf(x)) {
                System.out.printf("%-5d %10d ", Nval, x);
                if(!timed_out_loop)
                {
                    //print out loop info
                    System.out.printf("%15s %12d %3s %15.4f %20.2f %20.2f", loopVal.AbbreviatedValue(), lTime, lPrefix, lDouble, (double)100, (double)100);
                                                            //Based on the calculations of the expected 10x ratios these would always be equal to 100
                }
                else
                {
                    //print out NA
                    System.out.printf("%20s %18s %20s", " ", "Timed out : NA", " ");
                }
                if(!timed_out_matrix)
                {
                    //print out matrix info
                    System.out.printf("%15s %12d %3s %15.4f %20.2f %20.2f\n", matrixVal.AbbreviatedValue(), mTime, mPrefix, mDouble, meXDouble, meNDouble);
                }
                else
                {
                    //print out NA
                    System.out.printf("%20s %18s %20s\n", " ", "Timed out : NA", " ");
                }
            }
        }

    }

    public static String fibCache(int N) {
        for(int i=0; i<=N; i++) {
            if (cache.size() > i) {
                cache.set(i, "0");
            }
            else
                cache.add("0");
        }
        return fibCacheHelper(N);
    }

    public static String fibCacheHelper(int val) {
        if(val==0||val==1)
            return Integer.toString(val);
        else if(!cache.get(val).equals("0"))
            return cache.get(val);
        else
        {
            //cache.set(val, (fibCacheHelper(val-1)+fibCacheHelper(val-2)));
            MyBigInteger Aint = new MyBigInteger();
            MyBigInteger Bint = new MyBigInteger();
            Aint.value = fibCacheHelper(val-1);
            Bint.value = fibCacheHelper(val-2);
            Aint.value = Aint.plus(Bint);
            cache.set(val, Aint.value);
            return cache.get(val);
        }

    }

    public static String fibLoop(int N){
        MyBigInteger next = new MyBigInteger("0");
        MyBigInteger A = new MyBigInteger("0");
        MyBigInteger B = new MyBigInteger("1");
        if(N < 2)
            return Integer.toString(N);
        for(int i=1; i<N; i++) {
            next.value = A.plus(B);
            A.value = B.value;
            B.value = next.value;
        }
        return next.value;
    }

    public static String fibMatrix(int N){
        String val;

        MyBigInteger[] As = new MyBigInteger[4];
        MyBigInteger[] Vs = new MyBigInteger[4];
        MyBigInteger[] t = new MyBigInteger[2];
        for(int i=0; i<4; i++)
        {
            if(i==3)
                Vs[i] = new MyBigInteger("0");
            else
                Vs[i] = new MyBigInteger("1");
            As[i] = new MyBigInteger();
            if(i<2)
                t[i] = new MyBigInteger();
        }
        Vs[0].value = "1";
        Vs[1].value = "1";
        Vs[2].value = "1";
        Vs[3].value = "0";

        String as=Vs[0].value, bs=Vs[1].value, cs=Vs[2].value, ds=Vs[3].value;

        for(int i=2; i<=N; i++) {
            //Matrix Multiply
            //A is temporary array
            As[0].value = as;
            As[1].value = bs;
            As[2].value = cs;
            As[3].value = ds;

            t[0].value = As[0].times(Vs[0]);
            t[1].value = As[1].times(Vs[2]);
            as = t[0].plus(t[1]);

            t[0].value = As[0].times(Vs[1]);
            bs = t[0].value;

            t[0].value = As[0].times(Vs[0]);
            cs = t[0].value;

            t[0].value = As[0].times(Vs[0]);
            ds = t[0].value;
        }
        val = As[0].value;
        return val;
    }

    //=========================================
    //Utilies
    //=========================================

    public static String generateBigInt(int N){
        StringBuilder genList = new StringBuilder(N);
        for(int i=0; i<N; i++)
            genList.insert(i, (int) (Math.random() * (10)));
        return genList.toString();
    }

    public static boolean printIf(int x) {
        return (x < 100 && x >= 10 && x % 10 == 0) || (x < 1000 && x >= 100 && x % 100 == 0) || (x < 10000 && x >= 1000 & x % 1000 == 0) || (x<100000 && x>=10000 &&x%10000==0);
    }

    public static String getSecondType (long time) {
        if((time/1000000000) > 60)
            return "m ";
        if(time >= 1000000000)
            return "s ";
        else if(time >= 1000000)
            return "µs";
        else if(time >= 1000)
            return "ms";
        else
            return "ns";
    }

    public static long convertNanoSeconds (long time) {
        long convertedTime=0;

        if(time >= 1000000000) {
            convertedTime = time / 1000000000;
            if(convertedTime>=60)   //Check if its a minute or more
                convertedTime = convertedTime/60;
        }
        else if(time >= 1000000)
            convertedTime = time / 1000000;
        else if(time >= 1000)
            convertedTime = time / 1000;
        else
            convertedTime = time;

        return convertedTime;
    }

    /* Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;

    }

}
