/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thenorthw.tc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by theNorthW on 30/12/2016.
 * blog: thenorthw.com
 *
 * @author theNorthW
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties getProperties(String filename){
        Map<String,String> result = new HashMap<String, String>();

        File file = new File(filename);

        if(!file.exists()){
            logger.error("Properties file : {} not exists, please check your classpath configure.",filename);
        }

        System.out.println(file.getAbsolutePath());

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(file));

        } catch (FileNotFoundException e) {
            logger.error("Properties file : {} not exists, please check your classpath configure.",filename);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Properties file : {} read error",filename);
            e.printStackTrace();
        }

        return properties;
    }
}
