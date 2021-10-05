import os
import time
from subprocess import Popen, PIPE,check_output
import socket
import re
import threading
from datetime import datetime

# import pandas as pd

lock = threading.RLock()    # 创建锁
datalock = 0

shutdownstr="akjsdfuewirmzxcvhbreith32434"
shutdownabordstr="aafsdfaw342fq23qfea32thgeyu567iii"

DESTINATION_IP = "192.168.0.106"
DESTINATION_PORT = 51235

def udpthreading(n):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    # 绑定端口:
    s.bind(('192.168.0.108', 51234))
    # 创建Socket时，SOCK_DGRAM指定了这个Socket的类型是UDP。绑定端口和TCP一样，但是不需要调用listen()
    # 方法，而是直接接收来自任何客户端的数据：
    print('Bind UDP on 1234...')
    while True:
        # 接收数据:
        data, addr = s.recvfrom(1024)
        lock.acquire()
        strdata=data.decode("utf-8")
        print('Received from %s:data is %s.' % addr,strdata)

        if(shutdownstr==strdata):
            windowexcut='will shutdown'
            # os.system("shutdown /l")
        elif (shutdownabordstr==strdata):
            windowexcut='shutdown abord '
        else:
            print("not match")


        # time.sleep(2)
        # s.sendto(b'Hello, %s!\n' % data, addr)
        # s.sendto(b'close it!\n', addr)

        # payload_hex_string = "68656c6c6f5f776f726c64"  # hello_world
        # payload = bytes.fromhex(payload_hex_string)

        # payload=bytes.fromhex("windows close")

        date=datetime.today().strftime(("%Y-%m-%d %H:%M:%S"))
        # date=pd.to_datetime('today')


        meesage=windowexcut+" at: "+date

        payload=meesage.encode()

        # send out the packet

        s.sendto(payload, (DESTINATION_IP, DESTINATION_PORT))

        refind = re.findall("(.*?)", strdata)
        if refind:
            # for m in refind:
            print(refind)

        if(shutdownstr==strdata):
            os.system("shutdown /s")
            print("shutdownstr")
            # os.system("shutdown /l")
        elif (shutdownabordstr==strdata):
            print("shutdownabordstr")
            os.system("shutdown /a")
        datalock=1
        lock.release()

if __name__ == "__main__":
    t2 = threading.Thread(target=udpthreading, args=("thread 2",))
    t2.start()


exit()
shutdown = input("Do you wish to shutdown your computer ? (yes / no): ")

if shutdown == 'no':
    exit()
else:
    os.system("shutdown /a")
    time.sleep(1)
    os.system("shutdown /s /t 6666")