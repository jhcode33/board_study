package com.board.study;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.study.dto.board.BoardRequestDto;
import com.board.study.dto.board.BoardResponseDto;
import com.board.study.service.BoardService;

@SpringBootTest
class BoardStudyApplicationTests {

	@Autowired
	private BoardService boardService;
	
	@Test
	void save() {
		BoardRequestDto boardSaveDto = new BoardRequestDto();
		
		boardSaveDto.setTitle("제목 테스트2");
		boardSaveDto.setContent("내용 테스트2");
		boardSaveDto.setRegisterId("작성자 테스트2");
		
		//Board tmp = boardService.save(boardSaveDto);
		
		//if(tmp != null) { 
		//	System.out.println("# save() success~~");
		//	findAll();
		//	findById(tmp.getId());
		//}
	
	}
	
	//모든 내용을 찾는 것.
//	void findAll() {
//		List<BoardResponseDto> list = boardService.findAll();
//		
//		if(list != null) {
//			System.out.println("# success findAll(): " + list.toString());
//		} else {
//			System.out.println("# fail findAll()~ ");
//		}
//	}
//	//id에 게시글을 찾는거
//	void findById(Long id) {
//        BoardResponseDto info = boardService.findById(id);
//
//        if (info != null) {
//            System.out.println("# Success findById() : " + info.toString());
//            //이 부분 때문에 updateBoard에 @Test가 없어도 실행되는 거임.
//            //updateBoard(id);
//        } else {
//            System.out.println("# Fail findById() ~");
//        }
//    }

//	//업데이트 하는거.
//	@Disabled //@Ignore 대신에 사용하는 것, junit5부터
//    void updateBoard(Long id) {
//
//        BoardRequestDto boardRequestDto = new BoardRequestDto();
//
//        boardRequestDto.setId(id);
//        boardRequestDto.setTitle("업데이트 제목");
//        boardRequestDto.setContent("업데이트 내용");
//        boardRequestDto.setRegisterId("작성자");
//
//        int result = boardService.updateBoard(boardRequestDto);
//
//        if (result > 0) {
//            System.out.println("# Success updateBoard() ~");
//        } else {
//            System.out.println("# Fail updateBoard() ~");
//        }
//    }
}
