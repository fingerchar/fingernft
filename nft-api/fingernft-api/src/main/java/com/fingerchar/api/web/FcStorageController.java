package com.fingerchar.api.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.StorageService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcStorage;


/**
 * 对象存储服务
 */
@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/storage")
public class FcStorageController extends BaseController {

    @Autowired
    private StorageService storageService;


    /**
     * 上传文件
     *    1、获取到前端上传来的MultipartFile对象
     *    2、获取到源文件名称
     *    3、将文件交给service层处理
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        FcStorage fcStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
        if(null == fcStorage) {
        	return ResponseUtil.fail(-1, "upload fail, please check ipfs server is ok or not");
        } else {
        	return ResponseUtil.ok(fcStorage);        	
        }
    }
    
    @PostMapping("/multiupload")
    public Object multiUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
    	int len = files.length;
    	String[] fileNames = new String[len];
    	InputStream[] inputStreams = new InputStream[len];
    	Long[] contentLengths = new Long[len];
    	String[] contentTypes = new String[len];
    	int i=0;
    	for(MultipartFile file: files) {
    		fileNames[i] = file.getOriginalFilename();
    		inputStreams[i] = file.getInputStream();
    		contentLengths[i] = file.getSize();
    		contentTypes[i] = file.getContentType();
    		i++;
    	}
    	List<FcStorage> list = storageService.store(inputStreams, contentLengths, contentTypes, fileNames);
        if(null == list) {
        	return ResponseUtil.fail(-1, "upload fail, please check ipfs server is ok or not");
        } else {
        	return ResponseUtil.ok(list);        	
        }
    }
}
