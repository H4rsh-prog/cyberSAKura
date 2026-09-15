package com.tools.module.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.bouncycastle.util.Arrays;

import com.tools.module.model.LZDTO;


public class DecompressionService {
	private HashMap<byte[], Integer> cache_bytesToInt = new HashMap<byte[], Integer>();
	
	public byte[] decompressData(LZDTO compressedData) {
		byte[] data = compressedData.data();
		ArrayList<byte[]> dictionary = compressedData.dictionary();
		ArrayList<Integer> indiceList = compressedData.indiceList();
		byte[] bucket;
		for(int i=indiceList.size()-1;i>=0;i--) {
			bucket = new byte[1];
			int placementIndex = indiceList.get(i);
			bucket[0] = data[placementIndex];
			int byteLength = bytesToInt(bucket);
			bucket = new byte[byteLength];
			bucket = Arrays.copyOfRange(data, placementIndex+1, placementIndex+byteLength+1);
			int dictionaryIndex = bytesToInt(bucket);
			data = decompressBytes(data, dictionary, placementIndex, dictionaryIndex);
		}
		return data;
	}
	private byte[] decompressBytes(byte[] data, ArrayList<byte[]> dictionary, int placementIndex, int dictionaryIndex) {
		return new byte[0];
	}
	private int bytesToInt(byte[] bytes) {
		if(this.cache_bytesToInt.containsKey(bytes)) return this.cache_bytesToInt.get(bytes);
		int intVal = 0;
		for(byte b: bytes) {
			intVal = (intVal<<8) | (b&0xFF);
			/*
			 * (intVal<<8) left-shifts the integer bytes 8 bits so that 00000000 00001001 essentially becomes 00001001 00000000
			 * meanwhile (b&0xFF) ensures only the last 8 bits (0xFF) are used such that 11100010 00001001 becomes 00000000 00001001
			 * lastly the bitwise-OR (|) operator appends the last 8 bits extracted from "b" to the 8 bit left-shifted space in intVal
			 * 		i.e 00001001 00000000 (OR) 00000000 00001001 = 00001001 00001001
			 * */
		}
		this.cache_bytesToInt.put(bytes, intVal);
		return intVal;
	}
}
