package rs.neks.administration.util;

import org.springframework.core.convert.converter.Converter;

public class StringAmountConverter implements Converter<String, Double> {

	@Override
	public Double convert(String arg0) {
		if(TextUtils.notEmpty(arg0)) {
			
		}
		return 0d;
	}

}
