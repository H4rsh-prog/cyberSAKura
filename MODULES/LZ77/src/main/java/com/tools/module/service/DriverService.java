package com.tools.module.service;

import java.util.ArrayList;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import com.tools.module.model.ByteArrayWrapper;
import com.tools.module.model.LZDTO;

public class DriverService {
	@Autowired DictionaryService dictionaryService;
	@Autowired CompressionService compressor;
	@Autowired DecompressionService decompressor;
	
	public LZDTO compressSinglePhase(byte[] data) {
		ArrayList<byte[]> dictionary = (ArrayList<byte[]>) this.dictionaryService.createDictionaryDAC(data).stream().map(new Function<ByteArrayWrapper, byte[]>() {
			@Override
			public byte[] apply(ByteArrayWrapper t) {
				return t.getData();
			}
		});
		LZDTO compressedData = this.compressor.compressData(dictionary, data);
		return compressedData;
	}
	
	public byte[] decompressSinglePhase(LZDTO compressedData) {
		return this.decompressor.decompressData(compressedData);
	}
}
