package com.tools.module.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerializationService {
	
	public byte[] serializeDictionary(ArrayList<byte[]> dictionary) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			for(byte[] element : dictionary) {
				baos.write(VarIntegerBytesParser.encode(element.length));
				baos.write(element);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	public ArrayList<byte[]> deserializeDictionary(byte[] bytes) {
		ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
		int i = 0;
		int size = bytes.length;
		for(int j=i;j<size;j++) {
			if((bytes[j] & 0x80) == 0) {
				byte[] buffer = Arrays.copyOfRange(bytes, i, j+1);
				int length = VarIntegerBytesParser.decode(buffer);
				i = j+length+1;
				buffer = Arrays.copyOfRange(bytes, j+1, i);
				dictionary.add(buffer);
				j += length;
			}
		}
		return dictionary;
	}
}
