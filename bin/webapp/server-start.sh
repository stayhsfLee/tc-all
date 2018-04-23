#!/usr/bin/env bash
#if [[ "admin" != $(/usr/bin/whoami) ]]; then
#    echo "only executable under admin user, exiting";
#    exit 1;
#fi

MS_VERSION='1.0.0'

SCRIPT_NAME=$0;
BIN_DIR=`dirname ${SCRIPT_NAME}`;
MS_HOME=$(cd ${BIN_DIR}/..; pwd);
MAIN_CLASS="com.thenorthw.tc.web.Main"
CLASSPATH=".:${BIN_DIR}:${MS_HOME}/lib/*:${MS_HOME}/tc-web-${MS_VERSION}.jar"

PID_FILE="${MS_HOME}/.tcserver.pid"

START_COMMAND="java -classpath ${CLASSPATH}  ${MAIN_CLASS}"

function printCopyright()
{
    copyrightInfo="TeamCoding Server ${MS_VERSION}, From theNorthW.com! \n"
    echo -e ${copyrightInfo}
}

function printDebugInfo()
{
    HOSTNAME=`hostname`;
    IP=`ping ${HOSTNAME} -c 1 | grep PING | awk '{len=length($3)-2;print substr($3,2,len);}'` ||IP="Unknown local ip"
    echo "debug mode, localIp:"${IP}" port:1818"
}

function usage()
{
    echo "Usage: `basename $0` [-d] -j "jvm parameters""
    exit 1
}

function is_sigle()
{
    if [ -f ${PID_FILE} ]; then
        old_pid=`cat ${PID_FILE}`;
        if [ ! -z "${old_pid}" ]; then
            pids=`ps aux | grep "${MAIN_CLASS}" | grep java | grep -v grep | awk '{print $2}'`;
            for pid in ${pids}; do
                if [ "${pid}" == "${old_pid}" ]; then
                    echo "process is running as ${pid}, please stop it first.";
                    exit 0;
                fi
            done
        fi
    fi
}

function after_start()
{
    new_pid=$1
    pids=`ps aux | grep "${MAIN_CLASS}" | grep java | grep -v grep | awk '{print $2}'`;
    num=${#pids[@]}
    if [ ${num} -le 0 ]; then
        echo "TeamCoding Server Starts Failed!"
        exit 1
    elif [ ${num} -eq 1 ]; then
        ps_pid=${pids[0]}
        if [ "${new_pid}" == "${ps_pid}" ]; then
            echo "TeamCoding Server Starts Success! Pid[${new_pid}]"
            exit 0
        else
            echo "some error, start pid[${new_pid}], but running pid[${ps_pid}]"
            echo "TeamCoding Server Starts Failed!"
            exit 1
        fi
    else
        pid_str="";
        for pid in ${array[*]}; do
            if [ "${pid_str}" == "" ]; then
                pid_str=${pid}
            else
               pid_str="${pid_str},${pid}"
            fi
        done
        echo "There are more than 1 running process[${pid_str}], please check!"
        echo "TeamCoding Server Starts Failed!"
        exit 1
    fi
}

## start
printCopyright;
cd ..
PWD=$(pwd)
PWDA=$(echo ${PWD//\//\\/})
sed -i "s/jetty.descriptor=.*/jetty.descriptor=${PWDA}\/webapp\/WEB-INF\/web.xml/g" jetty-config.properties
cd -

while getopts dj: OPTION; do
    case ${OPTION} in
        d)
            printDebugInfo;
            DEFAULT_JVM=${DEFAULT_JVM}" "${REMOTE_DEBUG_CONFIG}
            START_COMMAND="java -server ${DEFAULT_JVM} ${DEFAULT_PROPERTY_CONF} -classpath ${CLASS_PATH} ${MAIN_CLASS}"
            ;;
        j)
            DEFAULT_JVM=${DEFAULT_JVM}" "${OPTARG}
            START_COMMAND="java -server ${DEFAULT_JVM} ${DEFAULT_PROPERTY_CONF} -classpath ${CLASS_PATH} ${MAIN_CLASS}"
            ;;
        \?)
            usage
            ;;
    esac
done

if [ -z ${JAVA_HOME} ]; then
    echo "can not getFile JAVA_HOME, please set it first. jdk1.7 is recommended!"
    echo "TeamCoding Server Starts Failed!"
    exit 1
elif [ ! -d ${JAVA_HOME} ]; then
    echo "can not getFile JAVA_HOME[${JAVA_HOME}]"
    echo "TeamCoding Server Starts Failed!"
    exit 1
else
    echo "getFile JAVA_HOME: "${JAVA_HOME}
fi

# 判断是否已有程序启动
is_sigle;

# 启动程序
echo "START_COMMAND: "${START_COMMAND}
# 回到jar包所在层级
cd ..
nohup ${START_COMMAND} >./nohup.out 2>&1 &
if [ $? -ne 0 ]; then
    echo "TeamCoding Server Starts Failed!"
    exit 1
fi

# 无论启动失败与否，先touch pid文件
echo "echo pid";
echo $! > ${PID_FILE};

sleep 10

# 判断启动是否成功
after_start $!