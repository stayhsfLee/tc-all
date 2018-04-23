package com.thenorthw.tc.common.utils.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @autuor theNorthW
 * @date 19/08/2017.
 * blog: thenorthw.com
 */
public class JsonComparator {
	private Map<String,String> diffMapInJson1 = new HashMap<String, String>();
	private Map<String,String> diffMapInJson2 = new HashMap<String, String>();
	private Map<String,String> sameMap = new HashMap<String, String>();


	public  void compareJson(String json1, String json2,String parentKey){
		innerCompareJson(json1,json2,parentKey);

		//输出到当前文件夹下的diffInJson1.json
		//输出到当前文件夹下的diffInJson2.json
		outputToFile();
	}

	/**
	 * 目前针对两层树结构进行比较
	 * @param json1 需要被比较的json
	 * @param json2 标准的json
	 */
	private void innerCompareJson(String json1, String json2,String parentKey){
		String sameFilePath = "same.txt";
		String diffFilePath = "diff.txt";

		JsonObject jsonObject1 = (JsonObject)new JsonParser().parse(json1);
		JsonObject jsonObject2 = (JsonObject)new JsonParser().parse(json2);

		Set<Entry<String,JsonElement>> entrySet1 = jsonObject1.entrySet();
		Set<Entry<String,JsonElement>> entrySet2 = jsonObject2.entrySet();

		//将json1,json2转化成k-v Map
		Entry<String,JsonElement>[] entryArra1 = new Entry[entrySet1.size()];
		Entry<String,JsonElement>[] entryArra2 = new Entry[entrySet2.size()];
		entrySet1.toArray(entryArra1);
		entrySet2.toArray(entryArra2);
		Map<String,JsonElement> map1 = new HashMap<String, JsonElement>(16);
		Map<String,JsonElement> map2 = new HashMap<String, JsonElement>(16);

		for(Entry<String,JsonElement> e1 : entryArra1){
			map1.put(e1.getKey(), e1.getValue());
		}
		for(Entry<String,JsonElement> e1 : entryArra2){
			map2.put(e1.getKey(), e1.getValue());
		}

		//TODO  万一json2有的key json1没有，这种情况也要考虑
		//找出json2中多余的key
		for(Entry<String,JsonElement> e2 : entryArra2){
			String currentKey = parentKey.equals("") ? e2.getKey() : parentKey + "$" + e2.getKey();
			if(!map1.containsKey(e2.getKey())){
				outputDiff2(currentKey,"Json2中有的而Json1中没有的key",e2.getValue().toString());
			}
		}

		//开始循环迭代比较
		for(Entry<String,JsonElement> e : entrySet1){
			String currentKey = parentKey.equals("") ? e.getKey() : parentKey + "$" + e.getKey();

			//判断json2中有没有相关key - 此处要注意json没有顺序性
			if(!map2.containsKey(e.getKey())){
				//直接输出diff
				outputDiff(currentKey,e.getValue().toString(),"Json1中有的而Json2中没有的key");
			}else{
				//进一步比较实质内容
				String j1 = e.getValue().toString();
				String j2 = map2.get(e.getKey()).toString();

				if(j1.startsWith("{") && j2.startsWith("{")) {
					innerCompareJson(j1, j2, currentKey);
				}else{
					if(j1.equals(j2)){
						outputSame(currentKey,j1,j2);
					}else{
						outputDiff(currentKey,j1,j2);
					}
				}
			}
		}
	}

	private void outputDiff(String key,String s1,String s2){
		diffMapInJson1.put(key,s1 + "-----" +s2.replaceAll("\"",""));
	}

	private void outputDiff2(String key,String s1,String s2){
		diffMapInJson2.put(key,s1 + "-----" +s2.replaceAll("\"",""));
	}

	private void outputSame(String key,String s1,String s2){
		sameMap.put(key,s1 + "-----" +s2);
	}

	private static int compareTwoString(String s1,String s2){
		int len1 = s1.length();
		int len2 = s2.length();
		int lim = Math.min(len1, len2);
		char[] cs1 = s1.toCharArray();
		char[] cs2 = s2.toCharArray();

		for(int i=0;i<lim;i++){
			if(cs1[i] != cs2[i]){
				return i;
			}
		}

		return lim;
	}

	private void outputToFile(){
		File file1 = new File("diffInJson1.json");
		File file2 = new File("diffInJson2.json");

		Gson gson = new Gson();

		PrintWriter printWriter1 = null;
		PrintWriter printWriter2 = null;

		try {
			if (!file1.exists()) {
				file1.createNewFile();
				System.out.println(file1.getAbsolutePath());
			}
			if (file2.exists()) {
				file2.createNewFile();
			}

			printWriter1 = new PrintWriter(file1);
			printWriter2 = new PrintWriter(file2);

			printWriter1.print(gson.toJson(diffMapInJson1));
			printWriter2.print(gson.toJson(diffMapInJson2));
		}catch (IOException io){
			System.out.println(io);
		}finally {
			printWriter1.close();
			printWriter2.close();
		}
	}
}
