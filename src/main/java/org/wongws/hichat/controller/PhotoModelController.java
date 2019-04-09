package org.wongws.hichat.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wongws.hichat.util.ImageProducerUtil;

@Controller
public class PhotoModelController {
	// 根据请求的路径中的参数id,从本地磁盘中读取图片，picUrl是从配置文件中读取出来的
	@RequestMapping("/toFindUserimg")
	public void picToJSP(@PathParam("id") String id, HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("application/octet-stream;charset=UTF-8");
		try {
			// 图片读取路径
			String photoName = id + ".png";
			FileInputStream in = new FileInputStream(ImageProducerUtil.getUserimg() + "/" + photoName);
			int i = in.available();
			byte[] data = new byte[i];
			in.read(data);
			in.close();

			// 写图片
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
