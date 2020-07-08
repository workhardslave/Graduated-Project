package com.example.demo.reserve.controller;


import com.example.demo.config.auth.LogExecutionTime;
import com.example.demo.config.auth.LoginFindMember;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.Member;
import com.example.demo.reserve.service.ReserveService;
import com.example.demo.reserve.vo.ReserveResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReserveController {

    private final ReserveService reserveService;
    private final MemberService memberService;


    // 사용자 자신의 예약 정보 조회 홈페이지
    @GetMapping("/member/reservesInfo")
    @LogExecutionTime
    public String ReserveInfo(Model model, @LoginFindMember Member member) {                   // principle: session DB에 저장되어 있는 값 가져옴

        List<ReserveResponseDto> Reserves = reserveService.findAllDesc(member);

        model.addAttribute("reserves", Reserves);
        return "members/reserves/reserveInfo";
    }

    // 사용자 병원 예약 수정 및 삭제 홈페이지
    @GetMapping(value = "/reserves/settings/{id}")
    public String updateForm(@PathVariable Long id, Model model) {

        ReserveResponseDto dto = reserveService.findById(id);
        model.addAttribute("reserve", dto);
        log.info(dto.getDate());

        return "members/reserves/reserveModify";
    }

    // 관리자 -> 사용자 병원 예약 수정 및 삭제 홈페이지
    @GetMapping(value = "/admin/reserves/settings/{id}")
    public String updateFormAdmin(@PathVariable Long id, Model model) {

        ReserveResponseDto dto = reserveService.findById(id);
        model.addAttribute("reserve", dto);

        return "admin/reserves/reserveModifyAdmin";
    }

    // 관리자 -> 사용자 병원 예약 정보 조회
    @GetMapping(value = "/admin/reserveList")
    public String reserveInfo(Model model) {

        List<ReserveResponseDto> reserves = reserveService.findAll();
        model.addAttribute("reserves", reserves);

        return "admin/reserves/reserveList";
    }

}
