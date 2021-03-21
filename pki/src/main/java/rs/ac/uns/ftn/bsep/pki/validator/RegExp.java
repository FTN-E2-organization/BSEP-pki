package rs.ac.uns.ftn.bsep.pki.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
	
    public boolean isValidId(Long id){
        String number = Long.toString(id);
        String nameRegex = "^[0-9]{1,6}$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(number);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }
}
