package com.board.study.entity.file;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
	//:boardId인 바인드 변수를 사용했다, 사용자에게 파라매터로 넘어오 boardId 값이 매핑되어 적용된다. mybits에서 #{ }과 preparedStatement의 ?에 해당하는 것과 똑같다.
	static final String SELECT_FILE_ID = "SELECT id FROM board_file " +
										 "WHERE board_id = :boardId AND DELETE_YN != 'Y'";

	
	static final String UPDATE_DELETE_YN = "UPDATE board_file " +
										   "SET DELETE_YN = 'Y' " +
										   "WHERE ID IN (:deleteIdList)";
								
	static final String DELETE_BOARD_FILE_YN = "UPDATE board_file " +
											   "SET DELETE_YN = 'Y' " +
											   "WHERE BOARD_ID IN (:boardIdList)";

	@Query(value = SELECT_FILE_ID, nativeQuery = true)
    public List <Long> findByBoardId(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query(value = UPDATE_DELETE_YN, nativeQuery = true)
    public int updateDeleteYn(@Param("deleteIdList") Long[] deleteIdList);

    //음... 사용하지 않았음. 왜지?
    @Transactional
    @Modifying
    @Query(value = DELETE_BOARD_FILE_YN, nativeQuery = true)
    public int deleteBoardFileYn(@Param("boardIdList") Long[] boardIdList);

}
