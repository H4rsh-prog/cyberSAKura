package com.tools.module.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.tools.module.model.ByteArrayWrapper;


@Service
public class DictionaryService {
	private int maxBytesUsed = 1;
	private boolean verbose = false;
	private int dictionaryLimit = 254;
	private int additionalMarkerBytes = 1;
	
	public ArrayList<ByteArrayWrapper> createDictionary(byte[] byteArr) {
		HashMap<ByteArrayWrapper, Integer> frequencyTable = new HashMap<>();
		HashMap<ByteArrayWrapper, Integer> temp_frequencyTable = new HashMap<>();
		HashSet<Byte> potentialBlockStart = new HashSet<Byte>();
		int byteArrSize = byteArr.length;
		int i;
		for(i=0;i<100000 && i<byteArrSize;i++) {
			if(this.verbose) System.out.print("FINDING REPETITION PROGRESS : ["+i+"/"+byteArrSize+"]\r");
			if(potentialBlockStart.contains(byteArr[i])) {
				ByteArrayWrapper dataBlock = findByteBlock(byteArr, temp_frequencyTable, i);
				temp_frequencyTable.put(dataBlock, temp_frequencyTable.getOrDefault(dataBlock, 0)+1);
				i += dataBlock.getData().length-1;
			} else {
				potentialBlockStart.add(byteArr[i]);
			}
		}
		if(this.verbose) System.out.print("COMMITING FIRST REPETITION BATCH... \r");
		for(Entry<ByteArrayWrapper, Integer> e: temp_frequencyTable.entrySet()) {
			frequencyTable.put(e.getKey(), e.getValue());
		}
		temp_frequencyTable.clear();
		while(i<byteArrSize) {
			int k;
			for(k=i;k<i+100000 && k<byteArrSize;k++) {
				if(this.verbose) System.out.print("FINDING REPETITION PROGRESS : ["+k+"/"+byteArrSize+"]\r");
				if(potentialBlockStart.contains(byteArr[k])) {
					ByteArrayWrapper dataBlock = findByteBlock(byteArr, temp_frequencyTable, k);
					temp_frequencyTable.put(dataBlock, Math.max(frequencyTable.getOrDefault(dataBlock, 0), temp_frequencyTable.getOrDefault(dataBlock, 0))+1);
					k += dataBlock.getData().length-1;
				} else {
					potentialBlockStart.add(byteArr[k]);
				}
			}
			if(this.verbose) System.out.print("COMMITING REPETITION BATCH NO. ["+(int)k%100000+"] \r");
			for(Entry<ByteArrayWrapper, Integer> e: temp_frequencyTable.entrySet()) {
				frequencyTable.put(e.getKey(), frequencyTable.getOrDefault(e.getKey(), 0)+e.getValue());
			}
			temp_frequencyTable.clear();
			i=k;
		}
		int preLength = frequencyTable.size();
		for(ByteArrayWrapper invalidKeys : frequencyTable.keySet().stream().filter(new Predicate<ByteArrayWrapper>() {
			@Override
			public boolean test(ByteArrayWrapper t) {
				return frequencyTable.get(t)==1;
			}
		}).toList()) {
			frequencyTable.remove(invalidKeys);
		}
		int postLength = frequencyTable.size();
		if(verbose) System.out.println("TABLE REDUCED BY ["+(preLength-postLength)+"] ENTRIES AFTER FILTERING NON REPEATING BLOCKS NOW WITH REMAINING ENTRIES : "+frequencyTable.size());
		preLength = postLength;
		if(preLength>255) {
			if(preLength>Math.pow(2, 16)) {
				if(preLength>Math.pow(2, 24)) {
					this.maxBytesUsed = 4;
				} else {
					this.maxBytesUsed = 3;
				}
			} else {
				this.maxBytesUsed = 2;
			}
		} else {
			this.maxBytesUsed = 1;
		}
		this.dictionaryLimit = (int) Math.pow(255, this.maxBytesUsed);
		if(this.verbose) System.out.println("SETTING DELIMITER TO ["+this.maxBytesUsed+":"+this.dictionaryLimit+"] BYTES WITH THE FREQUENCY TABLE ENTRIES EXCEEDING ["+preLength+"]");
		for(ByteArrayWrapper invalidKeys : frequencyTable.keySet().stream().filter(new Predicate<ByteArrayWrapper>() {
			@Override
			public boolean test(ByteArrayWrapper t) {
				return t.getData().length<=(maxBytesUsed+additionalMarkerBytes);
			}
		}).toList()) {
			frequencyTable.remove(invalidKeys);
		}
		postLength = frequencyTable.size();
		if(this.verbose) System.out.println("TABLE REDUCED BY ["+(preLength-postLength)+"] ENTRIES AFTER FILTERING BLOCKS LARGER THAN DICTIONARY LIMIT NOW REMAINING ENTRIES : "+frequencyTable.size());
		return generateSortedBytesFromFrequency(frequencyTable);
	}
	private ByteArrayWrapper findByteBlock(byte[] byteArr, HashMap<ByteArrayWrapper, Integer> frequencyTable, int startIndx) {
		Set<ByteArrayWrapper> repeatingBlocks = frequencyTable.keySet();
		byte[] start_byteArr = new byte[1];
		start_byteArr[0] = byteArr[startIndx];
		ByteArrayWrapper dataBlock = new ByteArrayWrapper(ByteBuffer.wrap(start_byteArr));
		int byteArrSize = byteArr.length;
		for(int i=startIndx+1;i<byteArrSize;i++) {
			if(repeatingBlocks.contains(dataBlock)) {
				frequencyTable.put(dataBlock, frequencyTable.getOrDefault(dataBlock, 0)+1);
				start_byteArr = dataBlock.getData();
				ByteBuffer newBuffer = ByteBuffer.allocate(i-startIndx+1);
				newBuffer.put(0, start_byteArr);
				newBuffer.put(i-startIndx, byteArr[i]);
				newBuffer.rewind();
				dataBlock.updateArray(newBuffer);
				continue;
			}
			break;
		}
		return dataBlock;
	}
	private ArrayList<ByteArrayWrapper> generateSortedBytesFromFrequency(HashMap<ByteArrayWrapper, Integer> frequencyTable) {
		ArrayList<ByteArrayWrapper> sortedBytes = new ArrayList<>();
		sortedBytes.addAll(frequencyTable.keySet());
		sortedBytes.sort(new Comparator<ByteArrayWrapper>() {
			@Override
			public int compare(ByteArrayWrapper o1, ByteArrayWrapper o2) {
				if(o2.getData().length!=o1.getData().length) return o2.getData().length-o1.getData().length;
				return frequencyTable.get(o2).intValue()-frequencyTable.get(o1).intValue();
			}
		});
		return new ArrayList<>(sortedBytes.subList(0, Math.min(this.dictionaryLimit, sortedBytes.size())));	//LIMIT ENTRIES;
	}

}
