package typingTutor;

import typingTutor.FallingWord;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class HungryWordMover extends Thread {

    private FallingWord worD;
    private AtomicBoolean done;
    private AtomicBoolean pause;
    private Score score;
    CountDownLatch startLatch;
    HungryWordMover ( FallingWord word) {
        worD = word;
    }
    HungryWordMover( FallingWord word,WordDictionary dict, Score score,
               CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
        //this(worD);
        this.worD=word;
        this.startLatch = startLatch;
        this.score=score;
        this.done=d;
        this.pause=p;
    }

    public void run() {

        System.out.println(worD.getWord() + " falling speed = " + worD.getSpeed());
        try {
            System.out.println(worD.getWord() + " waiting to start " );
            startLatch.await();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } //wait for other threads to start
        System.out.println(worD.getWord() + " started" );
        while (!done.get()) {
            //animate the word
            while (!worD.dropped() && !done.get()) {
                worD.moveHor(10);

                //System.out.println(worD);
                try {
                    sleep(worD.getSpeed());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };
                while(pause.get()&&!done.get()) {};
            }
            if (!done.get() && worD.dropped()) {
                score.missedWord();
                worD.resetWord();
            }
            worD.resetWord();
        }
    }

    public FallingWord getWorD() {
        return worD;
    }
}