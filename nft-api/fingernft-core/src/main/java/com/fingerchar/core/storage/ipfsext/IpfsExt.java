package com.fingerchar.core.storage.ipfsext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.ipfs.api.JSONParser;
import io.ipfs.api.KeyInfo;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.api.Peer;
import io.ipfs.api.Version;
import io.ipfs.cid.Cid;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

public class IpfsExt {
	    public static final Version MIN_VERSION = Version.parse("0.4.11");
	    public enum PinType {all, direct, indirect, recursive}
	    public List<String> ObjectTemplates = Arrays.asList("unixfs-dir");
	    public List<String> ObjectPatchTypes = Arrays.asList("add-link", "rm-link", "set-data", "append-data");
	    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000;
	    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 60_000;

	    public final String host;
	    public final int port;
	    public final String protocol;
	    private final String version;
	    private final int connectTimeoutMillis;
	    private final int readTimeoutMillis;
	    public final Key key = new Key();
	    public final Pin pin = new Pin();
	    public final RemotePin remotePin = new RemotePin();
	    public final Repo repo = new Repo();
	    public final IPFSObject object = new IPFSObject();
	    public final Swarm swarm = new Swarm();
	    public final Bootstrap bootstrap = new Bootstrap();
	    public final Block block = new Block();
	    public final Dag dag = new Dag();
	    public final Diag diag = new Diag();
	    public final Config config = new Config();
	    public final Refs refs = new Refs();
	    public final Update update = new Update();
	    public final DHT dht = new DHT();
	    public final File file = new File();
	    public final Stats stats = new Stats();
	    public final Name name = new Name();
	    public final Pubsub pubsub = new Pubsub();

	    public IpfsExt(String host, int port) {
	        this(host, port, "/api/v0/", false);
	    }

	    public IpfsExt(String multiaddr) {
	        this(new MultiAddress(multiaddr));
	    }

	    public IpfsExt(MultiAddress addr) {
	        this(addr.getHost(), addr.getTCPPort(), "/api/v0/", detectSSL(addr));
	    }

	    public IpfsExt(String host, int port, String version, boolean ssl) {
	        this(host, port, version, DEFAULT_CONNECT_TIMEOUT_MILLIS, DEFAULT_READ_TIMEOUT_MILLIS, ssl);
	    }

