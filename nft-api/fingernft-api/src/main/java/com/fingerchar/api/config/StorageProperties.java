package com.fingerchar.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fc.storage")
public class StorageProperties {
    private String active;
    private Local local;
    private Ipfs ipfs;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Ipfs getIpfs() {
		return ipfs;
	}

	public void setIpfs(Ipfs ipfs) {
		this.ipfs = ipfs;
	}

	public static class Local {
        private String address;
        private String storagePath;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }
    }
    
    public static class Ipfs {
    	private String host;
    	private Integer port;
    	private String localPath;
    	private String requestBase;
    	private String remoteService;
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		public String getLocalPath() {
			return localPath;
		}
		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}
		public String getRequestBase() {
			return requestBase;
		}
		public void setRequestBase(String requestBase) {
			this.requestBase = requestBase;
		}
		public String getRemoteService() {
			return remoteService;
		}
		public void setRemoteService(String remoteService) {
			this.remoteService = remoteService;
		}
    }
}
