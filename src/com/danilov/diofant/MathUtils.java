package com.danilov.diofant;

public class MathUtils {
	
	public static int getComparisonByModule(int modifiedX, int module, int equals){
		if(modifiedX == 0 && equals != 0){
			return -1;
		}
		if(equals == 0){
			return 0;
		}
		int result = 0;
		boolean flag = true;
		int i = 0;
		while(flag){
			int tmp = (modifiedX * i) % module;
			if(tmp == equals){
				result = i;
				flag = false;
			}
			i++;
		}
		return result;
	}
	
	public static int getModForNonPositive(int val, int module){
		int result = val;
		while(result < 0){
			result = module + result;
		}
		return result;
	}
}
