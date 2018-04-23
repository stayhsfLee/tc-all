#!/usr/bin/env bash
#if [[ "admin" != $(/usr/bin/whoami) ]]; then
#    echo "only executable under admin user, exiting";
#    exit 1;
#fi

SCRIPT_NAME=$0;
BIN_DIR=`dirname ${SCRIPT_NAME}`;
MS_HOME=$(cd ${BIN_DIR}/..; pwd);
MAIN_CLASS="com.thenorthw.tc.web.Main"

PID_FILE="${MS_HOME}/.tcserver.pid"

function is_alive()
{
    old_pid=`cat ${PID_FILE}`;
    pids=`ps aux | grep ${MAIN_CLASS} | grep java | grep -v grep | awk '{print $2}'`;
    for pid in ${pids}; do
        if [ "${pid}" == "${old_pid}" ]; then
            return 1
        fi
    done
    return 0
}

function stop()
{
    (is_alive)
    if [ $? == 0 ] ; then
        return 0
    fi

    old_pid=`cat ${PID_FILE}`;
    echo "discover pid：${PID_FILE}";
    kill $1 $old_pid;

    (is_alive)
    if [ $? == 0 ] ; then
        return 0
    fi

    return 1
}


# 不存在pidFile，表明已经停止
if [ ! -f ${PID_FILE} ]; then
    echo "pid file ${PID_FILE} not found";
    exit 0;
fi

# 循环3次kill
iter=0
while [[ $iter -lt 3 ]]; do
    stop
    if [ $? == 0 ] ; then
        echo "TeamCoding Server Process Stopped.";
        rm -rf $PID_FILE;
        exit 0;
    fi

    sleep 1
    (( iter=iter+1 ))
done

echo "use \"kill -9\" to kill"
(stop -9)
sleep 1

