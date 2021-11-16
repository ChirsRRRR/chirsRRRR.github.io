package com.gzu.campus_market.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.gzu.campus_market.bean.CodeMsg;
import com.gzu.campus_market.bean.Result;
import com.gzu.campus_market.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公用的上传类
 * @author Administrator
 *
 */
@RequestMapping("/admin/upload")
@Controller
public class AdminUploadController {

	@Value("${ylrc.upload.photo.sufix}")
	private String uploadPhotoSufix;

	//文件上传最大容量
	@Value("${ylrc.upload.photo.maxsize}")
	private long uploadPhotoMaxSize;

	//指定文件上传的位置
	@Value("${ylrc.upload.photo.path}")
	private String uploadPhotoPath;//文件保存位置
	
	private Logger log = LoggerFactory.getLogger(AdminUploadController.class);
	
	/**
	 * 图片统一上传类
	 * @param photo
	 * @return
	 */
	@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> uploadPhoto(@RequestParam(name="photo",required=true)MultipartFile photo){
		//判断文件类型是否是图片
		String originalFilename = photo.getOriginalFilename();
		//获取文件后缀
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
		//如果后缀不包含在.jpg,.png,.gif,.jpeg中，则按所定义的错误报错
		if(!uploadPhotoSufix.contains(suffix.toLowerCase())){
			return Result.error(CodeMsg.UPLOAD_PHOTO_SUFFIX_ERROR);
		}
		//getsize返回单位为字节，若上传的文件大于1024kb，报错
		if(photo.getSize()/1024 > uploadPhotoMaxSize){
			CodeMsg codeMsg = CodeMsg.UPLOAD_PHOTO_ERROR;
			codeMsg.setMsg("图片大小不能超过" + (uploadPhotoMaxSize/1024) + "M");
			return Result.error(codeMsg);
		}
		//准备保存文件
		File filePath = new File(uploadPhotoPath);
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		filePath = new File(uploadPhotoPath + "/" + StringUtil.getFormatterDate(new Date(), "yyyyMMdd"));
		//判断当天日期的文件夹是否存在，若不存在，则创建
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		//指定保存在服务器端的文件名。以日期时间作为文件名
		String filename = StringUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis() + suffix;
		try {
			//真正将上传的文件写到服务器上
			photo.transferTo(new File(uploadPhotoPath+"/"+filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("图片上传成功，保存位置：" + uploadPhotoPath + filename);
		//回复上传成功信息
		return Result.success(filename);
	}
}
