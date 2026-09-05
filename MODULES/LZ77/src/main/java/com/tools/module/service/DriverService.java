package com.tools.module.service;

import java.util.ArrayList;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import com.tools.module.model.ByteArrayWrapper;

public class DriverService {
	@Autowired DictionaryService dictionaryService;
	@Autowired CompressionService compressionService;
	
	public byte[] compressSinglePhase(byte[] data) {
		ArrayList<byte[]> dictionary = (ArrayList<byte[]>) this.dictionaryService.createDictionary(data).stream().map(new Function<ByteArrayWrapper, byte[]>() {
			@Override
			public byte[] apply(ByteArrayWrapper t) {
				return t.getData();
			}
		});
		byte[] compressedData = this.compressionService.compressData(dictionary, data);
		return new byte[0];
	}
}
