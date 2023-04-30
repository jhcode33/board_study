package com.board.study.entity.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.board.study.dto.board.BoardRequestDto;

public interface BoardRepository extends JpaRepository<Board, Long> {

	// 새글 등록 쿼리
	// : 바인딩 변수임을 나타낸다.
	static final String UPDATE_BOARD = "UPDATE Board SET TITLE = :#{#boardRequestDto.title}, "
									 + "CONTENT = :#{#boardRequestDto.content}, " 
									 + "UPDATE_TIME = NOW() "
									 + "WHERE ID = :#{#boardRequestDto.id}";
	static final String UPDATE_BOARD_READ_CNT_INC = "UPDATE Board SET READ_CNT = READ_CNT + 1 WHERE ID = :id";
	static final String DELETE_BOARD = "DELETE FROM Board WHERE ID IN (:deleteList)";
	
	@Transactional
	public Page<Board> findAllByOrderByIdDesc(Pageable pageable);

	// 조회수를 추가하는 쿼리
	@Transactional
	@Modifying
	@Query(value = UPDATE_BOARD_READ_CNT_INC, nativeQuery = true)
	public int updateBoardReadCntInc(@Param("id") Long id);

	// 목록에서 삭제(여러개)
	@Transactional
	@Modifying
	@Query(value = DELETE_BOARD, nativeQuery = true)
	public int deleteBoard(@Param("deleteList") Long[] deleteList);

	@Transactional
	@Modifying
	@Query(value = UPDATE_BOARD, nativeQuery = true)
	public int updateBoard(@Param("boardRequestDto") BoardRequestDto boardRequestDto);

}
