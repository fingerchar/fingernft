
# 源码结构

- nft-admin 后台java源码
- nft-api 市场java源码
- nft-contract 智能合约
- nft-h5 移动端uniswap源码
- nft-pc pc端vue源码
- vue-admin 后台前端源码
- token-contract ERC20代币合约

# 服务器搭建
Finger Nft搭建可以用宝塔面板，通过宝塔安装，后续如果为了安全，可以把宝塔关闭，建议不要安装phpAdmin。

## 环境搭建
以下可以用宝塔面板安装，如果没有宝塔，请自行编译安装或yum安装

- jdk 1.8+
- mysql 8.0+
- nginx最新

## IPFS搭建

### IPFS安装
```
wget https://dist.ipfs.io/go-ipfs/v0.9.1/go-ipfs_v0.9.1_linux-amd64.tar.gz
tar –zxvf go-ipfs_v0.9.1_linux-amd64.tar.gz
// 或者分两步
gunzip go-ipfs_v0.9.1_linux-amd64.tar.gz
tar –xvf go-ipfs_v0.9.1_linux-amd64.tar
cd go-ipfs
sh install.sh
```

### 初始化IPFS
```
ipfs init
```
### 配置ipfs跨域
```
ipfs config --json API.HTTPHeaders.Access-Control-Allow-Methods '["PUT","GET", "POST", "OPTIONS"]'
ipfs config --json API.HTTPHeaders.Access-Control-Allow-Origin '["*"]'
ipfs config --json API.HTTPHeaders.Access-Control-Allow-Credentials '["true"]'
ipfs config --json API.HTTPHeaders.Access-Control-Allow-Headers '["Authorization"]'
ipfs config --json API.HTTPHeaders.Access-Control-Expose-Headers '["Location"]'
```
### 配置IPFS外网访问控制台
```
cd ~/.ipfs
vi config
```
把127.0.0.1修改成0.0.0.0

### IPFS端口说明

4001 主要端口，进行p2p连接和同步数据
5001 ipfs的api端口，管理页面的端口，可以进行数据的读写
8080 ipfs gateway端口，用于读取ipfs节点数据，默认只读


> 备注：如果使用防火墙，记得把这几个端口开放，否则外网还是无法访问

### 启动IPFS

```
ipfs daemon
```

没有异常的话，我们就可以使用浏览器访问控制台
上面的启动如果关闭了链接，ipfs会停止，所以，在真正部署的时候使用nohup, 执行：

```
nohup ipfs daemon > ipfs.log &
```

记得执行完成后，敲一下回车，直接断开连接，有可能也导致ipfs关闭


## Finger Nft市场搭建

市场对应的工程是fingernft-api-对应版本（eg:fingernft-api-base）

### 打包市场

- Eclipse: 在父工程执行run-> maven install
Idea: 打开父工程maven面板，执行mvaen install
打包完成后，第一次部署使用dist底下的zip包，后面使用target目录底下的jar包进行升级

- 上传zip包到/data/java底下解压： unzip  zip文件
解压后目录：

```
bin: 核心启停脚本
lib: jar包目录
start.sh  启动脚本
stop.sh 停止脚本
restart.sh 重启脚本
status.sh 查询状态脚本
bin里面的run-manager.sh 可以配置java运行参数
```

- 把java配置文件合并成一个application.yml文件上传到/data/java/上面解压的目录
- 执行chmod +x 上面解压的目录
- 配置文件上传路径，mkdir –p /data/java/static/upload;mkdir –p /data/java/static/image
- 启动执行：sh start.sh, 这个时候会生成一个logs目录，放置日志信息

> 备注：unbuntu和redhat等其他系统，在执行脚本的时候，需要加sudo(eg: sudo sh start.sh),或者使用  ./start.sh
如果没有创建目录权限，把上面的zip解压目录设置成777权限：chmod 777 解压目录


## 市场后台搭建
admin对应的工程是fingernft-admin-对应版本（eg:fingernft-admin-base）

部署和上面市场工程一样(这是一个单独的工程)

## Nginx配置
5001是默认端口，如有修改，请设置成修改后的端口


### ipfs配置

