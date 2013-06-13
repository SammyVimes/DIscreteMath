package com.danilov.diofant;
import java.util.*;
import android.app.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import java.math.BigInteger;

import android.os.*;

public class Equation
{
     private int a;
	 private int b;
	 private int c;
	 private ArrayList<Column> steps = new ArrayList<Column>();
	 private Answer answer;
	 public boolean solveAble;
   
   public Equation(int  a, int  b, int c){
		 this.a = a;
		 this.b = b;
		 this.c = c;
	 }
	 
	 public boolean solve(){
		solveAble = true;
		int gcd = gcdThing(a,b);
		int tmp = c / gcd;
		if((c % gcd) != 0){
			solveAble = false;
			return false;
		}
		boolean isSecondMinus = false;
		boolean isFirstMinus = false;
		int i = 2;
		if(b < 0){
			isSecondMinus = true;
			b = -b;
		}
		if(a < 0){
			isFirstMinus = true;
			a = -a;
		}
		steps.add(new Column(a, 1, 0, 0));
		steps.add(new Column(b, 0, 1, 0)); 	
        int r;
		int q;
		int x;
		int y;
	   	do{
		   r = steps.get(i - 2) .r% steps.get(i - 1).r;
		   q = steps.get(i - 2) .r/ steps.get(i - 1).r;
		   x = steps.get(i - 2).x - steps.get(i - 1).x*q;
		   y = steps.get(i - 2).y - steps.get(i - 1).y*q;
		   i++;
		  steps.add(new Column(r, x, y, q));
	}while(r != 0);
	   	int secondModifier = 1;
		int firstModifier = 1;
	   	if(isSecondMinus){
	   		secondModifier = -1;
	   	}
	   	if(isFirstMinus){
	   		firstModifier = -1;
	   	}
        answer = new Answer(firstModifier*steps.get(i-2).x*tmp, secondModifier*steps.get(i-2).y*tmp);
		return true;
	
	 }
	 
	 private static int gcdThing(int a, int b) {
		    BigInteger b1 = new BigInteger(""+a); // there's a better way to do this. I forget.
		    BigInteger b2 = new BigInteger(""+b);
		    BigInteger gcd = b1.gcd(b2);
		    return gcd.intValue();
		}
	 
	 private class Column{
           public int x;
           public int y;
           public int q;
		   public int r;

		 private Column(int r, int x, int y, int q){
			 this.q = q;
			 this.x = x;
			 this.y = y;
			 this.r = r;
		 }
	 }
	 
	private class Answer{
		private int x;
		private int y;

		private Answer(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
	}
	
	public String getAnswer(){
		String ans = new String();
		if(solveAble){
			ans = new String("x0 = " + answer.getX() + "\ny0 = " + answer.getY());
		}else{
			ans = new String("Нет решений!");
		}
		return ans;
	}
	
	public ArrayList<String> getSteps(){
		ArrayList<String> allSteps = new ArrayList<String>();
		for(int i = 0; i < steps.size(); i++){
			int r = steps.get(i).r;
			int q = steps.get(i).q;
			int x = steps.get(i).x;
			int y = steps.get(i).y;
			String tmp = new String((i + 1) + ". r = " + r + "; q = " + q
					+ "; x = " + x + "; y = " + y);
			allSteps.add(tmp);
		}
		return allSteps;
	}
}
