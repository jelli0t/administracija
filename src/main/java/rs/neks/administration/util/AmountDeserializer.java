/**
 * 
 */
package rs.neks.administration.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author jelles
 *
 */
public class AmountDeserializer extends JsonDeserializer<Double> {

	@Override
	public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Double value = null;
		String txtAmount = p.readValueAs(String.class);
		if(TextUtils.notEmpty(txtAmount)) {
			System.out.println("Uneta vrednost: " + txtAmount);
			
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			if(txtAmount.matches(TextUtils.DECIMAL_NUM_COMMA_FORMAT)) {
				symbols.setDecimalSeparator(',');
				symbols.setGroupingSeparator('.');
			}			
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.###", symbols);					
			try {
				value = decimalFormat.parse(txtAmount).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

}
