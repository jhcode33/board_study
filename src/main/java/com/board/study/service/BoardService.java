package com.board.study.service;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.study.dto.board.BoardRequestDto;
import com.board.study.dto.board.BoardResponseDto;
import com.board.study.entity.board.Board;
import com.board.study.entity.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardFileService boardFileService;

	//왜 id를 반환하는지 모르겠는데 객체를 반환하는 것으로 했음, 아마 이거 수정하려면 boardRepository해야할듯
	@Transactional
	public boolean save(BoardRequestDto boardRequestDto, MultipartHttpServletRequest multiRequest) throws Exception {
		Board result = boardRepository.save(boardRequestDto.toEntity());
		
		boolean resultFlag = false;
		
		//DB에 저장한 게시판이 Null이 아니면 아래 코드 수행
		if (result != null) {
			//게시판의 아이디를 통해 해당 게시판에 파일 저장함.
			boardFileService.uploadFile(multiRequest, result.getId());
			resultFlag = true;
		}
		
		return resultFlag;
	}
	
	 //게시글 전체조회(페이징x)
//	@Transactional(readOnly = true) //
//	public List<BoardResponseDto> findAll(){
//		return boardRepository.findAll().stream().map(BoardResponseDto::new).collect(Collectors.toList());
//	}
	
	//게시글 페이징 처리 전체 조회
	@Transactional (readOnly = true)
	public HashMap<String, Object> findAll(Integer page, Integer size){
		//결과를 담을 Map 타입의 변수 생성
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//대용량 데이터를 여러 페이지로 나누어 보여주기 위해, Spring Data JPA에서 제공하는 Page 클래스를 사용함.
		Page<Board> list = boardRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
		//현재 list에는 boardRepository의 findAll()를 통해 해당 페이지와 요청된 정보의 크기에 따라 Entity 클래스의 형태로 반환된 값들이 List에 저장되어있다.
		//list를 .stream()을 통해 stream 객체로 변환하고, stream객체에 있는 각각의 Entity 클래스를 BoardResponseDto를 생성해 값을 맵핑한다.
		//그러면 list를 통해 변환된 stream 객체에는 여러개의 BoardResponseDto를 가지고 있고 이를 .collect()를 통해 List 형태로 최종 반환하게 된다.
		//stream 객체를 사용하는 이유는 코드 가독성, 필요할 때마다 요소를 처리하여 메모리 사용량 감소, Stream API가 제공하는 다양한 메서드 사용가능
		resultMap.put("list", list.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
		resultMap.put("paging", list.getPageable());
		resultMap.put("totalCnt", list.getTotalElements());
		resultMap.put("totalPage", list.getTotalPages());
		
		return resultMap;
	}
	
	//상세 페이지 조회(+조회수 업데이트 기능 추가)
	//Map 타입으로 정보를 실어서 controller에 보냄, 게시판 정보 + 파일 정보
	public HashMap<String, Object> findById(Long id) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>(); 
		
		boardRepository.updateBoardReadCntInc(id);
		
		BoardResponseDto info = new BoardResponseDto(boardRepository.findById(id).get());
		
		resultMap.put("info", info);
		resultMap.put("fileList", boardFileService.findByBoardId(info.getId()));
		
		return resultMap;
	}

	//게시글 업데이트
	//파일도 저장하는 것을 추가함.
	public boolean updateBoard(BoardRequestDto boardRequestDto, MultipartHttpServletRequest multiRequest) throws Exception {
		
		int result = boardRepository.updateBoard(boardRequestDto);
		
		boolean resultFlag = false;
		
		if (result > 0) {
			boardFileService.uploadFile(multiRequest, boardRequestDto.getId());
			resultFlag = true;
		}
		return resultFlag;
	}
    
//    //조회수 업데이트 -> Repository 부분으로 옮김, DB에 대한 정보를 수정하는 것이기 때문
//    public int updateBoardReadCntInc(Long id) {
//    	return boardRepository.updateBoardReadCntInc(id);
//    }

    //boardFileService에서 오류를 던지고 있기 때문에 해당 오류를 던지거나 잡아줘야함.
    //게시글 삭제
    public void deleteById(Long id) throws Exception {
    	Long[] idArr = {id};
		boardFileService.deleteBoardFileYn(idArr);
		boardRepository.deleteById(id);
    }
    
    //목록에서 게시글 삭제(여러개)
    public void deleteAll(Long[] deleteIdList) throws Exception {
    	boardFileService.deleteBoardFileYn(deleteIdList);
        boardRepository.deleteBoard(deleteIdList);
    }
    
}
