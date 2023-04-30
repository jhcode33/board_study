package com.board.study.dto.file;

import com.board.study.entity.file.BoardFile;

import lombok.Getter;

//사용자에게 파일 정보를 전달하는 DTO
@Getter
public class BoardFileResponseDto {
	
	private String origFileName;
	private String saveFileName;
	private String filePath;
	
	//생성자
	public BoardFileResponseDto(BoardFile entity) {
		this.origFileName = entity.getOrigFileName();
		this.saveFileName = entity.getSaveFileName();
		this.filePath     = entity.getFilePath();
	}

	@Override
	public String toString() {
		return "FileMstResponseDto [origFileName=" + origFileName +
			   ", saveFileName=" + saveFileName + 
			   ", filePath="     + filePath + "]";
	}
}
