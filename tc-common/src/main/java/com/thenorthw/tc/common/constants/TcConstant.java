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
package com.thenorthw.tc.common.constants;

/**
 * Created by LeeAutumn on 19/12/2016.
 * blog: thenorthw.com
 *
 * @author LeeAutumn
 */
public class TcConstant {
    //http header
    public static String TOKEN_HEADER                 =       "X-token";

    //remoting
    public static String LINE_SEPARATOR               =       System.getProperty("line.separator");


    //Http
    public static String HTTP_REQUEST_ID_HEADER       =        "X-request-id";
    public static String HTTP_REQUEST_URL_HEADER      =        "X-request-url";

    //jwt
    public static String ACCOUNT_ID                   =         "accountId";

}
