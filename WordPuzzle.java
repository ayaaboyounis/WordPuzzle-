package il.ac.tau.cs.sw1.ex4;
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WordPuzzle 
{
	public static final char HIDDEN_CHAR = '_';
	public static final int MAX_VOCABULARY_SIZE = 3000;

	
	/*
	 * @pre: template is legal for word
	 */
	public static char[] createPuzzleFromTemplate(String word, boolean[] template)
	{ // Q - 1
		char [] puzzle=new char[template.length];
		for (int i=0;i<template.length;i++)
		{
			if(template[i]==true)
			{
				puzzle[i]=HIDDEN_CHAR;	
			}
			else
			{
				puzzle[i]=word.charAt(i);
			}
		}
		return puzzle;
	}

	public static boolean checkLegalTemplate(String word, boolean[] template)
	{
		if(word.length()!=template.length)
			return false;
		int crt_true=0;
		int crt_false=0;
		
		for(int i=0;i<template.length;i++)
		{
			if(template[i]==true) crt_true++;
			else crt_false++;
		}
		if(crt_true==0 || crt_false==0) return false;
		
		for(int i=0;i<template.length;i++)//check this if
		{
			char alpha=word.charAt(i);
			boolean curr=template[i];
			for(int j=i+1;j<template.length;j++)
			{
				if(word.charAt(j)==alpha)
				{
					if(template[j]!=curr)
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/*
	 * @pre: 0 < k < word.lenght(), word.length() <= 10
	 */
	public static boolean[][] getAllLegalTemplates(String word, int k)
	{  
		final int n = word.length();
		int crtlegal=0;
		boolean [][] alltemplates=new boolean[(int)Math.pow(2, n)][(int)Math.pow(2, n)];
		
	    for (int i = 0; i < Math.pow(2, n); i++)
	    {
	    	String bin = Integer.toBinaryString(i);
	    	while (bin.length() < n)
	    		bin = "0" + bin;
	    	char[] chars = bin.toCharArray();
	        boolean[] boolArray = new boolean[n];
	        for (int j = 0; j < chars.length; j++)
	        {
	        	boolArray[j] = chars[j] == '0' ? true : false;
	        }
	        int crt=0;
	        for(int j=0;j<n;j++)
	        {
	        	if(boolArray[j]==true)
	        		crt++;
	        }
	        if(crt==k)
	        {
	        	if(checkLegalTemplate(word, boolArray))
	        	{
	        		for(int c=0;c<n;c++)
	        		{
	        			alltemplates[crtlegal][c]=boolArray[c];
	        		}
	        		
	        		crtlegal++;
	        	}
	        		
	        		
	        }
	    }
	    boolean[][] finaltemp=new boolean[crtlegal][n];
	    for(int o=crtlegal-1;o>=0;o--)
	    {
	    	for(int z=0;z<n;z++)
	    		{
	    		finaltemp[o][z]=alltemplates[Math.abs(o-(crtlegal-1))][z];
	    		}
	    }
	    
	   
	    return finaltemp;
	    
	}
	
	
	/*
	 * @pre: puzzle is a legal puzzle constructed from word, guess is in [a...z]
	 */
	public static int applyGuess(char guess, String word, char[] puzzle)
	{ 
		int crt=0;
		for(int i=0;i<puzzle.length;i++) 
		{
			if(word.charAt(i)==guess)
			{
				if(puzzle[i]=='_')
				{
					puzzle[i]=guess;
					crt++;
				}
			}
		}
		return crt;
	}
	

	/*
	 * @pre: puzzle is a legal puzzle constructed from word
	 * @pre: puzzle contains at least one hidden character. 
	 * @pre: there are at least 2 letters that don't appear in word, and the user didn't guess
	 */
	public static char[] getHint(String word, char[] puzzle, boolean[] already_guessed)
	{ 
		char[]alphabet="abcdefghijklmnopqurstuvwxyz".toCharArray();
		char[] hint=new char[2];
		Random rand=new Random();
		int rand1=rand.nextInt(word.length());
		
		while(puzzle[rand1]!=HIDDEN_CHAR)
		{
			rand1=rand.nextInt(word.length());
		}
		
		int rand2=rand.nextInt(26);
		char alpha2=alphabet[rand2];
		
		while(word.indexOf(alpha2)!=-1 || already_guessed[rand2])
		{
			rand2=rand.nextInt(26);
			alpha2=alphabet[rand2];
		}
		
		if(word.charAt(rand1)<alpha2)
		{
			hint[0]=word.charAt(rand1);
			hint[1]=alpha2;
		}
		else
		{
			hint[0]=alpha2;
			hint[1]=word.charAt(rand1);
		}
		return hint;
	}

	

	public static char[] mainTemplateSettings(String word, Scanner inputScanner)
	{ // Q - 6
		printSettingsMessage();
		
		
		boolean flag=false;
		while(!flag)
		{
			printSelectTemplate();
			int onetwo=inputScanner.nextInt();
			if(onetwo==1)
			{
				printSelectNumberOfHiddenChars();
				int NumOfhiddenChars=inputScanner.nextInt();
				boolean[][] check=getAllLegalTemplates(word,NumOfhiddenChars);
				if(check.length==0)
					printWrongTemplateParameters();
				else
				{
					Random rand=new Random();
					int rand1=rand.nextInt(check.length);
					return createPuzzleFromTemplate(word,check[rand1]);
				}
			}
			if(onetwo==2)
			{
				printEnterPuzzleTemplate();
				String TemplateInput=inputScanner.next();
				String[] TemplateSplit=TemplateInput.split(",");
				boolean[] TemplateSplitBool=new boolean[TemplateSplit.length];
				String myHidChar = Character.toString(HIDDEN_CHAR);
				for(int i=0;i<TemplateSplit.length;i++)
				{
					if(TemplateSplit[i]==myHidChar)
					{
						TemplateSplitBool[i]=true;
					}
					else TemplateSplitBool[i]=false;
					
				}
				
				if(checkLegalTemplate(word,TemplateSplitBool))
				{
					char[] puzzlecurr=createPuzzleFromTemplate(word,TemplateSplitBool);
					return puzzlecurr;
				}
			
				else 
				{
					printWrongTemplateParameters();
					
				}
			}	
		}
		return null;
	}
	
	public static void mainGame(String word, char[] puzzle, Scanner inputScanner)
	{ 
		boolean[] already_guessed=new boolean[26];
		for(int x=0;x<26;x++)
			already_guessed[x]=false;

		boolean []booltamplate=new boolean[puzzle.length];
		int Crthidden=3;
		
		for (int j=0;j<puzzle.length;j++)
		{
			char currchar=word.charAt(j);
			for(int x=j;x<puzzle.length;x++)
			{
				if(puzzle[x]==currchar)
					booltamplate[x]=true;
				if(puzzle[x]==HIDDEN_CHAR&&booltamplate[x]==false)
				{
					Crthidden++;
					booltamplate[x]=true;
				}
				
			}
		}
		
		printSettingsMessage();
		for(int a=0;a<Crthidden;a++)
		{
			 printEnterYourGuessMessage();
			 char azH=inputScanner.next().charAt(0);
			 if(azH=='H')
			 {
				 char[] hintarr=new char[2];
				 hintarr=getHint(word, puzzle,already_guessed);
				 printHint(hintarr);
				 a--;
				 continue;
			 }
			 int azHI=(int)(azH);
			 if(azHI>=97||azHI<=122)
			 {
				 already_guessed[azHI-97]=true;
				 if(word.indexOf(azHI)!=-1)
				 {
					 if(puzzle[word.indexOf(azHI)]==HIDDEN_CHAR)
					 {
						 int cnt=0;
						 for(int b=0;b<puzzle.length;b++)
						 {
							 if(word.charAt(b)==azH)
							 {
								 puzzle[b]=azH;
							 }
							 if(puzzle[b]!=HIDDEN_CHAR)
							 {
								 cnt++;
							 }
						 }
					 
						 if(cnt==word.length())
						 {
							 printWinMessage();
							 break;
						 }
						 else printCorrectGuess(Crthidden-a-1);
						 if(Crthidden-a-1==0)
						 {
							 printGameOver();
							 break;
						 }
							 
					 }	 
				 }
				 else
				 {
					 printWrongGuess(Crthidden-a-1);
					 if(Crthidden-a-1==0)
					 {
						 printGameOver();
						 break;
					 }
					 
				 }
			
			 }
		}
			

	}
				
				


/*************************************************************/
/********************* Don't change this ********************/
/*************************************************************/

	public static void main(String[] args) throws Exception 
	{ 
		if (args.length < 1){
			throw new Exception("You must specify one argument to this program");
		}
		String wordForPuzzle = args[0].toLowerCase();
		if (wordForPuzzle.length() > 10){
			throw new Exception("The word should not contain more than 10 characters");
		}
		Scanner inputScanner = new Scanner(System.in);
		char[] puzzle = mainTemplateSettings(wordForPuzzle, inputScanner);
		mainGame(wordForPuzzle, puzzle, inputScanner);
		inputScanner.close();
	}


	public static void printSettingsMessage() 
	{
		System.out.println("--- Settings stage ---");
	}

	public static void printEnterWord()
	{
		System.out.println("Enter word:");
	}
	
	public static void printSelectNumberOfHiddenChars()
	{
		System.out.println("Enter number of hidden characters:");
	}
	public static void printSelectTemplate()
	{
		System.out.println("Choose a (1) random or (2) manual template:");
	}
	
	public static void printWrongTemplateParameters()
	{
		System.out.println("Cannot generate puzzle, try again.");
	}
	
	public static void printEnterPuzzleTemplate()
	{
		System.out.println("Enter your puzzle template:");
	}


	public static void printPuzzle(char[] puzzle)
	{
		System.out.println(puzzle);
	}


	public static void printGameStageMessage() {
		System.out.println("--- Game stage ---");
	}

	public static void printEnterYourGuessMessage() {
		System.out.println("Enter your guess:");
	}

	public static void printHint(char[] hist){
		System.out.println(String.format("Here's a hint for you: choose either %s or %s.", hist[0] ,hist[1]));

	}
	public static void printCorrectGuess(int attemptsNum) {
		System.out.println("Correct Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWrongGuess(int attemptsNum) {
		System.out.println("Wrong Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWinMessage() {
		System.out.println("Congratulations! You solved the puzzle!");
	}

	public static void printGameOver() {
		System.out.println("Game over!");
	}

}
