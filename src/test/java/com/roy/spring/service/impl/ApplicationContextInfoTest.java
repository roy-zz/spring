package com.roy.spring.service.impl;

import com.roy.spring.configuration.ApplicationConfig;
import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.MemberService;
import com.roy.spring.service.impl.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.beans.factory.config.BeanDefinition.ROLE_APPLICATION;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name: " + beanDefinitionName + ", object: " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name: " + beanDefinitionName + ", object: " + bean);
            }
        }
    }

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertTrue(memberService instanceof MemberServiceImpl);

        assertThrows(NoSuchBeanDefinitionException.class, () -> {
           ac.getBean("AnonymousBean");
        });
    }

    @Test
    @DisplayName("타입으로 조회")
    void findByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertTrue(memberService instanceof MemberServiceImpl);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBySpecificType() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertTrue(memberService instanceof MemberServiceImpl);
    }

    private final AnnotationConfigApplicationContext hasDuplicatedTypeBeansAc
            = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("동일 타입 조회 NoUniqueBeanDefinitionException 발생 테스트")
    void findDuplicatedTypeBeanTest() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            hasDuplicatedTypeBeansAc.getBean(MemberRepository.class);
        });
    }

    @Test
    @DisplayName("동일 타입 조회 빈 이름 지정 테스트")
    void findDuplicatedTypeBeanByNameTest() {
        MemberRepository memberRepository
                = hasDuplicatedTypeBeansAc.getBean("redisMemberRepository", MemberRepository.class);
        assertTrue(memberRepository instanceof MemoryMemberRepository);
    }

    @Test
    @DisplayName("특정 타입 모두 조회 테스트")
    void findAllDuplicatedTypeTest() {
        Map<String, MemberRepository> mapOfBeans = hasDuplicatedTypeBeansAc.getBeansOfType(MemberRepository.class);
        for (String key : mapOfBeans.keySet()) {
            System.out.println("key: " + key + ", value: " + mapOfBeans.get(key));
        }
    }

    @Test
    @DisplayName("동일 부모 조회 NoUniqueBeanDefinitionException 발생 테스트")
    void findDuplicatedParentTypeBeanTest() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            hasDuplicatedTypeBeansAc.getBean(DiscountPolicy.class);
        });
    }

    @Test
    @DisplayName("동일 부모 조회 이름 지정 테스트")
    void findDuplicatedParentTypeBeanByNameTest() {
        DiscountPolicy ratioDiscountPolicy
                = hasDuplicatedTypeBeansAc.getBean("ratioDiscountPolicy", DiscountPolicy.class);
        assertTrue(ratioDiscountPolicy instanceof RatioDiscountPolicy);
    }

    @Test
    @DisplayName("동일 부모 조회 타입 지정 테스트")
    void findDuplicatedParentTypeBeanBySpecificTypeTest() {
        DiscountPolicy ratioDiscountPolicy
                = hasDuplicatedTypeBeansAc.getBean(RatioDiscountPolicy.class);
        assertTrue(ratioDiscountPolicy instanceof RatioDiscountPolicy);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회 테스트")
    void findByParentTypeTest() {
        Map<String, DiscountPolicy> beansOfType = hasDuplicatedTypeBeansAc.getBeansOfType(DiscountPolicy.class);
        assertEquals(2, beansOfType.size());
        for (String key : beansOfType.keySet()) {
            System.out.println("key: " + key + ", value: " + beansOfType.get(key));
        }
    }

    @Test
    @DisplayName("Object 타입으로 모두 조회 테스트")
    void findByObjectTypeTest() {
        Map<String, Object> beansOfType = hasDuplicatedTypeBeansAc.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key: " + key + ", value: " + beansOfType.get(key));
        }
    }

}