```
upstream ipfs_view_site {
  ip_hash;
  server 127.0.0.1:8080;
}

server
{
    listen 80;
	  listen 443 ssl http2;
    server_name ipfs.abc.com;   // 此处配置ipfs域名，可做cdn加速
    index index.html;
    root 随意;
    
    location ~* /(.+)!(\d+)x(\d+)$ {
      set $w $2;
      set $h $3;
      proxy_redirect          off;
      proxy_set_header        Host            $host;
      proxy_set_header        X-Real-IP       $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_connect_timeout   3;
      proxy_send_timeout      30;
      proxy_read_timeout      30;
      proxy_pass http://ipfs_view_site;
      image_filter resize  $w $h;
      image_filter_buffer  100M;
      image_filter_jpeg_quality 75;
    }
    
    location / {
      proxy_redirect          off;
      proxy_set_header        Host            $host;
      proxy_set_header        X-Real-IP       $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_connect_timeout   3;
      proxy_send_timeout      30;
      proxy_read_timeout      30;
      proxy_pass http://ipfs_view_site;
    }
    
    #SSL-START SSL相关配置，请勿删除或修改下一行带注释的404规则
    #error_page 404/404.html;
    #HTTP_TO_HTTPS_START
    if ($server_port !~ 443){
        rewrite ^(/.*)$ https://$host$1 permanent;
    }
    #HTTP_TO_HTTPS_END
    ssl_certificate    pem文件路径;
    ssl_certificate_key    key文件路径;
    ssl_protocols TLSv1.1 TLSv1.2 TLSv1.3;
    ssl_ciphers EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    add_header Strict-Transport-Security "max-age=31536000";
    error_page 497  https://$host$request_uri;

    #SSL-END
    
    #ERROR-PAGE-START  错误页配置，可以注释、删除或修改
    #error_page 404 /404.html;
    #error_page 502 /502.html;
    #ERROR-PAGE-END
    
    
    #禁止访问的文件或目录
    location ~ ^/(\.user.ini|\.htaccess|\.git|\.svn|\.project|LICENSE|README.md)
    {
        return 404;
    }

    
    location ~ .*\.(js|css)?$
    {
        expires      12h;
        error_log /dev/null;
        access_log /dev/null; 
    }
    access_log  ipfs日志路径;
    error_log  ipfs错误日志路径;
}
```

### 后台配置

```
upstream eth_admin_site {
  ip_hash;
  server 127.0.0.1:9802;
}

server
{
    listen 80;
	listen 443 ssl http2;
    server_name admin.abc.com;
    index index.html;
    root 后台页面放置路径;
    
    location /admin/ {
      proxy_redirect          off;
      proxy_set_header        Host            $host;
      proxy_set_header        X-Real-IP       $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_connect_timeout   3;
      proxy_send_timeout      30;
      proxy_read_timeout      30;
      proxy_pass http://eth_admin_site;
    }
    
    #SSL-START SSL相关配置，请勿删除或修改下一行带注释的404规则
    #error_page 404/404.html;
    #HTTP_TO_HTTPS_START
    if ($server_port !~ 443){
        rewrite ^(/.*)$ https://$host$1 permanent;
    }
    #HTTP_TO_HTTPS_END
    ssl_certificate    ssl pem文件.pem;
    ssl_certificate_key    ssl key文件.pem;
    ssl_protocols TLSv1.1 TLSv1.2 TLSv1.3;
    ssl_ciphers EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    add_header Strict-Transport-Security "max-age=31536000";
    error_page 497  https://$host$request_uri;

    #SSL-END
    
    #ERROR-PAGE-START  错误页配置，可以注释、删除或修改
    #error_page 404 /404.html;
    #error_page 502 /502.html;
    #ERROR-PAGE-END
    
    #禁止访问的文件或目录
    location ~ ^/(\.user.ini|\.htaccess|\.git|\.svn|\.project|LICENSE|README.md)
    {
        return 404;
    }
    
    #alias对应的路径是上面说明创建的路径
    location ~* /static/image/(.+)\.(jpg|jpeg|gif|png)!(\d+)x(\d+)$ {
      set $w $3;
      set $h $4;
      image_filter resize  $w $h;
      image_filter_buffer  10M;
      image_filter_jpeg_quality 75;
      try_files /$1.$2  /notfound.jpg;
      expires 1d;
      alias  /data/java/static/image;  
    }

		#root对应的路径是上面说明创建的路径
    location ~* /static/image/ {
  		root /data/java;  
    }
    
    location ~ .*\.(js|css)?$
    {
        expires      12h;
        error_log /dev/null;
        access_log /dev/null; 
    }
    
    location / {
      try_files $uri $uri/ @router;
    }

    location @router {
      rewrite ^.*$ /index.html last;
    }
    
    access_log  日志文件路径;
    error_log  错误日志文件路径;
}

```

### 市场配置

