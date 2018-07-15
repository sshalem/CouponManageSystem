package shabtay.coupon.system.DBDAO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * @author User
 *	a custom JsonSerializer serializer to write out the DateTime to JSON 
 * which will return the date as JSON in the following format "yyyy-MM-dd"
 * I use the the @JsonSerializer in the Coupon class for methods of getStartDate() , getEndDate()
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		String formattedDate = dateFormat.format(date);
		gen.writeString(formattedDate);
		
	}

}
