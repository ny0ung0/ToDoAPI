package com.clush.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clush.todolist.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