```
upstream eth_web_site {
  ip_hash;
  server 127.0.0.1:8801;
}

server
{
    listen 80;
	listen 443 ssl http2;
    server_name www.abc.com;
    index index.html;
    root pc页面路径;
    
    #fingernft前缀可以在java工程里面修改 SysConfConstant.URL_PREFIX
    location /fingernft/ {
      proxy_redirect          off;
      proxy_set_header        Host            $host;
      proxy_set_header        X-Real-IP       $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_connect_timeout   3;
      proxy_send_timeout      30;
      proxy_read_timeout      30;
      proxy_pass http://eth_web_site;
    }
    
    #SSL-START SSL相关配置，请勿删除或修改下一行带注释的404规则
    #error_page 404/404.html;
    #HTTP_TO_HTTPS_START
    if ($server_port !~ 443){
        rewrite ^(/.*)$ https://$host$1 permanent;
    }
    #HTTP_TO_HTTPS_END
    ssl_certificate    pem文件.pem;
    ssl_certificate_key    key文件.pem;
    ssl_protocols TLSv1.1 TLSv1.2 TLSv1.3;
    ssl_ciphers EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    add_header Strict-Transport-Security "max-age=31536000";
    error_page 497  https://$host$request_uri;

    #SSL-END
    
    #ERROR-PAGE-START  错误页配置，可以注释、删除或修改
    #error_page 404 /404.html;
    #error_page 502 /502.html;
    #ERROR-PAGE-END
    
    #禁止访问的文件或目录
    location ~ ^/(\.user.ini|\.htaccess|\.git|\.svn|\.project|LICENSE|README.md)
    {
        return 404;
    }

    location ~ .*\.(js|css)?$
    {
        if ( $http_user_agent ~ "(MIDP)|(WAP)|(UP.Browser)|(Smartphone)|(Obigo)|(Mobile)|(AU.Browser)|(wxd.Mms)|(WxdB.Browser)|(CLDC)|(UP.Link)|(KM.Browser)|(UCWEB)|(SEMC-Browser)|(Mini)|(Symbian)|(Palm)|(Nokia)|(Panasonic)|(MOT-)|(SonyEricsson)|(NEC-)|(Alcatel)|(Ericsson)|(BENQ)|(BenQ)|(Amoisonic)|(Amoi-)|(Capitel)|(PHILIPS)|(SAMSUNG)|(Lenovo)|(Mitsu)|(Motorola)|(SHARP)|(WAPPER)|(LG-)|(LG/)|(EG900)|(CECT)|(Compal)|(kejian)|(Bird)|(BIRD)|(G900/V1.0)|(Arima)|(CTL)|(TDG)|(Daxian)|(DAXIAN)|(DBTEL)|(Eastcom)|(EASTCOM)|(PANTECH)|(Dopod)|(Haier)|(HAIER)|(KONKA)|(KEJIAN)|(LENOVO)|(Soutec)|(SOUTEC)|(SAGEM)|(SEC-)|(SED-)|(EMOL-)|(INNO55)|(ZTE)|(iPhone)|(Android)|(Windows CE)|(Wget)|(Java)|(curl)|(Opera)"){       
          root h5页面放置路径;
        }
        expires      12h;
        error_log /dev/null;
        access_log /dev/null; 
    }
    
    #alias对应的路径是上面说明创建的路径
    location ~* /static/upload/(.+)\.(jpg|jpeg|gif|png)!(\d+)x(\d+)$ {
      set $w $3;
      set $h $4;
      image_filter resize  $w $h;
      image_filter_buffer  20M;
      image_filter_jpeg_quality 75;
      try_files /$1.$2  /notfound.jpg;
      expires 1d;
      alias  /data/java/static/upload/;  
    }
    
     #alias对应的路径是上面说明创建的路径
    location ~* /static/image/(.+)\.(jpg|jpeg|gif|png)!(\d+)x(\d+)$ {
      set $w $3;
      set $h $4;
      image_filter resize  $w $h;
      image_filter_buffer  20M;
      image_filter_jpeg_quality 75;
      try_files /$1.$2  /notfound.jpg;
      expires 1d;
      alias  /data/java/static/image/;  
    }

		 #root对应的路径是上面说明创建的路径
    location ~* /static/(upload|image)/ {
  		root /data/java;
    }

    location / {
      if ( $http_user_agent ~ "(MIDP)|(WAP)|(UP.Browser)|(Smartphone)|(Obigo)|(Mobile)|(AU.Browser)|(wxd.Mms)|(WxdB.Browser)|(CLDC)|(UP.Link)|(KM.Browser)|(UCWEB)|(SEMC-Browser)|(Mini)|(Symbian)|(Palm)|(Nokia)|(Panasonic)|(MOT-)|(SonyEricsson)|(NEC-)|(Alcatel)|(Ericsson)|(BENQ)|(BenQ)|(Amoisonic)|(Amoi-)|(Capitel)|(PHILIPS)|(SAMSUNG)|(Lenovo)|(Mitsu)|(Motorola)|(SHARP)|(WAPPER)|(LG-)|(LG/)|(EG900)|(CECT)|(Compal)|(kejian)|(Bird)|(BIRD)|(G900/V1.0)|(Arima)|(CTL)|(TDG)|(Daxian)|(DAXIAN)|(DBTEL)|(Eastcom)|(EASTCOM)|(PANTECH)|(Dopod)|(Haier)|(HAIER)|(KONKA)|(KEJIAN)|(LENOVO)|(Soutec)|(SOUTEC)|(SAGEM)|(SEC-)|(SED-)|(EMOL-)|(INNO55)|(ZTE)|(iPhone)|(Android)|(Windows CE)|(Wget)|(Java)|(curl)|(Opera)"){       
        root h5页面放置的路径;
      }
      try_files $uri $uri/ @router;
    }

    location @router {
      rewrite ^.*$ /index.html last;
    }
    
    access_log  日志路径;
    error_log  错误日志路径;
}
```



