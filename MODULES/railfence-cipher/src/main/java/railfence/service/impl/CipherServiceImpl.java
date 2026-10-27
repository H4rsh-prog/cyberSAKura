package railfence.service.impl;

import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Service;

import railfence.service.CipherService;

@Service 
public class CipherServiceImpl implements CipherService {

    @Override
    public String encode(String plainText, int key) {
        sanitize(plainText, key);
        StringBuilder[] rails = new StringBuilder[key];
        for(int i=0; i<key; i++) {
            rails[i] = new StringBuilder();
        }
        
        int row = 0; int step = 1;
        for(char c : plainText.toCharArray()) {
            if(key == (row+step) || -1 == (row+step)) step = (step == 1) ? -1 : 1;
            rails[row].append(c);
            row += step;
        }

        StringBuilder response = new StringBuilder();
        for(StringBuilder rail : rails) {
            response.append(rail.toString());
        }
        return response.toString();
    }

    @Override
    public String decode(String cipherText, int key) {
        sanitize(cipherText, key);
        StringBuilder[] rails = new StringBuilder[key];
        int[] railIndex = new int[key];

        HashMap<Integer, Integer> railFrequency = new HashMap<>();
        int row = 0; int step = 1;
        for(int i=cipherText.length();i>0;i--) {
            if(key == (row+step) || -1 == (row+step)) step = (step == 1) ? -1 : 1;
            railFrequency.put(row, railFrequency.getOrDefault(row, 0)+1);
            row += step;
        }

        int r_split = 0; int l_split = 0;
        for(int i=0; i<key; i++) {
            r_split = railFrequency.get(i);
            rails[i] = new StringBuilder(cipherText.substring(l_split, l_split + r_split));
            railIndex[i] = 0;
            l_split = l_split + r_split;
        }
        
        StringBuilder response = new StringBuilder();
        row = 0; step = 1;
        for(int i=cipherText.length();i>0;i--) {
            if(key == (row+step) || -1 == (row+step)) step = (step == 1) ? -1 : 1;
            response.append(rails[row].charAt(railIndex[row]++));
            row += step;
        }

        return response.toString();
    }

    private void sanitize(String text, int key) {
        if (Objects.requireNonNull(text, "Null-Check Failed [Input Text Cannot Be NULL]").isBlank())
            throw new IllegalArgumentException("Blank-Check Failed [Input Text Cannot Be Blank]");
        if (1 >= key || key >= text.length())
            throw new IllegalArgumentException("Invalid Key Scope [Enter a key between 2 and length(request_text)]");
    }
}
