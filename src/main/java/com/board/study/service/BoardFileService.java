package com.board.study.service;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.study.dto.file.BoardFileResponseDto;
import com.board.study.entity.file.BoardFile;
import com.board.study.entity.file.BoardFileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final, @NotNull이 붙은 필드를 인자로 받는 생성자 만듬
@Service
public class BoardFileService {
	private final BoardFileRepository boardFileRepository;
	
	//BoardFile(Entity) 객체를 통해 DTO 객체로 정보 이동, DB에 저장되어있는 파일의 정보를 가지고 온다.
	public BoardFileResponseDto findById(Long id) throws Exception {
		return new BoardFileResponseDto(boardFileRepository.findById(id).get());
	}
	
	//음.. 이건 무슨 코드일까, 파일을 수정할 게시글의 목록을 넘기는거 같기는 한데
	public List<Long> findByBoardId(Long boardId) throws Exception {
		return boardFileRepository.findByBoardId(boardId);
	}
	
	public boolean uploadFile(MultipartHttpServletRequest multiRequest, Long boardId) throws Exception {
		
		//파일을 저장할 게시글의 id 값이 없는 경우 오류를 발생시킨다.
		if(boardId == null) throw new NullPointerException("Empty board_id");
		
		//HTTP 요청으로 전달된 파일의 이름을 key로, 파일을 MultipartFile 객체를 value로 가지는 Map 타입을 만듬.
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		
		//Map 타입인 files를 entrySet 타입으로 변환하고 이를 다시 Iterator 타입으로 변환하여 반복자를 사용할 수 있도록 만듬.
		//Map 타입으로 저장될 때 하나의 객체인 Entry를 사용한다. 각각 key와 value를 지정하는데 해당 Entry 객체에 대한 제네릭을 String MultipartFile로 선언하고, Iterator을 다시 Entry 제네릭을 선언한 것.
		Iterator <Entry<String, MultipartFile>> it = files.entrySet().iterator();
		
		//반복자를 사용해서 하나의 File에 대한 정보를 담기 위한 변수 선언
		MultipartFile mFile;
		String saveFilePath = "", randomFileName="";
		
		//파일 경로를 지정할 때, 현재 년월을 작성해주기 위해 필요한 객체
		Calendar cal = Calendar.getInstance();
		
		//DB에 저장된 id의 개수를 담기위한 리스트.
		List<Long> resultList = new ArrayList<Long>();
		
		//반복자의 다음이 있을경우 계속 실행
		while(it.hasNext()) {
			//하나의 파일은 Entry 타입으로 담음.
			Entry<String, MultipartFile> entry = it.next();
			
			//Entry는 value로 MultipartFile 타입으로 파일에 대한 정보를 담고 있음.
			mFile = entry.getValue();
			int fileSize = (int) mFile.getSize();
			
			//사용자가 전송한 파일이 있을 경우, 파일이 저장될 위치를 지정함.
			if(fileSize > 0) {
				String filePath = "C:/Users/jhcode33/ProjectAll/STS4.project/board_study/uploadFiles";
			
				//파일이 저장될 경로에 현재 년월일을 추가함.
				filePath = filePath + 
						   //File.separator은 파일 경로를 지정할 때 오류가 발생하지 않게 하기 위함. 운영체제에 맞는 경로(windows = /)를 삽입해준다.
						   //년,월을 구함, 월은 0부터 시작하기 때문에 1을 더해줘야함.
						   File.separator + String.valueOf(cal.get(Calendar.YEAR)) +
						   File.separator + String.valueOf(cal.get(Calendar.MONTH) + 1);
//				Apache Commons Lang 라이브러리에서 제공하는 랜덤 파일 이름을 생성하는 코드이다. 보안과 중복을 방지하기 위해 이 코드가 사용되어 진다.
//				count: 생성하려는 문자열의 길이입니다.
//				start: 문자열에서 사용될 문자들 중 가장 작은 문자의 코드 포인트 값입니다.
//				end: 문자열에서 사용될 문자들 중 가장 큰 문자의 코드 포인트 값입니다.
//				letters: 문자열에서 문자만 사용할 것인지 여부입니다.
//				numbers: 문자열에서 숫자만 사용할 것인지 여부입니다.
//				chars: 문자열에서 사용될 문자 집합입니다.
//				random: 랜덤 넘버 생성기입니다
				randomFileName = "FILE_" + RandomStringUtils.random(8,0,0, false, true, null, new SecureRandom());
		
				String realFileName = mFile.getOriginalFilename();
				//파일 확장자 명을 짜른다.
				String fileExt = realFileName.substring(realFileName.lastIndexOf(".") +1);
				String saveFileName = randomFileName + "."	+ fileExt;
				//파일저장 경로와, 파일명을 합쳐서 파일 경로를 만듬. 나중에 수정 삭제할 때 사용함.
				saveFilePath = filePath + File.separator + saveFileName;
				
				//파일을 담을 폴더를 생성함. 년월로 관리할 폴더.
				File filePyhFolder = new File(filePath);
				if (!filePyhFolder.exists()) {
                    // 부모 폴더까지 포함하여 경로에 폴더를 만든다.
                    if (!filePyhFolder.mkdirs()) {
                        throw new Exception("File.mkdir() : Fail.");
                    }
                }
				//파일경로를 통해서, 파일 객체를 생성함.
				File saveFile = new File(saveFilePath);
				
				//파일 경로에 동일한 파일이 있으면 true를 반환하고 아래 중복제거 코드를 실행함.
				if(saveFile.isFile()) {
					boolean _exist = true;
					
					int index = 0;
					//동일한 파일명이 생성되지 않도록 만들어주는 코드
					while(_exist) {
						index++;
						//파일이름에 (숫자).확장자를 추가해서 새로 만드는 코드
						saveFileName = randomFileName + "(" + index + ")." + fileExt;
						//새로 만든 파일이름으로 경로를 설정함.
						String dictFile = filePath + File.separator + saveFileName;
						//새로 만든 파일 이름으로 객체가 만들어지는지 확인함. 중복되면 true 반환.
						_exist = new File(dictFile).isFile();
						//중복되지 않으면, 해당 경로를 saveFilePath에 저장함.
						if(!_exist) {
							saveFilePath = dictFile;
						}
					}
					//파일 데이터를 해당 경로 + 확장자 명을 가진 파일에 담음
					//해당 데이터는 iterator을 사용하여 하나씩 담았음.
					mFile.transferTo(new File(saveFilePath));
				
				//파일 경로에 동일한 파일이 없을 경우
				}else {
					mFile.transferTo(saveFile);
				}
				//지금까지 위에서 로컬 피씨에 폴더를 생성하고, 데이터를 저장했다면
				//DB(BoardFile 테이블)에 해당 내용을 저장해서, 나중에 수정하거나, 삭제 등의 작업을 할 때 게시판의 아이디를 통해
				//파일의 정보를 가져오기 위해 DB에 정보를 저장할 정보를 boardFile Entity 객체로 만들고, 해당 객체를 이용해
				//repository의 save 메서드로 매칭된 테이블에 정보를 저장하는 작업.
				
				BoardFile boardFile = BoardFile.builder()
	                    .boardId(boardId) 
	                    .origFileName(realFileName)
	                    .saveFileName(saveFileName)
	                    .fileSize(fileSize)
	                    .fileExt(fileExt)
	                    .filePath(filePath)
	                    .deleteYn("N")
	                    .build();

	            resultList.add(boardFileRepository.save(boardFile).getId());
			}
		}
		//파일이 정상적으로 업로드 되었는지 확인하기 위해, Http 요청으로 보내온 파일의 개수와 resultList(파일업로드를 처리하고 DB에 정보가 담긴 개수)의 개수를
		//비교하여 true, false를 반환함.
		return (files.size() == resultList.size()) ? true : false;
	}//uploadFile메서드의 끝.
	
	public int updateDeleteYn(Long[] deleteIdList) throws Exception {
        return boardFileRepository.updateDeleteYn(deleteIdList);
    }

	//해당 메서드는 작성하고 사용하지 않음. 이게 목록에서 첨부파일을 삭제하는 기능? 같은데
    public int deleteBoardFileYn(Long[] boardIdList) throws Exception {
        return boardFileRepository.deleteBoardFileYn(boardIdList);
    }
}
