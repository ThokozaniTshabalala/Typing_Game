package typingTutor;

import typingTutor.FallingWord;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class HungryWordMover extends Thread {

    private FallingWord worD;
    private FallingWord[] words;
    private AtomicBoolean done;
    private AtomicBoolean pause;
    private Score score;
    CountDownLatch startLatch;
    HungryWordMover ( FallingWord word) {
        worD = word;
    }


    HungryWordMover( FallingWord word,WordDictionary dict, Score score,
               CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p, FallingWord[] wordSS) {
        //this(worD);
        this.worD=word;
        this.startLatch = startLatch;
        this.score=score;
        this.done=d;
        this.pause=p;
        words=wordSS;
    }
    public void setWordList(FallingWord[] wordls){
        words=wordls;

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
                //worD.moveHor(10);
                //HungryDeletion();

                if(worD.isGreen()){
                    worD.moveHor(10);
                    for(int i = 0 ; i<words.length; i++){
                    if(!words[i].isGreen())
                    {
                        int y1=worD.getY(); int y2=words[i].getY();
                        //System.out.println("y1 = "+y1+" y2 = "+y2);
                        if(y1==y2)
                        {
                            int refVal=worD.getX()+(worD.getWord().length());
                            int valOther=words[i].getX()+words[i].getWord().length();
                            System.out.println("refVal = "+refVal+" and valOther = "+valOther);
                            int val=refVal-valOther;
                            int val2=valOther-refVal;
                            //int val=worD.getX()+(worD.getWord().length())-words[i].getX();
                            //int val2=words[i].getWord().length()-worD.getX() +words[i].getX();
                            System.out.println("for the word = "+words[i].getWord());
                            System.out.println("val = "+val);
                            System.out.println("val2 = "+val2);
                            if( val>0 && val<valOther)
                            {
                                System.out.println("for the word = "+words[i].getWord()+"| There should be deletion");
                                score.missedWord();
                                words[i].resetWord();
                            }
                            else if(val2>0 && val2<refVal)
                            {
                                System.out.println("There should be deletion");
                                score.missedWord();
                                words[i].resetWord();
                            }
                        }
                    }
                    }
                }


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
