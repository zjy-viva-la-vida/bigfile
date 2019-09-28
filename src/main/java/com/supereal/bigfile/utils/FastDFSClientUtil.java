package com.supereal.bigfile.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author jianyang.zhao
 * @description： fastdfs工具类
 * @date： 2019/9/4
 * @version： 1.0
 **/
@Slf4j
public class FastDFSClientUtil {

	/**
	 * 上传文件到fastdfs
	 * @param client
	 * @param file
	 * @return
	 */
	public static String[] uploadFile(TrackerClient client, MultipartFile file,String suffix){

		byte[] fileBytes = null;
		try{
			fileBytes = file.getBytes();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
//		String path = null;
//		String fileName = file.getName();
//		String extendName = fileName.substring(fileName.lastIndexOf(".") + 1);

		TrackerServer trackerServer = null;
		try {
			trackerServer = client.getConnection();
		} catch (Exception e) {
			log.error("上传文件到fastdfs出错：" + e.getMessage());
			e.printStackTrace();
		}
		StorageServer storageServer = null;
		StorageClient sclient = new StorageClient(trackerServer, storageServer);
		String[] fsReturn = null;
		try {
			fsReturn = sclient.upload_file(fileBytes, suffix, null);
			log.info("上传文件到fastdfs,result:" + JSON.toJSON(fsReturn));
			/*path = fsReturn[0]+"/"+fsReturn[1];*/
		} catch (IOException | MyException e) {
			e.printStackTrace();
			ExceptionRes res = ExceptionResponseUtil.spliceMsgFromException(e);
			log.info("上传文件至fastHfs出错：" + res.getMsg());
		}
		try {
			trackerServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fsReturn;
	}



	/**
	 * 删除文件
	 *
	 * @param group
	 * @param filePath
	 * @return
	 */
	public static int deleteFile (String group, String filePath,TrackerClient trackerClient) {
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;

		try {
			//3、通过trackerClient获取一个连接，连接到Tracker，得到一个TrackerServer
			trackerServer = trackerClient.getConnection();

			//4、通过trackerClient获取一个存储节点的StorageServer
			storageServer = trackerClient.getStoreStorage(trackerServer);

			//5、通过trackerServer和storageServer构造一个Storage客户端
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			//fastdfs删除文件
			log.info("删除文件：group,filePath:" + group + "/" + filePath);
			int i = storageClient.delete_file(group, filePath);
			if (i == 0) {
				log.info("FastDFS删除文件成功");
			} else {
				log.info("FastDFS删除文件失败");
			}
			return i;

		} catch (Exception e) {
			log.error("删除fastDfs文件出错：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				//关闭服务，释放资源
				if (null != storageServer) {
					storageServer.close();
				}
				if (null != trackerServer) {
					trackerServer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}


}
