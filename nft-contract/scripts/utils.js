const fs = require('fs')

module.exports = {
  isFileExisted(filename){
    return new Promise(function(resolve) {
      fs.access(filename, (err) => {
        if (err) return resolve(false);
        return resolve(true);
      });
    });
  },
  readFile(filename){
    return new Promise(function(resolve){
      fs.readFile(filename, function(err, data){
        if(err) return resolve({});
        return resolve(JSON.parse(data));
      });
    });
  },
  writeFile(filename, data){
    return new Promise(function(resolve){
      fs.writeFile(filename, data, function(err){
        if(err) return resolve(false);
        return resolve(true);
      });
    });
  },
  async readJsonFile(filename){
    let exists = await this.isFileExisted(filename);
    if(!exists) return {};
    return await this.readFile(filename);
  },
  async writeJsonFile(filename, data){
    var _data = JSON.stringify(data);
    return await this.writeFile(filename, _data);
  },
}
