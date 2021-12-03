
# 源码结构

- nft-admin 后台java源码
- nft-api 市场java源码
- nft-contract 智能合约
- nft-h5 移动端uniswap源码
- nft-pc pc端vue源码
- token-contract ERC20代币合约

# 服务器搭建
Finger Nft搭建可以用宝塔面板，通过宝塔安装，后续如果为了安全，可以把宝塔关闭，建议不要安装phpAdmin。

## 环境搭建
以下可以用宝塔面板安装，如果没有宝塔，请自行编译安装或yum安装

- jdk 1.8+
- mysql 8.0+
- redis 5.8+
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
8080 控制台端口   （http://IP:8080/webui）
4001 api请求端口 （上传的时候需要用到）
5001 文件访问端口 （测试的时候可以使用，eg: http://IP:5001/ipfs/文件hash值）

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

## 市场监听搭建
事件监听对应的工程是fingernft-event-对应版本（eg:fingernft-event-base）

部署和上面市场工程一样

## 市场后台搭建
admin对应的工程是fingernft-admin-对应版本（eg:fingernft-admin-base）

部署和上面市场工程一样(这是一个单独的工程)

## Nginx配置
5001是默认端口，如有修改，请设置成修改后的端口


### ipfs配置

```
upstream ipfs_view_site {
  ip_hash;
  server 127.0.0.1:5001;
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

智能合约在truffle-contract

### 安装一例埃

```
npm i // 安装依赖包
```

### 配置部署脚本

部署脚本在migrations文件夹中,

```

1_NFT_721_deploy.js   // ERC721合约

// 代理合约，分离出来，可以单独升级市场、盲盒、拍卖合约
3_transfer_proxy_deploy.js  // 转账ERC721
4_transfer_proxy_deprecated_deploy.js // 转账已废弃的ERC721 ( 当前系统用不到 )
5_erc20_transfer_proxy_deploy.js    // 转账代币代理合约

// 市场售卖合约
6_exchange_state_deploy.js    // 售卖状态合约
7_exchange_order_holder_deploy.js   // 寄卖合约 ( 当前系统用不到 )
8_exchange_deploy.js  // 售卖交易合约
```

在部署之前, 先生成以太坊助记词,将助记词配置在truffle-config.js文件中

```
provider: () => new HDWalletProvider("这里是助记词", `https://rinkeby.infura.io/v3/{这里是infura的token}`),
```
配置好truffle-config.js后, 可以开始部署智能合约了

```
truffle migrate --network rinkeby
```

> 注意，犹豫部署过程中会出现网络延迟，导致终端，可以对个别合约部署, 记下部署好的合约地址后，配置migrations文件夹中对应的合约参数。( 为了避免重复部署，浪费交易费，推荐这种方式)

### 智能合约授权
使用智能合约之前，需要对合约进行授权, 否则上链时候会出现"OperatorRole: caller does not have the Operator role"错误。
需要授权的合约如下:

- TranferProxy
- TransferProxyForDeprecated
- ERC20TransferProxy
- ExchangeState

需要授权对象合约:

- NftExchange


授权交易在scripts/set.js中


```
contract_address // 对应需要授权的合约地址
private_key // 部署合约的钱包私钥
address // 部署合约的钱包地址
api_url // 以太坊rpc地址，可到infura官网申请
operator  // 对应授权对象合约地址
```


## 前端部署
后台、移动端、pc端都一样

```
npm i // 安装依赖包
yarn build
```



