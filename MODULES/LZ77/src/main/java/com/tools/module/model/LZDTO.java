package com.tools.module.model;

import java.util.ArrayList;

public record LZDTO(
			byte[] data,
			ArrayList<byte[]> dictionary,
			ArrayList<Integer> indiceList
		) {}
