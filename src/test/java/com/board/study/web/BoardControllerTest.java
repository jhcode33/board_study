package com.board.study.web;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.board.study.dto.board.BoardResponseDto;
import com.board.study.entity.board.Board;
import com.board.study.service.BoardService;

@SpringBootTest //@RunWith 포함하고 있음
@AutoConfigureMockMvc
public class BoardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardService boardService;
	
	@Test
	public void testGetBoardListPage() throws Exception{
		//given - 해당 테스트 케이스에서 필요한 데이터 설정
		//DB와 연동되어 있긴 하지만 DB를 사용하지 않고 코드가 잘 실행되는지 확인하기 위해서 이렇게 함
		HashMap<String, Object> resultMap = new HashMap();
		List<BoardResponseDto> list = new ArrayList();
		Board entity = Board.builder()
							.id(1L)
							.title("test title")
							.content("test content")
							.registerId("1")
							.build();
		list.add(new BoardResponseDto(entity));
		resultMap.put("list", list);
		
		//when  특정 동작 수행
		HashMap<String, Object> result = boardService.findAll(0, 5);
		
		//then
		assertThat(result.get("list")).isEqualTo(list);
	}



}
