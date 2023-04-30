package com.board.study.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.study.dto.file.BoardFileRequestDto;
import com.board.study.dto.file.BoardFileResponseDto;
import com.board.study.service.BoardFileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final, @NotNull 이 붙은 필드(멤버변수)를 매개변수로 가지는 생성자를 만드는 것.
@Controller
public class BoardFileController {
	
	private final BoardFileService boardFileService;
	
	//파일 다운로드
	@GetMapping("/file/download")
	public void downloadFile(@RequestParam() Long id, HttpServletResponse response) throws Exception{
		
		try {
			//게시판의 아이디를 가지고, 현재 게시판의 파일이 있는지 조회한다.
			BoardFileResponseDto fileInfo = boardFileService.findById(id);
			if(fileInfo == null) throw new FileNotFoundException("Empty FileData");
			
			//DB에 저장되어있는 파의 경로와 이름으로 파일 객체를 생성함. java.io에서 제공하는 클래스임.
			File dFile = new File(fileInfo.getFilePath(), fileInfo.getSaveFileName());
			int fSize= (int) dFile.length();
			
			if(fSize > 0) {
				//파일 다운로드 시 다운로드 받는 파일의 종류와 이름, 인코딩하여 설정하는 부분
				String encodedFilename = "attachment; filename*=" + "UTF-8" + "''" +
										 URLEncoder.encode(fileInfo.getOrigFileName(), "UTF-8");
				
				//Client의 요청에 응답으로 보낼 파일의 MIME 타입 설정
				//문자 인코딩은 UTF-8 설정함.
                response.setContentType("application/octet-stream; charset=utf-8");

                // Header 설정, 응답헤더의 일종으로 Client가 요청한 정보에 대한 메타데이터(정보가 가진 정보)를 제공함.
                response.setHeader("Content-Disposition", encodedFilename);

                // ContentLength 설정, 응답으로 보낼 컨텐츠의 길이
                response.setContentLengthLong(fSize);
                
                //파일 입출력을 통해서 로컬 피시에 저장된 파일의 데이터를 서버로 전송하는 과정
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(dFile));
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                
                try {
                	//한번에 읽을 수 있는 데이터의 크기는 4096byte이고, 읽은 데이터는 bytesRead로 표시함.
                	byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    //while문을 통해 dFile의 데이터를 전부 읽음
                    while ((bytesRead = in .read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    //데이터를 Client 쪽으로 전송함.
                    out.flush();
				} finally {
					in .close();
                    out.close();
				}
			} else {
				throw new FileNotFoundException("Empty FileData.");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	@PostMapping("/file/delete.ajax")
    public String updateDeleteYn(Model model, BoardFileRequestDto boardFileRequestDto) throws Exception {
        try {
            model.addAttribute("result", boardFileService.updateDeleteYn(boardFileRequestDto.getIdArr()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return "jsonView";
    }
}
