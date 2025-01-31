package org.example.sbb.app.global.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.sbb.app.domain.question.question.service.QuestionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

    @Pointcut("execution(* org.example.sbb.app.domain.question.question.service.QuestionService.modify(..)) ||" +
            "execution(* org.example.sbb.app.domain.question.question.service.QuestionService.writeQuestion(..))||" +
            "execution(* org.example.sbb.app.domain.question.question.service.QuestionService.delete(..))||" +
            "execution(* org.example.sbb.app.domain.question.question.service.QuestionService.recommend())||" +
            "execution(* org.example.sbb.app.domain.question.answer.service.AnswerService.prepareAnswerForm(..))||" +
            "execution(* org.example.sbb.app.domain.question.answer.service.AnswerService.modify(..))||" +
            "execution(* org.example.sbb.app.domain.question.answer.service.AnswerService.delete(..))||" +
            "execution(* org.example.sbb.app.domain.question.answer.service.AnswerService.recommend(..))")
    public void needAuthService(){}

    @Around("needAuthService()")
    public Object aroundModify(ProceedingJoinPoint joinPoint) throws Throwable {

        QuestionService target = (QuestionService) joinPoint.getTarget();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if( auth.getPrincipal() instanceof User user){
            target.setUserId(user.getUsername());
        }else{
            throw new UsernameNotFoundException("Need login");
        }
        return joinPoint.proceed();
    }

}