	    public IpfsExt(String host, int port, String version, int connectTimeoutMillis, int readTimeoutMillis, boolean ssl) {
	        if (connectTimeoutMillis < 0) throw new IllegalArgumentException("connect timeout must be zero or positive");
	        if (readTimeoutMillis < 0) throw new IllegalArgumentException("read timeout must be zero or positive");
	        this.host = host;
	        this.port = port;
	        this.connectTimeoutMillis = connectTimeoutMillis;
	        this.readTimeoutMillis = readTimeoutMillis;

	        if (ssl) {
	            this.protocol = "https";
	        } else {
	            this.protocol = "http";
	        }

	        this.version = version;
	        // Check IPFS is sufficiently recent
	        try {
	            Version detected = Version.parse(version());
	            if (detected.isBefore(MIN_VERSION))
	                throw new IllegalStateException("You need to use a more recent version of IPFS! >= " + MIN_VERSION);
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    /**
	     * Configure a HTTP client timeout
	     * @param timeout (default 0: infinite timeout)
	     * @return current IPFS object with configured timeout
	     */
	    public IpfsExt timeout(int timeout) {
	        return new IpfsExt(host, port, version, connectTimeoutMillis, readTimeoutMillis, protocol.equals("https"));
	    }

	    public List<MerkleNode> add(NamedStreamable file) throws IOException {
	        return add(file, false);
	    }

	    public List<MerkleNode> add(NamedStreamable file, boolean wrap) throws IOException {
	        return add(file, wrap, false);
	    }

	    public List<MerkleNode> add(NamedStreamable file, boolean wrap, boolean hashOnly) throws IOException {
	        return add(Collections.singletonList(file), wrap, hashOnly);
	    }

	    @SuppressWarnings("unchecked")
		public List<MerkleNode> add(List<NamedStreamable> files, boolean wrap, boolean hashOnly) throws IOException {
	        MultipartExt m = new MultipartExt(protocol + "://" + host + ":" + port + version + "add?stream-channels=true&w="+wrap + "&n="+hashOnly, "UTF-8");
	        for (NamedStreamable file: files) {
	            if (file.isDirectory()) {
	                m.addSubtree(Paths.get(""), file);
	            } else
	                m.addFilePart("file", Paths.get(""), file);
	        };
	        String res = m.finish();
	        return JSONParser.parseStream(res).stream()
	                .map(x -> MerkleNode.fromJSON((Map<String, Object>) x))
	                .collect(Collectors.toList());
	    }

	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public List<MerkleNode> ls(Multihash hash) throws IOException {
	        Map reply = retrieveMap("ls?arg=" + hash);
	        return ((List<Object>) reply.get("Objects"))
	                .stream()
	                .flatMap(x -> ((List<Object>)((Map) x).get("Links"))
	                        .stream()
	                        .map(MerkleNode::fromJSON))
	                .collect(Collectors.toList());
	    }

	    public byte[] cat(Multihash hash) throws IOException {
	        return retrieve("cat?arg=" + hash);
	    }

	    public byte[] cat(Multihash hash, String subPath) throws IOException {
	        return retrieve("cat?arg=" + hash + URLEncoder.encode(subPath, "UTF-8"));
	    }

	    public byte[] get(Multihash hash) throws IOException {
	        return retrieve("get?arg=" + hash);
	    }

	    public InputStream catStream(Multihash hash) throws IOException {
	        return retrieveStream("cat?arg=" + hash);
	    }

	    @SuppressWarnings("rawtypes")
		public List<Multihash> refs(Multihash hash, boolean recursive) throws IOException {
	        String jsonStream = new String(retrieve("refs?arg=" + hash + "&r=" + recursive));
	        return JSONParser.parseStream(jsonStream).stream()
	                .map(m -> (String) (((Map) m).get("Ref")))
	                .map(Cid::decode)
	                .collect(Collectors.toList());
	    }

	    @SuppressWarnings("rawtypes")
		public Map resolve(String scheme, Multihash hash, boolean recursive) throws IOException {
	        return retrieveMap("resolve?arg=/" + scheme+"/"+hash +"&r="+recursive);
	    }


	    @SuppressWarnings("rawtypes")
		public String dns(String domain, boolean recursive) throws IOException {
	        Map res = retrieveMap("dns?arg=" + domain + "&r=" + recursive);
	        return (String)res.get("Path");
	    }

	    @SuppressWarnings("rawtypes")
		public Map mount(java.io.File ipfsRoot, java.io.File ipnsRoot) throws IOException {
	        if (ipfsRoot != null && !ipfsRoot.exists())
	            ipfsRoot.mkdirs();
	        if (ipnsRoot != null && !ipnsRoot.exists())
	            ipnsRoot.mkdirs();
	        return (Map)retrieveAndParse("mount?arg=" + (ipfsRoot != null ? ipfsRoot.getPath() : "/ipfs" ) + "&arg=" +
	                (ipnsRoot != null ? ipnsRoot.getPath() : "/ipns" ));
	    }

	    // level 2 commands
	    public class Refs {
	        @SuppressWarnings("rawtypes")
			public List<Multihash> local() throws IOException {
	            String jsonStream = new String(retrieve("refs/local"));
	            return JSONParser.parseStream(jsonStream).stream()
	                    .map(m -> (String) (((Map) m).get("Ref")))
	                    .map(Cid::decode)
	                    .collect(Collectors.toList());
	        }
	    }

	    /* Pinning an object ensures a local copy of it is kept.
	     */
	    public class Pin {
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public List<Multihash> add(Multihash hash) throws IOException {
	            return ((List<Object>)((Map)retrieveAndParse("pin/add?stream-channels=true&arg=" + hash)).get("Pins"))
	                    .stream()
	                    .map(x -> Cid.decode((String) x))
	                    .collect(Collectors.toList());
	        }

	        public Map<Multihash, Object> ls() throws IOException {
	            return ls(PinType.direct);
	        }

	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public Map<Multihash, Object> ls(PinType type) throws IOException {
	            return ((Map<String, Object>)(((Map)retrieveAndParse("pin/ls?stream-channels=true&t="+type.name())).get("Keys"))).entrySet()
	                    .stream()
	                    .collect(Collectors.toMap(x -> Cid.decode(x.getKey()), x-> x.getValue()));
	        }

	        public List<Multihash> rm(Multihash hash) throws IOException {
	            return rm(hash, true);
	        }

	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public List<Multihash> rm(Multihash hash, boolean recursive) throws IOException {
	            Map json = retrieveMap("pin/rm?stream-channels=true&r=" + recursive + "&arg=" + hash);
	            return ((List<Object>) json.get("Pins")).stream().map(x -> Cid.decode((String) x)).collect(Collectors.toList());
	        }

	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public List<Multihash> update(Multihash existing, Multihash modified, boolean unpin) throws IOException {
	            return ((List<Object>)((Map)retrieveAndParse("pin/update?stream-channels=true&arg=" + existing + "&arg=" + modified + "&unpin=" + unpin)).get("Pins"))
	                    .stream()
	                    .map(x -> Cid.decode((String) x))
	                    .collect(Collectors.toList());
	        }
	    }
	    
	    public class RemotePin {
	    	
	    	public Map<String, String> add(Multihash hash, String service, String name) throws IOException {
	            return this.add(hash, service, name, false);
	        }
	    	
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public Map<String, String> add(Multihash hash, String service, String name, boolean background) throws IOException {
	        	if(null == this.ls(service, hash.toString())) {	
	        		return ((Map)retrieveAndParse("pin/remote/add?stream-channels=true&arg=" + hash + "&service=" + service + "&name=" + name + "&background=" + background));
	        	}
	        	return null;
	        }
	        
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public Map<String, String> ls(String service, String cid) throws IOException {
	            return ((Map)retrieveAndParse("pin/remote/ls?stream-channels=true&service=" + service + "&cid=" + cid + "&status=pinned"));
	        }
	    }

	    /* 'ipfs repo' is a plumbing command used to manipulate the repo.
	     */
	    public class Key {
	        public KeyInfo gen(String name, Optional<String> type, Optional<String> size) throws IOException {
	            return KeyInfo.fromJson(retrieveAndParse("key/gen?arg=" + name + type.map(t -> "&type=" + t).orElse("") + size.map(s -> "&size=" + s).orElse("")));
	        }

	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public List<KeyInfo> list() throws IOException {
	            return ((List<Object>)((Map)retrieveAndParse("key/list")).get("Keys"))
	                    .stream()
	                    .map(KeyInfo::fromJson)
	                    .collect(Collectors.toList());
	        }

	        public Object rename(String name, String newName) throws IOException {
	            return retrieveAndParse("key/rename?arg="+name + "&arg=" + newName);
	        }

	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public List<KeyInfo> rm(String name) throws IOException {
	            return ((List<Object>)((Map)retrieveAndParse("key/rm?arg=" + name)).get("Keys"))
	                    .stream()
	                    .map(KeyInfo::fromJson)
	                    .collect(Collectors.toList());
	        }
	    }

	    /* 'ipfs repo' is a plumbing command used to manipulate the repo.
	     */
	    public class Repo {
	        public Object gc() throws IOException {
	            return retrieveAndParse("repo/gc");
	        }
	    }

	    public class Pubsub {
	        public Object ls() throws IOException {
	            return retrieveAndParse("pubsub/ls");
	        }

	        public Object peers() throws IOException {
	            return retrieveAndParse("pubsub/peers");
	        }

	        public Object peers(String topic) throws IOException {
	            return retrieveAndParse("pubsub/peers?arg="+topic);
	        }

	        /**
	         *
	         * @param topic
	         * @param data url encoded data to be published
	         * @return
	         * @throws IOException
	         */
	        public Object pub(String topic, String data) throws Exception {
	            return retrieveAndParse("pubsub/pub?arg="+topic + "&arg=" + data);
	        }

	        public Stream<Map<String, Object>> sub(String topic) throws Exception {
	            return sub(topic, ForkJoinPool.commonPool());
	        }

	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public Stream<Map<String, Object>> sub(String topic, ForkJoinPool threadSupplier) throws Exception {
	            return retrieveAndParseStream("pubsub/sub?arg=" + topic, threadSupplier).map(obj -> (Map)obj);
	        }

	        /**
	         * A synchronous method to subscribe which consumes the calling thread
	         * @param topic
	         * @param results
	         * @throws IOException
	         */
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public void sub(String topic, Consumer<Map<String, Object>> results, Consumer<IOException> error) throws IOException {
	            retrieveAndParseStream("pubsub/sub?arg="+topic, res -> results.accept((Map)res), error);
	        }


	    }

	    /* 'ipfs block' is a plumbing command used to manipulate raw ipfs blocks.
	     */
	    public class Block {
	        public byte[] get(Multihash hash) throws IOException {
	            return retrieve("block/get?stream-channels=true&arg=" + hash);
	        }

	        public byte[] rm(Multihash hash) throws IOException {
	            return retrieve("block/rm?stream-channels=true&arg=" + hash);
	        }

	        public List<MerkleNode> put(List<byte[]> data) throws IOException {
	            return put(data, Optional.empty());
	        }

	        public List<MerkleNode> put(List<byte[]> data, Optional<String> format) throws IOException {
	            // N.B. Once IPFS implements a bulk put this can become a single MultipartExt call with multiple 'files'
	            List<MerkleNode> res = new ArrayList<>();
	            for (byte[] value : data) {
	                res.add(put(value, format));
	            }
	            return res;
	        }

	        @SuppressWarnings("unchecked")
			public MerkleNode put(byte[] data, Optional<String> format) throws IOException {
	            String fmt = format.map(f -> "&format=" + f).orElse("");
	            MultipartExt m = new MultipartExt(protocol +"://" + host + ":" + port + version+"block/put?stream-channels=true" + fmt, "UTF-8");
	            try {
	                m.addFilePart("file", Paths.get(""), new NamedStreamable.ByteArrayWrapper(data));
	                String res = m.finish();
	                return JSONParser.parseStream(res).stream().map(x -> MerkleNode.fromJSON((Map<String, Object>) x)).findFirst().get();
	            } catch (IOException e) {
	                throw new RuntimeException(e.getMessage(), e);
	            }
	        }

	        @SuppressWarnings("rawtypes")
			public Map stat(Multihash hash) throws IOException {
	            return retrieveMap("block/stat?stream-channels=true&arg=" + hash);
	        }
	    }

	    /* 'ipfs object' is a plumbing command used to manipulate DAG objects directly. {Object} is a subset of {Block}
	     */
	    public class IPFSObject {
	        @SuppressWarnings("unchecked")
			public List<MerkleNode> put(List<byte[]> data) throws IOException {
	            MultipartExt m = new MultipartExt(protocol +"://" + host + ":" + port + version+"object/put?stream-channels=true", "UTF-8");
	            for (byte[] f : data)
	                m.addFilePart("file", Paths.get(""), new NamedStreamable.ByteArrayWrapper(f));
	            String res = m.finish();
	            return JSONParser.parseStream(res).stream().map(x -> MerkleNode.fromJSON((Map<String, Object>) x)).collect(Collectors.toList());
	        }

	        @SuppressWarnings("unchecked")
			public List<MerkleNode> put(String encoding, List<byte[]> data) throws IOException {
	            if (!"json".equals(encoding) && !"protobuf".equals(encoding))
	                throw new IllegalArgumentException("Encoding must be json or protobuf");
	            MultipartExt m = new MultipartExt(protocol +"://" + host + ":" + port + version+"object/put?stream-channels=true&encoding="+encoding, "UTF-8");
	            for (byte[] f : data)
	                m.addFilePart("file", Paths.get(""), new NamedStreamable.ByteArrayWrapper(f));
	            String res = m.finish();
	            return JSONParser.parseStream(res).stream().map(x -> MerkleNode.fromJSON((Map<String, Object>) x)).collect(Collectors.toList());
	        }

	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public MerkleNode get(Multihash hash) throws IOException {
	            Map json = retrieveMap("object/get?stream-channels=true&arg=" + hash);
	            json.put("Hash", hash.toBase58());
	            return MerkleNode.fromJSON(json);
	        }

	        @SuppressWarnings("rawtypes")
			public MerkleNode links(Multihash hash) throws IOException {
	            Map json = retrieveMap("object/links?stream-channels=true&arg=" + hash);
	            return MerkleNode.fromJSON(json);
	        }

	        @SuppressWarnings("unchecked")
			public Map<String, Object> stat(Multihash hash) throws IOException {
	            return retrieveMap("object/stat?stream-channels=true&arg=" + hash);
	        }

	        public byte[] data(Multihash hash) throws IOException {
	            return retrieve("object/data?stream-channels=true&arg=" + hash);
	        }

	        @SuppressWarnings("rawtypes")
			public MerkleNode _new(Optional<String> template) throws IOException {
	            if (template.isPresent() && !ObjectTemplates.contains(template.get()))
	                throw new IllegalStateException("Unrecognised template: "+template.get());
	            Map json = retrieveMap("object/new?stream-channels=true"+(template.isPresent() ? "&arg=" + template.get() : ""));
	            return MerkleNode.fromJSON(json);
	        }

	        public MerkleNode patch(Multihash base, String command, Optional<byte[]> data, Optional<String> name, Optional<Multihash> target) throws IOException {
	            if (!ObjectPatchTypes.contains(command))
	                throw new IllegalStateException("Illegal Object.patch command type: "+command);
	            String targetPath = "object/patch/"+command+"?arg=" + base.toBase58();
	            if (name.isPresent())
	                targetPath += "&arg=" + name.get();
	            if (target.isPresent())
	                targetPath += "&arg=" + target.get().toBase58();

	            switch (command) {
	                case "add-link":
	                    if (!target.isPresent())
	                        throw new IllegalStateException("add-link requires name and target!");
	                case "rm-link":
	                    if (!name.isPresent())
	                        throw new IllegalStateException("link name is required!");
	                    return MerkleNode.fromJSON(retrieveMap(targetPath));
	                case "set-data":
	                case "append-data":
	                    if (!data.isPresent())
	                        throw new IllegalStateException("set-data requires data!");
	                    MultipartExt m = new MultipartExt(protocol +"://" + host + ":" + port + version+"object/patch/"+command+"?arg="+base.toBase58()+"&stream-channels=true", "UTF-8");
	                    m.addFilePart("file", Paths.get(""), new NamedStreamable.ByteArrayWrapper(data.get()));
	                    String res = m.finish();
	                    return MerkleNode.fromJSON(JSONParser.parse(res));

	                default:
	                    throw new IllegalStateException("Unimplemented");
	            }
	        }
	    }

	    public class Name {
	        @SuppressWarnings("rawtypes")
			public Map publish(Multihash hash) throws IOException {
	            return publish(hash, Optional.empty());
	        }

	        @SuppressWarnings("rawtypes")
			public Map publish(Multihash hash, Optional<String> id) throws IOException {
	            return retrieveMap("name/publish?arg=/ipfs/" + hash + id.map(name -> "&key=" + name).orElse(""));
	        }

	        @SuppressWarnings("rawtypes")
			public String resolve(Multihash hash) throws IOException {
	            Map res = (Map) retrieveAndParse("name/resolve?arg=" + hash);
	            return (String)res.get("Path");
	        }
	    }

	    public class DHT {
	        @SuppressWarnings("unchecked")
			public List<Map<String, Object>> findprovs(Multihash hash) throws IOException {
	            return getAndParseStream("dht/findprovs?arg=" + hash).stream()
	                    .map(x -> (Map<String, Object>) x)
	                    .collect(Collectors.toList());
	        }

	        @SuppressWarnings("rawtypes")
			public Map query(Multihash peerId) throws IOException {
	            return retrieveMap("dht/query?arg=" + peerId.toString());
	        }

	        @SuppressWarnings("rawtypes")
			public Map findpeer(Multihash id) throws IOException {
	            return retrieveMap("dht/findpeer?arg=" + id.toString());
	        }

	        @SuppressWarnings("rawtypes")
			public Map get(Multihash hash) throws IOException {
	            return retrieveMap("dht/get?arg=" + hash);
	        }

	        @SuppressWarnings("rawtypes")
			public Map put(String key, String value) throws IOException {
	            return retrieveMap("dht/put?arg=" + key + "&arg="+value);
	        }
	    }

	    public class File {
	        @SuppressWarnings("rawtypes")
			public Map ls(Multihash path) throws IOException {
	            return retrieveMap("file/ls?arg=" + path);
	        }
	    }

	    // Network commands

	    @SuppressWarnings("unchecked")
		public List<MultiAddress> bootstrap() throws IOException {
	        return ((List<String>)retrieveMap("bootstrap/").get("Peers"))
	                .stream()
	                .flatMap(x -> {
	                    try {
	                        return Stream.of(new MultiAddress(x));
	                    } catch (Exception e) {
	                        return Stream.empty();
	                    }
	                }).collect(Collectors.toList());
	    }

	    public class Bootstrap {
	        public List<MultiAddress> list() throws IOException {
	            return bootstrap();
	        }

	        @SuppressWarnings("unchecked")
			public List<MultiAddress> add(MultiAddress addr) throws IOException {
	            return ((List<String>)retrieveMap("bootstrap/add?arg="+addr).get("Peers")).stream().map(x -> new MultiAddress(x)).collect(Collectors.toList());
	        }

	        public List<MultiAddress> rm(MultiAddress addr) throws IOException {
	            return rm(addr, false);
	        }

	        @SuppressWarnings("unchecked")
			public List<MultiAddress> rm(MultiAddress addr, boolean all) throws IOException {
	            return ((List<String>)retrieveMap("bootstrap/rm?"+(all ? "all=true&":"")+"arg="+addr).get("Peers")).stream().map(x -> new MultiAddress(x)).collect(Collectors.toList());
	        }
	    }

	    /*  ipfs swarm is a tool to manipulate the network swarm. The swarm is the
	        component that opens, listens for, and maintains connections to other
	        ipfs peers in the internet.
	     */
	    public class Swarm {
	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public List<Peer> peers() throws IOException {
	            Map m = retrieveMap("swarm/peers?stream-channels=true");
	            return ((List<Object>)m.get("Peers")).stream()
	                    .flatMap(json -> {
	                        try {
	                            return Stream.of(Peer.fromJSON(json));
	                        } catch (Exception e) {
	                            return Stream.empty();
	                        }
	                    }).collect(Collectors.toList());
	        }

	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public Map<Multihash, List<MultiAddress>> addrs() throws IOException {
	            Map m = retrieveMap("swarm/addrs?stream-channels=true");
	            return ((Map<String, Object>)m.get("Addrs")).entrySet()
	                    .stream()
	                    .collect(Collectors.toMap(
	                            e -> Multihash.fromBase58(e.getKey()),
	                            e -> ((List<String>)e.getValue())
	                                    .stream()
	                                    .map(MultiAddress::new)
	                                    .collect(Collectors.toList())));
	        }

	        @SuppressWarnings("rawtypes")
			public Map connect(MultiAddress multiAddr) throws IOException {
	            Map m = retrieveMap("swarm/connect?arg="+multiAddr);
	            return m;
	        }

	        @SuppressWarnings("rawtypes")
			public Map disconnect(MultiAddress multiAddr) throws IOException {
	            Map m = retrieveMap("swarm/disconnect?arg="+multiAddr);
	            return m;
	        }
	    }

	    public class Dag {
	        public byte[] get(Cid cid) throws IOException {
	            return retrieve("dag/get?stream-channels=true&arg=" + cid);
	        }

	        public MerkleNode put(byte[] object) throws IOException {
	            return put("json", object, "cbor");
	        }

	        public MerkleNode put(String inputFormat, byte[] object) throws IOException {
	            return put(inputFormat, object, "cbor");
	        }

	        public MerkleNode put(byte[] object, String outputFormat) throws IOException {
	            return put("json", object, outputFormat);
	        }

	        public MerkleNode put(String inputFormat, byte[] object, String outputFormat) throws IOException {
	            String prefix = protocol + "://" + host + ":" + port + version;
	            MultipartExt m = new MultipartExt(prefix + "dag/put/?stream-channels=true&input-enc=" + inputFormat + "&f=" + outputFormat, "UTF-8");
	            m.addFilePart("file", Paths.get(""), new NamedStreamable.ByteArrayWrapper(object));
	            String res = m.finish();
	            return MerkleNode.fromJSON(JSONParser.parse(res));
	        }
	    }

	    public class Diag {
	        public String cmds() throws IOException {
	            return new String(retrieve("diag/cmds?stream-channels=true"));
	        }

	        public String sys() throws IOException {
	            return new String(retrieve("diag/sys?stream-channels=true"));
	        }
	    }

	    @SuppressWarnings("rawtypes")
		public Map ping(Multihash target) throws IOException {
	        return retrieveMap("ping/" + target.toBase58());
	    }

	    @SuppressWarnings("rawtypes")
	    public Map id(Multihash target) throws IOException {
	        return retrieveMap("id/" + target.toBase58());
	    }

	    @SuppressWarnings("rawtypes")
	    public Map id() throws IOException {
	        return retrieveMap("id");
	    }
	    
	    public class Stats {
	    	@SuppressWarnings("rawtypes")
	        public Map bw() throws IOException {
	            return retrieveMap("stats/bw");
	        }
	    }

	    // Tools
	    @SuppressWarnings("rawtypes")
	    public String version() throws IOException {
	        Map m = (Map)retrieveAndParse("version");
	        return (String)m.get("Version");
	    }

	    @SuppressWarnings("rawtypes")
	    public Map commands() throws IOException {
	        return retrieveMap("commands");
	    }

	    @SuppressWarnings("rawtypes")
	    public Map log() throws IOException {
	        return retrieveMap("log/tail");
	    }

	    public class Config {
	    	@SuppressWarnings("rawtypes")
	        public Map show() throws IOException {
	            return (Map)retrieveAndParse("config/show");
	        }

			public void replace(NamedStreamable file) throws IOException {
	            MultipartExt m = new MultipartExt(protocol +"://" + host + ":" + port + version+"config/replace?stream-channels=true", "UTF-8");
	            m.addFilePart("file", Paths.get(""), file);
	            m.finish();
	        }

	        @SuppressWarnings("rawtypes")
	        public Object get(String key) throws IOException {
	            Map m = (Map)retrieveAndParse("config?arg="+key);
	            return m.get("Value");
	        }

	        @SuppressWarnings("rawtypes")
	        public Map set(String key, Object value) throws IOException {
	            return retrieveMap("config?arg=" + key + "&arg=" + value);
	        }
	    }

	    public Object update() throws IOException {
	        return retrieveAndParse("update");
	    }

	    public class Update {
	        public Object check() throws IOException {
	            return retrieveAndParse("update/check");
	        }

	        public Object log() throws IOException {
	            return retrieveAndParse("update/log");
	        }
	    }

	    @SuppressWarnings("rawtypes")
	    private Map retrieveMap(String path) throws IOException {
	        return (Map)retrieveAndParse(path);
	    }

	    private Object retrieveAndParse(String path) throws IOException {
	        byte[] res = retrieve(path);
	        return JSONParser.parse(new String(res));
	    }

	    private Stream<Object> retrieveAndParseStream(String path, ForkJoinPool executor) throws IOException {
	        BlockingQueue<CompletableFuture<byte[]>> results = new LinkedBlockingQueue<>();
	        InputStream in = retrieveStream(path);
	        executor.submit(() -> getObjectStream(in,
	                res -> {
	                    results.add(CompletableFuture.completedFuture(res));
	                },
	                err -> {
	                    CompletableFuture<byte[]> fut = new CompletableFuture<>();
	                    fut.completeExceptionally(err);
	                    results.add(fut);
	                })
	        );
	        return Stream.generate(() -> {
	            try {
	                return JSONParser.parse(new String(results.take().get()));
	            } catch (Exception e) {
	                throw new RuntimeException(e);
	            }
	        });
	    }

	    /**
	     * A synchronous stream retriever that consumes the calling thread
	     * @param path
	     * @param results
	     * @throws IOException
	     */
	    private void retrieveAndParseStream(String path, Consumer<Object> results, Consumer<IOException> err) throws IOException {
	        getObjectStream(retrieveStream(path), d -> results.accept(JSONParser.parse(new String(d))), err);
	    }

	    private byte[] retrieve(String path) throws IOException {
	        URL target = new URL(protocol, host, port, version + path);
	        return IpfsExt.get(target, connectTimeoutMillis, readTimeoutMillis);
	    }

	    private static byte[] get(URL target, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
	        HttpURLConnection conn = configureConnection(target, "POST", connectTimeoutMillis, readTimeoutMillis);
	        conn.setDoOutput(true);
	        /* See IPFS commit for why this is a POST and not a GET https://github.com/ipfs/go-ipfs/pull/7097
	           This commit upgrades go-ipfs-cmds and configures the commands HTTP API Handler
	           to only allow POST/OPTIONS, disallowing GET and others in the handling of
	           command requests in the IPFS HTTP API (where before every type of request
	           method was handled, with GET/POST/PUT/PATCH being equivalent).
	           The Read-Only commands that the HTTP API attaches to the gateway endpoint will
	           additional handled GET as they did before (but stop handling PUT,DELETEs).
	           By limiting the request types we address the possibility that a website
	           accessed by a browser abuses the IPFS API by issuing GET requests to it which
	           have no Origin or Referrer set, and are thus bypass CORS and CSRF protections.
	           This is a breaking change for clients that relay on GET requests against the
	           HTTP endpoint (usually :5001). Applications integrating on top of the
	           gateway-read-only API should still work (including cross-domain access).
	        */
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");

	        try {
	            OutputStream out = conn.getOutputStream();
	            out.write(new byte[0]);
	            out.flush();
	            out.close();
	            InputStream in = conn.getInputStream();
	            ByteArrayOutputStream resp = new ByteArrayOutputStream();

	            byte[] buf = new byte[4096];
	            int r;
	            while ((r = in.read(buf)) >= 0)
	                resp.write(buf, 0, r);
	            return resp.toByteArray();
	        } catch (ConnectException e) {
	            throw new RuntimeException("Couldn't connect to IPFS daemon at "+target+"\n Is IPFS running?");
	        } catch (IOException e) {
	            InputStream errorStream = conn.getErrorStream();
	            String err = errorStream == null ? e.getMessage() : new String(readFully(errorStream));
	            throw new RuntimeException("IOException contacting IPFS daemon.\n"+err+"\nTrailer: " + conn.getHeaderFields().get("Trailer"), e);
	        }
	    }

	    private void getObjectStream(InputStream in, Consumer<byte[]> processor, Consumer<IOException> error) {
	        byte LINE_FEED = (byte)10;

	        try {
	            ByteArrayOutputStream resp = new ByteArrayOutputStream();

	            byte[] buf = new byte[4096];
	            int r;
	            while ((r = in.read(buf)) >= 0) {
	                resp.write(buf, 0, r);
	                if (buf[r - 1] == LINE_FEED) {
	                    processor.accept(resp.toByteArray());
	                    resp.reset();
	                }
	            }
	        } catch (IOException e) {
	            error.accept(e);
	        }
	    }

	    private List<Object> getAndParseStream(String path) throws IOException {
	        InputStream in = retrieveStream(path);
	        byte LINE_FEED = (byte)10;

	        ByteArrayOutputStream resp = new ByteArrayOutputStream();

	        byte[] buf = new byte[4096];
	        int r;
	        List<Object> res = new ArrayList<>();
	        while ((r = in.read(buf)) >= 0) {
	            resp.write(buf, 0, r);
	            if (buf[r - 1] == LINE_FEED) {
	                res.add(JSONParser.parse(new String(resp.toByteArray())));
	                resp.reset();
	            }
	        }
	        return res;
	    }

	    private InputStream retrieveStream(String path) throws IOException {
	        URL target = new URL(protocol, host, port, version + path);
	        return IpfsExt.getStream(target, connectTimeoutMillis, readTimeoutMillis);
	    }

	    private static InputStream getStream(URL target, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
	        HttpURLConnection conn = configureConnection(target, "POST", connectTimeoutMillis, readTimeoutMillis);
	        return conn.getInputStream();
	    }

	    /*private Map postMap(String path, byte[] body, Map<String, String> headers) throws IOException {
	        URL target = new URL(protocol, host, port, version + path);
	        return (Map) JSONParser.parse(new String(post(target, body, headers, connectTimeoutMillis, readTimeoutMillis)));
	    }*/

	    /*private static byte[] post(URL target, byte[] body, Map<String, String> headers, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
	        HttpURLConnection conn = configureConnection(target, "POST", connectTimeoutMillis, readTimeoutMillis);
	        for (String key: headers.keySet())
	            conn.setRequestProperty(key, headers.get(key));
	        conn.setDoOutput(true);
	        OutputStream out = conn.getOutputStream();
	        out.write(body);
	        out.flush();
	        out.close();

	        InputStream in = conn.getInputStream();
	        return readFully(in);
	    }*/

	    private static final byte[] readFully(InputStream in) {
	        try {
	            ByteArrayOutputStream resp = new ByteArrayOutputStream();
	            byte[] buf = new byte[4096];
	            int r;
	            while ((r=in.read(buf)) >= 0)
	                resp.write(buf, 0, r);
	            return resp.toByteArray();
	            
	        } catch(IOException ex) {
	            throw new RuntimeException("Error reading InputStrean", ex);
	        }
	    }

	    private static boolean detectSSL(MultiAddress multiaddress) {
	        return multiaddress.toString().contains("/https");
	    }
	    
	    private static HttpURLConnection configureConnection(URL target, String method, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
	        HttpURLConnection conn = (HttpURLConnection) target.openConnection();
	        conn.setRequestMethod(method);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setConnectTimeout(connectTimeoutMillis);
	        conn.setReadTimeout(readTimeoutMillis);
	        return conn;
	    }
	
}
