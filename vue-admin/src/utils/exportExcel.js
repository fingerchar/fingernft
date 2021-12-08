//导出excel
//_tHeader :['名称','性别']
//_filterVal:['name','sex']
//_exData:需导出内容
//_fileName:文件名称
export const export2Excel = function (_tHeader, _filterVal, _exData, _fileName) {
    require.ensure([], () => {
      const { export_json_to_excel } = require("../vendor/Export2Excel"); //这里必须使用绝对路径
      const tHeader = _tHeader; // 导出的表头名
      const filterVal = _filterVal; // 导出的表头字段名
      //const list = exData;
  
      const data = formatJson(filterVal, _exData);
      export_json_to_excel(tHeader, data, _fileName); // 导出的表格名称，根据需要自己命名
    });
  };
  
  export const formatJson = function (filterVal, jsonData) {
    return jsonData.map(v => filterVal.map(j => v[j]));
  };