## 智能合约部署

智能合约在nft-contract

### 安装依赖包

```
cd nft-contract
yarn // 安装依赖包
```

### 编译合约

```
npm install truffle -g
truffle compile // 编译成功后，会在nft-contract目录下生成build目录
```


### 配置

你可以用truffle脚本部署，也可以用我们已经写好的部署脚本，这里只讲使用脚本部署合约。
部署脚本在migrations文件夹中,


#### 配置脚本
```
cd scripts
vim config.js
```
config.js如下:

```
module.exports = {
  privateKey: "",	// 表示私钥
  apiUrl: "https://rinkeby.infura.io/v3/{token}",	// rinkeby的rpc
  chainId: 4,	// rinkeby 的network id
  NFTName: "FingerNFT",		// nft合约名称
  miner: "0x974423356ba75b1aaf24bbec7c8cc8cf0678554f", // 铸造nft时，授权的钱包地址
  beneficiary: "0x79aca18162577437cc763e36df07bac6938b0b69", // 收取手续费钱包地址
  buyerFeeSigner: "0x364beb2672323691088d9055518b2f750d82eee5",	// 交易授权签名
}
```
配置好后，执行如下脚本
```
node deploy.js	// 部署合约
node operator.js  //  初始化合约
```

执行成功后，会在本地生成一个json文件: {address}_{network_id}.json, 记录了合约的信息

> 注意，犹豫部署过程中会出现网络延迟，导致异常退出，可以重新执行脚本


## 创建自己的代币

智能合约在token-contract


### 安装依赖包

```
cd token-contract
yarn
```

#### 编辑合约

```
npm install truffle -g  // 如果上一步安装过，可以不用安装 
truffle compile // 编译成功后，会在token-contract目录下生成build目录
```

### 配置

你可以用truffle脚本部署，也可以用我们已经写好的部署脚本，这里只讲使用脚本部署合约。
部署脚本在migrations文件夹中,

#### 配置脚本
```
cd scripts
vim config.js
```
config.js如下:

```
module.exports = {
  privateKey: "",	// 表示私钥
  apiUrl: "https://rinkeby.infura.io/v3/{token}",	// rinkeby的rpc
  chainId: 4,	// rinkeby 的network id
  name: "Finger Token",   // 代币名称
  symbol: "Finger",   // 代币符号
  supply: "100000000",    // 发行量
}
```

配置好后，执行如下脚本

```
node deploy.js	// 部署合约
```

执行成功后，会在本地生成一个json文件: {address}_{network_id}.json, 记录了合约的信息

> 如果你想重新创建代币，记得删除json文件，因为执行deploy.js脚本，会检测json文件，判断是否创建。


## 前端部署
后台、pc端都一样

后台账号密码: admin 12345678

```
yarn  // 安装依赖包
yarn build
```



## 数据库配置
数据库初始文件在源码根目录的sql文件夹下

[数据库初始文件](./sql/fingernft.sql)

导入数据后，还需要再后台配置初始信息


### 市场配置

#### 币种添加
进入后台，选择币种管理，添加当前链的币种

> 因为Bid操作必须是代币，后台至少添加一个代币。


#### 网站配置

![网站配置](https://cdn.fingerchar.com/images/website.png)

#### 合约配置

![合约配置](https://cdn.fingerchar.com/images/contract.png)

####  IPFS配置

![IPFS配置](https://cdn.fingerchar.com/images/ipfs.png)
