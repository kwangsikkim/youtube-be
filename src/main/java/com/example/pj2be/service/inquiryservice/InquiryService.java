package com.example.pj2be.service.inquiryservice;

import com.example.pj2be.domain.answer.AnswerDTO;
import com.example.pj2be.domain.inquiry.InquiryDTO;
import com.example.pj2be.mapper.inquirymapper.InquiryMapper;
import com.example.pj2be.service.websocketservice.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // 모든 Exception 발생하면 RollBack
public class InquiryService {

    private final InquiryMapper mapper;
    private final WebSocketService webSocketService;

    public Map<String, Object> list(Integer page, InquiryDTO dto) {

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 페이징 필요한 것들
        // 전체페이지, 보여줄페이지 수, 왼쪽끝페이지, 오른쪽끝페이지, 담페이지, 이전페이지,
        int countAll;

        System.out.println("dto = " + dto);

        if (dto.getRole_name().equals("운영자")) {
            countAll = mapper.selectAllpage();
        } else {
            countAll = mapper.selectPageByMemberId(dto.getLogin_member_id());
        }
        int lastPageNumber = (countAll - 1) / 5 + 1;
        int startPageNumber = (page - 1) / 5 * 5 + 1;
        int endPageNumber = (startPageNumber + (5 - 1));
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 5;
        int nextPageNumber = endPageNumber + 1;
        int initialPage = 1;

        // 넘겨줄 것들 put

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
            pageInfo.put("initialPage", initialPage);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
            pageInfo.put("lastPageNumber", lastPageNumber);
        }

        int from = (page - 1) * 5;

        // 운영자인지, 멤버인지에 따라 게시물 출력

        if (dto.getRole_name().equals("운영자")) {
            map.put("inquiryList", mapper.selectAll(from));
            map.put("pageInfo", pageInfo);
        } else {
            map.put("inquiryList", mapper.selectByMemberId(dto.getLogin_member_id(), from));
            map.put("pageInfo", pageInfo);
        }




        return map;
    }

    public void add(InquiryDTO dto) {

        mapper.insert(dto);
        System.out.println(dto.getInquiry_member_id() + "유저가 " + "문의게시판에 글을 작성하였습니다.(" + dto.getId() + "번 게시물)" );
    }

    public boolean validate(InquiryDTO dto) {

        if (dto == null) {
            return false;
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            return false;
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            return false;
        }
        if (dto.getInquiry_member_id() == null || dto.getInquiry_member_id().isBlank()) {
            return false;
        }
        if (dto.getInquiry_category() == null || dto.getInquiry_category().isBlank()) {
            return false;
        }
            return true;

    }

    public InquiryDTO get(Integer id) {
        return mapper.selectByInquiryId(id);
    }

    public void delete(Integer id) {
        mapper.deleteByInquiryId(id);
    }

    public void update(InquiryDTO dto) {

        mapper.update(dto);
    }

//    답변추가
    public void answerAdd(AnswerDTO dto) {
        mapper.insertAnswer(dto);

        InquiryDTO inquiryDTO = new InquiryDTO();
        inquiryDTO.setId(dto.getAnswer_board_id());
        inquiryDTO.setAnswer_status("답변완료");
        mapper.updateAnswerState(inquiryDTO);


    }
}
