package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	int d2; // d2 is the position of the second duplicate in the words array
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
	
	private static  FallingWord[] words; //list of words
	private static int noWords; //how many
	private static Score score; //user score
	
	CatchWord(String typedWord) {
		target=typedWord;
	}
	
	public static void setWords(FallingWord[] wordList) {
		words=wordList;	
		noWords = words.length;
	}
	
	public static void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	// method to check if there is another word in the array of words that matches the target other the one we found
	public Boolean checkDuplicates(int p){
	 for(int r=p+1;r<noWords;r++){
		 if(words[r].matchWord(target)){
			 d2=r;
			 return true;
		 }
		 else{
			 continue;
		 }
	 }
	 return false;
	}

	/** Method to check which of the two words is lower
	 It returns the word with the lowest position.*/
	public FallingWord lowerPos(FallingWord word1,FallingWord word2){
		int y1=word1.getY();
		int y2=word2.getY();

		if(y1>y2){
			return word1;
		}
		else if(y1==y2){
			return word1;
		}
		else{
			return word2;
		}

	}

	
	public void run() {
		int i=0;
		while (i<noWords) {		
			while(pause.get()) {};
			if (words[i].matchWord(target)) {

				// check if we have duplicate words
				if((checkDuplicates(i))==true){

					FallingWord wordToRemove=lowerPos(words[i],words[d2]);
					wordToRemove.resetWord(); // reset the position of the word at the lowest position
				}
				else{
					words[i].resetWord();
				}

				System.out.println( " score! '" + target); //for checking
				score.caughtWord(target.length());	
				//FallingWord.increaseSpeed();
				break;
			}
		   i++;
		}
		
	}	
}
