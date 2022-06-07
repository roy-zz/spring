package com.roy.spring.basic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.beans.factory.config.BeanDefinition.ROLE_APPLICATION;

import com.roy.spring.basic.configuration.PureApplicationConfig;
import com.roy.spring.basic.repository.MemberRepository;
import com.roy.spring.basic.repository.impl.MemoryMemberRepository;
import com.roy.spring.basic.service.DiscountPolicy;
import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.impl.config.TestConfig;
import com.roy.spring.basic.service.impl.MemberServiceImpl;
import com.roy.spring.basic.service.impl.RatioDiscountPolicy;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PureApplicationConfig.class);

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

    ApplicationContext xmlAc = new GenericXmlApplicationContext("applicationConfig.xml");

    @Test
    @DisplayName("XML 빈 등록 테스트")
    void findViaXmlApplicationContext() {
        MemberService memberService = xmlAc.getBean("memberService", MemberService.class);
        assertTrue(memberService instanceof MemberServiceImpl);
    }
    
    @Test
    @DisplayName("BeanDefinition 메타정보 조회 테스트")
    void getBeanDefinitionMetaInfo() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == ROLE_APPLICATION) {
                System.out.println("beanDefinition.getBeanClassName() = " + beanDefinition.getBeanClassName());
                System.out.println("beanDefinition.getFactoryBeanName() = " + beanDefinition.getFactoryBeanName());
                System.out.println("beanDefinition.getFactoryMethodName() = " + beanDefinition.getFactoryMethodName());
                System.out.println("beanDefinition.getScope() = " + beanDefinition.getScope());
                System.out.println("beanDefinition.isLazyInit() = " + beanDefinition.isLazyInit());
                System.out.println("beanDefinition.getInitMethodName() = " + beanDefinition.getInitMethodName());
                System.out.println("beanDefinition.getDestroyMethodName() = " + beanDefinition.getDestroyMethodName());
                System.out.println("beanDefinition.getConstructorArgumentValues() = " + beanDefinition.getConstructorArgumentValues());
                System.out.println("beanDefinition.getPropertyValues() = " + beanDefinition.getPropertyValues());
            }
        }
    }

}
