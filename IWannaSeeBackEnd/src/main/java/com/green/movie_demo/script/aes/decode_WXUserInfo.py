from WXBizDataCrypt import WXBizDataCrypt
import sys

def main():
    appId, sessionKey, encryptedData, iv = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
    
    pc = WXBizDataCrypt(appId, sessionKey)

    decryptedData = pc.decrypt(encryptedData, iv)
    print(decryptedData)

if __name__ == '__main__':
    main()
