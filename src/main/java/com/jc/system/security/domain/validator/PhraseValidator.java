package com.jc.system.security.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.system.security.domain.Phrase;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class PhraseValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> arg0) {
		return Phrase.class.equals(arg0);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		Phrase v =  (Phrase)arg0;
		if(v.getContent()!=null&&v.getContent().length()>255){
			arg1.reject("content", "Content is too long");
		}
		if(v.getPhraseType()!=null&&v.getPhraseType().length()>1){
			arg1.reject("phraseType", "PhraseType is too long");
		}
	}
}
