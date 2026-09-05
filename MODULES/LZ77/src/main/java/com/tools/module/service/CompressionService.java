package com.tools.module.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CompressionService {
	private HashMap<Integer, byte[]> cache_intToBytes = new HashMap<Integer, byte[]>();
	
	public byte[] compressData(ArrayList<byte[]> dictionary, byte[] data) {
		ArrayList<Integer> indiceList = new ArrayList<Integer>();
		int dictionarySize = dictionary.size();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int index = buffer.position()-1;
		while(buffer.remaining()>0) {
			for(int i=0;i<dictionarySize;i++) {
				byte[] query = dictionary.get(i);
				int searchFieldSize = Math.min(query.length, buffer.remaining());
				byte[] searchField = new byte[searchFieldSize];
				buffer.get(index, searchField);
				if(Arrays.equals(query, searchField)) {
					byte[] parsedDictionaryIndex = intToBytes(i);
					buffer = compressBuffer(buffer.array(), index, query.length, parsedDictionaryIndex);
					indiceList.add(index);
					index += parsedDictionaryIndex.length;
					break;
				}
			}
			index++;
			buffer.position(index);
		}
		return buffer.array();
	}
	public ByteBuffer compressBuffer(byte[] originalBuffer, int placeholderIndex, int queryOffset, byte[] replacementBytes) {
		byte[] Lbytes, RBytes, MBytes;
		int originalBufferSize = originalBuffer.length;
		MBytes = RLEBytes(replacementBytes);
		int compressedBytesSize = MBytes.length;
		Lbytes = Arrays.copyOfRange(originalBuffer, 0, placeholderIndex);
		RBytes = Arrays.copyOfRange(originalBuffer, placeholderIndex+queryOffset, originalBufferSize);
		ByteBuffer buffer = ByteBuffer.allocate(originalBufferSize+(compressedBytesSize-queryOffset));
		buffer.put(0, Lbytes);
		buffer.put(placeholderIndex, MBytes);
		buffer.put(placeholderIndex+compressedBytesSize, RBytes);
		return buffer;
	}
	public byte[] RLEBytes(byte[] bytes) {
		// This method is Runtime Length Encoding the bytes by left padding the byte array with its length
		int byteSize = bytes.length;
		if(byteSize>254) throw new RuntimeException("Byte Length Overflow");
		ByteBuffer buffer = ByteBuffer.allocate(byteSize+1);
		buffer.put(0, intToBytes(byteSize)[0]);
		buffer.put(1, bytes);
		buffer.rewind();
		return buffer.array();
	}
	private byte[] intToBytes(int intVal) {
		if(this.cache_intToBytes.containsKey(intVal)) return this.cache_intToBytes.get(intVal);
		if(intVal==0) {
			byte[] result = new byte[] {0x0};
			this.cache_intToBytes.put(intVal, result);
			return result;
		}
		// calculating byte length
		String byteString = Integer.toBinaryString(intVal | Integer.MAX_VALUE+1);
		int len = byteString.length();
		char[] binCharArr = byteString.toCharArray();
		for(int i=1;i<len;i++) {
			if(binCharArr[i]=='0') continue;
			byteString = byteString.substring(i);
			break;
		}
		len = ceilDiv(byteString.length(),8);
		//populating bytes
		byte[] result = new byte[len];
		for(int i=len-1;i>=0;i--) {
			result[i] = (byte) (intVal & 0xFF);
			intVal>>=8;
			/*
			 * (intVal & 0xFF) extracts the last 8 bits of the integer and assigns them to i'th index of result
			 * meanwhile intVal>>=8 right shifts those 8 bits such that they are not present for the next iteration
			 * i.e intVal = 01011001 00001001
			 * 01011001 00001001 & 00000000 11111111 = 00000000 00001001
			 * intVal = 01011001 00001001 >> 8 = 00000000 01011001
			 * */
		}
		this.cache_intToBytes.put(intVal, result);
		return result;
	}
	// FOR SOME REASON MATH.CEILDIV IS THROWING AN UNRESOLVED EXCEEPTION AS IT COULD NOT FIND IT
	int ceilDiv(int x, int y) {
        final int q = x / y;
        if ((x ^ y) >= 0 && (q * y != x)) {
            return q + 1;
        }
        return q;
    }
}
