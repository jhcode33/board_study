package com.board.study.dto.board;

import com.board.study.entity.board.Board;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //파라메타가 없는 기본 생성자를 생성해줌.
public class BoardRequestDto {
	private Long id;
	private String title;
	private String content;
	private String registerId;
	
	
	public Board toEntity() {
		return Board.builder()
				.title(title)
				.content(content)
				.registerId(registerId)
				.build();
	}
	//Board.java를 @AllargsConstructor로 바꿀경우
	/*
	 * 
	 * 
	 * */
	
}
