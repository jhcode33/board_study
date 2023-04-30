package com.board.study.entity.file;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//DB와 직접적으로 정보를 가져오는 객체, Getter만 만들어서 정보가 중간에서 수정되지 못하게 해야함.
@NoArgsConstructor(access = AccessLevel.PROTECTED) //매개변수로 받는 인자가 없는 private 생성자를 만들어서 외부에서 접근하지 못하도록 만듬
@Getter
@Entity
public class BoardFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //기본값을 지정함, 시퀀스라고 생각해도 될듯, 여러 타입이 있는데 아직 자세하겐 모르겠음.
	private Long id;
	private Long boardId;
	private String origFileName;
    private String saveFileName;
    private int fileSize;
    private String fileExt;
    private String filePath;
    private String deleteYn;
    
    @CreatedDate //스프링 데이터 JPA에서 제공하는 기능, 엔티티 객체가 생성될 때, 현재 시간을 해당 변수에 저장해줌.
    private LocalDateTime registerTime;
    
    @Builder
    public BoardFile(Long id, Long boardId, String origFileName, String saveFileName, int fileSize, String fileExt,
        String filePath, String deleteYn, LocalDateTime registerTime) {
        this.id = id;
        this.boardId = boardId;
        this.origFileName = origFileName;
        this.saveFileName = saveFileName;
        this.fileSize = fileSize;
        this.fileExt = fileExt;
        this.filePath = filePath;
        this.deleteYn = deleteYn;
        this.registerTime = registerTime;
    }
}
