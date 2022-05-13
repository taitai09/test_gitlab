package omc.spop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import omc.spop.model.DownLoadFile;

@Component
public class NewFileDownload extends AbstractView {
	public NewFileDownload() {
		// content type을 지정.
//        setContentType("apllication/download; charset=utf-8");
	}

	// FileDownload 확장자에러에 따른 new version.
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		DownLoadFile download = (DownLoadFile) model.get("downloadFile");
		File file = new File(download.getFile_path() + download.getFile_nm());

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			logger.debug("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		res.setContentType(mimeType);
		res.setContentLength((int) file.length());
		res.setHeader("Content-Disposition",
				"attachment; filename=\"" + java.net.URLEncoder.encode(download.getOrg_file_nm(), "utf-8") + "\";");
		res.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = res.getOutputStream();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		out.flush();
		out.close();
	}
}
