package com.example.demo.member.controller;

import com.example.demo.diagnosis.domain.Diagnosis;
import com.example.demo.diagnosis.repository.AirRepository;
import com.example.demo.diagnosis.repository.CornaRepository;
import com.example.demo.diagnosis.repository.DiagnosisRepository;
import com.example.demo.diagnosis.repository.MacakRepository;
import com.example.demo.diagnosis.service.DiagnosisService;
import com.example.demo.hospital.service.HospitalService;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.Member;
import com.example.demo.member.vo.MemberUpdatePwd;
import com.example.demo.member.vo.MemberUpdateRequestDto;
import com.example.demo.member.vo.Role;
import com.example.demo.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final FindByIndexNameSessionRepository sessionRepository;
    private final  MemberService memberService;

    // 회원이 직접정보를 수정하는 API
    @PutMapping("/api/member/settings/{id}")
    public Long updateForm(@PathVariable Long id, @RequestBody MemberUpdateRequestDto requestDto) {

        return memberService.update(id, requestDto);
    }

    // 회원 패스워드 변경전용 API
    @PutMapping("/api/member/settingsPwd/{id}")
    public Long updatePwd(@PathVariable Long id, @RequestBody MemberUpdatePwd requestDto) {
        System.out.println(requestDto.getPassword().isEmpty());
        System.out.println(requestDto.getPassword2().isEmpty());
        if(!requestDto.getPassword().equals(requestDto.getPassword2())) {
            throw new IllegalStateException("패스워드 확인바람");
        }
        else if(requestDto.getPassword2().isEmpty()){
            throw new IllegalStateException("패스워드 확인바람");
        }

        return memberService.updatePwd(id, requestDto);
    }

    // 회원이 직접정보를 삭제하는 api
    @DeleteMapping("/api/member/delete/{id}")
    public Long delete(@PathVariable Long id, Principal principal) {
        sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                principal.getName()).keySet().forEach(session -> sessionRepository.deleteById((String) session));

//        Member member = memberRepository.findOne(id);
//
//
//
//        log.info(member.getClass().getName());
//        log.info(member.getEmail());
//        //병원관리자일 경우 병원정보도 삭제한다.
//        if(member.getRole().equals(Role.VET)){
//            hospitalService.delete(member.getHospital().getId());
//        }
//        reserveService.delete_member(member); //예약정보 같이삭제
//        List<Diagnosis> diagnosis = diagnosisRepository.findAllDesc(member); //진단정보 같이삭제
//        diagnosisService.delete(diagnosis);
        memberService.delete(id); // 회원정보삭제 (회원이 만약 병원관리자라면?)

        return id;
    }

    // 관리자가 회원정보를 수정하는 API
    @PutMapping("/api/admin/member/settings/{id}")
    public Long updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequestDto requestDto) {
        return memberService.updateMember(id, requestDto);
    }

    // 관리자가 회원정보를 삭제하는 api
    @DeleteMapping("/api/admin/member/delete/{id}")
    public Long deleteMember(@PathVariable Long id) {
//        Member member = memberRepository.findOne(id);
//        List<Diagnosis> diagnosis = diagnosisRepository.findAllDesc(member);
//
//        //병원관리자일 경우 병원정보도 삭제한다.
//        if(member.getRole().equals(Role.VET)){
//            hospitalService.delete(member.getHospital().getId());
//        }
//        reserveService.delete_member(member);
//        diagnosisService.delete(diagnosis);
        memberService.delete(id);

        return id;
    }

    @PostMapping("/api/checkEmail")
    public int checkEmail(@RequestBody String user_email){
        return memberService.validateDuplicateMember(user_email);
    }
}