package hello.core;


import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( // 수동으로 만들어 놓은 것을 빼기위해(예제 코드를 안전하게 유지하기위해)
        //basePackages = {"hello.core.member","hello.service"},
        //basePackageClasses = AutoAppConfig.class,
        // default 는 위에 패키지 설정정보클래스의 패지키가 시작위치
        // 따라서 설정정보클래스의 위치를 프로젝트 최상단에 두는것이좋다.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
