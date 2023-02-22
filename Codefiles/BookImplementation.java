package com.anttijuustila.tira;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;

import java.nio.charset.StandardCharsets;





public class BookImplementation implements Book{
    String [] top100;
    int [] top100luvut;
    public String kirja;
    public String turhat;
    int uniqueCount = 0;
    static final int MAX_WORD_LEN = 100;
    int koko = 150000;
    String [] ignoreArray = new String[50];
    WordCount [] uniqueWordsHashTable = new WordCount[koko];
    int totalCount = 0;
    int ignoreCount = 0;
    int ignoreosumat = 0;
    int collisions = 0;

    @Override
    public void setSource(String fileName, String ignoreWordsFile) throws FileNotFoundException {
        File kirjafile = new File(fileName);  
        if(kirjafile.exists() == false){
            throw new FileNotFoundException();
        }
        File ignorefile = new File(ignoreWordsFile);
        if(ignorefile.exists() == false){
            throw new FileNotFoundException();
        }
        else{
            kirja = fileName;
            turhat = ignoreWordsFile;
        }      
    }

    @Override
    public void countUniqueWords() throws IOException, OutOfMemoryError {
        int i = 0;
        int c;
        int x = 0;
        FileReader lukija = new FileReader(kirja, StandardCharsets.UTF_8);
        FileReader ignore = new FileReader(turhat, StandardCharsets.UTF_8);
        int[] array = new int[MAX_WORD_LEN];
       
        while((c = ignore.read()) != -1){
            if(Character.isLetter(c)){
                array[i] = c;
                i++;
            }
            else{
                String sana = new String(array, 0, i);
                sana = sana.toLowerCase();
                
                ignoreArray[x] = sana;
                x++;
                ignoreCount++;
                i = 0;
            }
        }
        i = 0;
        while((c = lukija.read())!= -1){
            if(Character.isLetter(c)){
                array[i] = c;
                i++;
            }
            else if(i == 1){
                i = 0;
            }
            else if(i > 1){
                x = 0;
                String sana = new String(array, 0, i);
                sana = sana.toLowerCase();
                int hash = Hashfunktio(sana, uniqueWordsHashTable.length, x);
    

                for(int t = 0; t < ignoreArray.length; t++){
                    if(sana.equals(ignoreArray[t])){
                        ignoreosumat++;
                        break;
                    }
                    else if(!sana.equals(ignoreArray[t]) && t == ignoreArray.length -1){
                        while(uniqueWordsHashTable[hash] != null && !sana.equals(uniqueWordsHashTable[hash].word)){
                            collisions++;
                            x++;
                            hash = Hashfunktio(sana, uniqueWordsHashTable.length, x);
                        }
                        if(uniqueWordsHashTable[hash] == null){
                            uniqueWordsHashTable[hash] = new WordCount();
                            uniqueWordsHashTable[hash].word = sana;
                        }
                        uniqueWordsHashTable[hash].count++;
                        totalCount++;
                    }
                }   
                i = 0;
                }
            }
        
    lukija.close();
    ignore.close();       
  
}

    @Override
    public void report() {
        nullitpois();
        quicksort(uniqueWordsHashTable, 0, uniqueWordsHashTable.length -1);
        if(uniqueCount < 100){
            for(int i = 0; i < uniqueCount; i++){
                if(uniqueWordsHashTable[i] != null){
                    System.out.printf("%d. %s, %d\n",i+1, uniqueWordsHashTable[i].word, uniqueWordsHashTable[i].count);
                }
            }
        }
        else{
            for(int i = 0; i < 100; i++){
                if(uniqueWordsHashTable[i] != null){
                    System.out.printf("%d. %s, %d\n", i+1, uniqueWordsHashTable[i].word, uniqueWordsHashTable[i].count);
                }
            }
        }
        System.out.printf("Total words in file: %d\n", totalCount); 
        System.out.printf("Unique words in file: %d\n", uniqueCount);
        System.out.printf("Ignored words count: %d\n", ignoreCount); 
        System.out.printf("Ignored words in book: %d\n", ignoreosumat);
        System.out.printf("Collisions: %d\n", collisions);

    }

    @Override
    public void close() {
        
    }

    @Override
    public int getUniqueWordCount() {
        // TODO Auto-generated method stub
        return uniqueCount;
    }

    @Override
    public int getTotalWordCount() {
        return totalCount;
    }

    @Override
    public String getWordInListAt(int position) {
        if(position >= totalCount){
            return null;
        }
        else{
            return uniqueWordsHashTable[position].word;
        }
    }

    @Override
    public int getWordCountInListAt(int position) {
        if(position >= totalCount || position < 0){
            return -1;
        }
        else{
            return uniqueWordsHashTable[position].count;
        }
    }
    public int Hashfunktio (String sana, int koko, int x){
        int hash = 31;
        for(int i = 0; i < sana.length(); i++){
            hash = (hash * 31) + sana.charAt(i);
        }
        hash = (hash + x & 0x7fffffff) % koko;
        return hash;
    }
    private void quicksort(WordCount[] uniqueWordsHashTable2, int low, int high){
        
        int [] stacki = new int[high-1+1];
        int top = -1;
        stacki[++top] = low;
        stacki[++top] = high;
        while(top >= 0){
            high = stacki[top--];
            low = stacki[top--];
            int p = partition(uniqueWordsHashTable2, low, high);
            if(p-1 > low){
                stacki[++top] = low;
                stacki[++top] = p-1;
            }
            if(p+1 < high){
                stacki[++top] = p+1;
                stacki[++top] = high;
            }
        }
     }
     private int partition(WordCount[] uniqueWordsHashTable2, int low, int high){
        int x = uniqueWordsHashTable2[high].count;
        int i = low - 1;
        for(int j = low; j < high; j++){
           if(uniqueWordsHashTable2[j].count >= x){
              i += 1;
              WordCount temp = uniqueWordsHashTable2[i];
              uniqueWordsHashTable2[i] = uniqueWordsHashTable2[j];
              uniqueWordsHashTable2[j] = temp;
           }
        }
        WordCount temp = uniqueWordsHashTable2[i+1];
        uniqueWordsHashTable2[i + 1] = uniqueWordsHashTable2[high];
        uniqueWordsHashTable2[high] = temp;

        return i + 1;
     }
     private void nullitpois(){
        WordCount[] uusiArraySana = new WordCount [uniqueWordsHashTable.length];
        int j = 0;
        for(int i = 0; i < koko; i++){
            if(uniqueWordsHashTable[i] != null){
                uusiArraySana[j] = uniqueWordsHashTable[i];
                j++;
            }
        }
        WordCount[] nullpoistosana = new WordCount[j];
        for(int i = 0; i < j; i++){
            nullpoistosana[i] = uusiArraySana[i];
        }
        uniqueCount = j;
        uniqueWordsHashTable = nullpoistosana;
     }
}

class WordCount {
    String word;
    int count = 0;
 }