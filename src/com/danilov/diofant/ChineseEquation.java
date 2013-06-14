package com.danilov.diofant;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;

@SuppressLint("UseValueOf")
public class ChineseEquation {
	
	private ArrayList<ModuleEquation> equations = new ArrayList<ModuleEquation>();
	private HashMap<String, Variable> variables = new HashMap<String, Variable>();
	private int innerVariableCount = 0;
	private boolean noAnswer = false;
	
	public ChineseEquation(ArrayList<String> strings){
		int len = strings.size() / 3;
		for(int i = 0; i < len; i++){
			int first = (i * 3) + 0;
			int second = (i * 3) + 1;
			int third = (i * 3) + 2;
			ModuleEquation me = new ModuleEquation(strings.get(first),
					strings.get(second), strings.get(third));
			equations.add(me);
		}
	}
	
	
	private class Variable{
		String innerVariableName = "";
		int coeff = 0;
		int freeMember = 0; //Aah, variable names
		
		public Variable(String varName, int coeff, int freeMember){
			this.innerVariableName = varName;
			this.coeff = coeff;
			this.freeMember = freeMember;
		}
		
		public String toString(){
			String str = coeff + "*" + innerVariableName + " + " + freeMember;
			return str;
		}
	}
			
		
	private class ModuleEquation{
		int xCoeff;
		int module;
		int equals;
		
		public ModuleEquation(String xCoeff, String module, String equals){
			this.xCoeff = new Integer(xCoeff);
			this.module = new Integer(module);
			this.equals = new Integer(equals);
		}
		
		public void setXCoeff(int val){
			xCoeff = val;
		}
		
		public void setEquals(int val){
			equals = val;
		}
		
		public int getXCoeff(){
			return xCoeff;
		}
		
		public int getModule(){
			return module;
		}
		
		public int getEquals(){
			return equals;
		}
		
		public String toString(){
			return xCoeff + "*x = " + equals + " (mod" + module + ")" ;
		}
	}
	
	public void solve(){
		ModuleEquation me = equations.get(0);
		innerVariableCount++;
		Variable first = new Variable("x" + innerVariableCount, me.getModule(), me.getEquals());
		variables.put("x0", first);
		Variable curVariable = first;
		for(int i = 1; i < equations.size(); i++){
			for(int j = i; j < equations.size(); j++){
				me = equations.get(j);
				int prevXCoeff = me.getXCoeff();
				int newXCoeff = prevXCoeff*curVariable.coeff;
				if(newXCoeff < 0){
					newXCoeff = MathUtils.getModForNonPositive(newXCoeff, me.module);
				}
				if(newXCoeff > 0){
					newXCoeff = newXCoeff % me.module;
				}
				me.setXCoeff(newXCoeff);
				int equals = me.getEquals() - prevXCoeff*curVariable.freeMember;
				if(equals < 0){
					equals = MathUtils.getModForNonPositive(equals, me.module);
				}
				if(equals > 0){
					equals = equals % me.module;
				}
				me.setEquals(equals);
			}
			innerVariableCount++;
			me = equations.get(i);
			int freeMember = MathUtils.getComparisonByModule(me.getXCoeff(), me.getModule(), me.getEquals());
			if(freeMember == -1){
				noAnswer = true;
				break;
			}
			curVariable = new Variable("x" + innerVariableCount, me.getModule(), freeMember);
			variables.put("x" + i, curVariable);
		}
	}
	
	public String getAnswer(){
		if(noAnswer){
			return new String("Нет решений!");
		}
		String answer = "";
		String tmpAnswer = "";
		int size = variables.size() - 1;
		int num = size;
		String tmp = variables.get("x" + num).toString();
		for(int i = 0; i < size; i++){
			num = size - i;
			tmpAnswer = variables.get("x" + (num - 1)).toString();
			answer = tmpAnswer.replace("x" + num, "("+ tmp +")");
			tmp = answer;
		}
		answer = "x = " + answer;
		return answer;
	}
}
