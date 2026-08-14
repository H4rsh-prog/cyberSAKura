package com.tools.module.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.tools.module.model.ByteArrayWrapper;

public class DriverService {
	@Autowired DictionaryService dictionaryService;
	@Autowired CompressionService compressionService;
	
	public byte[] compressSinglePhase(byte[] data) {
		ArrayList<ByteArrayWrapper> dictionary = this.dictionaryService.createDictionary(data);
		byte[] compressedData = this.compressionService.compressData(dictionary, data);
		return new byte[0];
	}
}
