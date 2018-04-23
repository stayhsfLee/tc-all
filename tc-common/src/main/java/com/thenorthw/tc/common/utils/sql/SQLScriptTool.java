package com.thenorthw.tc.common.utils.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theNorthW on 05/04/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class SQLScriptTool {
    private static Logger logger = LoggerFactory.getLogger(SQLScriptTool.class);

    public static List<String> getSQLSetences(String filepath){
        File file = new File(filepath);
        List<String> setences = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String temp;
            StringBuilder stringBuilder = new StringBuilder("");

            while ((temp = bufferedReader.readLine()) != null){
                int t = temp.indexOf("--");
                if(t>=0){
                    temp = temp.substring(0,t);
                }

                if(temp.indexOf(";") < 0){
                    //clear zhushi

                    if(temp.isEmpty()){
                        continue;
                    }
                    stringBuilder.append(temp);
                }else{
                    stringBuilder.append(temp);
                    setences.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder("");
                }
            }
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return setences;
    }
}

