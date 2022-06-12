package com.fingerchar.core.storage;

import com.fingerchar.core.storage.ipfsext.IpfsExt;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class IpfsStorage extends StorageAdaptor {
	
	private static final Log logger = LogFactory.getLog(IpfsStorage.class);
	
	private static String host;
	
	private static Integer port;
	
	private static Path localLocation;
	
	private static String requestBase;

	private static String basePath;
	
	private static String remoteService;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		IpfsStorage.host = host;
	}
	
	public void setLoclLocation(String localPath) {
		if(!localPath.startsWith("/")) {
			Path temp = Paths.get("");
			String curPath = temp.toFile().getAbsolutePath();
			curPath = curPath.substring(0, curPath.lastIndexOf(File.separator));
			localPath = curPath + File.separator + localPath;
		}
		IpfsStorage.localLocation = Paths.get(localPath);
        try {
            Files.createDirectories(localLocation);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

	public void setPort(Integer port) {
		IpfsStorage.port = port;
	}
	public void setRequestBase(String requestBase) {
		IpfsStorage.requestBase = requestBase;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		IpfsStorage.basePath = basePath;
	}
	
	public void setRemoteService(String remoteService) {
		IpfsStorage.remoteService = remoteService;
	}

	@Override
	public String store(InputStream inputStream, String fileName) {
		Path temp = localLocation.resolve(fileName);
		File localFile = temp.toFile();
		try {
			Files.copy(inputStream, temp, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Failed to store temp file " + fileName, e);
		}
		IpfsExt ipfs = new IpfsExt(host, port);
		NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(localFile);
		try {
			List<MerkleNode> list = ipfs.add(file);
			if(null != list && list.size() > 0) {
				String fileHash = list.get(0).hash.toString();
				String newName = fileHash + fileName.substring(fileName.lastIndexOf("."));
				localFile.renameTo(new File(localFile.getParent() + "/" + newName));
				//持久化
				if(!StringUtils.isEmpty(remoteService)) {
					ipfs.remotePin.add(list.get(0).hash, remoteService, newName, true);
				}
				return newName;
			}
			return null; 
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to store file to ipfs " + fileName, e);
		}
	}

	@Override
	public String[] store(InputStream[] inputStreams, String[] fileNames, String dirPath) {
		String basePath = localLocation.toFile().getPath();
		File localDir = new File(basePath + "/" + dirPath);
		if(!localDir.exists()) {
			localDir.mkdir();
		}
		int len = inputStreams.length;
		File[] files = new File[len];
		try {
			Path temp = null;
			for(int i=0; i<len; i++) {				
				temp = localLocation.resolve(dirPath + "/" + fileNames[i]);
				files[i] = temp.toFile();
				Files.copy(inputStreams[i], temp, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to store local file", e);
		}
		IpfsExt ipfs = new IpfsExt(host, port);
		try {
			NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(localDir);
			List<MerkleNode> list = ipfs.add(file);
			if(null != list && list.size() > 0) {
				String publicHash = null;
				for(int i=0; i<list.size(); i++) {
					if(dirPath.equals(list.get(i).name.get())) {
						publicHash = list.get(i).hash.toString();
						//持久化
						if(!StringUtils.isEmpty(remoteService)) {
							ipfs.remotePin.add(list.get(i).hash, remoteService, "file-" + publicHash, true);
						}
					}
				}
				localDir.renameTo(new File(basePath + "/" + publicHash));
				String[] ipfsFiles = new String[2];
				for(int i=0; i<len; i++) {
					String newName = publicHash + "/" + fileNames[i];
					ipfsFiles[i] = newName;
				}
				return ipfsFiles;
			}
			return null; 
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to store file to ipfs ", e);
		}
	}

	@Override
	public String generateUrl(String keyName) {
		if(IpfsStorage.requestBase.endsWith("/")) {			
			return IpfsStorage.requestBase + keyName;
		} else {
			return IpfsStorage.requestBase + "/" + keyName;
		}
	}
	
	public static String getIpfsData(String ipfsHash) {
		if(StringUtils.isEmpty(ipfsHash)){
			return null;
		}
		try {
			IpfsExt ipfs = new IpfsExt(host, port);
			Multihash filePointer =Multihash.fromBase58(ipfsHash);
			byte[] data = ipfs.cat(filePointer);
			return new String(data);
		} catch (IOException e) {
			logger.error("获取ipfs数据" + ipfsHash + "异常=>", e);
		}
		return null;
	}
}
